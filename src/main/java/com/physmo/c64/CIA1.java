package com.physmo.c64;

public class CIA1 {

	public static final int kModeProcessor = 0;
	public static final int kModeCNT = 1;
	public static final int kModeTimerA = 2;
	public static final int kModeTimerACNT = 3;

	public static final int kModeRestart = 0;
	public static final int kModeOneTime = 1;

	int prev_cpu_cycles_ = 0;
	// cpu_->cycles() - prev_cpu_cycles_
	// int fakeCycles = 10;

	int timer_a_latch_ = 0;
	int timer_b_latch_ = 0;
	int pra_, prb_;
	boolean timer_a_enabled_;
	boolean timer_b_enabled_;
	boolean timer_a_irq_enabled_;
	boolean timer_b_irq_enabled_;
	boolean timer_a_irq_triggered_;
	boolean timer_b_irq_triggered_;
	int timer_a_run_mode_;
	int timer_b_run_mode_;
	int timer_a_input_mode_;
	int timer_b_input_mode_;
	int timer_a_counter_;
	int timer_b_counter_;

	CPU6502 cpu = null;
	Rig rig = null;

	public CIA1(CPU6502 cpu, Rig rig) {
		timer_a_latch_ = timer_b_latch_ = timer_a_counter_ = timer_b_counter_ = 0;
		timer_a_enabled_ = timer_b_enabled_ = timer_a_irq_enabled_ = timer_b_irq_enabled_ = false;
		timer_a_irq_triggered_ = timer_b_irq_triggered_ = false;
		timer_a_input_mode_ = timer_b_input_mode_ = kModeProcessor;
		timer_a_run_mode_ = timer_b_run_mode_ = kModeRestart;
		pra_ = prb_ = 0xff;
		// prev_cpu_cycles_ = 0;
		this.cpu = cpu;
		this.rig = rig;
	}

	public boolean tick() {

		// System.out.println(" "+timer_a_counter_);

		/* timer a */
		if (timer_a_enabled_) {
			switch (timer_a_input_mode_) {
			case kModeProcessor:

				// TODO: implement this correctly
				timer_a_counter_ -= cpu.cycles - prev_cpu_cycles_;
				// timer_a_counter_ -= fakeCycles;

				if (timer_a_counter_ <= 0) {
					if (timer_a_irq_enabled_) {
						timer_a_irq_triggered_ = true;
						cpu.irq();
					}
					reset_timer_a();
				}
				break;
			case kModeCNT:
				break;
			}
		}
		/* timer b */
		if (timer_b_enabled_) {
			switch (timer_b_input_mode_) {
			case kModeProcessor:
				// TODO: implement this correctly
				timer_b_counter_ -= cpu.cycles - prev_cpu_cycles_;
				// timer_b_counter_ -= fakeCycles;

				if (timer_b_counter_ <= 0) {
					if (timer_b_irq_enabled_) {
						timer_b_irq_triggered_ = true;
						// cpu_->irq();
						cpu.irq();
					}
					reset_timer_b();
				}
				break;
			case kModeCNT:
				break;
			case kModeTimerA:
				break;
			case kModeTimerACNT:
				break;
			}
		}

		// TODO: implement this correctly
		prev_cpu_cycles_ = cpu.cycles;

		return true;

	}

	public void write_register(int r, int v) {
		v = v & 0xff;

		// System.out.println("CIA1 write "+r+", "+v);
		switch (r) {
		/* data port a (PRA), keyboard matrix cols and joystick #2 */
		case 0x0:
			pra_ = v & 0xff;
			// System.out.println("set pra_ to "+pra_);
			break;
		/* data port b (PRB), keyboard matrix rows and joystick #1 */
		case 0x1:
			break;
		/* data direction port a (DDRA) */
		case 0x2:
			break;
		/* data direction port b (DDRB) */
		case 0x3:
			break;
		/* timer a low byte */
		case 0x4:
			timer_a_latch_ &= 0xff00;
			timer_a_latch_ |= (v & 0xff);
			break;
		/* timer a high byte */
		case 0x5:
			timer_a_latch_ &= 0x00ff;
			timer_a_latch_ |= v << 8;
			timer_a_latch_ = timer_a_latch_ & 0xffff;
			break;
		/* timer b low byte */
		case 0x6:
			timer_b_latch_ &= 0xff00;
			timer_b_latch_ |= (v & 0xff);
			break;
		/* timer b high byte */
		case 0x7:
			timer_b_latch_ &= 0x00ff;
			timer_b_latch_ |= v << 8;
			timer_b_latch_ = timer_b_latch_ & 0xffff;
			break;
		/* RTC 1/10s */
		case 0x8:
			break;
		/* RTC seconds */
		case 0x9:
			break;
		/* RTC minutes */
		case 0xa:
			break;
		/* RTC hours */
		case 0xb:
			break;
		/* shift serial */
		case 0xc:
			break;
		/* interrupt control and status */
		case 0xd:
			/**
			 * if bit 7 is set, enable selected mask of interrupts, else disable them
			 */
			if (ISSET_BIT(v, 0))
				timer_a_irq_enabled_ = ISSET_BIT(v, 7);
			if (ISSET_BIT(v, 1))
				timer_b_irq_enabled_ = ISSET_BIT(v, 7);
			break;
		/* control timer a */
		case 0xe:
			timer_a_enabled_ = ((v & (1 << 0)) != 0);
			timer_a_input_mode_ = (v & (1 << 5)) >> 5;
			/* load latch requested */
			if ((v & (1 << 4)) != 0)
				timer_a_counter_ = timer_a_latch_;
			break;
		/* control timer b */
		case 0xf:
			timer_b_enabled_ = ((v & 0x1) != 0);
			timer_b_input_mode_ = (v & (1 << 5)) | (v & (1 << 6)) >> 5;
			/* load latch requested */
			if ((v & (1 << 4)) != 0)
				timer_b_counter_ = timer_b_latch_;
			break;
		}
	}

