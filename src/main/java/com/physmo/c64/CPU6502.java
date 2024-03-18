package com.physmo.c64;

import com.physmo.c64.microcode.CodeTableManager;
import com.physmo.c64.microcode.MicroOp;

import java.io.IOException;

public class CPU6502 {

    public static int FLAG_CARRY = 0b00000001;
    public static int FLAG_ZERO = 0b00000010;
    public static int FLAG_INTERRUPT_DISABLE = 0b00000100;
    public static int FLAG_DECIMAL_MODE = 0b00001000;
    public static int FLAG_BREAK = 0b00010000;
    public static int FLAG_UNUSED = 0b00100000;
    public static int FLAG_OVERFLOW = 0b01000000;
    public static int FLAG_NEGATIVE = 0b10000000;
    private static String romPath = "/Users/nick/dev/emulatorsupportfiles/c64/rom/";
    public int firstUnimplimentedInstructionIndex = -1;
    public boolean lg = false;
    public boolean debugOutput = true;
    public boolean debugOutputIo = false;
    public int debugCallIndex = 0;
    public String stateString = "";
    public boolean simulateKeypress = false;
    public boolean printInstructions = false;
    CodeTableManager codeTableManager = new CodeTableManager();
    MEMC64 mem = null;
    int A; // Accumulator register
    int X; // X register
    // int debugAtAddress = 0xE394; // *** BASIC cold start entry point
    // int debugAtAddress = 0xE421; // E421
    // int debugAtAddress = 0xE5D6; // Key detected?
    // int debugAtAddress = 0xB8D7;
    int Y; // Y register
    int SP = 0xFF; // Stack pointer
    int PC; // Program counter
    int FL; // Flags
    int SB = 0x0100; // Stack base
    int interruptOcurred = 0;
    boolean unitTest = false;
    int debugAtAddress = -1; // Disable
    int cycles = 0;
    int tickCount = 0;
    int previousPC = 0;
    int addressBus = 0;
    int dataBus = 0;
    String addressString = "";

    public void attachHardware(MEMC64 mem) {
        this.mem = mem;
    }

