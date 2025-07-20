package com.physmo.c64;

import com.physmo.minvio.BasicDisplay;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;


public class IO {

    CPU6502 cpu = null;
    Rig rig = null;

    Map<Integer, Integer[]> keymap_ = new HashMap<>();
    Map<Integer, Integer> joyMap2 = new HashMap<>();

    // keymap_[SDL_SCANCODE_A] = std::make_pair(1,2);

    int[] keyboard_matrix_ = new int[8]; // REMOVE
    int[] portA = new int[8]; // Matrix Rows
    int[] portB = new int[8]; // Matrix Cols
    int joy1 = 0xff;
    int joy2 = 0xff;

    public IO(CPU6502 cpu, Rig rig) {
        System.out.println("Initialising IO");
        this.cpu = cpu;
        this.rig = rig;

        for (int i = 0; i < 8; i++) {
            keyboard_matrix_[i] = 0xff;
            portA[i] = 0xff;
            portB[i] = 0xff;
        }

        // keymap_.put(1, new pair(1, 2));

        keymap_.put((int) 'A', intPair(2, 1)); // A
        keymap_.put((int) 'B', intPair(4, 3)); // B
        keymap_.put((int) 'C', intPair(4, 2)); // C
        keymap_.put((int) 'D', intPair(2, 2)); // D
        keymap_.put((int) 'E', intPair(6, 1)); //
        keymap_.put((int) 'F', intPair(5, 2)); //
        keymap_.put((int) 'G', intPair(2, 3)); //
        keymap_.put((int) 'H', intPair(5, 3)); //
        keymap_.put((int) 'I', intPair(1, 4)); //
        keymap_.put((int) 'J', intPair(2, 4)); //
        keymap_.put((int) 'K', intPair(5, 4)); //
        keymap_.put((int) 'L', intPair(2, 5)); //
        keymap_.put((int) 'M', intPair(4, 4)); //
        keymap_.put((int) 'N', intPair(7, 4)); //
        keymap_.put((int) 'O', intPair(6, 4)); //
        keymap_.put((int) 'P', intPair(1, 5)); //
        keymap_.put((int) 'Q', intPair(6, 7)); //
        keymap_.put((int) 'R', intPair(1, 2)); //
        keymap_.put((int) 'S', intPair(5, 1)); //
        keymap_.put((int) 'T', intPair(6, 2)); //
        keymap_.put((int) 'U', intPair(6, 3)); //
        keymap_.put((int) 'V', intPair(7, 3)); //
        keymap_.put((int) 'W', intPair(1, 1)); //
        keymap_.put((int) 'X', intPair(7, 2)); //
        keymap_.put((int) 'Y', intPair(1, 3)); //
        keymap_.put((int) 'Z', intPair(4, 1)); //

        keymap_.put((int) '0', intPair(3, 4)); //
        keymap_.put((int) '1', intPair(0, 7)); //
        keymap_.put((int) '2', intPair(3, 7)); //
        keymap_.put((int) '3', intPair(0, 1)); //
        keymap_.put((int) '4', intPair(3, 1)); //
        keymap_.put((int) '5', intPair(0, 2)); //
        keymap_.put((int) '6', intPair(3, 2)); //
        keymap_.put((int) '7', intPair(0, 3)); //
        keymap_.put((int) '8', intPair(3, 3)); //
        keymap_.put((int) '9', intPair(0, 4)); //

        keymap_.put((int) ':', intPair(5, 5)); //
        keymap_.put((int) ';', intPair(2, 6)); //
        keymap_.put((int) '=', intPair(5, 6)); //
        keymap_.put((int) ',', intPair(7, 5)); //
        keymap_.put((int) '-', intPair(3, 5)); //
        keymap_.put((int) '.', intPair(4, 5)); //
        keymap_.put((int) '/', intPair(7, 6)); //
        // keymap_.put((int)'', new pair(, )); //

        // keymap_.put((int)'', new pair(, )); //

        keymap_.put((int) 8, intPair(0, 0)); // BACK SPACE
        keymap_.put((int) 10, intPair(1, 0)); // ENTER
        keymap_.put((int) 32, intPair(4, 7)); // SPACE
        keymap_.put((int) 16, intPair(7, 1)); // LEFT SHIFT
        keymap_.put((int) 27, intPair(7, 7)); // ESCAPE

        keymap_.put((int) 93, intPair(0, 5)); // + ] key
        keymap_.put((int) 91, intPair(1, 6)); // * [ key

        keymap_.put((int) 112, intPair(4, 0)); // F1
        keymap_.put((int) 114, intPair(5, 0)); // F3
        keymap_.put((int) 116, intPair(6, 0)); // F5
        keymap_.put((int) 118, intPair(3, 0)); // F7

        // Keycode, bit.
        joyMap2.put(38, 0); // UP
        joyMap2.put(40, 1); // DOWN
        joyMap2.put(37, 2); // LEFT
        joyMap2.put(39, 3); // RIGHT
        joyMap2.put(192, 4); // FIRE (`/~)

        // this.keyMatrixLocations[ 13 ] = [ 1, 0 ]; // flash.ui.Keyboard.ENTER
        // this.keyMatrixLocations[ 32 ] = [ 4, 7 ]; //flash.ui.Keyboard.SPACE
        // space = 32
        // enter = 10
    }

