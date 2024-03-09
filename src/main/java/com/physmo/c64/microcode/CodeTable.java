package com.physmo.c64.microcode;

import com.physmo.c64.Utils;

import java.util.HashMap;
import java.util.Map;


public class CodeTable {

    MicroOp[][] opsList = new MicroOp[500][];
    Map<Integer, String> names = new HashMap<>();
    Map<String, Integer> opCodeLookup = new HashMap<>();
    String name;

    public CodeTable(String name) {
        this.name = name;
        for (int i = 0; i < opsList.length; i++) {
            opsList[i] = new MicroOp[]{MicroOp.TODO};
        }
    }

    public void define(int opCode, String name, MicroOp... microcodes) {
        opsList[opCode] = microcodes;
        names.put(opCode, name);
        opCodeLookup.put(name, opCode);
    }

    public MicroOp[] getInstructionCode(int instruction) {
        if (opsList[instruction].length == 1 && opsList[instruction][0] == MicroOp.TODO) {
            throw new RuntimeException("Instruction 0x" + Utils.toHex2(instruction) + " not defined (Code table " + name + ")");
        }
        return opsList[instruction];
    }

    public String getInstructionName(int instruction) {
        return names.get(instruction);
    }

    public int getOpcodeByName(String name) {
        if (!opCodeLookup.containsKey(name)) {
            throw new RuntimeException("Opcode not found: " + name);
        }
        return opCodeLookup.get(name);
    }

}