    // Call instead of reset to load and run unit tests.
    public void resetAndTest() {
        int prgLoc = 0;

        try {
            // NOTE: the 65C02 is a different chip so we don't use that test file.
            Utils.ReadFileBytesToRAMMemoryLocation("resource/tests/6502_functional_test-2.bin", this, 0x000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 0 = 0x2f / was ff
        mem.RAM[0] = 0x2F; // set CPU control lines (for bank switching)
        mem.RAM[1] = 0x07; // 07 set CPU control lines (for bank switching)
        FL = 0;

        // start debug
        PC = 0x0400;
        debugOutput = true;
    }

    public void reset() {
        int prgLoc = 0;

        try {
            Utils.ReadFileBytesToROMMemoryLocation(romPath + "kernal.901227-03.bin", this, 0xE000); // 8192 bytes
            Utils.ReadFileBytesToROMMemoryLocation(romPath + "basic.901226-01.bin", this, 0xA000); //
            Utils.ReadFileBytesToROMMemoryLocation(romPath + "c64.bin", this, 0xD000); // Character set
        } catch (IOException e) {
            e.printStackTrace();
        }

        // https://blog.fynydd.com/crash-course-on-emulating-the-mos-6510-cpu/
        // According to this site, bits 0 and 1 should be initialised to 0xff and 0x07
        mem.RAM[0] = 0x2F; // set CPU control lines (for bank switching)
        mem.RAM[1] = 0x07; // 07 set CPU control lines (for bank switching)

        // Another site says 0x2f and 0x37
        // mem.RAM[0] = 0x2F; // set CPU control lines (for bank switching)
        // mem.RAM[1] = 0x37; // 07 set CPU control lines (for bank switching)

        mem.RAM[0xD018] = 16; // set screen text location???
        mem.RAM[0x02A6] = 0; // PAL/NTSC Flag, O= NTSC, 1 = PAL
        // mem.IO[0xD018] = 16; // set screen text location???
        // mem.IO[0x02A6] = 1; // PAL/NTSC Flag, O= NTSC, 1 = PAL

        // Init CIA2 values
        // mem.IO[0xff + (0xDD00 & 0x0F)] = 0xC7;
        // mem.IO[0xDD00] = 0xC7;
        mem.RAM[0xDD00] = 0xC7;

        // On reset, the processor will read address $FFFC and $FFFD
        PC = getWord(0xFFFC); // This contains 0xFCE2

        FL = 0;

        // setFlag5();
        setFlag(FLAG_UNUSED);
        unsetFlag(FLAG_DECIMAL_MODE);
        setFlag(FLAG_INTERRUPT_DISABLE);

    }

    private String buildDebugAddressString() {
        return "&" + Utils.toHex4(addressBus) + ":0x" + Utils.toHex2(mem.peek(addressBus)) + "  PLA:" + Utils.toHex2(mem.RAM[1]);
    }


    private void doMicroOp(MicroOp op) {

        int wrk = 0;

        switch (op) {

            case BRK:
                // NOTE: not sure if PC needs +1 here
                //
                pushWord(PC + 1); // PC+1
                pushByte(FL | FLAG_BREAK | FLAG_UNUSED);
                PC = getWord(0xfffe);
                setFlag(FLAG_INTERRUPT_DISABLE);
                setFlag(FLAG_BREAK);
                break;
            case NOP:
                break;
            /*
                Addressing modes
            */
            case SET_ADDRESS_ABSOLUTE:
                // $nnnn
                addressBus = getNextWord();
                addressString = buildDebugAddressString();
                break;
            case SET_ADDRESS_ABSOLUTE_INDIRECT:
                wrk = getNextWord();
                addressBus = mem.peekWord(wrk);
                addressString = buildDebugAddressString();
                break;
            case SET_ADDRESS_ABSOLUTE_X:
                // $nnnn,X
                addressBus = (getNextWord() + X) & 0xFFFF;
                addressString = buildDebugAddressString();
                break;
            case SET_ADDRESS_ABSOLUTE_Y:
                // $nnnn,Y
                addressBus = (getNextWord() + Y) & 0xFFFF;
                addressString = buildDebugAddressString();
                break;
            case SET_ADDRESS_ZERO_PAGE:
                // $nn
                addressBus = getNextByte() & 0xFF;
                addressString = buildDebugAddressString();
                break;
            case SET_ADDRESS_ZERO_PAGE_X:
                // $nn,X
                addressBus = ((getNextByte() + X) & 0xFF);
                addressString = buildDebugAddressString();
                break;
            case SET_ADDRESS_ZERO_PAGE_Y:
                // $nn,X
                addressBus = ((getNextByte() + Y) & 0xFF);
                addressString = buildDebugAddressString();
                break;
            case SET_ADDRESS_ZERO_PAGE_INDIRECT_X:
                // ($nn,X)
                wrk = ((getNextByte() + X) & 0xFF);
                addressBus = mem.peek(wrk) | (mem.peek(wrk + 1) << 8);
                addressString = "ZPG:" + Utils.toHex2(wrk) + "  " + buildDebugAddressString();
                break;
            case SET_ADDRESS_ZERO_PAGE_INDIRECT_Y:
                // ($nn),Y
                wrk = ((getNextByte()) & 0xFF);
                addressBus = mem.peek(wrk) | (mem.peek(wrk + 1) << 8);
                addressBus = (addressBus + Y) & 0xFFFF;
                addressString = "ZPG:" + Utils.toHex2(wrk) + "  " + buildDebugAddressString();
                break;
            case SET_ADDRESS_RELATIVE:
                wrk = ((getNextByte()) & 0xFF);
                addressBus = addSignedByteToWord(PC, wrk);
                addressString = buildDebugAddressString();
                break;

            case GET_NEXT_BYTE:
                dataBus = getNextByte() & 0xFF;
                addressString = "0x" + Utils.toHex2(dataBus);
                break;

            case FETCH_BYTE_FROM_ADDRESS:
                dataBus = mem.peek(addressBus);
                break;
            case STORE_BYTE_AT_ADDRESS:
                mem.poke(addressBus, dataBus);
                break;
            case FETCH_A:
                dataBus = A;
                break;
            case STORE_A:
                setA(dataBus);
                break;
            case AND:
                setA(A & dataBus);
                break;
            case ORA:
                setA(A | dataBus);
                break;
            case EOR:
                wrk = (dataBus & 0xff) ^ (A & 0xff);
                setA(wrk);
                break;
            case STA:
                mem.poke(addressBus, A);
                break;
            case STX:
                mem.poke(addressBus, X);
                break;
            case STY:
                mem.poke(addressBus, Y);
                break;

            // LOADS
            case LDA:
                setA(dataBus);
                break;
            case LDX:
                setX(dataBus);
                break;
            case LDY:
                setY(dataBus);
                break;
            case INC:
                // Increment memory by 1
                wrk = mem.peek(addressBus);
                wrk = (wrk + 1) & 0xff;
                wrk = setFlagsFromValue(wrk);
                mem.poke(addressBus, wrk);
                break;
            case DEC:
                wrk = mem.peek(addressBus);
                wrk = (wrk - 1) & 0xff;
                wrk = setFlagsFromValue(wrk);
                mem.poke(addressBus, wrk);
                break;
            // COMPARE
            case CMP:
                compare(A, dataBus);
                break;
            case CPX:
                compare(X, dataBus);
                break;
            case CPY:
                compare(Y, dataBus);
                break;

            // TRANSFERS
            case TAX:
                setX(A);
                break;
            case TXA:
                setA(X);
                break;
            case TAY:
                setY(A);
                break;
            case TYA:
                setA(Y);
                break;
            case TXS:
                SP = X & 0xff;
                break;
            case TSX:
                setX(SP & 0xff);
                break;

            // FLAG OPERATIONS
            case CLC:
                setFlagConditional(FLAG_CARRY, false);
                break;
            case CLD:
                setFlagConditional(FLAG_DECIMAL_MODE, false);
                break;
            case CLI:
                setFlagConditional(FLAG_INTERRUPT_DISABLE, false);
                break;
            case CLV:
                setFlagConditional(FLAG_OVERFLOW, false);
                break;
            case SEC:
                setFlagConditional(FLAG_CARRY, true);
                break;
            case SED:
                setFlagConditional(FLAG_DECIMAL_MODE, true);
                break;
            case SEI:
                setFlagConditional(FLAG_INTERRUPT_DISABLE, true);
                break;

            // JUMPS
            case JSR:
                pushWord(PC - 1);
                PC = addressBus;
                break;
            case JMP:
                PC = addressBus;
                break;
            // RETURN
            case RTS:
                PC = popWord() + 1;
                break;
            case RTI:
                // P from Stack, PC from Stack
                FL = popByte(); // pushByte(FL);
                PC = popWord(); // pushWord(PC);
                break;
            // BRANCH - BCC, BCS, BEQ, BMI, BNE, BPL, BVC, BVS
            case BCC:
                branchConditional(addressBus, (FL & FLAG_CARRY) == 0);
                break;
            case BCS:
                branchConditional(addressBus, (FL & FLAG_CARRY) > 0);
                break;
            case BEQ:
                branchConditional(addressBus, (FL & FLAG_ZERO) != 0);
                break;
            case BNE:
                branchConditional(addressBus, (FL & FLAG_ZERO) == 0);
                break;
            case BMI:
                branchConditional(addressBus, (FL & FLAG_NEGATIVE) > 0);
                break;
            case BPL:
                branchConditional(addressBus, (FL & FLAG_NEGATIVE) == 0);
                break;
            case BVC:
                branchConditional(addressBus, (FL & FLAG_OVERFLOW) == 0);
                break;
            case BVS:
                branchConditional(addressBus, (FL & FLAG_OVERFLOW) > 0);
                break;

                // BINARY
            case BIT:
                opBIT(dataBus);
                break;
            // STACK
            case PHA:
                pushByte(A);
                break;
            case PHP:
                pushByte(FL | FLAG_BREAK | FLAG_UNUSED);// | 0x10);
                break;
            case PLA:
                setA(popByte());
                break;
            case PLP:
                FL = popByte();
                break;

            case DEX:
                if (X == 0)
                    setX(0xff);
                else
                    setX(X - 1);
                break;
            case DEY:
                if (Y == 0)
                    setY(0xff);
                else
                    setY(Y - 1);
                break;
            case INY:
                if (Y == 0xff)
                    setY(0);
                else
                    setY(Y + 1);
                break;
            case INX:
                if (X == 0xff)
                    setX(0);
                else
                    setX(X + 1);
                break;

            case ROL:
                wrk = (dataBus & 0xff) << 1;
                if ((FL & 1) > 0)
                    wrk += 1;

                setFlagConditional(FLAG_CARRY, (wrk & 0x100) > 0);

                setFlagsFromValue(wrk);
                wrk = wrk & 0xff;

                // setA(val);
                //mem.poke(ac.addr, wrk);
                dataBus = wrk;

                break;

            case ROR:
                int lsb = dataBus & 1;
                wrk = (dataBus & 0xff) >> 1;

                if ((FL & FLAG_CARRY) > 0) {
                    wrk = wrk + 0x80;
                }

                setFlagConditional(FLAG_CARRY, lsb > 0);

                setFlagsFromValue(wrk);
                wrk = wrk & 0xff;
                dataBus = wrk;

                break;
            case ASL:
                wrk = (dataBus & 0xff) << 1;

                setFlagConditional(FLAG_CARRY, wrk > 0xff);

                setFlagsFromValue(wrk);
                wrk = wrk & 0xff;

                dataBus = wrk;
                break;
            case LSR:
                wrk = dataBus >> 1;

                setFlagConditional(FLAG_CARRY, (dataBus & 1) != 0);

                unsetFlag(FLAG_NEGATIVE);

                setFlagConditional(FLAG_ZERO, wrk == 0);
                dataBus = wrk;
                break;
            case ADC:
                setA(addWithCarry(dataBus, A));
                break;
            case SBC:
                wrk = dataBus;
                wrk = subtractWithCarry(A, wrk & 0xff);
                setA(wrk & 0xff);
                break;

            default:
                System.out.println("MicroOp not implemented: " + op.name());
        }
    }

    public void tick2() {
        cycles += 4;
        tickCount++;


        int startinggPC = PC;
        int currentInstruction = mem.peek(PC++);

        String hardwareState = Debug.getHardwareSummary(this);

        String instructionName = codeTableManager.codeTableMain.getInstructionName(currentInstruction);
        String dbg = "PC:0x" + Utils.toHex4(startinggPC) + "  " + hardwareState + "  0x" + Utils.toHex2(currentInstruction) + " " + instructionName;
        addressString = "";

        MicroOp[] microOps = codeTableManager.codeTableMain.getInstructionCode(currentInstruction);

        for (MicroOp microOp : microOps) {
            doMicroOp(microOp);
        }

        dbg += "  " + addressString;
        System.out.println(dbg);
    }


    public void tick(int callIndex) {

        int val;

        cycles += 4;
        tickCount++;

        // if (PC==debugAtAddress) debugOutput = true;
        // if((FL&FLAG_OVERFLOW)>0) debugOutput = true;

        // if (PC==debugAtAddress) Debug.dumpMemory(this, 0);
        // debugOutput = true;

        // if (tickCount>700000*4) debugOutput = true;
        // if (PC==0xbdcd) debugOutput=true;

        if (unitTest == true && tickCount > 700000 * 43)
            debugOutput = true;

        int entryPC = PC;
        int currentInstruction = mem.peek(PC++);
        // int oprnd=0, val=0, addr=0;
        debugCallIndex = callIndex;

        AddressContainer ac = new AddressContainer(); // helper object

        // This is time consuming - diable if not required.
        // buildStateString(); // Record machine state before operation for debug
        // output.

        // find op enum
        InstructionDefinition def = InstructionDefinition.getEnumFromId(currentInstruction);
        if (def == null) {
            System.out.println("Unhandled opcode: " + Utils.toHex2(currentInstruction));
            //Debug.logUnhandled(this, " enum: " + Utils.toHex2(currentInstruction), callIndex, entryPC);
            return;
        }

        // setFlag5();
        setFlag(FLAG_UNUSED);


        // String defDescription = def.getDescription();
        // System.out.println(defDescription);
        COMMAND command = def.getCommand();
        ADDRMODE addrmode = def.getAddressMode();
        processAddressMode(ac, addrmode);

        // .C:fcea D0 03 BNE $FCEF - A:30 X:05 Y:0A SP:ff ..-..I.C 39
        if (this.debugOutput && previousPC != entryPC) {// && !debugSkipLine(entryPC)) {
            Debug.buildStateString(this);
            String output = Utils.toHex4(entryPC) + "  ";
            output += Utils.toHex2(currentInstruction) + ac.bytesRead;
            output = Utils.padToLength(output, 15);
            output += def.getDescription();
            output = Utils.padToLength(output, 30) + stateString;
            output += " " + tickCount;
            System.out.println(output);
            //System.out.println("previousPC!=entryPC" + previousPC + "   " + entryPC);
        }

        switch (command) {
            case BRK:
                // NOTE: not sure if PC needs +1 here
                //
                pushWord(PC + 1); // PC+1
                pushByte(FL | FLAG_BREAK | FLAG_UNUSED);
                PC = getWord(0xfffe);
                setFlag(FLAG_INTERRUPT_DISABLE);
                setFlag(FLAG_BREAK);
                break;
            case LDX:
                setX(ac.val);
                break;
            case TXS:
                SP = X & 0xff;
                break;
            case TSX:
                setX(SP & 0xff);

                break;
            case CLD:
                unsetFlag(FLAG_DECIMAL_MODE);
                break;
            case JSR:
                pushWord(PC - 1);
                PC = ac.addr;
                break;
            case RTS:
                PC = popWord() + 1;
                if (PC == -1)
                    System.out.println("fuck b");
                break;
            case LDA:
                setA(ac.val);
                break;
            case CMP:
                compare(A, ac.val);
                break;
            case BNE:
                if ((FL & 2) == 0) {
                    PC = ac.addr;
                    if (PC == -1)
                        System.out.println("fuck c");
                }
                break;

            case STX:
                mem.poke(ac.addr, X);
                break;
            case STA:
                mem.poke(ac.addr, A);
                break;
            case DEX:
                if (X == 0)
                    setX(0xff);
                else
                    setX(X - 1);
                break;
            case DEY:
                if (Y == 0)
                    setY(0xff);
                else
                    setY(Y - 1);
                break;
            case INY:
                if (Y == 0xff)
                    setY(0);
                else
                    setY(Y + 1);
                break;
            case INX:
                if (X == 0xff)
                    setX(0);
                else
                    setX(X + 1);
                break;
            case BEQ:
                if ((FL & (2)) == 2) {
                    PC = ac.addr;
                }
                break;
            case JMP:
                PC = ac.addr;
                break;
            case AND:
                setA(A & ac.val);
                break;
            case ORA:
                setA(A | ac.val);

                break;
            case TAY:
                setY(A);
                break;
            case TYA:
                setA(Y);
                break;
            case LDY:
                setY(ac.val);
                break;
            case STY:
                mem.poke(ac.addr, Y);
                break;
            case INC:
                val = (ac.val + 1) & 0xff;
                val = setFlagsFromValue(val);
                mem.poke(ac.addr, val);
                break;
            case DEC:
                val = ac.val;
                val = (val - 1) & 0xff;
                val = setFlagsFromValue(val);
                mem.poke(ac.addr, val);
                break;

            case TAX:
                setX(A);
                break;
            case TXA:
                setA(X);
                break;
            case CLC:
                unsetFlag(FLAG_CARRY);
                break;
            case BCS: // Branch if carry set
                if ((FL & FLAG_CARRY) > 0) {
                    PC = ac.addr;
                }
                break;
            case BVS: // Branch if overflow set.
                if ((FL & FLAG_OVERFLOW) > 0) {
                    PC = ac.addr;
                }
                break;
            case BVC: // Branch if overflow set.
                if ((FL & FLAG_OVERFLOW) == 0) {
                    PC = ac.addr;
                }
                break;
            case BPL:
                if ((FL & 0x80) == 0) {
                    PC = ac.addr;
                }
                break;
            case ADC:
                setA(addWithCarry(ac.val, A));
                break;
            case BCC:
                if ((FL & 1) == 0) {
                    PC = ac.addr;
                }
                break;
            case CPX:
                compare(X, ac.val);
                break;
            case BMI:
                if ((FL & 0x80) > 0) {
                    PC = ac.addr;
                }
                break;
            case SEI:
                setFlag(FLAG_INTERRUPT_DISABLE);
                break;
            case CLI:
                unsetFlag(FLAG_INTERRUPT_DISABLE);
                break;
            case CLV:
                unsetFlag(FLAG_OVERFLOW);
                break;
            case SEC:
                setFlag(FLAG_CARRY);
                break;
            case SED:
                setFlag(FLAG_DECIMAL_MODE);
                break;
            case CPY:
                compare(Y, ac.val);
                break;
            case PHA:
                pushByte(A);
                break;
            case PLA:
                setA(popByte());
                break;
            case INS:
                // Illigal opcode:
                // This opcode INCs the contents of a memory location and
                // then SBCs the result from the A register.
                val = (ac.val + 1) & 0xff;
                val = setFlagsFromValue(val);
                mem.poke(ac.addr, val);

                val = subtractWithCarry(A, val & 0xff);
                setA(val & 0xff);

                break;
            case ASO:
                // Illigal opcode:
                // This opcode ASLs the contents of a memory location and then
                // ORs the result with the accumulator.
                val = (ac.val & 0xff) << 1;
                setFlagConditional(FLAG_CARRY, val > 0xff);
                setFlagsFromValue(val);
                val = val & 0xff;

                mem.poke(ac.addr, val); // Should we update the memory location too?

                setA(A | ac.val);

                break;
            case ASL:

                val = (ac.val & 0xff) << 1;

                setFlagConditional(FLAG_CARRY, val > 0xff);

                setFlagsFromValue(val);
                val = val & 0xff;

                mem.poke(ac.addr, val);
                break;
            case ROL:
                val = (ac.val & 0xff) << 1;
                if ((FL & 1) > 0)
                    val += 1;

                setFlagConditional(FLAG_CARRY, (val & 0x100) > 0);

                setFlagsFromValue(val);
                val = val & 0xff;

                // setA(val);
                mem.poke(ac.addr, val);

                break;

            case ROR:
                int lsb = ac.val & 1;
                val = (ac.val & 0xff) >> 1;

                if ((FL & FLAG_CARRY) > 0) {
                    val = val + 0x80;
                }

                setFlagConditional(FLAG_CARRY, lsb > 0);

                setFlagsFromValue(val);
                val = val & 0xff;

                mem.poke(ac.addr, val);

                break;
            case SBC:
                val = ac.val;
                val = subtractWithCarry(A, val & 0xff);
                setA(val & 0xff);
                break;
            case PHP:
                pushByte(FL | FLAG_BREAK | FLAG_UNUSED);// | 0x10);
                break;
            case PLP:
                FL = popByte();
                break;
            case BIT:
                val = ac.val; // peek(oprnd);
                opBIT(val);
                break;
            case LSR:
                val = ac.val >> 1;

                setFlagConditional(FLAG_CARRY, (ac.val & 1) != 0);

                mem.poke(ac.addr, val);

                unsetFlag(FLAG_NEGATIVE);

                setFlagConditional(FLAG_ZERO, val == 0);
                // uint8_t t = v >> 1;
                // cf((v&0x1)!=0);
                // SET_ZF(t);
                // SET_NF(t);

                break;
            case EOR:
                val = (ac.val & 0xff) ^ (A & 0xff);
                setA(val);
                break;
            case RTI:
                // P from Stack, PC from Stack
                FL = popByte(); // pushByte(FL);
                PC = popWord(); // pushWord(PC);
                break;
            case NOP:
                break;
            default:
                Debug.logUnhandled(this, "OPCODE ", callIndex, entryPC);
                System.out.println("unhandled");
                break;
        }
        previousPC = entryPC;
    }


    // trigger interrupt irq
    public void irq() {
        if (((FL & FLAG_INTERRUPT_DISABLE) == 0)) {
            unsetFlag(FLAG_BREAK);
            pushWord(PC);
            pushByte(FL & 0xef);
            // pushFlags();
            PC = getWord(0xfffe); // tempVal; 0xfffe
            // setFlagInterruptDisable();
            setFlag(FLAG_INTERRUPT_DISABLE);
            cycles += 5;
        }
    }

    // non-maskable interrupt
    public void nmi() {
        unsetFlag(FLAG_BREAK);
        pushWord(PC);
        pushByte(FL & 0xEF);
        // pushFlags();
        PC = getWord(0xfffa);
        // setFlag(FLAG_INTERRUPT_DISABLE);
        cycles += 5;
    }


    private void setFlag(int flag) {
        FL |= flag;
    }

    private void unsetFlag(int flag) {
        FL &= ~(flag);
    }

    private void setFlagConditional(int flag, boolean condition) {
        if (condition) {
            FL |= flag;
        } else {
            FL &= ~(flag);
        }
    }

    private void branchConditional(int address, boolean condition) {
        if (condition) PC = address;
    }


    // Compare values and set flags as result.
    public void compare(int v1, int v2) {

        int cmp = v1 - v2;

        setFlagConditional(FLAG_ZERO, cmp == 0);
        setFlagConditional(FLAG_CARRY, v1 >= v2);
        setFlagConditional(FLAG_NEGATIVE, (cmp & 0x80) > 0);

        // System.out.println(" campared "+Utils.toHex2(v1)+" to "+Utils.toHex2(v2));
    }

    // This is only for bytes and can modify the returned value.
    // NOTE ONLY affects ZERO and NEGATIVE flags.
    private int setFlagsFromValue(int val) {

        val = val & 0xff;
        setFlagConditional(FLAG_ZERO, val == 0);
        setFlagConditional(FLAG_NEGATIVE, (val & 0x80) > 0);

        return val & 0xff;
    }

    private int addWithCarry(int a, int v) {

        int t;

        if ((FL & FLAG_DECIMAL_MODE) > 0) {
            // System.out.println("Called ADC decimal mode");

            t = (a & 0xf) + (v & 0xf) + ((FL & FLAG_CARRY) > 0 ? 1 : 0);
            if (t > 0x09)
                t += 0x6;

            t += (a & 0xf0) + (v & 0xf0);
            if ((t & 0x1f0) > 0x90)
                t += 0x60;
        } else {
            t = a + v + ((FL & FLAG_CARRY) > 0 ? 1 : 0);
        }

        setFlagConditional(FLAG_CARRY, t > 0xff);

        t = t & 0xff;

        setFlagConditional(FLAG_OVERFLOW, (((a ^ v) & 0x80) == 0) && (((a ^ t) & 0x80) > 0));

        // setFlagConditionally(FLAG_ZERO, val == 0);

        return t & 0xff;
    }

    public int subtractWithCarry(int a, int v) {
        int t;
        if ((FL & FLAG_DECIMAL_MODE) > 0) {
            // System.out.println("decimal mode");

            t = (a & 0xf) - (v & 0xf) - ((FL & FLAG_CARRY) > 0 ? 0 : 1);

            if ((t & 0x10) != 0)
                t = ((t - 0x6) & 0xf) | ((a & 0xf0) - (v & 0xf0) - 0x10);
            else
                t = (t & 0xf) | ((a & 0xf0) - (v & 0xf0));

            if ((t & 0x100) != 0)
                t -= 0x60;

        } else {
            t = a - v - ((FL & FLAG_CARRY) > 0 ? 0 : 1);
        }

        setFlagConditional(FLAG_CARRY, t >= 0);

        setFlagConditional(FLAG_OVERFLOW, ((a ^ t) & 0x80) > 0 && ((a ^ v) & 0x80) > 0);

        return t & 0xff;
    }

    // Set register value and handle flags.
    public void setA(int val) {
        A = setFlagsFromValue(val);
    }

    public void setX(int val) {
        X = setFlagsFromValue(val);
    }

    public void setY(int val) {
        Y = setFlagsFromValue(val);
    }

    public int addSignedByteToWord(int wd, int bt) {
        if ((bt & 128) > 0) {
            bt ^= 0xff;
            bt = -(bt + 1);
        }

        return (wd + bt) & 0xffff;
    }

    public int getNextWord() {
        int oprnd = mem.peek(PC++) & 0xff;
        oprnd = (oprnd) + ((mem.peek(PC++) & 0xff) << 8);
        //System.out.println("getNextWord: 0x"+Utils.toHex4(oprnd));
        return oprnd;
    }

    public int getNextByte() {
        int oprnd = mem.peek(PC++) & 0xff;
        return oprnd;
    }

    // Return a word from memory.
    public int getWord(int addr) {
        int val = ((mem.peek(addr + 1) & 0xff) << 8) + (mem.peek(addr) & 0xff);
        return val;
    }

    public void pushByte(int v) {
        mem.RAM[SB + SP] = v & 0xff;
        SP = (SP - 1) & 0xff;
    }

    public void pushWord(int v) {
        // hi then low
        int v1 = v & 0xff;
        int v2 = (v >> 8) & 0xff;
        pushByte(v2);
        pushByte(v1);
    }

    public int popByte() {
        SP = (SP + 1) & 0xff;
        int val = mem.RAM[(SB + SP)] & 0xff;
        return val;
    }

    public int popWord() {
        int v1 = popByte();
        int v2 = popByte();
        return (v2 << 8) + v1;
    }

    public void opLDX(int val) {

        setX(val);
    }

    public void opBIT(int val) {
        int cmp = (A & 0xff) & (val & 0xff);

        setFlagConditional(FLAG_ZERO, cmp == 0);

        setFlagConditional(FLAG_OVERFLOW, (val & (1 << 6)) > 0);

        setFlagConditional(FLAG_NEGATIVE, (val & (1 << 7)) > 0);

    }

    // Pushing flags to the stack does some extra work.
    public void pushFlags() {
        // Pushing flags also sets the break flag.
        pushByte(FL | FLAG_BREAK | FLAG_UNUSED);
    }

    // based on the address mode, get some stuff, increase PC accordingly.
    public void processAddressMode(AddressContainer ac, ADDRMODE mode) {
        int peeked = 0;
        boolean enableBytesRead = true;
        switch (mode) {
            case IMDT:
                peeked = getNextByte();
                ac.val = peeked;
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked);
                break;
            case IMPL:
                break;
            case ABS:
                peeked = getNextWord();
                ac.addr = peeked;
                ac.val = mem.peek(ac.addr);
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked & 0xff) + " " + Utils.toHex2((peeked >> 8) & 0xff);
                break;
            case ABS_X:
                peeked = getNextWord();
                ac.addr = peeked + X;
                ac.val = mem.peek(ac.addr);
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked & 0xff) + " " + Utils.toHex2((peeked >> 8) & 0xff);
                break;
            case ABS_Y:
                peeked = getNextWord();
                ac.addr = peeked + Y;
                ac.val = mem.peek(ac.addr);
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked & 0xff) + " " + Utils.toHex2((peeked >> 8) & 0xff);
                break;
            case REL:
                peeked = getNextByte();
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked);
                ac.val = peeked;
                ac.addr = addSignedByteToWord(PC, ac.val);
                break;
            case ZPG:
                peeked = getNextByte();
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked);
                ac.addr = peeked;
                ac.val = mem.peek(peeked);
                break;
            case ZPG_X:
                peeked = getNextByte();
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked);
                ac.addr = (peeked + X) & 0xff;
                ac.val = mem.peek(ac.addr);
                break;
            case ZPG_Y:
                peeked = getNextByte();
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked);
                ac.addr = (peeked + Y) & 0xff;
                ac.val = mem.peek(ac.addr);
                break;
            case IND_Y:
                peeked = getNextByte();
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked);
                ac.addr = getWord(peeked) + Y;
                ac.val = mem.peek(ac.addr);
                break;
            case IND:
                peeked = getNextWord();
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked & 0xff) + " " + Utils.toHex2((peeked >> 8) & 0xff);
                ac.addr = getWord(peeked);
                ac.val = mem.peek(ac.addr);
                break;
            case A:
                ac.val = A;
                ac.addr = -1; // Special address -1 = register A.
                break;
            case X_IND:
                peeked = getNextByte();
                if (enableBytesRead)
                    ac.bytesRead += " " + Utils.toHex2(peeked);
                ac.addr = getWord((peeked + X) & 0xff);
                ac.val = mem.peek(ac.addr);
                break;
            default:
                Debug.logUnhandled(this, "address mode" + mode.toString(), 0, 0);
        }

    }

    public boolean debugSkipLine(int addr) {

        // if (addr >= 0xE5CD && addr <= 0xE5D4)
        // return true;

        return false;
    }

}

