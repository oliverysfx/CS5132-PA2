import java.util.ArrayList;

public class DecisionNode extends Node<Double>{
    private final int index;
    DecisionNode[] neighbours;

    /**
     * Initiates a decision tree node with the splitting value and index of decision value
     * @param value the splitting value
     * @param index the index of value to compare to splitting value in 1 data
     */
    public DecisionNode(double value, int index) {
        super(value, 2);
        this.index = index;
        neighbours = new DecisionNode[2];
    }

    /**
     * Split a dataset based on value and feature
     * @param dataset dataset to be split
     * @return An array of two arrayLists containing split data
     */
    public ArrayList<double[]>[] decide(ArrayList<double[]> dataset){
        ArrayList<double[]>[] output = new ArrayList[2];
        output[0] = new ArrayList<>();
        output[1] = new ArrayList<>();
        for(double[] data : dataset){
            if(data[index] < getItem()){
                output[0].add(data);
                continue;
            }
            output[1].add(data);
        }
        return output;
    }

    /**
     * choose to go to left child or right child
     * @param features features being used to predict
     * @return value representing left child or right child
     */
    public int predict(double[] features){
        return features[index] < getItem() ? 0 : 1;
    }

    public int getIndex() {
        return index;
    }

    public Double getValue() {
        return super.getItem();
    }

    public String toString(){
        return getValue() + " " + getIndex();
    }
}
