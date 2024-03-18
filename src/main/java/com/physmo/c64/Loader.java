package com.physmo.c64;

import java.io.IOException;

public class Loader {
	private static String gamePath = "/Users/nick/dev/emulatorsupportfiles/c64/games/";


	public static void loadAndStartData(CPU6502 cpu) {
		//String gamePath = "resource/games/";
		//String path = gamePath+"jetsetwi.prg";
		//String path = gamePath+"finders.prg";
		// String path = gamePath+"huntersm.prg";
		//String path = gamePath+"bombjack.prg";
		// String path = gamePath+"1942.prg";
		//String path = gamePath+"wizball.prg";
		String path = gamePath+"finders2.prg";
		// String path = gamePath+"brucelee.prg";
		// String path = gamePath+"impossiblemission.prg";
		//String path = gamePath+"willy.prg";
		//String path = gamePath+"rambo.prg";
		// String path = gamePath+"actionbiker.prg";
		//String path = gamePath+"manic.prg";
		// String path = gamePath+"arkanoid.prg";
		//String path = gamePath+"nemesis.prg";
		// String path = gamePath+"christmas.prg";
		//String path = gamePath+"humanrace.prg";
		//String path = gamePath+"sherwood.prg";
		//String path = gamePath+"bmxsim.prg";
		//String path = gamePath+"droid.prg";
		//String path = gamePath+"outrun.prg";
		//String path = gamePath+"paradroid.prg";
		
		
		

		
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
}