	public int read_register(int r) {
		int retval = 0;

		switch (r) {
		/* data port a (PRA), keyboard matrix cols and joystick #2 */
		case 0x0:
			// retval = 0x7f;
			retval = pra_;
			break;
		/* data port b (PRB), keyboard matrix rows and joystick #1 */
		case 0x1:
			if (pra_ == 0xff)
				retval = 0xff;
			else if (pra_ > 0) {

				int col = 0;
				int v = (~pra_) & 0xff;

				col = getBinaryIndex(v);

				retval = rig.io.keyboard_matrix_row(col);

				if (retval != 255) {
					System.out.println("Cia keyb col:" + col + "  val:" + retval);
				}

			}
			break;
		/* data direction port a (DDRA) */
		case 0x2:
			break;
		/* data direction port b (DDRB) */
		case 0x3:
			break;
		/* timer a low byte */
		case 0x4:
			retval = (int) (timer_a_counter_ & 0x00ff);
			break;
		/* timer a high byte */
		case 0x5:
			retval = (int) ((timer_a_counter_ & 0xff00) >> 8);
			break;
		/* timer b low byte */
		case 0x6:
			retval = (int) (timer_b_counter_ & 0x00ff);
			break;
		/* timer b high byte */
		case 0x7:
			retval = (int) ((timer_b_counter_ & 0xff00) >> 8);
			break;
		/* RTC 1/10s */
		case 0x8:
			break;
		/* RTC seconds */
		case 0x9:
			break;
		/* RTC minutes */
		case 0xa:
			break;
		/* RTC hours */
		case 0xb:
			break;
		/* shift serial */
		case 0xc:
			break;
		/* interrupt control and status */
		case 0xd:
			if (timer_a_irq_triggered_ || timer_b_irq_triggered_) {
				retval |= (1 << 7); // IRQ occured
				if (timer_a_irq_triggered_)
					retval |= (1 << 0);
				if (timer_b_irq_triggered_)
					retval |= (1 << 1);
			}
			break;
		/* control timer a */
		case 0xe:
			break;
		/* control timer b */
		case 0xf:
			break;
		}
		return retval;
	}

	public boolean ISSET_BIT(int v, int bit) {
		if ((v & (1 << bit)) > 0)
			return true;
		return false;
	}

	public void reset_timer_a() {
		switch (timer_a_run_mode_) {
		case kModeRestart:
			timer_a_counter_ = timer_a_latch_;
			break;
		case kModeOneTime:
			timer_a_enabled_ = false;
			break;
		}
	}

	public void reset_timer_b() {
		switch (timer_b_run_mode_) {
		case kModeRestart:
			timer_b_counter_ = timer_b_latch_;
			break;
		case kModeOneTime:
			timer_b_enabled_ = false;
			break;
		}
	}

	// Return position of set bit
	public int getBinaryIndex(int val) {
		if (val == 0b0000_0001)
			return 0;
		if (val == 0b0000_0010)
			return 1;
		if (val == 0b0000_0100)
			return 2;
		if (val == 0b0000_1000)
			return 3;
		if (val == 0b0001_0000)
			return 4;
		if (val == 0b0010_0000)
			return 5;
		if (val == 0b0100_0000)
			return 6;
		if (val == 0b1000_0000)
			return 7;
		return 0;
	}

}
