package com.physmo.c64;

// Debug helper functions.
public class Debug {
	

	public static String getPCInfo(CPU6502 cpu, int ptr) {
		String str = "";
		str += " PC:" + Utils.toHex4(ptr);
		str += " BYTES: " + Utils.toHex2(cpu.mem.peek(ptr));
		str += " " + Utils.toHex2(cpu.mem.peek(ptr + 1));
		str += " " + Utils.toHex2(cpu.mem.peek(ptr + 2));
		return str;
	}

	public static String getHardwareSummary(CPU6502 cpu) {
		String str = "";
		str += " A:" + Utils.toHex2(cpu.A);
		str += " X:" + Utils.toHex2(cpu.X);
		str += " Y:" + Utils.toHex2(cpu.Y);
		str += " SP:" + Utils.toHex2(cpu.SP);
		//str += " PC:" + Utils.toHex4(cpu.PC);
		//str += " FL:"+Utils.toHex2(cpu.FL);
		str += " " + renderFlags(cpu.FL);
		str += printStack(cpu);
		str += "  "+(char)cpu.A+(char)cpu.X+(char)cpu.Y;
		return str;
	}

	public static String printStack(CPU6502 cpu) {
		String str = "";
		for (int i = 0; i < 8; i++) {
			str += " " + Utils.toHex2(cpu.mem.RAM[cpu.SB + cpu.SP + i + 1]);
		}
		return str;
	}

	public static String renderBits(int val) {
		String str = "";
		for (int i = 0; i < 8; i++) {
			if ((val & (1 << i)) > 0)
				str += "1";
			else
				str += "0";
		}
		return str;
	}

	/*
	 * 0 Carry Flag C Indicates when a bit of the result is to be carried to, or
	 * borrowed from, another byte. 1 Zero Flag Z Indicates when the result is
	 * equal, or not, to zero. 2 Interrupt Request Disable Flag I Indicates when
	 * preventing, or allowing, non-maskable interrupts (NMI). 3 Decimal Mode Flag D
	 * Indicates when switching between decimal/binary modes. 4 Break Command Flag B
	 * Indicates when stopping the execution of machine code instructions. 5 Unused
	 * - Cannot be changed. 6 Overflow Flag V Indicates when the result is greater,
	 * or less, than can be stored in one byte (including sign). 7 Negative Flag N
	 * Indicates when the result is negative, or positive, in signed operations.
	 */
	public static String renderFlags(int val) {
		String str = "";
		str += ((val & 1 << 7) > 0) ? "N" : ".";
		str += ((val & 1 << 6) > 0) ? "V" : ".";
		str += ((val & 1 << 5) > 0) ? "-" : "-";
		str += ((val & 1 << 4) > 0) ? "B" : ".";
		str += ((val & 1 << 3) > 0) ? "D" : ".";
		str += ((val & 1 << 2) > 0) ? "I" : ".";
		str += ((val & 1 << 1) > 0) ? "Z" : ".";
		str += ((val & 1 << 0) > 0) ? "C" : ".";
		return str;
	}

	public static void buildStateString(CPU6502 cpu) {
		cpu.stateString = getHardwareSummary(cpu);
	}

	public static void logLine(CPU6502 cpu, int l, String str) {
		if (cpu.debugOutput == false)
			return;

		String outStr = "" + Utils.toHex4(l) + " " + str;
		outStr = Utils.padToLength(outStr, 48);
		outStr += cpu.stateString; // getHardwareSummary();
		System.out.println(outStr);

	}

	
	public static void logUnhandled(CPU6502 cpu, String message, int callIndex, int entryPC) {
		System.out.println("*** UNIMPLIMENTED: " + message + "  PC: " + Utils.toHex4(entryPC) + "  iter: " + callIndex);
		if (cpu.firstUnimplimentedInstructionIndex == -1)
			cpu.firstUnimplimentedInstructionIndex = callIndex;
	}
	
	public static void dumpMemory(CPU6502 cpu, int addr) {
		
		int numRows=16;
		String rowStr = "";
		for (int row=0;row<numRows;row++) {
			rowStr = Utils.toHex4(addr)+"  ";
			for (int col=0;col<16;col++) {
				rowStr+=" "+Utils.toHex2(cpu.mem.RAM[addr++]);
				if ((col+1)%4==0) rowStr+=" ";
			}
			System.out.println(rowStr);
		}
		
	}
}
