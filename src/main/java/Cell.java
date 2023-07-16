
public abstract class Cell {

    private String bgColour;
    private String value;

    //    private int cellIndex;
    public Cell() {
    }

    public void setBgColour(String bgColour) {
        this.bgColour = bgColour;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void printCell() {
        System.out.print(bgColour + value + PrintColor.RESET);
    }

}
