import java.util.ArrayList;

public class Metrics{
    /**
     * Calculates the Gini Index of a decision node
     * @param group an array list containing data
     * @param classes the classes that the classifier should produce
     * @return The Gini Index
     */
    public static double gini(ArrayList<double[]> group, double[] classes){
        double size = group.size();
        if(size == 0){
            return 0;
        }
        double score = 0;
        for (double class_val : classes){
            double p = 0;
            for(double[] data : group){
                p += data[data.length-1] == class_val ? 1 : 0;
            }
            p /= size;
            score += p * (1-p);
        }
        return score;
    }
}
