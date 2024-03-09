package com.physmo.c64;

import com.physmo.minvio.BasicDisplay;

import java.awt.Color;


// http://www.zimmers.net/cbmpics/cbm/c64/vic-ii.txt

public class VIC {

	private int ADDR_RASTER_D012 = 0xD012;
	private int ADDR_COLOR_RAM = 0xD800;
	private int ADDR_SCREEN_RAM = 0x0400;
	private int ADDR_CHARACTER_ROM = 0xD000;
	private int ADDR_BACKGROUND_COLOR = 53281; // 0xD021;

	CPU6502 cpu = null;
	BasicDisplay bd = null;
	Rig rig = null;

	/* sprites */
	int[] mx_ = new int[8]; // 8
	int[] my_ = new int[8]; // 8
	int msbx_;
	int sprite_enabled_;
	int sprite_priority_;
	int sprite_multicolor_;
	int sprite_double_width_;
	int sprite_double_height_;
	int[] sprite_shared_colors_ = new int[2]; // 2
	int[] sprite_colors_ = new int[8]; // 8
	/* background and border colors */
	int border_color_;
	int[] bgcolor_ = new int[4]; // 4
	/* cpu sync */
	int next_raster_at_;
	/* frame counter */
	int frame_c_;
	/* control registers */
	int cr1_;
	int cr2_;
	/* raster */
	int raster_c_;
	int raster_irq_;
	/* interrupt control */
	int irq_status_;
	int irq_enabled_;
	/* screen, character memory and bitmap addresses */
	int screen_mem_;
	int char_mem_;
	int bitmap_mem_;
	int mem_pointers_;
	int graphic_mode_;

	static final int kScreenLines = 312;
	static final int kScreenCols = 504;
	static final int kVisibleScreenWidth = 403;
	static final int kVisibleScreenHeight = 284;
	static final int kFirstVisibleLine = 14;
	static final int kLastVisibleLine = 298;
	static final int kLineCycles = 63;
	static final int kBadLineCycles = 23;
	static final double kRefreshRate = 1 / 50.125; // ~50Hz (PAL)
	static final int kSpritePtrsOffset = 0x3f8;

	 /* graphics constants */
    static final int kGResX = 320;
    static final int kGResY = 200;
    static final int kGCols = 40;
    static final int kGRows = 25;
    static final int kGFirstLine = 56;
    static final int kGLastLine  = 256;
    static final int kGFirstCol  = 42;
    
	static final int kCharMode = 0;
	static final int kMCCharMode = 1;
	static final int kBitmapMode = 2;
	static final int kMCBitmapMode = 3;
	static final int kExtBgMode = 4;
	static final int kIllegalMode = 5;

	public VIC(CPU6502 cpu, BasicDisplay bd, Rig rig) {
		this.cpu = cpu;
		this.bd = bd;
		this.rig = rig;

		raster_irq_ = raster_c_ = 0;
		irq_enabled_ = irq_status_ = 0;
		next_raster_at_ = kLineCycles;

		for (int i = 0; i < 8; i++) {
			mx_[i] = my_[i] = sprite_colors_[i] = 0;
		}
		msbx_ = sprite_double_height_ = sprite_double_width_ = 0;
		sprite_enabled_ = sprite_priority_ = sprite_multicolor_ = 0;
		sprite_shared_colors_[0] = sprite_shared_colors_[1] = 0;
		/* colors */
		border_color_ = 0;
		bgcolor_[0] = bgcolor_[1] = bgcolor_[2] = bgcolor_[3] = 0;
		/* control regs */
		cr1_ = cr2_ = 0;
		/* frame counter */
		frame_c_ = 0;
		/* default memory pointers */
		screen_mem_ = 0x0400;// Memory::kBaseAddrScreen;
		char_mem_ = 0xd000; // Memory::kBaseAddrChars;
		bitmap_mem_ = 0x0000; // Memory::kBaseAddrBitmap;
		/* bit 0 is unused */
		mem_pointers_ = (1 << 0);
		/* current graphic mode */
		graphic_mode_ = kCharMode;
	}

	// Recalculate graphics mode when control registers change.
	public void calculateGraphicsMode() {
		boolean ECM = testBit(cr1_, 6);
		boolean BMM = testBit(cr1_, 5);
		boolean MCM = testBit(cr2_, 4);
		if (!ECM && !BMM && !MCM)
			graphic_mode_ = kCharMode;
		if (!ECM && !BMM && MCM)
			graphic_mode_ = kMCCharMode;
		if (!ECM && BMM && !MCM)
			graphic_mode_ = kBitmapMode;
		if (!ECM && BMM && MCM)
			graphic_mode_ = kMCBitmapMode;
	}

