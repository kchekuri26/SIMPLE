import java.util.ArrayList;
import java.util.List;

public class BytecodeInterpreter {

    public static final int LOAD = 0;
    public static final int LOADI = 1;
    public static final int STORE = 2;

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

    private void load(int value){
        accumulator += memory[value];
    }

    private void loadi(int value){
        accumulator+=value;
    }

    private void store(int value){
        memory[value] = accumulator;
        accumulator = 0;
    }

    public void run(){
        int i = 0;
        while (i<bytecode.size()){
            if (bytecode.get(i) == LOAD){
                load(bytecode.get(i+1));
            }
            else if (bytecode.get(i) == LOADI){
                loadi(bytecode.get(i+1));
            }
            else if (bytecode.get(i) == STORE){
                store(bytecode.get(i+1));
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