    public Integer[] intPair(int row, int column) {
        return new Integer[]{row, column};
    }

    public int keyboard_matrix_row(int col) {
        return keyboard_matrix_[col];
    }

    public int[] getPortA() {
        return portA;
    }

    public int[] getPortB() {
        return portB;
    }

    public void handle_keydown(int k) {
        Integer[] pair = keymap_.get(k);
        if (pair == null)
            return;
        int row = pair[0];
        int column = pair[1];
        keyboard_matrix_[column] &= ~(1 << row);
        portB[column] &= ~(1 << row);
        portA[row] &= ~(1 << column);
    }

    public void handle_keyup(int k) {
        Integer[] pair = keymap_.get(k);
        if (pair == null)
            return;
        int row = pair[0];
        int column = pair[1];
        keyboard_matrix_[column] |= 1 << row;
        portB[column] |= 1 << row;
        portA[row] |= 1 << column;
    }

    public void resetAllKeys() {
        for (int i = 0; i < 8; i++) {
            keyboard_matrix_[i] = 0xff;
//            portA[i]=0xff;
//            portB[i]=0xff;
        }
    }

    public void checkKeyboard(BasicDisplay bd) {

        boolean control = bd.getKeyState()[KeyEvent.VK_CONTROL] > 0;
        //rig.cia1.pra_ |= 0x1F; // Set joystick bits.

        joy2 = 0xff;

        for (int key : joyMap2.keySet()) {
            if (bd.getKeyState()[key] > 0) {
                //System.out.println("joy " + key);
                joy2 &= (~(1 << joyMap2.get(key)) & 0xff);
                //return;
            }
        }
        joy1 = joy2;
        //System.out.println(""+Utils.toBinary2(joy2));

        int loadKey = KeyEvent.VK_L;
        if (isFirstPress(bd, loadKey) && control) {
            System.out.println("loading");
            Loader.loadAndStartData(cpu);
            return;
        }

        int tapeKey = KeyEvent.VK_A;
        if (isFirstPress(bd, tapeKey) && control) {
            System.out.println("loading");

            Loader.loadT64File("/Users/nick/dev/emulatorsupportfiles/c64/t64/" + getT64Name(), cpu);
            return;
        }

        int testKey = KeyEvent.VK_T;
        if (isFirstPress(bd, testKey) && control) {
            System.out.println("Running tests");
            cpu.resetAndTest();
            return;
        }

        // Toggle debug output
        if (isFirstPress(bd, KeyEvent.VK_D) && control) {
            if (!cpu.debugOutput) cpu.debugOutput = true;
            else cpu.debugOutput = false;
            return;
        }

        for (int i = 0; i < 0xff; i++) {
            if (bd.getKeyState()[i] > 0 && bd.getKeyStatePrevious()[i] == 0) {
                handle_keydown(i);
            } else if (bd.getKeyState()[i] == 0 && bd.getKeyStatePrevious()[i] > 0) {
                handle_keyup(i);
            }
        }

        // Remap ctrl+1 to F1
        if (control) {
            int one = (int) '1';

            if (bd.getKeyState()[one] > 0 && bd.getKeyStatePrevious()[one] == 0) {
                handle_keydown(112);
            } else if (bd.getKeyState()[one] == 0 && bd.getKeyStatePrevious()[one] > 0) {
                handle_keyup(112);
            }
        }


        int pressedCount = 0;
        for (int i = 0; i < 0xff; i++) {
            if (bd.getKeyState()[i] > 0)
                pressedCount++;
        }
        if (pressedCount == 0)
            resetAllKeys();

        bd.tickInput();


        // DEBUG
//        String dbgA = "";
//        String dbgB = "";
//        for (int i=0;i<8;i++) {
//            dbgA += Utils.toBinary2(portA[i])+"-";
//            dbgB += Utils.toBinary2(portB[i])+"-";
//        }
//        System.out.println("PortA "+dbgA+":"+dbgB);
    }

