package com.physmo.c64;
import java.awt.Color;

public class C64Palette {
	public static Color[] palette = new Color[16];

	{
		init();
	}

	public static void init() {
System.out.println("Palette initialised");
		palette[0] = new Color(0x000000);
		palette[1] = new Color(0xFFFFFF);
		palette[2] = new Color(0x68372B);
		palette[3] = new Color(0x70A4B2);
		palette[4] = new Color(0x6F3D86);
		palette[5] = new Color(0x588D43);
		palette[6] = new Color(0x352879);
		palette[7] = new Color(0xB8C76F);
		palette[8] = new Color(0x6F4F25);
		palette[9] = new Color(0x433900);
		palette[10] = new Color(0x9A6759);
		palette[11] = new Color(0x444444);
		palette[12] = new Color(0x6C6C6C);
		palette[13] = new Color(0x9AD284);
		palette[14] = new Color(0x6C5EB5);
		palette[15] = new Color(0x959595);
		
		
		
	}
}
