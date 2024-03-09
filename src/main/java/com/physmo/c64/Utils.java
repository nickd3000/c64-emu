package com.physmo.c64;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {

	// TODO: change this to take an array reference instead of CPU class reference.
	public static void ReadFileBytesToROMMemoryLocation(String fileName, CPU6502 cpu, int targetLocation) throws IOException {
		FileInputStream in = null;
        
        try {
            in = new FileInputStream(fileName);
        
            int c;
            int count=0;

            while ((c = in.read()) != -1) {
            	cpu.mem.ROM[targetLocation+count++] = c;
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
	}
	
	public static void ReadFileBytesToRAMMemoryLocation(String fileName, CPU6502 cpu, int targetLocation) throws IOException {
		FileInputStream in = null;
        
        try {
            in = new FileInputStream(fileName);
        
            int c;
            int count=0;

            while ((c = in.read()) != -1) {
            	cpu.mem.RAM[targetLocation+count++] = c;
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
	}
	
	/*
	 * 
var buff = buffer_load(argument0);
var size = buffer_get_size(buff);
debug("size="+string(size));
var Add = buffer_read(buff,buffer_u16);
var Address=Add;
size-=2;

var EndAdd = Add+size;
for(var i=0;i<size;i++){
    pMemory[# Add++,0] = buffer_read(buff,buffer_u8);
}

Doke($2b,Address); // start address?
Doke($2d,EndAdd); // start address+size?
Doke($2f,EndAdd);
Doke($31,EndAdd);

buffer_delete(buff);
	 */
	public static int ReadPrgFileBytesToMemoryLocation(String fileName, CPU6502 cpu, int targetLocation) throws IOException {
		FileInputStream in = null;
		int basicStartLocation = 0x0801;
		boolean loadAsBasic = false;
        int loc = 0;
        try {
        	System.out.println("loading file");
            in = new FileInputStream(fileName);
        
            int c;
            int count=0;

            int b1 = in.read();
            int b2 = in.read();

            System.out.println("b1: 0x"+Utils.toHex2(b1) +"  b2: 0x"+Utils.toHex2(b2));
             loc = (b2<<8)|b1;
             
             //if (loadAsBasic) loc = 0x0801;
             if (loc==basicStartLocation) {
            	 System.out.println("Loading as basic");
             }
             
            System.out.println("Location: 0x"+Utils.toHex4(loc));
            
//            for (int sk=0;sk<94;sk++) {
//            	in.read();
//            }
            
            //loc=0x0801;
            //in.reset();
            while ((c = in.read()) != -1) {
            	if (c>0xff) System.out.println("big byte: "+c);
            	cpu.mem.RAM[loc+count++] = c&0xff;
            }

            //writeWord(0x002b, 0x0801);
            
            //count--;
            
            //cpu.mem.pokeWord(0x2b, 0x0801); 
            if (loc==basicStartLocation) {
	            cpu.mem.pokeWord(0x2b, loc);
	            cpu.mem.pokeWord(0x2d, loc+count);
	            cpu.mem.pokeWord(0x2f, loc+count);
	            cpu.mem.pokeWord(0x31, loc+count);
            }
            else {
            	cpu.PC = loc;
            }
            
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return loc;
        //return targetLocation;
	}
	
	public static void printTextScreen(int[] data, int address) {
		int i=0;
		String strRow = "";
		for (int y=0;y<30;y++) {
			strRow = "";
			for (int x=0;x<40;x++) {
				int m = data[address+i];
				strRow += getSafeChar(m);
				i++;
			}
			System.out.println(strRow);
		}
	}
	
	public static void printMem(int[] data, int address, int byteCount) {
		int numBytes = 16; // Number of bytes to display per line.
		String strCombined = "";
		String strRow = "";
		String strChars = "";
		int charCount = 0;
		for (int line=0;line<0xffff;line++) {
			strRow = String.format("%04X", address+(line*numBytes));//Integer.toHexString(address);
			strChars = "";
			for (int i=0;i<numBytes;i++) {
				if (charCount>byteCount) break;
				int m = data[address+i+(line*numBytes)];
				
				//strRow += " " + Integer.toHexString(m);
				strRow += " " + String.format("%02X", m);
				
				strChars += getSafeChar(m);
				charCount++;
			}
			strCombined+=strRow+"  " +strChars+"\n";
			if (charCount>byteCount) break;
		}
		
		System.out.println(strCombined);
	}
	
	// https://www.c64-wiki.com/wiki/PETSCII
	// NOTE: need to convert to c64 codes before converting to chars.
	public static char getSafeChar(int v) {
		// 48-57 = 0-9
		// 65-90 = a-z
		// 97-122 = a-z
		if (v==0x20) return ' ';
		if (v==0x2A) return '*';
		
		if (v>=48 && v<=57) {
			return (char)((v-48)+'0');
		}
		if (v>=65 && v<=90) {
			return (char)((v-65)+'A');
		}
		if (v>=97 && v<=122) {
			return (char)((v-97)+'a');
		}
		
		// 0x01 was being used as 'A' when I looked in memory... not sure why
		if (v>=1 && v<=26) {
			return (char)((v-1)+'A');
		}
		
		return '.';
	}
	
	public static String toHex2(int v) {	
		return String.format("%02X", v);
	}
	public static String toHex4(int v) {	
		return String.format("%04X", v);
	}
	
	// Messy function to add spaces to a string to bring it up to a certain length.
	public static String padToLength(String str, int length) {
		String spaces = "                                                             ";
		int curLen = str.length();
		if (curLen>length) return str;
		return str+spaces.substring(0, length-curLen);
	}
	
	public static void logIoAccess(int addr, String strVal, String prefix) {
		String strAddress = Utils.toHex4(addr);
		//String strVal = val==-55?"":(" val:"+Utils.toHex2(val));
		if (addr>=0xD000 && addr<=0xD3FF) System.out.println(prefix+ " VIC-II registers addr:" + strAddress + strVal);
		if (addr>=0xD400 && addr<=0xD7FF) System.out.println(prefix+ " SID registers addr:" + strAddress+ strVal);
		if (addr>=0xD800 && addr<=0xDBFF) System.out.println(prefix+ " Color memory addr:" + strAddress+ strVal);
		if (addr>=0xDC00 && addr<=0xDCFF) System.out.println(prefix+ " CIA1 addr:" + strAddress+ strVal);
		if (addr>=0xDD00 && addr<=0xDDFF) System.out.println(prefix+ " CIA2 addr:" + strAddress+ strVal);
		if (addr>=0xDE00 && addr<=0xDEFF) System.out.println(prefix+ " I/O 1 addr:" + strAddress+ strVal);
		if (addr>=0xDF00 && addr<=0xDFFF) System.out.println(prefix+ " I/O 2 addr:" + strAddress+ strVal);
		
		if (addr==0x0001) System.out.println(prefix+ " Page table control byte:" + strAddress+ strVal);
	}
	
	public static void loadTestCode(CPU6502 cpu) {
		int location = 0x100;
		int [] code = {0xa9, 0x0a, 0x69, 0x0f, 0xa9, 0x32, 0x69, 0x32, 0xa9, 0xe7, 0x69, 0xce, 0xa9, 0x88, 0x69, 0xec, 0xa9, 0x43, 0x69, 0x3c, 0xa9, 0x0f, 0xe9, 0x0a, 0xa9, 0x0a, 0xe9, 0x0d, 0xa9, 0x78, 0xe9, 0xf9, 0xa9, 0x88, 0xe9, 0xf8, 0xa2, 0xfe, 0xe8, 0xe8, 0xca, 0xca, 0xa9, 0xfe, 0x85, 0x64, 0xe6, 0x64, 0xe6, 0x64, 0xc6, 0x64, 0xc6, 0x64};
		int ptr = location;
		for (int i : code) {
			cpu.mem.RAM[ptr++] = i;
		}
		cpu.PC = 0x100;
	}
}

/*
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyBytes {
    public static void main(String[] args) throws IOException {

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream("xanadu.txt");
            out = new FileOutputStream("outagain.txt");
            int c;

            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
*/