	void raster_counter(int v) {
		raster_c_ = (int) (v & 0xff);
		cr1_ &= 0x7f;
		cr1_ |= ((v >> 1) & 0x80);
	}

	int raster_counter() {
		return (raster_c_ | ((cr1_ & 0x80) << 1));
	}

	boolean raster_irq_enabled() {
		return ISSET_BIT(irq_enabled_, 0);
	}

	public boolean ISSET_BIT(int v, int bit) {
		if ((v & (1 << bit)) > 0)
			return true;
		return false;
	}

	int vertical_scroll() {
		return (cr1_ & 0x7);
	}

	public boolean is_bad_line() {
		int rstr = raster_counter();
		return (rstr >= 0x30 && rstr <= 0xf7 && (rstr & 0x7) == (vertical_scroll() & 0x7));
	}

	static int skipper = 0;
	private int scale;

	public boolean tick() {
		if ((read_register(0x19) & 0x80) != 0) {
			cpu.irq();
		}

		skipper++;

		/* are we at the next raster line? */
		// if (cpu_->cycles() >= next_raster_at_)
		if ((skipper % 10) == 0) {
			int rstr = raster_counter();
			/* check raster IRQs */
			if (raster_irq_enabled() && rstr == raster_irq_) {
				/* set interrupt origin (raster) */
				irq_status_ |= (1 << 0);
				/* raise interrupt */
				cpu.irq();
			}
			if (rstr >= kFirstVisibleLine && rstr < kLastVisibleLine) {
				/* draw border */
				int screen_y = rstr - kFirstVisibleLine;
				// TODO:
				// io_->screen_draw_border(screen_y,border_color_);

				/* draw raster on current graphic mode */
				switch (graphic_mode_) {
				case kCharMode:
					renderScanlineCharMono();
					renderSprites();
					break;
				case kMCCharMode:
					renderScanlineCharCol();
					renderSprites();
					break;
				case kBitmapMode:
				case kMCBitmapMode:
					 //draw_raster_bitmap_mode();
					//System.out.println("Bitmap mode");

					break;
				default:
					// D("unsupported graphic mode: %d\n",graphic_mode_);
					return false;
				}
				/* draw sprites */
				// draw_raster_sprites();
			}
			/* next raster */
			if (is_bad_line())
				next_raster_at_ += kBadLineCycles;
			else
				next_raster_at_ += kLineCycles;
			/* update raster */
			raster_counter(++rstr);
			if (rstr >= kScreenLines) {
				// TODO:
				// io_->screen_refresh();
				bd.repaint();

				frame_c_++;

				raster_counter(0);
			}
		}
		return true;
	}

	public void VICStub(CPU6502 cpu, BasicDisplay bd) {

		// Raster line
		// int val = cpu.io[0xD012];
		// val = (val + 1) & 0xff;
		// cpu.io[ADDR_RASTER_D012] = val;
		// ram[0xd012] = val;

		// renderScanline();

		// if (y==0) cpu.irq();
	}

	static final int kBaseAddrChars = 0xd000;

	// public int vic_read_byte(int addr)
	// {
	// int v;
	//
	// int vic_addr = cpu.cia2.vic_base_address() + (addr & 0x3fff);
	// if((vic_addr >= 0x1000 && vic_addr < 0x2000) ||
	// (vic_addr >= 0x9000 && vic_addr < 0xa000))
	// v = cpu.mem.ROM[kBaseAddrChars + (vic_addr & 0xfff)];
	// else
	// v = cpu.mem.RAM[vic_addr];
	// return v;
	// }

	public int vic_read_byte(int addr) {
		int v;

		int vic_addr = rig.cia2.vic_base_address() + (addr & 0x3fff);

		if ((vic_addr >= 0x1000 && vic_addr < 0x2000) || (vic_addr >= 0x9000 && vic_addr < 0xa000))
			v = cpu.mem.ROM[kBaseAddrChars + (vic_addr & 0xfff)];
		else
			v = cpu.mem.RAM[vic_addr];
		return v;
	}

