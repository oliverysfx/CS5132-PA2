import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Training {
    private static final double TEST_SIZE = 0.2;
    private static final long SEED = 39;
    private static final int NUM_FEATURES = 11;
    private static final String FILENAME = "data.csv";
    private static final String[] ABBR = {"AA", "BP", "FS", "PA", "PC", "PH", "PN", "PO", "PP", "PS", "QP", "TB"};
    private static final String[] SPECIES = {
            "European Silver Fir (Abies Alba)", "Silver Birch (Betula Pendula)", "European Beech (Fagus Sylvatica)",
            "Norway Spruce (Picea Abies)", "Swiss Pine (Pinus Cembra)", "Aleppo Pine (Pinus Halenpensis)",
            "Austrian Pine (Pinus Nigra)", "Black Poplar (Populus Nigra)", "Maritime Pine (Pinus Pinaster)",
            "Scots Pine (Pinus Sylvetris)", "Sessile Oak (Quercus Petraea)", "English Yew (Taxus Baccata)"
    };
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(FILENAME));
        in.readLine();  // ignore header line
        String line;

        List<String> abbr = Arrays.asList(ABBR);
        List<String> species = Arrays.asList(SPECIES);

        ArrayList<double[]> train = new ArrayList<>();
        ArrayList<double[]> x_test = new ArrayList<>();
        ArrayList<String> y_test = new ArrayList<>();

        double[] x = new double[NUM_FEATURES + 1];
        String[] split;

        Random rng = new Random(SEED);

        while ((line = in.readLine()) != null) {  // file ends with a blank line
            split = line.split(",");
            for (int i = 1; i < NUM_FEATURES + 1; i++) {
                x[i] = Double.parseDouble(split[i]);
            }
            x[NUM_FEATURES] = abbr.indexOf(split[NUM_FEATURES + 1]);

            if (rng.nextDouble() < TEST_SIZE) {
                x_test.add(Arrays.copyOf(x, NUM_FEATURES));
                y_test.add((split[NUM_FEATURES + 1]));
            } else {
                train.add(Arrays.copyOf(x, NUM_FEATURES + 1));
            }
        }
        in.close();

        DecisionTree clf = new DecisionTree();
        clf.build(train);

        ArrayList<String> y_pred = new ArrayList<>();

        for (int i = 0 ; i < x_test.size(); i++){
            y_pred.add(abbr.get((int) clf.predict(x_test.get(i))));
            System.out.println(abbr.get((int) clf.predict(x_test.get(i))));
            System.out.println(y_test.get(i));
        }
        System.out.println(accuracy(y_test, y_pred));
    }

    public static double accuracy(ArrayList y_true, ArrayList y_pred){
        int correct = 0;

        for (int i = 0; i < y_true.size(); i++){
            if (y_true.get(i).equals(y_pred.get(i))){
                correct ++;
            }
        }
        return (double) correct / y_true.size();
    }
}
