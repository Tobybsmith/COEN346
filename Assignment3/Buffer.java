 

public class Buffer {
    private final static int BUFFER_SIZE = 10;

    private int[] buffer = new int[BUFFER_SIZE];
    private int in;
    private int out;
    private int count = 0;


    public Buffer(){
    }

    public int readCount(){
        return count;
    }

    public int getNextItem(int pos){
        int item = buffer[pos];
        buffer[pos] = 0;
        return item;
    }

    public void setIn(int pos){
        this.in = pos;
    }

    public int getNextPosition(){
        return in;
    }

    public void insertItem(int item, int pos){
        //no way to know if this is overwritten or not
        buffer[pos] = item;
        count += 1;
    }

}