	public void renderScanlineCharMono() {
		int x = 0;
		int rstr = raster_counter();
		int y = rstr - kFirstVisibleLine;
		int line = rstr-kGFirstLine;
		scale = 2;

		ADDR_SCREEN_RAM = screen_mem_; // looks good
		ADDR_CHARACTER_ROM = char_mem_;

		 if( ! ((rstr >= kGFirstLine) && 
			     (rstr < kGLastLine)) ) return;
		
		for (x = 0; x < 320; x++) {

			int charOffset = (x >> 3) + (((line) >> 3) * 40);
			int charIndex = vic_read_byte(ADDR_SCREEN_RAM + charOffset);

			int charColourId = (cpu.mem.COLOR_RAM[ADDR_COLOR_RAM + charOffset]);// & 0x07); // IO

			int scx = x % 8; // sub char pixel
			int scy = line % 8;
			int charScanlineByte = vic_read_byte(ADDR_CHARACTER_ROM + (charIndex * 8) + scy) & 0xff;

			Color c = C64Palette.palette[read_register(0x21) & 0x0F];

			if ((charScanlineByte & (128 >> scx)) > 0)
				c = C64Palette.palette[charColourId & 0x0F];

			bd.setDrawColor(c);
			bd.drawFilledRect((x+kGFirstCol) * scale, (y) * scale, scale, scale);

		}

//		if (y == 0 || y == 100)
//			bd.refresh();
	}

	public void renderScanlineCharCol() {
		int x = 0;
		int rstr = raster_counter();
		int y = rstr - kFirstVisibleLine;
		int line = rstr-kGFirstLine;
		
		scale = 2;

		ADDR_SCREEN_RAM = screen_mem_; // looks good
		ADDR_CHARACTER_ROM = char_mem_;
		 
		if( ! ((rstr >= kGFirstLine) && 
			     (rstr < kGLastLine)) ) return;
		 
		for (x = 0; x < 320; x += 2) {

			int charOffset = (x >> 3) + ((y >> 3) * 40);
			int charIndex = vic_read_byte(ADDR_SCREEN_RAM + charOffset);

			int charColourId = (cpu.mem.COLOR_RAM[ADDR_COLOR_RAM + charOffset]);// & 0x07); // IO

			int scx = x % 8; // sub char pixel
			int scy = line % 8;
			int charScanlineByte = vic_read_byte(ADDR_CHARACTER_ROM + (charIndex * 8) + scy) & 0xff;

			Color c = C64Palette.palette[read_register(0x21) & 0x0F];

			int combinedPixels = 0;
			if ((charScanlineByte & (128 >> scx)) > 0)
				combinedPixels += 2;
			if ((charScanlineByte & (64 >> scx)) > 0)
				combinedPixels += 1;

			// Resolve colour.
			switch (combinedPixels) {
			case 0:
				c = C64Palette.palette[bgcolor_[0] & 0x0F];
				break;
			case 1:
				c = C64Palette.palette[bgcolor_[1] & 0x0F];
				break;
			case 2:
				c = C64Palette.palette[bgcolor_[2] & 0x0F];
				break;
			case 3:
				//c = C64Palette.palette[combinedPixels];
				//c = C64Palette.palette[(charColourId>>6) & 0x0F];
				if ((charColourId&(1<<3))>0)
					c = C64Palette.palette[(charColourId) & 0x07];
				else
					c = C64Palette.palette[(charColourId) & 0x0F];
				break;
			}

			bd.setDrawColor(c);
			bd.drawFilledRect((x+kGFirstCol) * scale, y * scale, scale, scale);
			bd.drawFilledRect((x+kGFirstCol + 1) * scale, y * scale, scale, scale);

		}

//		if (y == 0 || y == 100)
//			bd.refresh();
	}

	static int kSpriteWidth = 24;
	static int kSpriteHeight = 21;
	static int kSpriteSize = 64;
	static int kSpritesFirstLine = 6;
	static int kSpritesFirstCol = 18;

	int sprite_x(int n)
	{
	  int x = mx_[n];
	  if(ISSET_BIT(msbx_,n))
	    x |= 1 << 8;
	  return x;
	}
	
	public void renderSprites() {
		int y = raster_counter() - kFirstVisibleLine;

		int sp_y = raster_counter() - kSpritesFirstLine;
		
		for (int i = 7; i >= 0; i--) {
			int sprCol = cpu.mem.peek(53287+i);
			int height = testBit(sprite_double_height_, i) ? kSpriteHeight * 2 : kSpriteHeight;
			int row = sp_y - my_[i];
			
			if (!testBit(sprite_enabled_, i)) continue;
			
			if (sp_y > my_[i] && sp_y < my_[i] + height) {
				if (testBit(sprite_multicolor_, i)) {
					drawSpriteScanlineMulticolour(i, sprite_x(i), y, row, sprCol);
				}
				else
				{
					drawSpriteScanlineMono(i, sprite_x(i), y, row, sprCol);
				}
			}

		}

	}

