package com.physmo.c64;

import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

public class Emulator {

    static BasicDisplay basicDisplay = null;

    public static void main(String[] args) {

        basicDisplay = new BasicDisplayAwt((320+80) * 2, (240+80) * 2);
        basicDisplay.setTitle("C64 Emulator");

        Rig rig = new Rig(basicDisplay);

        // C64Palette.init();
        //
        // basicDisplay = new BasicDisplay(320*2, 240*2);
        //
        // CPU cpu = new CPU();
        // VIC vic = new VIC();
        //
        // cpu.reset();
        // runToAddress(cpu, 0xFD88); // never gets to 0xFD90 ??

        rig.runForCycles(500000 * 2);

        // runToFlagSet(cpu, 0b01000000);

        /*
         * //Utils.printMem(cpu, 0xE000, 0xFFFF-0xE000); // Kernal //Utils.printMem(cpu,
         * 0x0400, 0x07FF-0x0400); // Screen ram //Utils.printMem(cpu, 0xD000,
         * 0xDFFF-0xD000); // Characters //Utils.printMem(cpu, 0xA000, 0xBFFF-0xA000);
         * // Basic
         *
         * cpu.debugOutput=false;
         *
         * // 563532 int runFor = 5 * 280 * 300;// 256*1755*2; for (int
         * i=0;i<runFor;i++) { //if (i>runFor-50) cpu.debugOutput=true; //else
         * cpu.debugOutput=false; //cpu.debugOutput=false;
         *
         * //if (cpu.PC==0xFD5F) System.out.println("breakpoint i:"+ i); //if
         * (cpu.PC==0xFD5F) cpu.debugOutput=true; // reset entry
         *
         * //if (cpu.debugOutput && i%10==0) System.out.println("");
         *
         * cpu.tick(i); cpu.VICStub(); if (cpu.firstUnimplimentedInstructionIndex!=-1) {
         * System.out.
         * println("### First unimplimented instruction occured at call number: "+cpu.
         * firstUnimplimentedInstructionIndex); break; } //if (i==60000)
         * Utils.printMem(cpu.ram, 0, 0xff); }
         */

        // System.out.println("### First unimplimented instruction occured at call
        // number: "+cpu.firstUnimplimentedInstructionIndex);
        Utils.printMem(rig.cpu.mem.RAM, 0x0400, 0x07FF - 0x0400); // Screen ram
        // Utils.printMem(cpu, 0, 0xffff); // everything.
        // Utils.printMem(cpu.ram, 0, 0xFfff); // everything.
        // Utils.printMem(cpu.io, 0, 0x0f); // CIA 1
        // Utils.printMem(cpu.io, 0xff, 0x0f); // CIA 1
        // Utils.printMem(cpu.rom, 0xEA0C-16, 32); // debug

        Utils.printMem(rig.cpu.mem.RAM, 0x00, 0xff); // Zero page

        Utils.printTextScreen(rig.cpu.mem.RAM, 0x0400);

    }

}
