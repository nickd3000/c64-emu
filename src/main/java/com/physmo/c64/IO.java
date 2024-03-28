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

    int[] keyboard_matrix_ = new int[8];

    public IO(CPU6502 cpu, Rig rig) {
        System.out.println("Initialising IO");
        this.cpu = cpu;
        this.rig = rig;

        for (int i = 0; i < 8; i++) {
            keyboard_matrix_[i] = 0xff;
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

    ;

    public void handle_keydown(int k) {
        Integer[] pair = keymap_.get(k);
        if (pair == null)
            return;
        int row = pair[0];
        int column = pair[1];
        keyboard_matrix_[column] &= ~(1 << row);
    }

    public void handle_keyup(int k) {
        Integer[] pair = keymap_.get(k);
        if (pair == null)
            return;
        int row = pair[0];
        int column = pair[1];
        keyboard_matrix_[column] |= 1 << row;
    }

    public void resetAllKeys() {
        for (int i = 0; i < 8; i++)
            keyboard_matrix_[i] = 0xff;
    }

    public void checkKeyboard(BasicDisplay bd) {

        boolean control = bd.getKeyState()[KeyEvent.VK_CONTROL] > 0;
        rig.cia1.pra_ |= 0x1F; // Set joystick bits.

        for (int key : joyMap2.keySet()) {
            if (bd.getKeyState()[key] > 0) {
                rig.cia1.pra_ &= (~(1 << joyMap2.get(key)));
                return;
            }
        }

        int loadKey = KeyEvent.VK_L;
        if (isFirstPress(bd, loadKey) && control) {
            System.out.println("loading");
            Loader.loadAndStartData(cpu);
            return;
        }

        int tapeKey = KeyEvent.VK_A;
        if (isFirstPress(bd, tapeKey) && control) {
            System.out.println("loading");

            Loader.loadT64File("/Users/nick/dev/emulatorsupportfiles/c64/t64/"+getT64Name(), cpu);
            return;
        }

        int testKey = KeyEvent.VK_T;
        if (isFirstPress(bd, testKey) && control) {
            System.out.println("Running tests");
            cpu.resetAndTest();
            return;
        }

        for (int i = 0; i < 0xff; i++) {
            if (bd.getKeyState()[i] > 0 && bd.getKeyStatePrevious()[i] == 0) {
                handle_keydown(i);
            } else if (bd.getKeyState()[i] == 0 && bd.getKeyStatePrevious()[i] > 0) {
                handle_keyup(i);
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
    }

    //int testKey = KeyEvent.VK_T;//112+11+1; // F1
    //	if (bd.getKeyState()[testKey] > 0 && bd.getKeyStatePrevious()[testKey] == 0) {

    public boolean isFirstPress(BasicDisplay bd, int key) {
        if (bd.getKeyState()[key] == 0) return false;
        if (bd.getKeyStatePrevious()[key] != 0) return false;
        return true;
    }

    public String getT64Name() {
        //return "bcbill.t64";
        //return "centiped.t64"; // Runs with graphics glitches
        //return "delta.t64"; // GFX glitches
        //return "wizball.t64"; // BRK loop
        //return "bcbill2.t64"; // Shows title
        //return "arcadia6.t64"; // runs with GFX glitches
        //return "bountybob.t64"; // shows loader then crashes
        //return "finderskeepers.t64"; // bad file?
        //return "uridium.t64"; // bad file?
        //return "greenber.t64"; // garbled loader
        //return "headoverheels.t64"; //
        return "humanrace.t64"; // Loads into game graphics messed up



    }

}