	// static const int kSpritesFirstLine = 6;
	// static const int kSpritesFirstCol = 18;

	public void drawSpriteScanlineMono(int id, int xpos, int ypos, int row, int sprCol) {
		bd.setDrawColor(C64Palette.palette[sprCol & 0x0F]);

		int kSpriteSize = 64;
		int sprAddr = kSpriteSize * vic_read_byte(screen_mem_ + kSpritePtrsOffset + id);
		int offset = 0;
		for (int b = 0; b < 3; b++) { // data byte
			int data = vic_read_byte(sprAddr + b + ((row) * 3));
			for (int bb = 0; bb < 8; bb++) { // byte bit
				if (testBit(data, 7 - bb))
					bd.drawFilledRect((xpos + (offset)) * scale, (ypos) * scale, scale, scale);

				offset++;
			}
		}
	}
	
	public void drawSpriteScanlineMulticolour(int id, int xpos, int ypos, int row, int sprCol) {
		
		Color c1 = C64Palette.palette[sprCol & 0x0F];
		Color c2 = C64Palette.palette[cpu.mem.peek(53285) & 0x0F];
		Color c3 = C64Palette.palette[cpu.mem.peek(53286) & 0x0F];
		
		int kSpriteSize = 64;
		int sprAddr = kSpriteSize * vic_read_byte(screen_mem_ + kSpritePtrsOffset + id);
		int offset = 0;
		for (int b = 0; b < 3; b++) { // data byte
			int data = vic_read_byte(sprAddr + b + ((row) * 3));
			for (int bb = 0; bb < 8; bb+=2) { // byte bit
				int combined = 0;
				combined += testBit(data, 7 - bb)?1:0;
				combined += testBit(data, 6 - bb)?2:0;
				
//				53285 Sprite multicolor 1 (only in multicolor mode)
//				53286 Sprite multicolor 2 (only in multicolor mode)
//				53287 Color of sprite 0
				
				switch (combined) {
				case 0:
					offset+=2;
					continue;
				case 1:
					bd.setDrawColor(c1);
					break;
				case 2:
					bd.setDrawColor(c2);
					break;
				case 3:
					bd.setDrawColor(c3);
					break;
				}
				bd.drawFilledRect((xpos + (offset)) * scale, (ypos) * scale, scale, scale);
				bd.drawFilledRect(((xpos+1) + (offset)) * scale, (ypos) * scale, scale, scale);

				offset+=2;
			}
		}
	}

	int read_register(int r) {
		int retval;
		switch (r) {
		/* get X coord of sprite n */
		case 0x0:
		case 0x2:
		case 0x4:
		case 0x6:
		case 0x8:
		case 0xc:
		case 0xe:
			retval = mx_[r >> 1];
			break;
		/* get Y coord of sprite n */
		case 0x1:
		case 0x3:
		case 0x5:
		case 0x7:
		case 0x9:
		case 0xb:
		case 0xd:
		case 0xf:
			retval = my_[r >> 1];
			break;
		/* MSBs of sprites X coordinates */
		case 0x10:
			retval = msbx_;
			break;
		/* control register 1 */
		case 0x11:
			retval = cr1_;
			break;
		/* raster counter */
		case 0x12:
			retval = raster_c_;
			break;
		/* sprite enable register */
		case 0x15:
			retval = sprite_enabled_;
			break;
		/* control register 2 */
		case 0x16:
			retval = cr2_;
			break;
		/* sprite double height */
		case 0x17:
			retval = sprite_double_height_;
			break;
		/* memory pointers */
		case 0x18:
			retval = mem_pointers_;
			break;
		/**
		 * interrupt status register IRQ| - | - | - | ILP|IMMC|IMBC|IRST|
		 */
		case 0x19:
			retval = (0xf & irq_status_);
			if (retval != 0)
				retval |= 0x80; // IRQ bit
			retval |= 0x70; // non-connected bits (always set)
			break;
		/**
		 * interrupt enable register - | - | - | - | ELP|EMMC|EMBC|ERST|
		 */
		case 0x1a:
			retval = (0xf0 | irq_enabled_);
			break;
		/* sprite priority register */
		case 0x1b:
			retval = sprite_priority_;
			break;
		/* sprite multicolor mode */
		case 0x1c:
			retval = sprite_multicolor_;
			break;
		/* sprite double width */
		case 0x1d:
			retval = sprite_double_width_;
			break;
		/* border color */
		case 0x20:
			retval = border_color_;
			break;
		/* background colors */
		case 0x21:
		case 0x22:
		case 0x23:
		case 0x24:
			retval = bgcolor_[r - 0x21];
			break;
		/* sprite colors */
		case 0x25:
		case 0x26:
			retval = sprite_shared_colors_[r - 0x25];
			break;
		case 0x27:
		case 0x28:
		case 0x29:
		case 0x2a:
		case 0x2b:
		case 0x2c:
		case 0x2d:
		case 0x2e:
			retval = sprite_colors_[r - 0x27];
			break;
		/* unused */
		case 0x2f:
		case 0x30:
		case 0x31:
		case 0x32:
		case 0x33:
		case 0x34:
		case 0x35:
		case 0x36:
		case 0x37:
		case 0x38:
		case 0x39:
		case 0x3a:
		case 0x3b:
		case 0x3c:
		case 0x3d:
		case 0x3e:
		case 0x3f:
		default:
			retval = 0xff;
			break;
		}
		return retval;
	}

