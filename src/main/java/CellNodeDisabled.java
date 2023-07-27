public class CellNodeDisabled extends Cell {
    public CellNodeDisabled(int nodeIndex) {
    if (nodeIndex < 10) {
        setValue(" 0" + nodeIndex + " ");
    } else {
        setValue(" " + nodeIndex + " ");
    }
    setBgColour(PrintColor.RED_BACKGROUND);
}

}
