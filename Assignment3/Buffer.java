class Buffer {

    private int currentSize;
    public int[] circularQueueElements;
    private int rear;
    private int front;

    public Buffer() {
        circularQueueElements = new int[10];
        currentSize = 0;
        front = -1;
        rear = -1;
    }

    public void enqueue(int item) {
        if (isFull()) {
            return;
        } else {
            rear = (rear + 1) % circularQueueElements.length;
            circularQueueElements[rear] = item;
            currentSize++;

            if (front == -1) {
                front = rear;
            }
        }
    }

    /**
     * Dequeue element from Front.
     */
    public int dequeue() {
        int deQueuedElement;
        if (isEmpty()) {
            return -1;
        } else {
            deQueuedElement = circularQueueElements[front];
            circularQueueElements[front] = 0;
            front = (front + 1) % circularQueueElements.length;
            currentSize--;
        }
        return deQueuedElement;
    }

    /**
     * Check if queue is full.
     */
    public boolean isFull() {
        return (currentSize == circularQueueElements.length);
    }

    /**
     * Check if Queue is empty.
     */
    public boolean isEmpty() {
        return (currentSize == 0);
    }

    public int getRear() {
        return rear;
    }

    public int getSize() {
        return currentSize;
    }
}