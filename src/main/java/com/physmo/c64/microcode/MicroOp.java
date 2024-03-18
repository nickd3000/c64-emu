package com.physmo.c64.microcode;

public enum MicroOp {
    TODO,
    NOP, BRK,

    GET_NEXT_BYTE,      // Read next byte to dataBus

    // Addressing modes
    SET_ADDRESS_ABSOLUTE,
    SET_ADDRESS_ABSOLUTE_INDIRECT,
    SET_ADDRESS_ABSOLUTE_X,
    SET_ADDRESS_ABSOLUTE_Y,
    SET_ADDRESS_ZERO_PAGE,
    SET_ADDRESS_ZERO_PAGE_X,
    SET_ADDRESS_ZERO_PAGE_Y,
    SET_ADDRESS_ZERO_PAGE_INDIRECT_X,
    SET_ADDRESS_ZERO_PAGE_INDIRECT_Y,
    SET_ADDRESS_RELATIVE,

    FETCH_BYTE_FROM_ADDRESS,
    STORE_BYTE_AT_ADDRESS,
    FETCH_A,
    STORE_A,


//    FETCH_8_ADDRESS,    // (Zero Page) Fetches the next 1 byte onto the lower end of the address bus
//    FETCH_16_ADDRESS,   // (Absolute) Fetches the next 2 bytes onto the Address bus
//    FETCH_BYTE_FROM_ADDR,
//    FETCH_WORD_FROM_ADDR,
//    FETCH_BYTE_FROM_ADDR_X,
//    FETCH_WORD_FROM_ADDR_Y,

    AND,
    ORA,
    EOR,


    // STORE
    STA, // Store A in memory
    STX,
    STY,


    // LOADS
    LDA,
    LDX, // Load value to X
    LDY, // Load value to Y

    INC, DEC,
    CMP,
    CPX,
    CPY,
    ADC,
    SBC,

    // INC and DEC X and Y
    DEX, DEY, INX, INY,

    // TRANSFERS
    TAX, TAY, TSX, TXA, TXS, TYA,

    // FLAGS
    CLC, CLD, CLI, CLV, SEC, SED, SEI,

    // JUMP and CALL
    JSR,
    JMP,

    // RETURN
    RTS, RTI,

    // BRANCH
    BCC, BCS, BEQ, BMI, BNE, BPL, BVC, BVS,

    // ROTATE and SHIFT
    ROL,
    ROR,
    ASL,
    LSR,

    // BINARY
    BIT,

