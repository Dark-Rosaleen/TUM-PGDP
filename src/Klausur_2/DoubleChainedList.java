package Klausur_2;

public class DoubleChainedList {
    private DoubleChainedListElement head;
    private DoubleChainedListElement tail;
    private int size;

    /*
    ====================================================================================================================
                                               Constructor and Reset
    ====================================================================================================================
     */
    public DoubleChainedList(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Reset current List
     */
    public void reset() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /*
    ====================================================================================================================
                                                Recursive Methods
    ====================================================================================================================
     */

    /**
     * add a new Element at the end of current List
     * @param value new Element value to be added
     */
    public void add_Recursive(int value) {
        if (head == null) {
            head = new DoubleChainedListElement(value);
        } else {
            head.add_fromElementPerspective(value);
        }
        size = calculateSize_Recursive();
    }

    /**
     * Get the Element at given index
     * @param idx target index
     * @return target element
     */
    public DoubleChainedListElement getElementByIndex_Recursive(int idx) {
        if (head == null) {
            throw new RuntimeException("Error: list is empty!");
        }
        DoubleChainedListElement result = head.getElementByIndex_FromElementPerspective(idx);
        if(result == null){
            throw new RuntimeException("Error: Index out of Bounds");
        }
        return result;
    }

    /**
     * Calculates current List Size recursively
     * @return current List Size
     */
    private int calculateSize_Recursive() {
        return head == null ? 0 : head.calculateSize_fromElementPerspective();
    }

    /*
    ====================================================================================================================
                                                Setter and Getter
    ====================================================================================================================
     */
    public int getSize() {
        return size;
    }

    public DoubleChainedListElement getHead() {
        return head;
    }

    public DoubleChainedListElement getTail() {
        return tail;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setHead(DoubleChainedListElement head) {
        this.head = head;
    }

    public void setTail(DoubleChainedListElement tail) {
        this.tail = tail;
    }
}
