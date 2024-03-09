package com.physmo.c64;


import com.physmo.minvio.BasicDisplay;

// FCE2 hardware reset
// E37B BASIC warm start entry point
// E5A0 initialise the vic chip
// FD50 Test RAM and find RAM end.
// E716 Output a character to the screen
// E3BF Initialize basic RAM locations.
// E544 Clear the screen
// FE25 - set top of memory (does something with a flag)
// E453 initialise the basic vectors
public class Rig {

	static BasicDisplay basicDisplay = null;

	CPU6502 cpu = null;
	MEMC64 mem = null;
	VIC vic = null;
	CIA1 cia1 = null;
	CIA2 cia2 = null;
	IO io = null;


	public Rig(BasicDisplay basicDisplay) {
		C64Palette.init();

		this.basicDisplay = basicDisplay;

		//basicDisplay = new BasicDisplay((320+80) * 2, (240+80) * 2);
//		basicDisplay = new BasicDisplayAwt((320+80) * 2, (240+80) * 2);
//		basicDisplay.setTitle("C64 - Emulator");

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
		cpu.debugOutput = false;
		int displayLines = 500; //
		int instructionsPerScanLine = 60;
		int tickCount = 0;

		for (int i = 0; i < runFor + displayLines; i++) {
			if (i > runFor - displayLines)
				cpu.debugOutput = true;

			/*
			 * if (basicDisplay.getKeyState()[65]>0) { //System.out.println("sim"); int
			 * prgLoc=0; try { System.out.println("Attempting to load prg"); prgLoc =
			 * Utils.ReadPrgFileBytesToMemoryLocation("src/huntersm.prg", cpu, 0xD000); }
			 * catch (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } // Character set cpu.PC=prgLoc; }
			 */

			for (int ii = 0; ii < instructionsPerScanLine; ii++) {

				if (tickCount % 100 == 0) {
					io.checkKeyboard(basicDisplay);
				}

				if (tickCount % 1000 == 0) {
					// cpu.setInterrupt(); // test
					// cpu.irq();
				}

				// Don't tick other components if unit test is active.
				if (cpu.unitTest == false) {
					cia1.tick();
					cia2.tick();
					cpu.tick(i);
					vic.tick();
				} else {
					cpu.tick(i);
				}

				tickCount++;
			}

			vic.VICStub(cpu, basicDisplay);

			if (tickCount % 100000 == 0) {
				// System.out.println("Tick count: "+tickCount);
			}

			if (cpu.firstUnimplimentedInstructionIndex != -1) {
				System.out.println("### First unimplimented instruction occured at call number: "
						+ cpu.firstUnimplimentedInstructionIndex);
				break;
			}

			// if (cpu.debugOutput) {
			// displayLines-=60;
			// if (displayLines<1) break;
			// }
		}
	}

	public static void runToAddress(CPU6502 cpu, VIC vic, int address) {
		cpu.debugOutput = false;
		int displayLines = 50; // 320; //
		for (int i = 0; i < 5000000; i++) {
			if (cpu.PC == address) {
				if (cpu.debugOutput == false)
					System.out.println("Hit breakpoint at iteration " + i);
				cpu.debugOutput = true;
			}

			cpu.tick(i);
			vic.VICStub(cpu, basicDisplay);

			if (cpu.debugOutput) {
				displayLines--;
				if (displayLines < 1)
					break;
			}
		}
	}

	public static void runToFlagSet(CPU6502 cpu, VIC vic, int flag) {
		cpu.debugOutput = false;
		int displayLines = 80; //
		for (int i = 0; i < 5000000; i++) {
			if ((cpu.FL & flag) == flag) {
				if (cpu.debugOutput == false)
					System.out.println("Flag set at iteration " + i);
				cpu.debugOutput = true;
			}

			cpu.tick(i);
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
