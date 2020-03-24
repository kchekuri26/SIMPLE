import java.util.ArrayList;
import java.util.List;

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

    public void generate(int operator, int operand){
        bytecode.add(operator);
        bytecode.add(operand);
    }

    private void add(int value){
        accumulator += memory[value];
    }

    private void minus(int value){
        accumulator = accumulator - memory[value];
    }

    private void addi(int value){
        accumulator += value;
    }

    private void minusi(int value){
        accumulator = accumulator - value;
    }

    private void store(){
        memory[memorySize] = accumulator;
        memorySize++;
        accumulator = 0;
    }

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

    public ArrayList<Integer> getBytecode() {
        return bytecode;
    }

    public int[] getMemory() {
        return memory;
    }

    public static void main(String[] args) {

    }


}