    //int testKey = KeyEvent.VK_T;//112+11+1; // F1
    //	if (bd.getKeyState()[testKey] > 0 && bd.getKeyStatePrevious()[testKey] == 0) {

    public boolean isFirstPress(BasicDisplay bd, int key) {
        if (bd.getKeyState()[key] == 0) return false;
        if (bd.getKeyStatePrevious()[key] != 0) return false;
        return true;
    }

    public String getT64Name() {

//         return "finderskeepers.t64"; // WORKING
//         return "bcbill.t64"; // mode 3
//         return "centiped.t64"; // Runs fine
//         return "delta.t64"; // JAM GFX glitches (intro working better now) good intro
//         return "wizball.t64"; // infinite loop BRK loop
//         return "arcadia6.t64"; // runs with GFX glitches - mode 3
//         return "bountybob.t64"; // gets to title screen
//         return "greenber.t64"; // hacker loader nearly perfect - cant exit intro
//         return "headoverheels.t64"; // Doesn't finish loading
//         return "humanrace.t64"; // Loads into game graphics messed up
//         return "kane.t64"; // loads but sprites messed up - DUAL mode game screen?
//         return "kentilla.t64"; // doesnt finish loading
         return "OllieAndLisa.t64"; // crashes before loader
//         return "Panther.t64"; // game display but cant move
//         return "pitfall2.t64"; // bad instruction
//         return "sanxion.t64"; // BRK
//         return "spbound.t64"; // bad instruction
//         return "stormbringer.t64"; // intro works but game does not load
//         return "zzzzz.t64"; // bad instruction - mode 2
//         return "dandy.t64"; // fails to load - GFX Mode 2
//         return "pedro.t64"; // uses graphics mode 3
//         return "Outrun.t64"; // bad instruction
//         return "spindizy.t64"; // bad instruction
//         return "zorro.t64"; // bad instruction
//         return "MONTY.t64"; // bad instruction
//         return "uridium.t64"; // bad instruction
//         return "manicmin.t64"; // loads to glitching game
//         return "rhood.t64"; // doesnt start
//         return "BMXRACE2.T64"; // loads to game with graphics and collision errors
//         return "brucelee.t64"; // crashes on load
//         return "COSMICCR.T64"; // working - GFX mode 3
//         return "daley.t64"; // loads - sprite glitcheds - GFX mode 3
//         return "eclipse.t64"; // glitchy hacker screen, cant get to game
//         return "entombed.t64"; // loads to title
//         return "spectipede.t64"; // doesnt start
//         return "action.t64"; // Working - Good scrolling test
//         return "headoverheels_b.t64"; // freezes on loading
//         return "hollywoodorbust.t64"; // loads and runs with glitching GFX
//         return "kobayashinaru.t64"; // freezes on loading
//         return "polepos.t64"; // Loads game and runs with some glitching
//         return "spyh.t64"; // freezes on loading

    }

}
