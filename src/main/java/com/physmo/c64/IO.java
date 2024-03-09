package com.physmo.c64;

import com.physmo.minvio.BasicDisplay;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;


public class IO {

	CPU6502 cpu = null;
	Rig rig = null;

	Map<Integer, IntPair> keymap_ = new HashMap<Integer, IntPair>();
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

		keymap_.put((int) 'A', new IntPair(2, 1)); // A
		keymap_.put((int) 'B', new IntPair(4, 3)); // B
		keymap_.put((int) 'C', new IntPair(4, 2)); // C
		keymap_.put((int) 'D', new IntPair(2, 2)); // D
		keymap_.put((int) 'E', new IntPair(6, 1)); //
		keymap_.put((int) 'F', new IntPair(5, 2)); //
		keymap_.put((int) 'G', new IntPair(2, 3)); //
		keymap_.put((int) 'H', new IntPair(5, 3)); //
		keymap_.put((int) 'I', new IntPair(1, 4)); //
		keymap_.put((int) 'J', new IntPair(2, 4)); //
		keymap_.put((int) 'K', new IntPair(5, 4)); //
		keymap_.put((int) 'L', new IntPair(2, 5)); //
		keymap_.put((int) 'M', new IntPair(4, 4)); //
		keymap_.put((int) 'N', new IntPair(7, 4)); //
		keymap_.put((int) 'O', new IntPair(6, 4)); //
		keymap_.put((int) 'P', new IntPair(1, 5)); //
		keymap_.put((int) 'Q', new IntPair(6, 7)); //
		keymap_.put((int) 'R', new IntPair(1, 2)); //
		keymap_.put((int) 'S', new IntPair(5, 1)); //
		keymap_.put((int) 'T', new IntPair(6, 2)); //
		keymap_.put((int) 'U', new IntPair(6, 3)); //
		keymap_.put((int) 'V', new IntPair(7, 3)); //
		keymap_.put((int) 'W', new IntPair(1, 1)); //
		keymap_.put((int) 'X', new IntPair(7, 2)); //
		keymap_.put((int) 'Y', new IntPair(1, 3)); //
		keymap_.put((int) 'Z', new IntPair(4, 1)); //

		keymap_.put((int) '0', new IntPair(3, 4)); //
		keymap_.put((int) '1', new IntPair(0, 7)); //
		keymap_.put((int) '2', new IntPair(3, 7)); //
		keymap_.put((int) '3', new IntPair(0, 1)); //
		keymap_.put((int) '4', new IntPair(3, 1)); //
		keymap_.put((int) '5', new IntPair(0, 2)); //
		keymap_.put((int) '6', new IntPair(3, 2)); //
		keymap_.put((int) '7', new IntPair(0, 3)); //
		keymap_.put((int) '8', new IntPair(3, 3)); //
		keymap_.put((int) '9', new IntPair(0, 4)); //

		keymap_.put((int) ':', new IntPair(5, 5)); //
		keymap_.put((int) ';', new IntPair(2, 6)); //
		keymap_.put((int) '=', new IntPair(5, 6)); //
		keymap_.put((int) ',', new IntPair(7, 5)); //
		keymap_.put((int) '-', new IntPair(3, 5)); //
		keymap_.put((int) '.', new IntPair(4, 5)); //
		keymap_.put((int) '/', new IntPair(7, 6)); //
		// keymap_.put((int)'', new pair(, )); //

		// keymap_.put((int)'', new pair(, )); //

		keymap_.put((int) 8, new IntPair(0, 0)); // BACK SPACE
		keymap_.put((int) 10, new IntPair(1, 0)); // ENTER
		keymap_.put((int) 32, new IntPair(4, 7)); // SPACE
		keymap_.put((int) 16, new IntPair(7, 1)); // LEFT SHIFT
		keymap_.put((int) 27, new IntPair(7, 7)); // ESCAPE

		keymap_.put((int) 93, new IntPair(0, 5)); // + ] key
		keymap_.put((int) 91, new IntPair(1, 6)); // * [ key

		keymap_.put((int) 112, new IntPair(4, 0)); // F1
		keymap_.put((int) 114, new IntPair(5, 0)); // F3
		keymap_.put((int) 116, new IntPair(6, 0)); // F5
		keymap_.put((int) 118, new IntPair(3, 0)); // F7

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

	public int keyboard_matrix_row(int col) {
		return keyboard_matrix_[col];
	};

	public void handle_keydown(int k) {
		IntPair pair = keymap_.get(k);
		if (pair == null)
			return;
		int row = pair.first;
		int column = pair.second;
		keyboard_matrix_[column] &= ~(1 << row);
	}

	public void handle_keyup(int k) {
		IntPair pair = keymap_.get(k);
		if (pair == null)
			return;
		int row = pair.first;
		int column = pair.second;
		keyboard_matrix_[column] |= 1 << row;
	}

	public void resetAllKeys() {
		for (int i = 0; i < 8; i++)
			keyboard_matrix_[i] = 0xff;
	}

	public void checkKeyboard(BasicDisplay bd) {

		rig.cia1.pra_ |= 0x1F; // Set joystick bits.
		
		for (int key : joyMap2.keySet()) {
			if (bd.getKeyState()[key] > 0) {
				rig.cia1.pra_ &= (~(1 << joyMap2.get(key)));
				return;
			}
		}

		int loadKey = KeyEvent.VK_Q;//112+11+1; // F1
		if (bd.getKeyState()[loadKey] > 0 && bd.getKeyStatePrevious()[loadKey] == 0) {
			System.out.println("loading");
			Loader.loadAndStartData(cpu);
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

}
