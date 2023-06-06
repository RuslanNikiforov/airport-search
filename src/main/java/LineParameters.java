public class LineParameters {

    //index in file
    private long index;
    // line number in file
    private int number;

    public LineParameters() {
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "LineParameters{" +
                "index=" + index +
                ", number=" + number +
                '}';
    }
}
