package com.physmo.c64;

public class CIA2 {

	public static final int kModeProcessor = 0;
	public static final int kModeCNT = 1;
	public static final int kModeTimerA = 2;
	public static final int kModeTimerACNT = 3;

	public static final int kModeRestart = 0;
	public static final int kModeOneTime = 1;

	// int fakeCycles = 15;
	int prev_cpu_cycles_ = 0;

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

	public CIA2(CPU6502 cpu, Rig rig) {
		timer_a_latch_ = timer_b_latch_ = timer_a_counter_ = timer_b_counter_ = 0;
		timer_a_enabled_ = timer_b_enabled_ = timer_a_irq_enabled_ = timer_b_irq_enabled_ = false;
		timer_a_irq_triggered_ = timer_b_irq_triggered_ = false;
		timer_a_input_mode_ = timer_b_input_mode_ = kModeProcessor;
		timer_a_run_mode_ = timer_b_run_mode_ = kModeRestart;
		pra_ = prb_ = 0xff;
		prev_cpu_cycles_ = 0;

		// prev_cpu_cycles_ = 0;
		this.cpu = cpu;
		this.rig = rig;
	}

	public boolean tick() {

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

						cpu.nmi();
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
						cpu.nmi();
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

	/**
	 * @brief retrieves vic base address
	 *
	 *        PRA bits (0..1)
	 *
	 *        %00, 0: Bank 3: $C000-$FFFF, 49152-65535 %01, 1: Bank 2: $8000-$BFFF,
	 *        32768-49151 %10, 2: Bank 1: $4000-$7FFF, 16384-32767 %11, 3: Bank 0:
	 *        $0000-$3FFF, 0-16383 (standard)
	 */
	public int vic_base_address() {
		return ((~pra_ & 0x3) << 14);
	}

	public void write_register(int r, int v) {
		switch (r) {
		/* data port a (PRA) */
		case 0x0:
			pra_ = v;
			break;
		/* data port b (PRB) */
		case 0x1:
			prb_ = v;
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
			timer_a_latch_ |= (v & 0xff) << 8;
			timer_a_latch_ = timer_a_latch_ & 0xffff;
			break;
		/* timer b low byte */
		case 0x6:
			timer_b_latch_ &= 0xff00;
			timer_b_latch_ |= v;
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
			if (TEST_BIT(v, 0))
				timer_a_irq_enabled_ = TEST_BIT(v, 7);
			if (TEST_BIT(v, 1))
				timer_b_irq_enabled_ = TEST_BIT(v, 7);
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
		/* data port a (PRA) */
		case 0x0:
			retval = pra_;
			break;
		/* data port b (PRB) */
		case 0x1:
			retval = prb_;
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

	public boolean TEST_BIT(int v, int bit) {
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

}