    // STACK
    PHA, PHP, PLA, PLP,

//    PREFIX_CB,
//    PREFIX_ED,
//    PREFIX_FD,
//    PREFIX_FD_CB,
//    PREFIX_DD_CB,
//    PREFIX_DD,
//
//    INC_8,      // INC temp (8 bit)
//    INC_16,     // INC temp (16 bit)
//    DEC_8,      // DEC temp (8 bit)
//    DEC_16,     // DEC temp (16 bit)
//
//    NONE,       // Used to do nothing (for auto generated microcodes)
//
//    FETCH_A,     // Read register A to temp
//    FETCH_B,     // Read register B to temp
//    FETCH_C,     // Read register C to temp
//    FETCH_D,     // Read register D to temp
//    FETCH_E,     // Read register E to temp
//    FETCH_H,     // Read register H to temp
//    FETCH_L,     // Read register L to temp
//    FETCH_F,     // Read register F to temp
//    FETCH_I,     // Read register I to temp
//    FETCH_R,     // Read register R to temp
//    FETCH_0,
//    FETCH_8,     // Read next byte
//    FETCH_16,    // Read next word
//
//    FETCH_AF,    // Read register AF to temp
//    FETCH_BC,    // Read register BC to temp
//    FETCH_DE,    // Read register DE to temp
//    FETCH_HL,    // Read register HL to temp
//    FETCH_IX,    // Read register IX to temp
//    FETCH_IY,    // Read register IX to temp
//    FETCH_IY_H,    // Read register IY high byte
//    FETCH_IY_L,    // Read register IY low byte
//    FETCH_IX_H,    // Read register IX high byte
//    FETCH_IX_L,    // Read register IX low byte
//    FETCH_SP,    // Read Stack Pointer to temp
//    FETCH_PC,    // Read Program Counter to temp
//    FETCH_pBC,   // Read value in memory location BC to temp
//    FETCH_pIY_D,    // Read value in memory location (IY + D) to temp
//    FETCH_pIX_D,    // Read value in memory location (IX + D) to temp
//    FETCH_BYTE_TO_DISPLACEMENT, // Fetch the next byte to the special D store for use int the (IY + D) commands.
//
//    FETCH_8_ADDRESS,    // Fetches the next 1 byte onto the lower end of the address bus
//    FETCH_16_ADDRESS, // Fetches the next 2 bytes onto the Address bus
//    FETCH_pHL,
//    FETCH_BYTE_FROM_ADDR,
//    FETCH_WORD_FROM_ADDR,
//
//    STORE_A,    // Store temp in A
//    STORE_B,    // Store temp in B
//    STORE_C,    // Store temp in C
//    STORE_D,    // Store temp in D
//    STORE_E,    // Store temp in E
//    STORE_H,    // Store temp in H
//    STORE_L,    // Store temp in L
//    STORE_I,    // Store temp in L
//    STORE_BC,
//    STORE_HL,
//    STORE_p16WORD,
//    STORE_DE,
//    STORE_SP,
//    STORE_IX,
//    STORE_IY,
//    STORE_IY_H,    // Store register IY high byte
//    STORE_IY_L,    // Store register IY low byte
//    STORE_IX_H,    // Store register IX high byte
//    STORE_IX_L,    // Store register IX low byte
//    STORE_pIY_D,
//    STORE_pIX_D,
//    STORE_pHL,
//    STORE_R,
//    OUT_PORT_C,
//
//
//    ADD_HL,    // Add HL to temp
//    ADD_IY,
//    ADD_IX,
//
//    RLC,
//    STOP,
//    HALT,
//
//    SET_ADDR_FROM_A,
//    SET_ADDR_FROM_HL,
//
//    SET_ADDR_FROM_BC,
//    SET_ADDR_FROM_DE,
//    STORE_BYTE_AT_ADDRESS,
//    STORE_WORD_AT_ADDRESS,
//    RRC,
//    SET_ADDR_FROM_HL_INC,
//    SET_ADDR_FROM_HL_DEC,
//
//    LD_A_I,
//
//    // Bit operations
//    BIT0, BIT1, BIT2, BIT3, BIT4, BIT5, BIT6, BIT7,
//    SET0, SET1, SET2, SET3, SET4, SET5, SET6, SET7,
//    RES0, RES1, RES2, RES3, RES4, RES5, RES6, RES7,
//
//    // Shifts
//    SLA,
//    SRA,
//    SLL,
//    SRL,
//
//    // Rotates
//    RLA,
//    RLCA,
//    RRCA,
//    RRA,
//    RL,
//    RR,
//
//    DAA,
//    CPL,
//
//    // Carry flag set and clear
//    SCF,
//    CCF,
//
//    ADD,
//    ADC,
//    SUB,
//    SBC,
//    CP,
//    OR,
//    XOR,
//    AND,
//    NEG,
//
//    SBC_HL, // 16 bit version applied to HL
//    ADC_HL,
//
//    // RETURNS
//    RET, RETI, RETZ, RETNZ,
//    RETNC, RETC,
//    RET_PO, RET_P, RET_M, RET_PE,
//    RETN,
//
//    // STACK
//    PUSHW,
//    POPW,
//
//    CALLNZ,
//
//    // JUMPS
//    JP, JRNZ, JRZ,
//    JRNC, JRC, JR,
//    JPZ, JPNZ, JPC, JPNC, DJNZ,
//    JP_PO, JP_PE, JP_P, JP_M,
//
//    // CALL
//    CALLZ, CALLC, CALLNC, CALL, CALLPO, CALLP, CALLPE, CALLM,
//
//    // PORT IN/OUT
//    OUT,
//    IN,
//    IN0, OUT0,
//
//    // Reset vectors
//    RST_00H, RST_08H,
//    RST_10H, RST_18H,
//    RST_20H, RST_28H,
//    RST_30H, RST_38H,
//
//    EXX,
//
//    // Special ED commands
//    LDI,
//    CPI,
//    INI,
//    OUTI,
//    OTIR,
//    LDD,
//    CPD,
//    IND,
//    OUTD,
//
//    // Repeating versions of the special ED commands
//    LDIR,
//    LDDR, // Data copying loop
//    CPIR,
//    CPDR,
//    OTDR,
//
//    MLT_BC, MLT_DE, MLT_HL, MLT_SP,
//
//    LDZPGA, ADDSPNN,
//    FETCH_ZPG,
//    STORE_AF,
//    STORE_PC,
//    // Interrupt enable/disable
//    DI, EI,
//    IM_0, IM_1, IM_2,
//    LDHLSPN, EX_DE_HL,
//    EX_AF_AF, FETCH_PORT_C, EX_SP_HL, RLD, RRD,       // Exchange AF with AF'
//    EX_SP_IY, EX_SP_IX
}