	void write_register(int r, int v) {
		switch (r) {
		/* store X coord of sprite n */
		case 0x0:
		case 0x2:
		case 0x4:
		case 0x6:
		case 0x8:
		case 0xc:
		case 0xe:
			mx_[r >> 1] = v;
			break;
		/* store Y coord of sprite n */
		case 0x1:
		case 0x3:
		case 0x5:
		case 0x7:
		case 0x9:
		case 0xb:
		case 0xd:
		case 0xf:
			my_[r >> 1] = v;
			break;
		/* MSBs of X coordinates */
		case 0x10:
			msbx_ = v;
			break;
		/* control register 1 */
		case 0x11:
			cr1_ = (v & 0x7f);
			raster_irq_ &= 0xff;
			raster_irq_ |= (v & 0x80) << 1;
			calculateGraphicsMode();
			break;
		/* raster irq */
		case 0x12:
			raster_irq_ = v | (raster_irq_ & (1 << 8));
			break;
		/* sprite enable register */
		case 0x15:
			sprite_enabled_ = v;
			break;
		/* control register 2 */
		case 0x16:
			cr2_ = v;
			calculateGraphicsMode();
			break;
		/* sprite double height */
		case 0x17:
			sprite_double_height_ = v;
			break;
		/* memory pointers */
		case 0x18:
			/* bits ----xxx- */
			char_mem_ = (v & 0xe) << 10;
			/* bits xxxx---- */
			screen_mem_ = (v & 0xf0) << 6;
			/* bit ----x--- */
			bitmap_mem_ = (v & 0x8) << 10;
			/* save reg value (last bit always set) */
			mem_pointers_ = v | (1 << 0);
			break;
		/* interrupt request register */
		case 0x19:
			/* acknowledge interrupts by mask */
			irq_status_ &= ((~(v & 0xf)) & 0xff);
			break;
		/* interrupt enable register */
		case 0x1a:
			irq_enabled_ = v;
			break;
		/* sprite priority register */
		case 0x1b:
			sprite_priority_ = v;
			break;
		/* sprite multicolor mode */
		case 0x1c:
			sprite_multicolor_ = v;
			break;
		/* sprite double width */
		case 0x1d:
			sprite_double_width_ = v;
			break;
		/* border color */
		case 0x20:
			border_color_ = v;
			break;
		/* background colors */
		case 0x21:
		case 0x22:
		case 0x23:
		case 0x24:
			bgcolor_[r - 0x21] = v;
			break;
		/* sprite colors */
		case 0x25:
		case 0x26:
			sprite_shared_colors_[r - 0x25] = v;
			break;
		case 0x27:
		case 0x28:
		case 0x29:
		case 0x2a:
		case 0x2b:
		case 0x2c:
		case 0x2d:
		case 0x2e:
			sprite_colors_[r - 0x27] = v;
			break;
		/* unused */
		case 0x2f:
		case 0x30:
		case 0x31:
		case 0x32:
		case 0x33:
		case 0x34:
		case 0x35:
		case 0x36:
		case 0x37:
		case 0x38:
		case 0x39:
		case 0x3a:
		case 0x3b:
		case 0x3c:
		case 0x3d:
		case 0x3e:
		case 0x3f:
		default:
			break;
		}
	}

	public boolean testBit(int v, int b) {
		if ((v & (1 << b)) != 0)
			return true;
		return false;
	}
}
