class Buffer {

    private int size;
    public int[] buff;
    private int rear;
    private int front;

    public Buffer() {
        buff = new int[10];
        size = 0;
        front = -1;
        rear = -1;
    }

    public void enqueue(int item) {
        if (!isFull()) {
            rear += 1;
            rear %= buff.length;

            buff[rear] = item;
            size += 1;

            if (front == -1) {
                front = rear;
            }
        }
    }

    public int dequeue() {
        int temp;
        if (isEmpty()) {
            return -1;
        } else {
            temp = buff[front];
            buff[front] = 0;
            front = (front + 1) % buff.length;
            size--;
        }
        return temp;
    }

    public boolean isFull() {
        return (size == buff.length);
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int s() {
        return size;
    }
}