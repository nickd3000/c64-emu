package com.physmo.c64;


import com.physmo.minvio.BasicDisplay;


public class Rig {

	BasicDisplay basicDisplay = null;

	CPU6502 cpu;
	MEMC64 mem = null;
	VIC vic = null;
	CIA1 cia1 = null;
	CIA2 cia2 = null;
	IO io = null;


	public Rig(BasicDisplay basicDisplay) {
		C64Palette.init();

		this.basicDisplay = basicDisplay;

		cpu = new CPU6502();
		vic = new VIC(cpu, basicDisplay, this);
		cia1 = new CIA1(cpu, this);
		cia2 = new CIA2(cpu, this);
		io = new IO(cpu, this);
		mem = new MEMC64(cpu, this);
		cpu.attachHardware(mem);

		if (cpu.unitTest == false)
			cpu.reset();
		else
			cpu.resetAndTest();

	}

	public void runForCycles(int runFor) {
		//cpu.debugOutput = true;
		int displayLines = 500; //
		int instructionsPerScanLine = 160;
		int tickCount = 0;


		for (int i = 0; i < runFor + displayLines; i++) {

			for (int ii = 0; ii < instructionsPerScanLine; ii++) {

				if (tickCount % 5000 == 0) {
					io.checkKeyboard(basicDisplay);
				}

				// Don't tick other components if unit test is active.
				if (!cpu.unitTest) {
					cia1.tick();
					cia2.tick();
					cpu.tick2();
					vic.tick();
				} else {
					cpu.tick2();
				}

				tickCount++;
			}

			if (cpu.firstUnimplimentedInstructionIndex != -1) {
				System.out.println("### First unimplimented instruction occured at call number: "
						+ cpu.firstUnimplimentedInstructionIndex);
				break;
			}

		}
	}

	public void runToAddress(CPU6502 cpu, VIC vic, int address) {
		cpu.debugOutput = false;
		int displayLines = 50; // 320; //
		for (int i = 0; i < 5000000; i++) {
			if (cpu.PC == address) {
				if (cpu.debugOutput == false)
					System.out.println("Hit breakpoint at iteration " + i);
				cpu.debugOutput = true;
			}

			cpu.tick2();
			vic.VICStub(cpu, basicDisplay);

			if (cpu.debugOutput) {
				displayLines--;
				if (displayLines < 1)
					break;
			}
		}
	}

	public void runToFlagSet(CPU6502 cpu, VIC vic, int flag) {
		cpu.debugOutput = false;
		int displayLines = 80; //
		for (int i = 0; i < 5000000; i++) {
			if ((cpu.FL & flag) == flag) {
				if (cpu.debugOutput == false)
					System.out.println("Flag set at iteration " + i);
				cpu.debugOutput = true;
			}

			cpu.tick2();
			vic.VICStub(cpu, basicDisplay);

			if (cpu.debugOutput) {
				displayLines--;
				if (displayLines < 1)
					break;
			}
		}
	}

	public void testAddSignedByteToWord(CPU6502 cpu) {
		// addSignedByteToWord(int wd, int bt) {
		int addr = 0x0100;
		int val = 0xF4;
		int res = cpu.addSignedByteToWord(addr, val);

	}

}
