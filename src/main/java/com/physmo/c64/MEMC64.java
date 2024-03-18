package com.physmo.c64;

import java.util.ArrayList;
import java.util.List;

public class MEMC64 {

    static int previousBankSetting = -1;
    public int RAM[] = new int[0x10000]; // 64k
    public int ROM[] = new int[0x10000]; // 64k
    public int COLOR_RAM[] = new int[0x10000]; // 64k
    boolean enableKernal = false;
    boolean enableBasic = false;
    boolean enableCharacter = false;
    boolean enableIO = false;
    CPU6502 cpu = null;
    Rig rig = null;

    public MEMC64(CPU6502 cpu, Rig rig) {
        this.cpu = cpu;
        this.rig = rig;
    }


    public void pokeWord(int addr, int val) {
        // evidence that we need to write low byte first
        int hi = (val >> 8) & 0xff;
        int lo = val & 0xff;
        poke(addr, lo);
        poke(addr + 1, hi);
    }


    public void poke(int addr, int val) {

        int page = addr & 0xF000;
        val = val & 0xff;

        // debugPoke(addr, val);

        if (addr == -1) {
            cpu.setA(val);
            return;
        }

        switchBanks(RAM[1]);

        // BANK E+F - Kernal / RAM
        if (page == 0xE000 || page == 0xF000) {
            if (enableKernal) {
                //ROM[addr] = val;
                RAM[addr] = val;
                return;
            } else {
                RAM[addr] = val;
                return;
            }
        }

        // BANK D - IO / Char / Ram
        if (page == 0xD000 && enableCharacter) {
            //ROM[addr] = val;
            RAM[addr] = val;
            return;
        }

        // BANK D - IO specific
        if (page == 0xD000 && enableIO) {
            switch (addr & 0xFF00) {

                case 0xDC00: // CIA1
                    rig.cia1.write_register(addr & 0x0f, val);
                    return;

                case 0xDD00: // CIA2
                    rig.cia2.write_register(addr & 0x0f, val);
                    return;

                case 0xDB00: // COLOR RAM
                case 0xDA00: // COLOR RAM
                case 0xD900: // COLOR RAM
                case 0xD800: // COLOR RAM
                    COLOR_RAM[addr] = val;
                    return;

                case 0xD300: // VIC
                case 0xD200: // VIC
                case 0xD100: // VIC
                case 0xD000: // VIC
                    rig.vic.write_register(addr & 0x7f, val);
                    return;
            }

        }

        // BANK A+B - Basic ROM / RAM.
        if (page == 0xA000 || page == 0xB000) {
            if (enableBasic) {
                //ROM[addr] = val;
                RAM[addr] = val;
                return;
            } else {
                RAM[addr] = val;
                return;
            }
        }

        RAM[addr] = val & 0xff;
    }

    public int peekWord(int addr) {
        int lo = peek(addr);
        int hi = peek(addr + 1);
        return ((hi << 8) | lo) & 0xFFFF;
    }

    public int peek(int addr) {

        int page = addr & 0xF000;

        //debugPeek(addr);

        if (addr == -1) {
            return cpu.A;
        }

        switchBanks(RAM[1]);

        // BANK E+F - Kernal / RAM
        if (page == 0xE000 || page == 0xF000) {
            if (enableKernal) {
                return ROM[addr] & 0xFF;
            } else {
                return RAM[addr] & 0xFF;
            }
        }

        // BANK D - IO / Char / Ram
        if (page == 0xD000 && enableCharacter) {
            return ROM[addr] & 0xFF;
        }

        // BANK D - IO specific
        if (page == 0xD000 && enableIO) {
            switch (addr & 0xFF00) {

                case 0xDC00: // CIA1
                    return rig.cia1.read_register(addr & 0x0f);

                case 0xDD00: // CIA2
                    return rig.cia2.read_register(addr & 0x0f);

                case 0xDB00: // COLOR RAM
                case 0xDA00: // COLOR RAM
                case 0xD900: // COLOR RAM
                case 0xD800: // COLOR RAM
                    return COLOR_RAM[addr] & 0xFF;

                case 0xD300: // VIC
                case 0xD200: // VIC
                case 0xD100: // VIC
                case 0xD000: // VIC
                    return rig.vic.read_register(addr & 0x7f);
            }

        }

        // BANK A+B - Basic ROM / RAM.
        if (page == 0xA000 || page == 0xB000) {
            if (enableBasic)
                return ROM[addr] & 0xFF;
            else
                return RAM[addr] & 0xFF;
        }

        return RAM[addr & 0xFFFF] & 0xFF;
    }

    // Switch bank pointers.
    public void switchBanks(int val) {

        if (previousBankSetting == val)
            return;

        previousBankSetting = val;

        enableKernal = (val & 2) == 2;
        enableBasic = (val & 3) == 3;
        enableCharacter = ((val & 4) == 0) && ((val & 3) != 0);
        enableIO = ((val & 4) == 4) && ((val & 3) != 0);


        // Force all banks to point to ram for testing
//        if (cpu.unitTest == true) {
//            enableKernal = false;
//            enableBasic = false;
//            enableCharacter = false;
//            enableIO = false;
//        }
    }


    public void debugPeek(int addr) {

        if (addr == 0) {
            System.out.println("peeked 0 : " + Debug.getPCInfo(cpu, cpu.PC));
        }

        if (cpu.debugOutputIo)
            Utils.logIoAccess(addr, "", "Peek IO ");
    }


    public void debugPoke(int addr, int val) {
        boolean debugPoke = true;
        if (debugPoke == false) return;

        int PC = cpu.PC;
        int debugCallIndex = 1; // TODO: placeholder copied from CPU
        boolean debugOutputIo = false; // TODO: placeholder copied from CPU

        if (addr == 0xFDDD)
            System.out.println("poked 0xFDDD:" + val + "  PC:" + Utils.toHex4(PC));

        if (addr == 0x0288)
            System.out.println("poked 0x0288:" + val);
        if (addr == 0x0283)
            System.out.println("poked 0x0283:" + val + " callIndex:" + debugCallIndex);

        if (addr == 0xE4DB) {
            System.out.println("POKED 0xE4DB");
        }

        if (debugOutputIo)
            Utils.logIoAccess(addr, " " + Utils.toHex2(val) + " PC:" + Utils.toHex4(PC) + "  ", "Poked IO ");

        List<Integer> watchList = new ArrayList<Integer>();
        watchList.add(0x0031);
        watchList.add(0x0032);
        watchList.add(0x0033);
        watchList.add(0x0034);

        if (watchList.contains(addr)) {
            System.out.println("poked 0x" + Utils.toHex4(addr) + " V:" + val + "  at PC:" + Utils.toHex4(PC));
        }

    }

}
