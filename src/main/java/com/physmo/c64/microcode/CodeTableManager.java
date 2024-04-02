package com.physmo.c64.microcode;

import com.physmo.c64.Utils;

import java.util.Map;

public class CodeTableManager {
    public CodeTable codeTableMain;

    public CodeTableManager() {
        codeTableMain = new CodeTable("Main");
        initCodeTableMain();
        opcodeReport();

    }

    public void opcodeReport() {
        System.out.println("OpCode report.");
        Map<Integer, String> names = codeTableMain.getNames();

        for (Integer i : names.keySet()) {
            System.out.println(Utils.toHex2(i) + "  " + names.get(i));
        }
    }


    public void initCodeTableMain() {
        codeTableMain.define(0x00, "BRK", 7, MicroOp.BRK);
        codeTableMain.define(0xEA, "NOP", 2, MicroOp.NOP);

        codeTableMain.define(0x29, "AND #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.AND);
        codeTableMain.define(0x2D, "AND $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x3D, "AND $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x39, "AND $nnnn,Y", 4, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x25, "AND $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x35, "AND $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x21, "AND ($nn,X)", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x31, "AND ($nn),Y", 5, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);


        codeTableMain.define(0x09, "ORA #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.ORA);
        codeTableMain.define(0x0D, "ORA $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x1D, "ORA $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x19, "ORA $nnnn,Y", 4, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x05, "ORA $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x15, "ORA $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x01, "ORA ($nn,X)", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x11, "ORA ($nn),Y", 5, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);

        codeTableMain.define(0x49, "EOR #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.EOR);
        codeTableMain.define(0x4D, "EOR $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x5D, "EOR $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x59, "EOR $nnnn,Y", 4, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x45, "EOR $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x55, "EOR $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x41, "EOR ($nn,X)", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x51, "EOR ($nn),Y", 5, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);


        codeTableMain.define(0x8D, "STA $nnnn", 3, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.STA);
        codeTableMain.define(0x9D, "STA $nnnn,X", 3, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.STA);
        codeTableMain.define(0x99, "STA $nnnn,Y", 3, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.STA);
        codeTableMain.define(0x85, "STA $nn", 2, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.STA);
        codeTableMain.define(0x95, "STA $nn,X", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.STA);
        codeTableMain.define(0x81, "STA ($nn,X)", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.STA);
        codeTableMain.define(0x91, "STA ($nn),Y", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.STA);


        codeTableMain.define(0x8E, "STX $nnnn", 3, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.STX);
        codeTableMain.define(0x86, "STX $nn", 2, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.STX);
        codeTableMain.define(0x96, "STX $nn,Y", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_Y, MicroOp.STX);

        codeTableMain.define(0x8C, "STY $nnnn", 3, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.STY);
        codeTableMain.define(0x84, "STY $nn", 2, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.STY);
        codeTableMain.define(0x94, "STY $nn,X", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.STY);


        codeTableMain.define(0xA9, "LDA #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.LDA);
        codeTableMain.define(0xAD, "LDA $nnnn", 3, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xBD, "LDA $nnnn,X", 3, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xB9, "LDA $nnnn,Y", 3, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xA5, "LDA $nn", 2, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xB5, "LDA $nn,X", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xA1, "LDA ($nn,X)", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xB1, "LDA ($nn),Y", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);


        codeTableMain.define(0xA2, "LDX #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.LDX);
        codeTableMain.define(0xAE, "LDX $nnnn", 3, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDX);
        codeTableMain.define(0xBE, "LDX $nnnn,Y", 3, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDX);
        codeTableMain.define(0xA6, "LDX $nn", 2, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDX);
        codeTableMain.define(0xB6, "LDX $nn,Y", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDX);

        codeTableMain.define(0xA0, "LDY #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.LDY);
        codeTableMain.define(0xAC, "LDY $nnnn", 3, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDY);
        codeTableMain.define(0xBC, "LDY $nnnn,X", 3, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDY);
        codeTableMain.define(0xA4, "LDY $nn", 2, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDY);
        codeTableMain.define(0xB4, "LDY $nn,X", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDY);


        codeTableMain.define(0xEE, "INC $nnnn", 3, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.INC);
        codeTableMain.define(0xFE, "INC $nnnn,X", 3, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.INC);
        codeTableMain.define(0xE6, "INC $nn", 2, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.INC);
        codeTableMain.define(0xF6, "INC $nn,X", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.INC);

        codeTableMain.define(0xCE, "DEC $nnnn", 3, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.DEC);
        codeTableMain.define(0xDE, "DEC $nnnn,X", 3, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.DEC);
        codeTableMain.define(0xC6, "DEC $nn", 2, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.DEC);
        codeTableMain.define(0xD6, "DEC $nn,X", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.DEC);


        // TRANSFERS: TAX, TAY, TSX, TXA, TXS, TYA
        codeTableMain.define(0xAA, "TAX", 2, MicroOp.TAX);
        codeTableMain.define(0xA8, "TAY", 2, MicroOp.TAY);
        codeTableMain.define(0xBA, "TSX", 2, MicroOp.TSX);
        codeTableMain.define(0x8A, "TXA", 2, MicroOp.TXA);
        codeTableMain.define(0x9A, "TXS", 2, MicroOp.TXS);
        codeTableMain.define(0x98, "TYA", 2, MicroOp.TYA);

        // FLAG OPERATIONS  CLC, CLD, CLI, CLV, SEC, SED, SEI,
        codeTableMain.define(0x18, "CLC", 2, MicroOp.CLC);
        codeTableMain.define(0xD8, "CLD", 2, MicroOp.CLD);
        codeTableMain.define(0x58, "CLI", 2, MicroOp.CLI);
        codeTableMain.define(0xB8, "CLV", 2, MicroOp.CLV);
        codeTableMain.define(0x38, "SEC", 2, MicroOp.SEC);
        codeTableMain.define(0xF8, "SED", 2, MicroOp.SED);
        codeTableMain.define(0x78, "SEI", 2, MicroOp.SEI);

        // JUMP & CALL
        codeTableMain.define(0x20, "JSR $nnnn", 6, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.JSR);
        codeTableMain.define(0x60, "RTS", 6, MicroOp.RTS);
        codeTableMain.define(0x40, "RTI", 6, MicroOp.RTI);


        codeTableMain.define(0x4C, "JMP $nnnn", 3, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.JMP);
        codeTableMain.define(0x6C, "JMP ($nnnn)", 5, MicroOp.SET_ADDRESS_ABSOLUTE_INDIRECT, MicroOp.JMP);


        // BRANCH - BCC, BCS, BEQ, BMI, BNE, BPL, BVC, BVS
        codeTableMain.define(0x90, "BCC $nnnn", 2, MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BCC);
        codeTableMain.define(0xB0, "BCS $nnnn", 2, MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BCS);
        codeTableMain.define(0xF0, "BEQ $nnnn", 2, MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BEQ);
        codeTableMain.define(0x30, "BMI $nnnn", 2, MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BMI);
        codeTableMain.define(0xD0, "BNE $nnnn", 2, MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BNE);
        codeTableMain.define(0x10, "BPL $nnnn", 2, MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BPL);
        codeTableMain.define(0x50, "BVC $nnnn", 2, MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BVC);
        codeTableMain.define(0x70, "BVS $nnnn", 2, MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BVS);


        codeTableMain.define(0xC9, "CMP #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.CMP);
        codeTableMain.define(0xCD, "CMP $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xDD, "CMP $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xD9, "CMP $nnnn,Y", 4, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xC5, "CMP $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xD5, "CMP $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xC1, "CMP ($nn,X)", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xD1, "CMP ($nn),Y", 5, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);


        codeTableMain.define(0xE0, "CPX #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.CPX);
        codeTableMain.define(0xEC, "CPX $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CPX);
        codeTableMain.define(0xE4, "CPX $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CPX);

        codeTableMain.define(0xC0, "CPY #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.CPY);
        codeTableMain.define(0xCC, "CPY $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CPY);
        codeTableMain.define(0xC4, "CPY $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CPY);


        codeTableMain.define(0x69, "ADC #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.ADC);
        codeTableMain.define(0x6D, "ADC $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x7D, "ADC $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x79, "ADC $nnnn,Y", 4, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x65, "ADC $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x75, "ADC $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x61, "ADC ($nn,X)", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x71, "ADC ($nn),Y", 5, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);

        codeTableMain.define(0xE9, "SBC #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.SBC);
        codeTableMain.define(0xED, "SBC $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xFD, "SBC $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xF9, "SBC $nnnn,Y", 4, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xE5, "SBC $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xF5, "SBC $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xE1, "SBC ($nn,X)", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xF1, "SBC ($nn),Y", 5, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);


        // INC and DEC X and Y - DEX, DEY, INX, INY,
        codeTableMain.define(0xCA, "DEX", 2, MicroOp.DEX);
        codeTableMain.define(0x88, "DEY", 2, MicroOp.DEY);
        codeTableMain.define(0xE8, "INX", 2, MicroOp.INX);
        codeTableMain.define(0xC8, "INY", 2, MicroOp.INY);


        // ROTATE and SHIFT
        codeTableMain.define(0x2A, "ROL A", 2, MicroOp.FETCH_A, MicroOp.ROL, MicroOp.STORE_A);
        codeTableMain.define(0x2E, "ROL $nnnn", 6, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x3E, "ROL $nnnn,X", 7, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x26, "ROL $nn", 5, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x36, "ROL $nn,X", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.STORE_BYTE_AT_ADDRESS);

        codeTableMain.define(0x6A, "ROR A", 2, MicroOp.FETCH_A, MicroOp.ROR, MicroOp.STORE_A);
        codeTableMain.define(0x6E, "ROR $nnnn", 6, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x7E, "ROR $nnnn,X", 7, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x66, "ROR $nn", 5, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x76, "ROR $nn,X", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROR, MicroOp.STORE_BYTE_AT_ADDRESS);

        codeTableMain.define(0x0A, "ASL A", 2, MicroOp.FETCH_A, MicroOp.ASL, MicroOp.STORE_A);
        codeTableMain.define(0x0E, "ASL $nnnn", 6, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x1E, "ASL $nnnn,X", 7, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x06, "ASL $nn", 5, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x16, "ASL $nn,X", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.STORE_BYTE_AT_ADDRESS);


        codeTableMain.define(0x4A, "LSR A", 2, MicroOp.FETCH_A, MicroOp.LSR, MicroOp.STORE_A);
        codeTableMain.define(0x4E, "LSR $nnnn", 6, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LSR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x5E, "LSR $nnnn,X", 7, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LSR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x46, "LSR $nn", 5, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LSR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x56, "LSR $nn,X", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LSR, MicroOp.STORE_BYTE_AT_ADDRESS);


        // BINARY
        codeTableMain.define(0x2C, "BIT $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.BIT);
        codeTableMain.define(0x24, "BIT $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.BIT);


        // STACK - PHA, PHP, PLA, PLP,
        codeTableMain.define(0x48, "PHA", 3, MicroOp.PHA);
        codeTableMain.define(0x08, "PHP", 3, MicroOp.PHP);
        codeTableMain.define(0x68, "PLA", 4, MicroOp.PLA);
        codeTableMain.define(0x28, "PLP", 4, MicroOp.PLP);

        /////////////////////////////////////////////////////////////////
        // UNDOCUMENTED INSTRUCTIONS

        codeTableMain.define(0x8F, "SAX $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.SAX, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x87, "SAX $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.SAX, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x97, "SAX $nn,Y", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_Y, MicroOp.SAX, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x83, "SAX ($nn,X)", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.SAX, MicroOp.STORE_BYTE_AT_ADDRESS);

        codeTableMain.define(0xEF, "ISC $nnnn", 6, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.INC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xFF, "ISC $nnnn,X", 7, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.INC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xFB, "ISC $nnnn,Y", 7, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.INC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xE7, "ISC $nn", 5, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.INC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xF7, "ISC $nn,X", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.INC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xE3, "ISC ($nn,X)", 8, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.INC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xF3, "ISC ($nn),Y", 8, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.INC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.SBC);

        codeTableMain.define(0x2F, "RLA $nnnn", 6, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.AND);
        codeTableMain.define(0x3F, "RLA $nnnn,X", 7, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.AND);
        codeTableMain.define(0x3B, "RLA $nnnn,Y", 7, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.AND);
        codeTableMain.define(0x27, "RLA $nn", 5, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.AND);
        codeTableMain.define(0x37, "RLA $nn,X", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.AND);
        codeTableMain.define(0x23, "RLA ($nn,X)", 8, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.AND);
        codeTableMain.define(0x33, "RLA ($nn),Y", 8, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.AND);

        codeTableMain.define(0xAB, "LAX #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.LDA, MicroOp.LDX);
        codeTableMain.define(0xAF, "LAX $nnnn", 3, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA, MicroOp.LDX);
        codeTableMain.define(0xBF, "LAX $nnnn,Y", 3, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA, MicroOp.LDX);
        codeTableMain.define(0xA7, "LAX $nn", 2, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA, MicroOp.LDX);
        codeTableMain.define(0xB7, "LAX $nn,Y", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA, MicroOp.LDX);
        codeTableMain.define(0xA3, "LAX ($nn,X)", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA, MicroOp.LDX);
        codeTableMain.define(0xB3, "LAX ($nn),Y", 2, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA, MicroOp.LDX);


        codeTableMain.define(0x1A, "NOP", 2, MicroOp.NOP);
        codeTableMain.define(0x3A, "NOP", 2, MicroOp.NOP);
        codeTableMain.define(0x5A, "NOP", 2, MicroOp.NOP);
        codeTableMain.define(0x7A, "NOP", 2, MicroOp.NOP);
        codeTableMain.define(0xDA, "NOP", 2, MicroOp.NOP);
        codeTableMain.define(0xFA, "NOP", 2, MicroOp.NOP);

        codeTableMain.define(0x80, "NOP #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.NOP);
        codeTableMain.define(0x82, "NOP #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.NOP);
        codeTableMain.define(0x89, "NOP #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.NOP);
        codeTableMain.define(0xC2, "NOP #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.NOP);
        codeTableMain.define(0xE2, "NOP #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.NOP);

        codeTableMain.define(0x0C, "NOP $nnnn", 4, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.NOP);

        codeTableMain.define(0x1C, "NOP $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.NOP);
        codeTableMain.define(0x3C, "NOP $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.NOP);
        codeTableMain.define(0x5C, "NOP $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.NOP);
        codeTableMain.define(0x7C, "NOP $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.NOP);
        codeTableMain.define(0xDC, "NOP $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.NOP);
        codeTableMain.define(0xFC, "NOP $nnnn,X", 4, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.NOP);

        codeTableMain.define(0x04, "NOP $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE,  MicroOp.NOP);
        codeTableMain.define(0x44, "NOP $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE,  MicroOp.NOP);
        codeTableMain.define(0x64, "NOP $nn", 3, MicroOp.SET_ADDRESS_ZERO_PAGE,  MicroOp.NOP);

        codeTableMain.define(0x14, "NOP $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.NOP);
        codeTableMain.define(0x34, "NOP $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.NOP);
        codeTableMain.define(0x54, "NOP $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.NOP);
        codeTableMain.define(0x74, "NOP $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.NOP);
        codeTableMain.define(0xD4, "NOP $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.NOP);
        codeTableMain.define(0xF4, "NOP $nn,X", 4, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.NOP);

        codeTableMain.define(0xCF, "DCP $nnnn", 6, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.DEC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xDF, "DCP $nnnn,X", 7, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.DEC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xDB, "DCP $nnnn,Y", 7, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.DEC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xC7, "DCP $nn", 5, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.DEC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xD7, "DCP $nn,X", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.DEC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xC3, "DCP ($nn,X)", 8, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.DEC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xD3, "DCP ($nn),Y", 8, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.DEC, MicroOp.STORE_BYTE_AT_ADDRESS, MicroOp.CMP);

        codeTableMain.define(0x0B, "ANC #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.ANC);
        codeTableMain.define(0x2B, "ANC #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.ANC);

        codeTableMain.define(0x0F, "SLO $nnnn", 6, MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.AND);
        codeTableMain.define(0x1F, "SLO $nnnn,X", 7, MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.AND);
        codeTableMain.define(0x1B, "SLO $nnnn,Y", 7, MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.AND);
        codeTableMain.define(0x07, "SLO $nn", 5, MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.AND);
        codeTableMain.define(0x17, "SLO $nn,X", 6, MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.AND);
        codeTableMain.define(0x03, "SLO ($nn,X)", 8, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.AND);
        codeTableMain.define(0x13, "SLO ($nn),Y", 8, MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.AND);

        codeTableMain.define(0x4B, "ASR #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.ASR, MicroOp.LDA );
        codeTableMain.define(0x6B, "ARR #$nn", 2, MicroOp.GET_NEXT_BYTE, MicroOp.AND, MicroOp.FETCH_A, MicroOp.LSR, MicroOp.LDA ); // TODO: Some extra flag operations
    }


}
