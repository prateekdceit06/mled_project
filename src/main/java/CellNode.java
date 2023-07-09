

public class CellNode extends Cell{
    public CellNode(int nodeIndex) {
        if (nodeIndex<10){
            setValue(" 0" + nodeIndex + " ");
        } else{
            setValue(" " + nodeIndex + " ");
        }
        setBgColour(PrintColor.GREEN_BACKGROUND);
    }

}
