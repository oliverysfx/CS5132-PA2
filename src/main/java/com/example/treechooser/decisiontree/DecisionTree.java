package com.example.treechooser.decisiontree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DecisionTree {
    DecisionNode root;

    public DecisionTree(){}

    /**
     * Constructor for reading from file
     * @param node root node
     */
    private DecisionTree(DecisionNode node){
        root = node;
    }

    /**
     * Builds a decision tree from a dataset with max depth of 3 and min size of 0
     * @param dataset dataset to be trained on
     */
    public void build(ArrayList<double[]> dataset){
        build(dataset, 3);
    }

    /**
     * Builds a decision tree from a dataset with min size 0
     * @param dataset dataset to be trained on
     * @param max_depth max depth of tree
     */
    public void build(ArrayList<double[]> dataset, int max_depth){
        build(dataset, max_depth, 0);
    }

    /**
     * Builds a decision tree form a dataset
     * @param dataset dataset to be trained on
     * @param max_depth max depth of tree
     * @param min_size minimum number of data in output
     */
    public void build(ArrayList<double[]> dataset, int max_depth, int min_size){
        root = getSplit(dataset);
        build(root, dataset, max_depth, min_size, 0);
    }

    /**
     * Recursive function to build the tree
     * @param root root of subtree
     * @param dataset dataset/partial dataset to be trained on
     * @param max_depth max depth of tree
     * @param min_size min number of data in output
     * @param depth current depth of tree
     */
    private void build(DecisionNode root, ArrayList<double[]> dataset, int max_depth, int min_size, int depth){
        ArrayList<double[]>[] split = root.decide(dataset);
        if(split[0].size() == 0){
            root.neighbours[1] = new DecisionNode(getValue(split[1]), -1);
            root.neighbours[0] = new DecisionNode(getValue(split[1]), -1);
            return;
        }
        if(split[1].size() == 0){
            root.neighbours[0] = new DecisionNode(getValue(split[0]), -1);
            root.neighbours[1] = new DecisionNode(getValue(split[0]), -1);
            return;
        }
        if(depth >= max_depth){
            root.neighbours[0] = new DecisionNode(getValue(split[0]), -1);
            root.neighbours[1] = new DecisionNode(getValue(split[1]), -1);
            return;
        }
        if(split[0].size() <= min_size){
            root.neighbours[0] = new DecisionNode(getValue(split[0]), -1);
        } else {
            root.neighbours[0] = getSplit(split[0]);
            build(root.neighbours[0], split[0], max_depth, min_size, depth + 1);
        }
        if(split[1].size() <= min_size){
            root.neighbours[1] = new DecisionNode(getValue(split[1]), -1);
        } else {
            root.neighbours[1] = getSplit(split[1]);
            build(root.neighbours[1], split[1], max_depth, min_size, depth + 1);
        }
    }

    /**
     * Find best feature and value to split dataset
     * @param dataset dataset/partial dataset to be trained on
     * @return Decision node that contains the best feature and value possible
     */
    private DecisionNode getSplit(ArrayList<double[]> dataset){
        HashSet<Double> tmp = new HashSet<>();
        double bestScore = 1;
        DecisionNode bestNode = null;
        for(double[] data : dataset){
            tmp.add(data[data.length-1]);
        }
        Double[] temp = tmp.toArray(new Double[0]);
        double[] classes = new double[temp.length];
        for(int i = 0; i < temp.length; i++){
            classes[i] = temp[i];
        }
        for(int i = 0; i < dataset.get(0).length - 1; i++){
            for(double[] data : dataset){
                DecisionNode test = new DecisionNode(data[i], i);
                ArrayList<double[]>[] split = test.decide(dataset);
                double score = (Metrics.gini(split[0], classes) * split[0].size() +
                        Metrics.gini(split[1], classes) * split[1].size())/ dataset.size();
//                System.out.println(score + " " + bestScore);
                if(score < bestScore){
                    bestScore = score;
                    bestNode = test;
                }
            }
        }
        return bestNode;
    }

    /**
     * gets the most common class present in dataset
     * @param dataset The dataset to find most common class in
     * @return the most common class
     */
    private double getValue(ArrayList<double[]> dataset){
        double value = 0;
        int count = 0;
        HashMap<Double, Integer> map = new HashMap<>();
        for(double[] data : dataset){
            if(map.containsKey(data[data.length-1])){
                map.put(data[data.length-1], map.get(data[data.length-1]) + 1);
                if(map.get(data[data.length-1]) > count){
                    count = map.get(data[data.length-1]);
                    value = data[data.length-1];
                }
                continue;
            }
            map.put(data[data.length-1], 1);
            if(count == 0){
                value = data[data.length-1];
                count = 1;
            }
        }
        return value;
    }

    /**
     * predict a class based on features
     * @param features features being predicted using
     * @return predicted class
     */
    public double predict(double[] features){
        if(root == null){
            throw new IllegalArgumentException("Why? Just, why?");
        }
        return predict(features, root);
    }

    /**
     * recursive function to predict a class
     * @param features features being predicted using
     * @param node current decision node
     * @return predicted class
     */
    private double predict(double[] features, DecisionNode node){
        if(node.getIndex() == -1){
            return node.getValue();
        }
        return predict(features, node.neighbours[node.predict(features)]);
    }

    /**
     * writes the trained decision tree to a file
     * @param path path to write to
     */
    public void save(String path){
        try(FileWriter out = new FileWriter(path)){
            out.write(String.join(",", preOrder(root, new ArrayList<>())));
            out.write(System.getProperty("line.separator"));
            out.write(String.join(",", inOrder(root, new ArrayList<>())));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * read in an already trained decision tree from a path
     * @param path path to read file from
     * @return constructed decision tree
     */
    public static DecisionTree read(String path){
        try(Scanner in = new Scanner(new File(path))){
            String line1 = in.nextLine();
            return construct(new ArrayList<>(List.of(line1.split(","))),
                    new ArrayList<>(List.of(in.nextLine().split(","))));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * recursive method to construct a trained decision tree from pre-order and in-order traversal
     * @param preOrder pre-order traversal of the trained tree
     * @param inOrder in-order traversal of the trained tree
     * @return trained decison tree
     */
    private static DecisionTree construct(ArrayList<String> preOrder, ArrayList<String> inOrder){
        if(preOrder.size() == 0) return new DecisionTree();
        DecisionTree tree = new DecisionTree(new DecisionNode(Double.parseDouble(preOrder.get(0).split(" ")[0]), Integer.parseInt(preOrder.get(0).split(" ")[1])));
        ArrayList<String> leftInOrder = new ArrayList<>(inOrder.subList(0, inOrder.indexOf(preOrder.get(0))));
        ArrayList<String> rightInOrder = new ArrayList<>(inOrder.subList(inOrder.indexOf(preOrder.get(0)) + 1, inOrder.size()));
        ArrayList<String> leftPreOrder = new ArrayList<>(preOrder.subList(1, leftInOrder.size() + 1));
        ArrayList<String> rightPreOrder = new ArrayList<>(preOrder.subList(leftInOrder.size() + 1, inOrder.size()));
        tree.root.neighbours[0] = construct(leftPreOrder, leftInOrder).root;
        tree.root.neighbours[1] = construct(rightPreOrder, rightInOrder).root;
        return tree;
    }

    /**
     * Generates in-order traversal of the decision tree
     * @return in-order traversal of the decision tree
     */
    private ArrayList<String> inOrder(DecisionNode curr, ArrayList<String> result){
        if(curr.neighbours[0] != null){
            inOrder(curr.neighbours[0], result);
        }
        result.add(curr.toString());
        if(curr.neighbours[1] != null){
            inOrder(curr.neighbours[1], result);
        }
        return result;
    }
    /**
     * Generates pre-order traversal of the decision tree
     * @return pre-order traversal of the decision tree
     */
    private ArrayList<String> preOrder(DecisionNode curr, ArrayList<String> result){
        result.add(curr.toString());
        if(curr.neighbours[0] != null){
            preOrder(curr.neighbours[0], result);
        }
        if(curr.neighbours[1] != null){
            preOrder(curr.neighbours[1], result);
        }
        return result;
    }
}
