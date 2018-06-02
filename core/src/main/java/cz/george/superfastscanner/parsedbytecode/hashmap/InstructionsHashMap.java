package cz.george.superfastscanner.parsedbytecode.hashmap;

import cz.george.superfastscanner.utils.map.ArrayListHashMap;
import cz.george.superfastscanner.parsedbytecode.clazz.*;

import java.util.*;

/**
 * Instructions-oriented hashmap
 */
public class InstructionsHashMap extends ArrayListHashMap<Instruction, Instruction> {
    public InstructionsHashMap(Set<Clazz> classes) {
        for (Clazz clazz : classes)
            for (Method method : clazz.methods)
                for (Instruction instruction : method.instructions) {
                    putEntry(instruction, instruction);
                }
    }
}