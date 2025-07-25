package com.physmo.c64;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Loader {
	private static String gamePath = "/Users/nick/dev/emulatorsupportfiles/c64/games/";


	public static void loadAndStartData(CPU6502 cpu) {
		//String gamePath = "resource/games/";
//		String path = gamePath+"jetsetwi.prg"; // Nothing happens
//		String path = gamePath+"finders.prg";
		// String path = gamePath+"huntersm.prg";
//		String path = gamePath+"bombjack.prg"; // gets to menu but fire button not recognised
//		String path = gamePath+"1942.prg"; // Invalid instruction
//		String path = gamePath+"wizball.prg"; // BRK Loop
//		String path = gamePath+"finders2.prg"; // Runs well
		// String path = gamePath+"brucelee.prg";
		// String path = gamePath+"impossiblemission.prg";
//		String path = gamePath+"willy.prg"; // failr to load
//		String path = gamePath+"rambo.prg"; // Loads and runs game well.
		// String path = gamePath+"actionbiker.prg";
//		String path = gamePath+"manic.prg"; // Doesnt get past intro
//		 String path = gamePath+"arkanoid.prg"; // fails after intro
//		String path = gamePath+"nemesis.prg"; // Fails in BRK loop
		// String path = gamePath+"christmas.prg";
		//String path = gamePath+"humanrace.prg";
		//String path = gamePath+"sherwood.prg";
//		String path = gamePath+"bmxsim.prg"; // PERFECT
//		String path = gamePath+"zzzz.prg"; // Fails in same way as TAPE file
//		String path = gamePath+"pitfall 2.prg"; // PERFECT
//		String path = gamePath+"sanxion.prg"; // Bad instruction
//		String path = gamePath+"ollilissa.prg"; // PERFECT
		//String path = gamePath+"droid.prg";
//		String path = gamePath+"outrun.prg"; // Crashes after loading menu
//		String path = gamePath+"paradroid.prg"; // Infinite loop when loading

//		String path = gamePath+"dandy.prg";
//		String path = gamePath+"entombed.prg";
		String path = gamePath+"monty mole.prg"; // Loads to game
		
		int loc = 0;
		try {
			loc = Utils.ReadPrgFileBytesToMemoryLocation(path, cpu, 0xC000); //
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Loading " + path + " loc=" + loc);

		// PC = loc;//+1;

		// debugOutput = true;

		// PC = 0xA871; // jump to run command?
		// PC = 0xA69C; // jump to list command?
	}

	public static void loadT64File(String path, CPU6502 cpu)  {
		int[] data;
        try {
			data = Utils.readFileToArray(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

		StringBuilder signature = new StringBuilder();
		for (int i=0;i<32;i++) {
			signature.append((char) data[i]);
		}
		if (!signature.toString().startsWith("C64")) throw new RuntimeException("File does not start with C64");

		int tapeVersionNumber = (data[0x21]<<8)|(data[0x20]);
		int maxEntries = (data[0x23]<<8)|(data[0x22]);
		int usedEntries = (data[0x25]<<8)|(data[0x24]);
		StringBuilder tapeContainerName = new StringBuilder();
		for (int i=0x28;i<0x3f;i++) {
			tapeContainerName.append((char) data[i]);
		}

		System.out.println("=============================================");
		System.out.println("signature:"+signature);
		System.out.println("tapeVersionNumber:"+tapeVersionNumber);
		System.out.println("maxEntries:"+maxEntries);
		System.out.println("usedEntries:"+usedEntries);
		System.out.println("tapeContainerName:"+tapeContainerName);
		System.out.println("=============================================");
		System.out.println();

		for (int i=0;i<maxEntries;i++) {
			readT64DirectoryEntry(data, 0x40+(i*32), cpu);
		}

    }

	public static void readT64DirectoryEntry(int[] data, int offset, CPU6502 cpu) {
		int c64SfileType = data[offset];
		int fileType = data[offset+0x01];
		int startAddress  = (data[offset+0x03]<<8)|(data[offset+0x02]);
		int endAddress  = (data[offset+0x05]<<8)|(data[offset+0x04]);
		int fileOffset = (data[offset+0x0B]<<24)|(data[offset+0x0A]<<16)|(data[offset+0x09]<<8)|(data[offset+0x08]);
		StringBuilder directoryEntryName = new StringBuilder();
		for (int i=0x10;i<0x1f;i++) {
			directoryEntryName.append((char) data[offset+i]);
		}
		int dataLength = endAddress-startAddress;

		if (c64SfileType==0) {
			//System.out.println("c64SfileType is 0, skipping");
			return;
		}

		System.out.println("c64SfileType:"+Utils.toHex2(c64SfileType));
		System.out.println("fileType:"+Utils.toHex2(fileType));
		System.out.println("startAddress:"+Utils.toHex4(startAddress));
		System.out.println("endAddress:"+Utils.toHex4(endAddress));
		System.out.println("fileOffset:"+Utils.toHex4(fileOffset));
		System.out.println("directoryEntryName:"+directoryEntryName);

		if (dataLength+fileOffset>data.length) {
			System.out.println("Warning: length is bigger than data");
			dataLength = data.length - fileOffset;
		}
		if (endAddress==0xC3C6) {
			System.out.println("Warning: End address is C3C6 - probably faulty CONV64 based file.");
			dataLength = data.length - fileOffset;
		}

		for (int i=0;i<dataLength;i++) {
			//if (fileOffset+i<data.length) { // why should we have to check this?
				cpu.mem.RAM[startAddress + i] = data[fileOffset + i];
			System.out.println("data: "+Utils.toHex2(data[fileOffset + i]));
			//}
		}
		System.out.println("Loaded data from "+Utils.toHex4(startAddress) +" to "+Utils.toHex4(startAddress+dataLength) );
	}
}
