import java.util.ArrayList;
import java.util.List;

/**
 * This class generates bytecode and interprets the bytecode programs and updates the memory.
 */
public class BytecodeInterpreter {

    public static final int ADD = 0;
    public static final int MINUS = 1;
    public static final int ADDI = 2;
    public static final int MINUSI = 3;
    public static final int STORE = 4;

    private ArrayList<Integer> bytecode;
    private static int memory[];
    private static int accumulator=0;
    private static int memorySize = 0;

    public BytecodeInterpreter(int size){
        bytecode = new ArrayList<Integer>();
        memory = new int[size];
    }

    /**
     * this method takes in a command and an operand as parameters and adds the command to the bytecode being generated.
     * @param operator
     * @param operand
     */
    public void generate(int operator, int operand){
        bytecode.add(operator);
        bytecode.add(operand);
    }

    /**
     * this method gets a value from the memory and adds it to the value in the accumulator.
     * @param value
     */
    private void add(int value){
        accumulator += memory[value];
    }

    /**
     * this method gets a value from the memory and subtracts it from the value in the accumulator.
     * @param value
     */
    private void minus(int value){
        accumulator = accumulator - memory[value];
    }

    /**
     * this method adds the inputted value to the value in the accumulator.
     * @param value
     */
    private void addi(int value){
        accumulator += value;
    }

    /**
     * this method subtracts the inputted value from the value in the accumulator.
     * @param value
     */
    private void minusi(int value){
        accumulator = accumulator - value;
    }

    /**
     * this method stores the value in the accumulator in the memory.
     */
    private void store(){
        memory[memorySize] = accumulator;
        memorySize++;
        accumulator = 0;
    }

    /**
     * this method runs the code in bytecode and modifies the memory.
     */
    public void run(){
        int i = 0;
        while (i<bytecode.size()){
            if (bytecode.get(i) == ADD){
                add(bytecode.get(i+1));
            }
            else if (bytecode.get(i) == MINUS){
                minus(bytecode.get(i+1));
            }
            else if (bytecode.get(i) == ADDI){
                addi(bytecode.get(i+1));
            }
            else if (bytecode.get(i) == MINUSI){
                minusi(bytecode.get(i+1));
            }
            else if (bytecode.get(i) == STORE){
                store();
            }
            i+=2;
        }
    }

    /**
     * this method returns the bytecode.
     * @return bytecode
     */
    public ArrayList<Integer> getBytecode() {
        return bytecode;
    }

    /**
     * this method returns the memory.
     * @return memory
     */
    public int[] getMemory() {
        return memory;
    }

    @Override
    public String toString() {
        return "BytecodeInterpreter{" +
                "bytecode=" + bytecode + "memory" + memory +
                '}';
    }
}
