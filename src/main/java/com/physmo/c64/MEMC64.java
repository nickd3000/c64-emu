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
    boolean enableCartLo = false;
    boolean enableCartHi1 = false;
    boolean enableCartHi2 = false;
    boolean enableUltimax = false; // Experimental, not sure how this should work.
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

        switchBanks(RAM[1]);

        // NOTE: Writing to ROM always writes to the RAM underneath.

        if ((page == 0x8000 || page == 0x9000) && enableCartLo) return;
        if ((page == 0xA000 || page == 0xB000) && enableCartHi1) return;
        if ((page == 0xE000 || page == 0xF000) && enableCartHi2) return;
        if ((page>= 0x1000 && page<=0xc000) &&  enableUltimax) return;


        // BANK E+F - Kernal / RAM
        if (page == 0xE000 || page == 0xF000) {
            RAM[addr] = val;
            return;
        }

        // BANK D - IO specific
        if (page == 0xD000 && enableIO) {

            switch (addr & 0xFF00) {

                case 0xDC00: // CIA1
                    System.out.println("IO Write: [CIA1] " + Utils.toHex4(addr) + " = " + Utils.toHex2(val));
                    rig.cia1.write_register(addr & 0x0f, val);
                    return;

                case 0xDD00: // CIA2
                    System.out.println("IO Write: [CIA2] " + Utils.toHex4(addr) + " = " + Utils.toHex2(val));
                    rig.cia2.write_register(addr & 0x0f, val);
                    return;

                case 0xDB00: // COLOR RAM
                case 0xDA00: // COLOR RAM
                case 0xD900: // COLOR RAM
                case 0xD800: // COLOR RAM
                    System.out.println("IO Write: [CRAM] " + Utils.toHex4(addr) + " = " + Utils.toHex2(val));
                    COLOR_RAM[addr] = val;
                    return;

                case 0xD400: // SID
                case 0xD500:
                case 0xD600:
                case 0xD700:
                    //System.out.println("IO Write: [SID] "+Utils.toHex4(addr)+" = "+ Utils.toHex2(val));
                    return; // SID

                case 0xD300: // VIC
                case 0xD200: // VIC
                case 0xD100: // VIC
                case 0xD000: // VIC
                    //System.out.println("IO Write: [VIC ] "+Utils.toHex4(addr)+" = "+ Utils.toHex2(val));
                    rig.vic.write_register(addr & 0x7f, val);
                    return;
            }

        }

        // BANK D - IO / Char / Ram
        if (page == 0xD000 && enableCharacter) {
            RAM[addr] = val;
            return;
        }

        // BANK A+B - Basic ROM / RAM.
        if (page == 0xA000 || page == 0xB000) {
            RAM[addr] = val;
            return;
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

        int cartMemValue = 0xff;


        switch (page) {

            case 0x0000:
                return RAM[addr & 0xFFFF] & 0xFF;

            case 0x1000:
            case 0x2000:
            case 0x3000:
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                if (enableUltimax) return cartMemValue;
                return RAM[addr & 0xFFFF] & 0xFF;

            case 0x8000:
            case 0x9000:
                if (enableCartLo) return cartMemValue;
                else return RAM[addr & 0xFFFF] & 0xFF;

            case 0xA000:
            case 0xB000:
                // BANK A+B - Basic ROM / RAM.
                if (enableCartHi1)
                    return cartMemValue;
                else if (enableBasic)
                    return ROM[addr] & 0xFF;
                else
                    return RAM[addr] & 0xFF;

            case 0xC000:
                return RAM[addr & 0xFFFF] & 0xFF;

//            case 0xD000:
//                break;

            case 0xE000:
            case 0xF000:
                // BANK E+F - Kernal / RAM
                if (enableCartHi2) {
                    return cartMemValue;
                } else if (enableKernal) {
                    return ROM[addr] & 0xFF;
                } else {
                    return RAM[addr] & 0xFF;
                }

        }


        // BANK E+F - Kernal / RAM
        if (page == 0xE000 || page == 0xF000) {
            if (enableKernal) {
                return ROM[addr] & 0xFF;
            } else {
                return RAM[addr] & 0xFF;
            }
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

                case 0xD400:
                case 0xD500:
                case 0xD600:
                case 0xD700:
                    return 0xFF; // SID

                case 0xD300: // VIC
                case 0xD200: // VIC
                case 0xD100: // VIC
                case 0xD000: // VIC
                    return rig.vic.read_register(addr & 0x7f);
            }

        }

        // BANK D - IO / Char / Ram
        if (page == 0xD000 && enableCharacter) {
            return ROM[addr] & 0xFF;
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
        val |= 0b00011000;
        val &= 0b00011111;

        if (previousBankSetting == val)
            return;

        previousBankSetting = val;

        enableKernal = (val & 2) == 2;
        enableBasic = (val & 3) == 3;
        enableCharacter = ((val & 4) == 0) && ((val & 3) != 0);
        enableIO = ((val & 4) == 4) && ((val & 3) != 0);

        enableIO = false;
        if (val >= 5 && val <= 7) enableIO = true;
        if (val >= 13 && val <= 23) enableIO = true;
        if (val >= 29 && val <= 31) enableIO = true;
        enableCharacter = false;
        if (val >= 2 && val <= 3) enableCharacter = true;
        if (val >= 9 && val <= 11) enableCharacter = true;
        if (val >= 25 && val <= 27) enableCharacter = true;
        enableBasic = false;
        if (val == 11) enableBasic = true;
        if (val == 15) enableBasic = true;
        if (val == 27) enableBasic = true;
        if (val == 31) enableBasic = true;
        enableKernal = false;
        if (val >= 2 && val <= 3) enableKernal = true;
        if (val >= 6 && val <= 7) enableKernal = true;
        if (val >= 10 && val <= 11) enableKernal = true;
        if (val >= 14 && val <= 15) enableKernal = true;
        if (val >= 26 && val <= 27) enableKernal = true;
        if (val >= 30 && val <= 31) enableKernal = true;
        enableCartLo = false;
        if (val == 3) enableCartLo = true;
        if (val == 7) enableCartLo = true;
        if (val == 11) enableCartLo = true;
        if (val >= 15 && val <=23) enableCartLo = true;
        enableCartHi1 = false;
        if (val == 2) enableCartHi1 = true;
        if (val == 3) enableCartHi1 = true;
        if (val == 6) enableCartHi1 = true;
        if (val == 7) enableCartHi1 = true;
        enableCartHi2 = false;
        if (val >= 16 && val <= 23) enableCartHi2 = true;
        enableUltimax = false;
        if (val >= 16 && val <= 23) enableUltimax = true;


//        enableIO = (val & 0b0100)>0;

        System.out.println("enableKernal=" + enableKernal + "  enableBasic=" + enableBasic + " enableCharacter=" + enableCharacter + "  enableIO=" + enableIO);
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
