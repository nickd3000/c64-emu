package com.physmo.c64.microcode;

import java.util.List;

public class CodeTableManager {
    public CodeTable codeTableMain;

    public CodeTableManager() {
        codeTableMain = new CodeTable("Main");
        initCodeTableMain();
    }

    public void initCodeTableMain() {
        codeTableMain.define(0x00, "BRK", MicroOp.BRK);
        codeTableMain.define(0xEA, "NOP", MicroOp.NOP);

        codeTableMain.define(0x29, "AND #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.AND);
        codeTableMain.define(0x2D, "AND $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND );
        codeTableMain.define(0x3D, "AND $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x39, "AND $nnnn,Y", MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x25, "AND $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x35, "AND $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x21, "AND ($nn,X)", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);
        codeTableMain.define(0x31, "AND ($nn),Y", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.AND);


        codeTableMain.define(0x09, "ORA #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.ORA);
        codeTableMain.define(0x0D, "ORA $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA );
        codeTableMain.define(0x1D, "ORA $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x19, "ORA $nnnn,Y", MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x05, "ORA $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x15, "ORA $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x01, "ORA ($nn,X)", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);
        codeTableMain.define(0x11, "ORA ($nn),Y", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ORA);

        codeTableMain.define(0x49, "EOR #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.EOR);
        codeTableMain.define(0x4D, "EOR $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR );
        codeTableMain.define(0x5D, "EOR $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x59, "EOR $nnnn,Y", MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x45, "EOR $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x55, "EOR $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x41, "EOR ($nn,X)", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);
        codeTableMain.define(0x51, "EOR ($nn),Y", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.EOR);



        codeTableMain.define(0x8D, "STA $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.STA);
        codeTableMain.define(0x9D, "STA $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.STA);
        codeTableMain.define(0x99, "STA $nnnn,Y", MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.STA);
        codeTableMain.define(0x85, "STA $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.STA);
        codeTableMain.define(0x95, "STA $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.STA);
        codeTableMain.define(0x81, "STA ($nn,X)", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.STA);
        codeTableMain.define(0x91, "STA ($nn),Y", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.STA);


        codeTableMain.define(0x8E, "STX $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.STX);
        codeTableMain.define(0x86, "STX $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.STX);
        codeTableMain.define(0x96, "STX $nn,Y", MicroOp.SET_ADDRESS_ZERO_PAGE_Y, MicroOp.STX);

        codeTableMain.define(0x8C, "STY $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.STY);
        codeTableMain.define(0x84, "STY $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.STY);
        codeTableMain.define(0x94, "STY $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.STY);


        codeTableMain.define(0xA9, "LDA #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.LDA);
        codeTableMain.define(0xAD, "LDA $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xBD, "LDA $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xB9, "LDA $nnnn,Y", MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xA5, "LDA $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xB5, "LDA $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xA1, "LDA ($nn,X)", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);
        codeTableMain.define(0xB1, "LDA ($nn),Y", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDA);


        codeTableMain.define(0xA2, "LDX #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.LDX);
        codeTableMain.define(0xAE, "LDX $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDX);
        codeTableMain.define(0xBE, "LDX $nnnn,Y", MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDX);
        codeTableMain.define(0xA6, "LDX $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDX);
        codeTableMain.define(0xB6, "LDX $nn,Y", MicroOp.SET_ADDRESS_ZERO_PAGE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDX);

        codeTableMain.define(0xA0, "LDY #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.LDY);
        codeTableMain.define(0xAC, "LDY $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDY);
        codeTableMain.define(0xBC, "LDY $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDY);
        codeTableMain.define(0xA4, "LDY $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDY);
        codeTableMain.define(0xB4, "LDY $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LDY);



        codeTableMain.define(0xEE, "INC $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.INC);
        codeTableMain.define(0xFE, "INC $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.INC);
        codeTableMain.define(0xE6, "INC $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.INC);
        codeTableMain.define(0xF6, "INC $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.INC);

        codeTableMain.define(0xCE, "DEC $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.DEC);
        codeTableMain.define(0xDE, "DEC $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.DEC);
        codeTableMain.define(0xC6, "DEC $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.DEC);
        codeTableMain.define(0xD6, "DEC $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.DEC);



        // TRANSFERS: TAX, TAY, TSX, TXA, TXS, TYA
        codeTableMain.define(0xAA, "TAX", MicroOp.TAX);
        codeTableMain.define(0xA8, "TAY", MicroOp.TAY);
        codeTableMain.define(0xBA, "TSX", MicroOp.TSX);
        codeTableMain.define(0x8A, "TXA", MicroOp.TXA);
        codeTableMain.define(0x9A, "TXS", MicroOp.TXS);
        codeTableMain.define(0x98, "TYA", MicroOp.TYA);

        // FLAG OPERATIONS  CLC, CLD, CLI, CLV, SEC, SED, SEI,
        codeTableMain.define(0x18, "CLC", MicroOp.CLC);
        codeTableMain.define(0xD8, "CLD", MicroOp.CLD);
        codeTableMain.define(0x58, "CLI", MicroOp.CLI);
        codeTableMain.define(0xB8, "CLV", MicroOp.CLV);
        codeTableMain.define(0x38, "SEC", MicroOp.SEC);
        codeTableMain.define(0xF8, "SED", MicroOp.SED);
        codeTableMain.define(0x78, "SEI", MicroOp.SEI);

        // JUMP & CALL
        codeTableMain.define(0x20, "JSR $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.JSR);
        codeTableMain.define(0x60, "RTS", MicroOp.RTS);
        codeTableMain.define(0x40, "RTI", MicroOp.RTI);


        codeTableMain.define(0x4C, "JMP $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.JMP);
        codeTableMain.define(0x6C, "JMP ($nnnn)", MicroOp.SET_ADDRESS_ABSOLUTE_INDIRECT, MicroOp.JMP);


        // BRANCH - BCC, BCS, BEQ, BMI, BNE, BPL, BVC, BVS
        codeTableMain.define(0x90, "BCC $nnnn", MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BCC);
        codeTableMain.define(0xB0, "BCS $nnnn", MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BCS);
        codeTableMain.define(0xF0, "BEQ $nnnn", MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BEQ);
        codeTableMain.define(0x30, "BMI $nnnn", MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BMI);
        codeTableMain.define(0xD0, "BNE $nnnn", MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BNE);
        codeTableMain.define(0x10, "BPL $nnnn", MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BPL);
        codeTableMain.define(0x50, "BVC $nnnn", MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BVC);
        codeTableMain.define(0x70, "BVS $nnnn", MicroOp.SET_ADDRESS_RELATIVE, MicroOp.BVS);


        codeTableMain.define(0xC9, "CMP #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.CMP);
        codeTableMain.define(0xCD, "CMP $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP );
        codeTableMain.define(0xDD, "CMP $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xD9, "CMP $nnnn,Y", MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xC5, "CMP $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xD5, "CMP $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xC1, "CMP ($nn,X)", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);
        codeTableMain.define(0xD1, "CMP ($nn),Y", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CMP);


        codeTableMain.define(0xE0, "CPX #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.CPX);
        codeTableMain.define(0xEC, "CPX $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CPX );
        codeTableMain.define(0xE4, "CPX $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CPX);

        codeTableMain.define(0xC0, "CPY #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.CPY);
        codeTableMain.define(0xCC, "CPY $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CPY );
        codeTableMain.define(0xC4, "CPY $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.CPY);


        codeTableMain.define(0x69, "ADC #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.ADC);
        codeTableMain.define(0x6D, "ADC $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC );
        codeTableMain.define(0x7D, "ADC $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x79, "ADC $nnnn,Y", MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x65, "ADC $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x75, "ADC $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x61, "ADC ($nn,X)", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);
        codeTableMain.define(0x71, "ADC ($nn),Y", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ADC);

        codeTableMain.define(0xE9, "SBC #$nn", MicroOp.GET_NEXT_BYTE, MicroOp.SBC);
        codeTableMain.define(0xED, "SBC $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC );
        codeTableMain.define(0xFD, "SBC $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xF9, "SBC $nnnn,Y", MicroOp.SET_ADDRESS_ABSOLUTE_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xE5, "SBC $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xF5, "SBC $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xE1, "SBC ($nn,X)", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);
        codeTableMain.define(0xF1, "SBC ($nn),Y", MicroOp.SET_ADDRESS_ZERO_PAGE_INDIRECT_Y, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.SBC);


        // INC and DEC X and Y - DEX, DEY, INX, INY,
        codeTableMain.define(0xCA, "DEX", MicroOp.DEX);
        codeTableMain.define(0x88, "DEY", MicroOp.DEY);
        codeTableMain.define(0xE8, "INX", MicroOp.INX);
        codeTableMain.define(0xC8, "INY", MicroOp.INY);


        // ROTATE and SHIFT
        codeTableMain.define(0x2A, "ROL A", MicroOp.FETCH_A, MicroOp.ROL, MicroOp.STORE_A);
        codeTableMain.define(0x2E, "ROL $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x3E, "ROL $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x26, "ROL $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x36, "ROL $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROL, MicroOp.STORE_BYTE_AT_ADDRESS);

        codeTableMain.define(0x6A, "ROR A", MicroOp.FETCH_A, MicroOp.ROR, MicroOp.STORE_A);
        codeTableMain.define(0x6E, "ROR $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x7E, "ROR $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x66, "ROR $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x76, "ROR $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ROR, MicroOp.STORE_BYTE_AT_ADDRESS);

        codeTableMain.define(0x0A, "ASL A", MicroOp.FETCH_A, MicroOp.ASL, MicroOp.STORE_A);
        codeTableMain.define(0x0E, "ASL $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x1E, "ASL $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x06, "ASL $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x16, "ASL $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.ASL, MicroOp.STORE_BYTE_AT_ADDRESS);


        codeTableMain.define(0x4A, "LSR A", MicroOp.FETCH_A, MicroOp.LSR, MicroOp.STORE_A);
        codeTableMain.define(0x4E, "LSR $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LSR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x5E, "LSR $nnnn,X", MicroOp.SET_ADDRESS_ABSOLUTE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LSR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x46, "LSR $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LSR, MicroOp.STORE_BYTE_AT_ADDRESS);
        codeTableMain.define(0x56, "LSR $nn,X", MicroOp.SET_ADDRESS_ZERO_PAGE_X, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.LSR, MicroOp.STORE_BYTE_AT_ADDRESS);


        // BINARY
        codeTableMain.define(0x2C, "BIT $nnnn", MicroOp.SET_ADDRESS_ABSOLUTE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.BIT);
        codeTableMain.define(0x24, "BIT $nn", MicroOp.SET_ADDRESS_ZERO_PAGE, MicroOp.FETCH_BYTE_FROM_ADDRESS, MicroOp.BIT);



        // STACK - PHA, PHP, PLA, PLP,
        codeTableMain.define(0x48, "PHA", MicroOp.PHA);
        codeTableMain.define(0x08, "PHP", MicroOp.PHP);
        codeTableMain.define(0x68, "PLA", MicroOp.PLA);
        codeTableMain.define(0x28, "PLP", MicroOp.PLP);


//        codeTableMain.define(0x01, "LD BC,d16", MicroOp.FETCH_16, MicroOp.STORE_BC);
//        codeTableMain.define(0x02, "LD (BC),A", MicroOp.FETCH_A, MicroOp.SET_ADDR_FROM_BC, MicroOp.STORE_BYTE_AT_ADDRESS);
//        codeTableMain.define(0x03, "INC BC", MicroOp.FETCH_BC, MicroOp.INC_16, MicroOp.STORE_BC);
//        codeTableMain.define(0x04, "INC B", MicroOp.FETCH_B, MicroOp.INC_8, MicroOp.STORE_B);
//        codeTableMain.define(0x05, "DEC B", MicroOp.FETCH_B, MicroOp.DEC_8, MicroOp.STORE_B);
//        codeTableMain.define(0x06, "LD B,d8", MicroOp.FETCH_8, MicroOp.STORE_B);
//        codeTableMain.define(0x07, "RLCA", MicroOp.FETCH_A, MicroOp.RLCA, MicroOp.STORE_A); // 8-bit rotation left
//        codeTableMain.define(0x08, "EX AF,AF'", MicroOp.EX_AF_AF);
//        codeTableMain.define(0x09, "ADD HL,BC", MicroOp.FETCH_BC, MicroOp.ADD_HL, MicroOp.STORE_HL);
//        codeTableMain.define(0x0A, "LD A,(BC)", MicroOp.SET_ADDR_FROM_BC, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
//        codeTableMain.define(0x0B, "DEC BC", MicroOp.FETCH_BC, MicroOp.DEC_16, MicroOp.STORE_BC);
//        codeTableMain.define(0x0C, "INC C", MicroOp.FETCH_C, MicroOp.INC_8, MicroOp.STORE_C);
//        codeTableMain.define(0x0D, "DEC C", MicroOp.FETCH_C, MicroOp.DEC_8, MicroOp.STORE_C);
//        codeTableMain.define(0x0E, "LD C,d8", MicroOp.FETCH_8, MicroOp.STORE_C);
//        codeTableMain.define(0x0F, "RRCA", MicroOp.FETCH_A, MicroOp.RRCA, MicroOp.STORE_A);
//
//
//        codeTableMain.define(0x10, "DJNZ d", MicroOp.FETCH_8, MicroOp.DJNZ);
//        codeTableMain.define(0x11, "LD DE,d16", MicroOp.FETCH_16, MicroOp.STORE_DE);
//        codeTableMain.define(0x12, "LD (DE),A", MicroOp.FETCH_A, MicroOp.SET_ADDR_FROM_DE, MicroOp.STORE_BYTE_AT_ADDRESS);
//        codeTableMain.define(0x13, "INC DE", MicroOp.FETCH_DE, MicroOp.INC_16, MicroOp.STORE_DE);
//        codeTableMain.define(0x14, "INC D", MicroOp.FETCH_D, MicroOp.INC_8, MicroOp.STORE_D);
//        codeTableMain.define(0x15, "DEC D", MicroOp.FETCH_D, MicroOp.DEC_8, MicroOp.STORE_D);
//        codeTableMain.define(0x16, "LD D,d8", MicroOp.FETCH_8, MicroOp.STORE_D);
//        codeTableMain.define(0x17, "RLA", MicroOp.FETCH_A, MicroOp.RLA, MicroOp.STORE_A);
//        codeTableMain.define(0x18, "JR r8", MicroOp.FETCH_8, MicroOp.JR);
//        codeTableMain.define(0x19, "ADD HL,DE", MicroOp.FETCH_DE, MicroOp.ADD_HL, MicroOp.STORE_HL);
//        codeTableMain.define(0x1A, "LD A,(DE)", MicroOp.SET_ADDR_FROM_DE, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
//        codeTableMain.define(0x1B, "DEC DE", MicroOp.FETCH_DE, MicroOp.DEC_16, MicroOp.STORE_DE);
//        codeTableMain.define(0x1C, "INC E", MicroOp.FETCH_E, MicroOp.INC_8, MicroOp.STORE_E);
//        codeTableMain.define(0x1D, "DEC E", MicroOp.FETCH_E, MicroOp.DEC_8, MicroOp.STORE_E);
//        codeTableMain.define(0x1E, "LD E,d8", MicroOp.FETCH_8, MicroOp.STORE_E);
//        codeTableMain.define(0x1F, "RRA", MicroOp.FETCH_A, MicroOp.RRA, MicroOp.STORE_A);
//
//        codeTableMain.define(0x20, "JR NZ,r8", MicroOp.FETCH_8, MicroOp.JRNZ);
//        codeTableMain.define(0x21, "LD HL,d16", MicroOp.FETCH_16, MicroOp.STORE_HL);
//        codeTableMain.define(0x22, "ld (nn),hl", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_HL, MicroOp.STORE_WORD_AT_ADDRESS);
//        codeTableMain.define(0x23, "INC HL", MicroOp.FETCH_HL, MicroOp.INC_16, MicroOp.STORE_HL);
//        codeTableMain.define(0x24, "INC H", MicroOp.FETCH_H, MicroOp.INC_8, MicroOp.STORE_H);
//        codeTableMain.define(0x25, "DEC H", MicroOp.FETCH_H, MicroOp.DEC_8, MicroOp.STORE_H);
//        codeTableMain.define(0x26, "LD H,d8", MicroOp.FETCH_8, MicroOp.STORE_H);
//        codeTableMain.define(0x27, "DAA", MicroOp.DAA);
//        codeTableMain.define(0x28, "JR Z,r8", MicroOp.FETCH_8, MicroOp.JRZ);
//        codeTableMain.define(0x29, "ADD HL,HL", MicroOp.FETCH_HL, MicroOp.ADD_HL, MicroOp.STORE_HL);
//        codeTableMain.define(0x2A, "LD hl,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_WORD_FROM_ADDR, MicroOp.STORE_HL);
//        codeTableMain.define(0x2B, "DEC HL", MicroOp.FETCH_HL, MicroOp.DEC_16, MicroOp.STORE_HL);
//        codeTableMain.define(0x2C, "INC L", MicroOp.FETCH_L, MicroOp.INC_8, MicroOp.STORE_L);
//        codeTableMain.define(0x2D, "DEC L", MicroOp.FETCH_L, MicroOp.DEC_8, MicroOp.STORE_L);
//        codeTableMain.define(0x2E, "LD L,d8", MicroOp.FETCH_8, MicroOp.STORE_L);
//        codeTableMain.define(0x2F, "CPL", MicroOp.CPL);
//
//        codeTableMain.define(0x30, "JR NC,r8", MicroOp.FETCH_8, MicroOp.JRNC);
//        codeTableMain.define(0x31, "LD SP,d16", MicroOp.FETCH_16, MicroOp.STORE_SP);
//        codeTableMain.define(0x32, "LD (nn),a", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_A, MicroOp.STORE_BYTE_AT_ADDRESS);
//        codeTableMain.define(0x33, "INC SP", MicroOp.FETCH_SP, MicroOp.INC_16, MicroOp.STORE_SP);
//        codeTableMain.define(0x34, "INC (HL)", MicroOp.FETCH_pHL, MicroOp.INC_8, MicroOp.STORE_pHL);
//        codeTableMain.define(0x35, "DEC (HL)", MicroOp.FETCH_pHL, MicroOp.DEC_8, MicroOp.STORE_pHL);
//        codeTableMain.define(0x36, "LD (HL),d8", MicroOp.FETCH_8, MicroOp.STORE_pHL);
//        codeTableMain.define(0x37, "SCF", MicroOp.SCF);
//        codeTableMain.define(0x38, "JR C,r8", MicroOp.FETCH_8, MicroOp.JRC);
//        codeTableMain.define(0x39, "ADD HL,SP", MicroOp.FETCH_SP, MicroOp.ADD_HL, MicroOp.STORE_HL);
//        codeTableMain.define(0x3A, "LD a,(nn)", MicroOp.FETCH_16_ADDRESS, MicroOp.FETCH_BYTE_FROM_ADDR, MicroOp.STORE_A);
//        codeTableMain.define(0x3B, "DEC SP", MicroOp.FETCH_SP, MicroOp.DEC_16, MicroOp.STORE_SP);
//        codeTableMain.define(0x3C, "INC A", MicroOp.FETCH_A, MicroOp.INC_8, MicroOp.STORE_A);
//        codeTableMain.define(0x3D, "DEC A", MicroOp.FETCH_A, MicroOp.DEC_8, MicroOp.STORE_A);
//        codeTableMain.define(0x3E, "LD A,d8", MicroOp.FETCH_8, MicroOp.STORE_A);
//        codeTableMain.define(0x3F, "CCF", MicroOp.CCF);
//
//
//        codeTableMain.define(0x40, "LD B,B", MicroOp.FETCH_B, MicroOp.STORE_B);
//        codeTableMain.define(0x41, "LD B,C", MicroOp.FETCH_C, MicroOp.STORE_B);
//        codeTableMain.define(0x42, "LD B,D", MicroOp.FETCH_D, MicroOp.STORE_B);
//        codeTableMain.define(0x43, "LD B,E", MicroOp.FETCH_E, MicroOp.STORE_B);
//        codeTableMain.define(0x44, "LD B,H", MicroOp.FETCH_H, MicroOp.STORE_B);
//        codeTableMain.define(0x45, "LD B,L", MicroOp.FETCH_L, MicroOp.STORE_B);
//        codeTableMain.define(0x46, "LD B,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_B);
//        codeTableMain.define(0x47, "LD B,A", MicroOp.FETCH_A, MicroOp.STORE_B);
//        codeTableMain.define(0x48, "LD C,B", MicroOp.FETCH_B, MicroOp.STORE_C);
//        codeTableMain.define(0x49, "LD C,C", MicroOp.FETCH_C, MicroOp.STORE_C);
//        codeTableMain.define(0x4A, "LD C,D", MicroOp.FETCH_D, MicroOp.STORE_C);
//        codeTableMain.define(0x4B, "LD C,E", MicroOp.FETCH_E, MicroOp.STORE_C);
//        codeTableMain.define(0x4C, "LD C,H", MicroOp.FETCH_H, MicroOp.STORE_C);
//        codeTableMain.define(0x4D, "LD C,L", MicroOp.FETCH_L, MicroOp.STORE_C);
//        codeTableMain.define(0x4E, "LD C,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_C);
//        codeTableMain.define(0x4F, "LD C,A", MicroOp.FETCH_A, MicroOp.STORE_C);
//
//        codeTableMain.define(0x50, "LD D,B", MicroOp.FETCH_B, MicroOp.STORE_D);
//        codeTableMain.define(0x51, "LD D,C", MicroOp.FETCH_C, MicroOp.STORE_D);
//        codeTableMain.define(0x52, "LD D,D", MicroOp.FETCH_D, MicroOp.STORE_D);
//        codeTableMain.define(0x53, "LD D,E", MicroOp.FETCH_E, MicroOp.STORE_D);
//        codeTableMain.define(0x54, "LD D,H", MicroOp.FETCH_H, MicroOp.STORE_D);
//        codeTableMain.define(0x55, "LD D,L", MicroOp.FETCH_L, MicroOp.STORE_D);
//        codeTableMain.define(0x56, "LD D,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_D);
//        codeTableMain.define(0x57, "LD D,A", MicroOp.FETCH_A, MicroOp.STORE_D);
//        codeTableMain.define(0x58, "LD E,B", MicroOp.FETCH_B, MicroOp.STORE_E);
//        codeTableMain.define(0x59, "LD E,C", MicroOp.FETCH_C, MicroOp.STORE_E);
//        codeTableMain.define(0x5A, "LD E,D", MicroOp.FETCH_D, MicroOp.STORE_E);
//        codeTableMain.define(0x5B, "LD E,E", MicroOp.FETCH_E, MicroOp.STORE_E);
//        codeTableMain.define(0x5C, "LD E,H", MicroOp.FETCH_H, MicroOp.STORE_E);
//        codeTableMain.define(0x5D, "LD E,L", MicroOp.FETCH_L, MicroOp.STORE_E);
//        codeTableMain.define(0x5E, "LD E,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_E);
//        codeTableMain.define(0x5F, "LD E,A", MicroOp.FETCH_A, MicroOp.STORE_E);
//
//        codeTableMain.define(0x60, "LD H,B", MicroOp.FETCH_B, MicroOp.STORE_H);
//        codeTableMain.define(0x61, "LD H,C", MicroOp.FETCH_C, MicroOp.STORE_H);
//        codeTableMain.define(0x62, "LD H,D", MicroOp.FETCH_D, MicroOp.STORE_H);
//        codeTableMain.define(0x63, "LD H,E", MicroOp.FETCH_E, MicroOp.STORE_H);
//        codeTableMain.define(0x64, "LD H,H", MicroOp.FETCH_H, MicroOp.STORE_H);
//        codeTableMain.define(0x65, "LD H,L", MicroOp.FETCH_L, MicroOp.STORE_H);
//        codeTableMain.define(0x66, "LD H,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_H);
//        codeTableMain.define(0x67, "LD H,A", MicroOp.FETCH_A, MicroOp.STORE_H);
//        codeTableMain.define(0x68, "LD L,B", MicroOp.FETCH_B, MicroOp.STORE_L);
//        codeTableMain.define(0x69, "LD L,C", MicroOp.FETCH_C, MicroOp.STORE_L);
//        codeTableMain.define(0x6A, "LD L,D", MicroOp.FETCH_D, MicroOp.STORE_L);
//        codeTableMain.define(0x6B, "LD L,E", MicroOp.FETCH_E, MicroOp.STORE_L);
//        codeTableMain.define(0x6C, "LD L,H", MicroOp.FETCH_H, MicroOp.STORE_L);
//        codeTableMain.define(0x6D, "LD L,L", MicroOp.FETCH_L, MicroOp.STORE_L);
//        codeTableMain.define(0x6E, "LD L,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_L);
//        codeTableMain.define(0x6F, "LD L,A", MicroOp.FETCH_A, MicroOp.STORE_L);
//
//        codeTableMain.define(0x70, "LD (HL),B", MicroOp.FETCH_B, MicroOp.STORE_pHL);
//        codeTableMain.define(0x71, "LD (HL),C", MicroOp.FETCH_C, MicroOp.STORE_pHL);
//        codeTableMain.define(0x72, "LD (HL),D", MicroOp.FETCH_D, MicroOp.STORE_pHL);
//        codeTableMain.define(0x73, "LD (HL),E", MicroOp.FETCH_E, MicroOp.STORE_pHL);
//        codeTableMain.define(0x74, "LD (HL),H", MicroOp.FETCH_H, MicroOp.STORE_pHL);
//        codeTableMain.define(0x75, "LD (HL),L", MicroOp.FETCH_L, MicroOp.STORE_pHL);
//        codeTableMain.define(0x76, "HALT", MicroOp.HALT);
//        codeTableMain.define(0x77, "LD (HL),A", MicroOp.FETCH_A, MicroOp.STORE_pHL);
//        codeTableMain.define(0x78, "LD A,B", MicroOp.FETCH_B, MicroOp.STORE_A);
//        codeTableMain.define(0x79, "LD A,C", MicroOp.FETCH_C, MicroOp.STORE_A);
//        codeTableMain.define(0x7A, "LD A,D", MicroOp.FETCH_D, MicroOp.STORE_A);
//        codeTableMain.define(0x7B, "LD A,E", MicroOp.FETCH_E, MicroOp.STORE_A);
//        codeTableMain.define(0x7C, "LD A,H", MicroOp.FETCH_H, MicroOp.STORE_A);
//        codeTableMain.define(0x7D, "LD A,L", MicroOp.FETCH_L, MicroOp.STORE_A);
//        codeTableMain.define(0x7E, "LD A,(HL)", MicroOp.FETCH_pHL, MicroOp.STORE_A);
//        codeTableMain.define(0x7F, "LD A,A", MicroOp.FETCH_A, MicroOp.STORE_A);
//
//        codeTableMain.define(0x80, "ADD A,B", MicroOp.FETCH_B, MicroOp.ADD);
//        codeTableMain.define(0x81, "ADD A,C", MicroOp.FETCH_C, MicroOp.ADD);
//        codeTableMain.define(0x82, "ADD A,D", MicroOp.FETCH_D, MicroOp.ADD);
//        codeTableMain.define(0x83, "ADD A,E", MicroOp.FETCH_E, MicroOp.ADD);
//        codeTableMain.define(0x84, "ADD A,H", MicroOp.FETCH_H, MicroOp.ADD);
//        codeTableMain.define(0x85, "ADD A,L", MicroOp.FETCH_L, MicroOp.ADD);
//        codeTableMain.define(0x86, "ADD A,(HL)", MicroOp.FETCH_pHL, MicroOp.ADD);
//        codeTableMain.define(0x87, "ADD A,A", MicroOp.FETCH_A, MicroOp.ADD);
//        codeTableMain.define(0x88, "ADC A,B", MicroOp.FETCH_B, MicroOp.ADC);
//        codeTableMain.define(0x89, "ADC A,C", MicroOp.FETCH_C, MicroOp.ADC);
//        codeTableMain.define(0x8A, "ADC A,D", MicroOp.FETCH_D, MicroOp.ADC);
//        codeTableMain.define(0x8B, "ADC A,E", MicroOp.FETCH_E, MicroOp.ADC);
//        codeTableMain.define(0x8C, "ADC A,H", MicroOp.FETCH_H, MicroOp.ADC);
//        codeTableMain.define(0x8D, "ADC A,L", MicroOp.FETCH_L, MicroOp.ADC);
//        codeTableMain.define(0x8E, "ADC A,(HL)", MicroOp.FETCH_pHL, MicroOp.ADC);
//        codeTableMain.define(0x8F, "ADC A,A", MicroOp.FETCH_A, MicroOp.ADC);
//
//        codeTableMain.define(0x90, "SUB A,B", MicroOp.FETCH_B, MicroOp.SUB);
//        codeTableMain.define(0x91, "SUB A,C", MicroOp.FETCH_C, MicroOp.SUB);
//        codeTableMain.define(0x92, "SUB A,D", MicroOp.FETCH_D, MicroOp.SUB);
//        codeTableMain.define(0x93, "SUB A,E", MicroOp.FETCH_E, MicroOp.SUB);
//        codeTableMain.define(0x94, "SUB A,H", MicroOp.FETCH_H, MicroOp.SUB);
//        codeTableMain.define(0x95, "SUB A,L", MicroOp.FETCH_L, MicroOp.SUB);
//        codeTableMain.define(0x96, "SUB A,(HL)", MicroOp.FETCH_pHL, MicroOp.SUB);
//        codeTableMain.define(0x97, "SUB A,A", MicroOp.FETCH_A, MicroOp.SUB);
//        codeTableMain.define(0x98, "SBC A,B", MicroOp.FETCH_B, MicroOp.SBC);
//        codeTableMain.define(0x99, "SBC A,C", MicroOp.FETCH_C, MicroOp.SBC);
//        codeTableMain.define(0x9A, "SBC A,D", MicroOp.FETCH_D, MicroOp.SBC);
//        codeTableMain.define(0x9B, "SBC A,E", MicroOp.FETCH_E, MicroOp.SBC);
//        codeTableMain.define(0x9C, "SBC A,H", MicroOp.FETCH_H, MicroOp.SBC);
//        codeTableMain.define(0x9D, "SBC A,L", MicroOp.FETCH_L, MicroOp.SBC);
//        codeTableMain.define(0x9E, "SBC A,(HL)", MicroOp.FETCH_pHL, MicroOp.SBC);
//        codeTableMain.define(0x9F, "SBC A,A", MicroOp.FETCH_A, MicroOp.SBC);
//
//
//        codeTableMain.define(0xA0, "AND A,B", MicroOp.FETCH_B, MicroOp.AND);
//        codeTableMain.define(0xA1, "AND A,C", MicroOp.FETCH_C, MicroOp.AND);
//        codeTableMain.define(0xA2, "AND A,D", MicroOp.FETCH_D, MicroOp.AND);
//        codeTableMain.define(0xA3, "AND A,E", MicroOp.FETCH_E, MicroOp.AND);
//        codeTableMain.define(0xA4, "AND A,H", MicroOp.FETCH_H, MicroOp.AND);
//        codeTableMain.define(0xA5, "AND A,L", MicroOp.FETCH_L, MicroOp.AND);
//        codeTableMain.define(0xA6, "AND A,(HL)", MicroOp.FETCH_pHL, MicroOp.AND);
//        codeTableMain.define(0xA7, "AND A,A", MicroOp.FETCH_A, MicroOp.AND);
//        codeTableMain.define(0xA8, "XOR A,B", MicroOp.FETCH_B, MicroOp.XOR);
//        codeTableMain.define(0xA9, "XOR A,C", MicroOp.FETCH_C, MicroOp.XOR);
//        codeTableMain.define(0xAA, "XOR A,D", MicroOp.FETCH_D, MicroOp.XOR);
//        codeTableMain.define(0xAB, "XOR A,E", MicroOp.FETCH_E, MicroOp.XOR);
//        codeTableMain.define(0xAC, "XOR A,H", MicroOp.FETCH_H, MicroOp.XOR);
//        codeTableMain.define(0xAD, "XOR A,L", MicroOp.FETCH_L, MicroOp.XOR);
//        codeTableMain.define(0xAE, "XOR A,(HL)", MicroOp.FETCH_pHL, MicroOp.XOR);
//        codeTableMain.define(0xAF, "XOR A,A", MicroOp.FETCH_A, MicroOp.XOR);
//
//        codeTableMain.define(0xB0, "OR A,B", MicroOp.FETCH_B, MicroOp.OR);
//        codeTableMain.define(0xB1, "OR A,C", MicroOp.FETCH_C, MicroOp.OR);
//        codeTableMain.define(0xB2, "OR A,D", MicroOp.FETCH_D, MicroOp.OR);
//        codeTableMain.define(0xB3, "OR A,E", MicroOp.FETCH_E, MicroOp.OR);
//        codeTableMain.define(0xB4, "OR A,H", MicroOp.FETCH_H, MicroOp.OR);
//        codeTableMain.define(0xB5, "OR A,L", MicroOp.FETCH_L, MicroOp.OR);
//        codeTableMain.define(0xB6, "OR A,(HL)", MicroOp.FETCH_pHL, MicroOp.OR);
//        codeTableMain.define(0xB7, "OR A,A", MicroOp.FETCH_A, MicroOp.OR);
//        codeTableMain.define(0xB8, "CP A,B", MicroOp.FETCH_B, MicroOp.CP);
//        codeTableMain.define(0xB9, "CP A,C", MicroOp.FETCH_C, MicroOp.CP);
//        codeTableMain.define(0xBA, "CP A,D", MicroOp.FETCH_D, MicroOp.CP);
//        codeTableMain.define(0xBB, "CP A,E", MicroOp.FETCH_E, MicroOp.CP);
//        codeTableMain.define(0xBC, "CP A,H", MicroOp.FETCH_H, MicroOp.CP);
//        codeTableMain.define(0xBD, "CP A,L", MicroOp.FETCH_L, MicroOp.CP);
//        codeTableMain.define(0xBE, "CP A,(HL)", MicroOp.FETCH_pHL, MicroOp.CP);
//        codeTableMain.define(0xBF, "CP A,A", MicroOp.FETCH_A, MicroOp.CP);
//
//        // TODO verify below
//        codeTableMain.define(0xC0, "RET NZ", MicroOp.RETNZ);
//        codeTableMain.define(0xC1, "POP BC", MicroOp.POPW, MicroOp.STORE_BC);
//        codeTableMain.define(0xC2, "JP NZ,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPNZ);
//        codeTableMain.define(0xC3, "JP a16", MicroOp.FETCH_16, MicroOp.STORE_PC);
//        codeTableMain.define(0xC4, "CALL NZ,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLNZ);
//        codeTableMain.define(0xC5, "PUSH BC", MicroOp.FETCH_BC, MicroOp.PUSHW);
//        codeTableMain.define(0xC6, "ADD A,d8", MicroOp.FETCH_8, MicroOp.ADD);
//        codeTableMain.define(0xC7, "RST 00H", MicroOp.RST_00H);
//        codeTableMain.define(0xC8, "RET Z", MicroOp.RETZ);
//        codeTableMain.define(0xC9, "RET", MicroOp.RET);
//        codeTableMain.define(0xCA, "JP Z,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPZ);
//        codeTableMain.define(0xCB, "PREFIX CB", MicroOp.PREFIX_CB);
//        codeTableMain.define(0xCC, "CALL Z,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLZ);
//        codeTableMain.define(0xCD, "CALL a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALL);
//        codeTableMain.define(0xCE, "ADC A,d8", MicroOp.FETCH_8, MicroOp.ADC);
//        codeTableMain.define(0xCF, "RST 08H", MicroOp.RST_08H);
//
//        codeTableMain.define(0xD0, "RET NC", MicroOp.RETNC);
//        codeTableMain.define(0xD1, "POP DE", MicroOp.POPW, MicroOp.STORE_DE);
//        codeTableMain.define(0xD2, "JP NC,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPNC);
//        codeTableMain.define(0xD3, "OUT d8,A", MicroOp.FETCH_8_ADDRESS, MicroOp.FETCH_A, MicroOp.OUT);
//        codeTableMain.define(0xD4, "CALL NC,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLNC);
//        codeTableMain.define(0xD5, "PUSH DE", MicroOp.FETCH_DE, MicroOp.PUSHW);
//        codeTableMain.define(0xD6, "SUB d8", MicroOp.FETCH_8, MicroOp.SUB);
//        codeTableMain.define(0xD7, "RST 10H", MicroOp.RST_10H);
//        codeTableMain.define(0xD8, "RET C", MicroOp.RETC);
//        codeTableMain.define(0xD9, "EXX", MicroOp.EXX);
//        codeTableMain.define(0xDA, "JP C,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.JPC);
//        codeTableMain.define(0xDB, "in a,(n)", MicroOp.FETCH_8, MicroOp.IN, MicroOp.STORE_A);
//        codeTableMain.define(0xDC, "CALL C,a16", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLC);
//        codeTableMain.define(0xDD, "Prefix DD", MicroOp.PREFIX_DD);
//        codeTableMain.define(0xDE, "SBC A,d8", MicroOp.FETCH_8, MicroOp.SBC);
//        codeTableMain.define(0xDF, "RST 18H", MicroOp.RST_18H);
//
//        codeTableMain.define(0xE0, "RET PO", MicroOp.RET_PO);
//        codeTableMain.define(0xE1, "POP HL", MicroOp.POPW, MicroOp.STORE_HL);
//        codeTableMain.define(0xE2, "JP PO,NN", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_PO);
//        codeTableMain.define(0xE3, "ex (sp),hl", MicroOp.EX_SP_HL);
//        codeTableMain.define(0xE4, "call po,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLPO);
//        codeTableMain.define(0xE5, "PUSH HL", MicroOp.FETCH_HL, MicroOp.PUSHW);
//        codeTableMain.define(0xE6, "AND d8", MicroOp.FETCH_8, MicroOp.AND);
//        codeTableMain.define(0xE7, "RST 20H", MicroOp.RST_20H);
//        codeTableMain.define(0xE8, "RET pe", MicroOp.RET_PE);
//        codeTableMain.define(0xE9, "JP (HL)", MicroOp.SET_ADDR_FROM_HL, MicroOp.JP); // CHECK
//        codeTableMain.define(0xEA, "JP PE,NN", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_PE);
//        codeTableMain.define(0xEB, "EX DE, HL", MicroOp.EX_DE_HL);
//        codeTableMain.define(0xEC, "call pe,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLPE);
//        codeTableMain.define(0xED, "Prefix ED", MicroOp.PREFIX_ED);
//        codeTableMain.define(0xEE, "XOR d8", MicroOp.FETCH_8, MicroOp.XOR);
//        codeTableMain.define(0xEF, "RST 28H", MicroOp.RST_28H);
//
//        codeTableMain.define(0xF0, "RET P", MicroOp.RET_P);
//        codeTableMain.define(0xF1, "POP AF", MicroOp.POPW, MicroOp.STORE_AF);
//        codeTableMain.define(0xF2, "JP P,NN", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_P);
//        codeTableMain.define(0xF3, "DI", MicroOp.DI);
//        codeTableMain.define(0xF4, "call p,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLP);
//        codeTableMain.define(0xF5, "PUSH AF", MicroOp.FETCH_AF, MicroOp.PUSHW);
//        codeTableMain.define(0xF6, "OR d8", MicroOp.FETCH_8, MicroOp.OR);
//        codeTableMain.define(0xF7, "RST 30H", MicroOp.RST_30H);
//        codeTableMain.define(0xF8, "RET m", MicroOp.RET_M); // FIX
//        codeTableMain.define(0xF9, "LD SP,HL", MicroOp.FETCH_HL, MicroOp.STORE_SP);
//        codeTableMain.define(0xFA, "jp m,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.JP_M);
//        codeTableMain.define(0xFB, "EI", MicroOp.EI);
//        codeTableMain.define(0xFC, "call m,nn", MicroOp.FETCH_16_ADDRESS, MicroOp.CALLM);
//        codeTableMain.define(0xFD, "Prefix FD", MicroOp.PREFIX_FD);
//        codeTableMain.define(0xFE, "CP d8", MicroOp.FETCH_8, MicroOp.CP);
//        codeTableMain.define(0xFF, "RST 38H", MicroOp.RST_38H);
    }


}
