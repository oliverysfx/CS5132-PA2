import java.util.ArrayList;

public class DecisionNode extends Node<Double>{
    private int index;

    /**
     * Initiates a decision tree node with the splitting value and index of decision value
     * @param item the splitting value
     * @param index the index of value to compare to splitting value in 1 data
     */
    public DecisionNode(double item, int index) {
        super(item, 2);
        this.index = index;
    }

    /**
     * Initiates a decision tree node with the splitting value and index of decision value
     * @param item the splitting value
     * @param neighbours the children of this decision node
     * @param index the index of value to compare to splitting value in 1 data
     */
    public DecisionNode(double item, DecisionNode[] neighbours, int index) {
        super(item, neighbours);
        this.index = index;
    }

    public DecisionNode(DecisionNode n) {
        super(n);
        this.index = n.index;
    }

    public ArrayList<double[]>[] decide(ArrayList<double[]> dataset){
        ArrayList<double[]>[] output = new ArrayList[2];
        output[0] = new ArrayList<>();
        output[1] = new ArrayList<>();
        for(double[] data : dataset){
            if(data[data.length-1] < getItem()){
                output[0].add(data);
                continue;
            }
            output[1].add(data);
        }
        return output;
    }
}
