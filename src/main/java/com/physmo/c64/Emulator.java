package com.physmo.c64;

import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

public class Emulator {

    static BasicDisplay basicDisplay = null;

    public static void main(String[] args) {

        basicDisplay = new BasicDisplayAwt((320+80) * 2, (240+80) * 2);
        basicDisplay.setTitle("C64 Emulator");

        Rig rig = new Rig(basicDisplay);

        rig.runForCycles(2000000);

        Utils.printMem(rig.cpu.mem.RAM, 0x0400, 0x07FF - 0x0400); // Screen ram
        Utils.printMem(rig.cpu.mem.RAM, 0x00, 0xff); // Zero page
        Utils.printTextScreen(rig.cpu.mem.RAM, 0x0400);

    }

}
