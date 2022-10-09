import java.util.ArrayList;

public class Metrics{
    /**
     * Calculates the Gini Index of a decision node
     * @param groups an array of ArrayLists split from decision node containing data
     * @param classes the classes that the classifier should produce
     * @return The Gini Index
     */
    public static double gini(ArrayList<double[]>[] groups, double[] classes){
        double totalSize = 0;
        for (ArrayList<double[]> group : groups) {
            totalSize += group.size();
        }
        double result = 0;
        for (ArrayList<double[]> group : groups){
            double size = group.size();
            if(size == 0){
                continue;
            }
            double score = 0;
            for (double class_val : classes){
                double p = 0;
                for(double[] data : group){
                    p += data[data.length-1] == class_val ? 1 : 0;
                }
                p /= size;
                score += p * p;
            }
            result += (1.0 - score) * size / totalSize;
        }
        return result;
    }
}
