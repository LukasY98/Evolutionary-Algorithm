package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Controller {

    private  Solution[] solutions = new Solution[20];
    private int dimensions;
    private int nodesAmount;
    private int iterations;
    private int size;
    private int mutationProbability = 15;

    @FXML
    private Button startBtn;
    @FXML
    private ChoiceBox dimBox = new ChoiceBox();
    @FXML
    private ChoiceBox nodBox = new ChoiceBox();
    @FXML
    private ChoiceBox itBox = new ChoiceBox();
    @FXML
    private ChoiceBox sizeBox = new ChoiceBox();
    @FXML
    private ListView eventLog = new ListView();

    @FXML
    public void initialize() {
        dimBox.getItems().removeAll(dimBox.getItems());
        dimBox.getItems().addAll(FXCollections.observableArrayList(2,3,4,5,6,7,8,9));
        dimBox.setValue(2);
        nodBox.getItems().removeAll(dimBox.getItems());
        nodBox.getItems().addAll(FXCollections.observableArrayList(20,50,100,500));
        nodBox.setValue(20);
        itBox.getItems().removeAll(itBox.getItems());
        itBox.getItems().addAll(FXCollections.observableArrayList(100,500,1000,5000));
        itBox.setValue(100);
        sizeBox.getItems().removeAll(sizeBox.getItems());
        sizeBox.getItems().addAll(FXCollections.observableArrayList(10,20,30,40,50));
        sizeBox.setValue(10);
    }

    @FXML
    public void onStartPressed() {
        ArrayList<Node> nodes = new ArrayList<>();
        dimensions = (int) dimBox.getValue();
        nodesAmount = (int) nodBox.getValue();
        iterations = (int) itBox.getValue();
        size = (int) sizeBox.getValue();
        eventLog.getItems().add("Dimensions: " + dimensions);
        eventLog.getItems().add("Nodes: " + nodesAmount);
        eventLog.getItems().add("Iteration: " + iterations);
        eventLog.getItems().add("Size: " + size);
        createNodes(nodes);
        generateSolutions(nodes);
        evaluateFitness();
        for (int i=0; i<iterations;i++) {
            System.out.println("####################### ITERATION " + i + " #######################");
            recombine();
            evaluateFitness();
            mutate();
            printLength();
            eventLog.getItems().add("Current Best: " + solutions[0].getLength());
            eventLog.scrollTo(eventLog.getItems().size());
        }
    }

    @FXML
    public void onClearPressed() {
        eventLog.getItems().clear();
    }

    /**
     * creates Nodes using the function "createNode" based on the selected amount of nodes.
     * @param nodes
     */
    private void createNodes(ArrayList<Node> nodes) {
        for (int i=0; i<nodesAmount; i++) {
            nodes.add(createNode());
        }
        printNodes(nodes);
    }

    /**
     * Generates 20 solutions by creating a random sequence of all generated nodes.
     * @param nodes
     */
    private void generateSolutions(ArrayList<Node> nodes) {
        for (int i=0; i<20; i++) {
            Collections.shuffle(nodes);
            solutions[i] = new Solution(new ArrayList(nodes));
        }
    }

    /**
     * Sorts the array of solutions using Bubble Sort based on the length of their respective path so that the shortest
     * path is at the beginning of the array and the longest path is at the end.
     */
    private void evaluateFitness() {
        for (int i=0; i<20; i++) {
            computeLength(solutions[i]);
        }
        boolean sorted = false;
        Solution temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < solutions.length - 1; i++) {
                if (solutions[i].getLength() > solutions[i+1].getLength()) {
                    temp = solutions[i];
                    solutions[i] = solutions[i+1];
                    solutions[i+1] = temp;
                    sorted = false;
                }
            }
        }
    }

    private void recombine() {
        for (int i=5; i<solutions.length; i++) { //solutions 0 to 4 are elites
            int pivot = (int) (1 + (Math.random() * solutions[i].getSequence().size() - 2));
            int combine; //index of a solution used for combination with solutions[i]

            //combination with oneself should not be possible
            do {
                combine = (int) (Math.random() * 10);
            } while (combine == i);

            //removes nodes in solutions[i] based on the sequence of nodes in solutions[j] and appends them to solution[i]
            for (int j=solutions[i].getSequence().size()-1-pivot; j<nodesAmount-1; j++) {
                solutions[i].getSequence().remove(solutions[combine].getSequence().get(j));
                solutions[i].getSequence().add(solutions[combine].getSequence().get(j));
            }
        }
    }

    private void mutate() {
        for (int i=5; i<solutions.length; i++) { //solutions 0 to 4 are elites
            if ((int) (Math.random() * 100) < mutationProbability) {
                //TESTING
                //System.out.println("Mutation occurred");
                //System.out.print("Changed: ");
                //for (int j=0; j<nodesAmount; j++) {
                //    Node node = solutions[i].getSequence().get(j);
                //    System.out.print(node.getCoordinates() + " ");
                //}
                int a = (int) (Math.random() * solutions[i].getSequence().size());
                int b;
                do {
                    b = (int) (Math.random() * solutions[i].getSequence().size());
                } while (a == b);
                Collections.swap(solutions[i].getSequence(), a, b);
                //TESTING
                //System.out.println();
                //System.out.print("To: ");
                //for (int j=0; j<nodesAmount; j++) {
                //    Node node = solutions[i].getSequence().get(j);
                //    System.out.print(node.getCoordinates() + " ");
                //}
                //System.out.println();
            }
        }
    }

    /**
     * Creates a Node in an coordinate system based on the selected amount of dimensions.
     * @return The created node with random coordinates
     */
    public Node createNode() {
        ArrayList coordinates = new ArrayList<>();
        Random r = new Random();
        for (int i=0; i<dimensions; i++) {
            float coordinate = (float) (-size + r.nextDouble() * (size*2));
            coordinates.add(coordinate);
        }
        return new Node(coordinates);
    }

    /**
     * Computes the length of the paths of all solution using the function "computeDistance" between all nodes of a
     * solution.
     * @param solution Array containing the sequences
     */
    public void computeLength(Solution solution) {
        float length = 0;
        for (int i=0; i<solution.getSequence().size()-1; i++) {
            length += computeDistance(solution.getSequence().get(i).getCoordinates(),
                    solution.getSequence().get(i+1).getCoordinates());
        }

        solution.setLength(length);
    }

    /**
     * Computes the distance between two points in a coordinate system independent of the amount of dimensions.
     * @param a Coordinates of one point
     * @param b Coordinates of the other point
     * @return The Distance between a and b
     */
    public float computeDistance(ArrayList<Float> a, ArrayList<Float> b) {
        float total = 0, diff;
        for (int i = 0; i < a.size(); i++) {
            diff = b.get(i) - a.get(i);
            total += diff * diff;
        }
        return (float) Math.sqrt(total);
    }

    /**
     * Prints the coordinates for all Nodes.
     */
    public void printNodes(ArrayList<Node> nodes) {
        int nodeNmb = 0;
        for (Node node : nodes) {
            System.out.print(nodeNmb + ": ");
            for (int i=0; i< node.getCoordinates().size(); i++) {
                System.out.print(node.getCoordinates().get(i) + "  ");
            }
            System.out.println();
            nodeNmb++;
        }
    }

    /**
     * Prints the sequences for all current solutions.
     * (Currently not used, but can be used for testing)
     */
    public void printSequence() {
        for (int i=0; i<solutions.length; i++) {
            for (int j=0; j<nodesAmount; j++) {
                Node node = solutions[i].getSequence().get(j);
                System.out.print(node.getCoordinates() + " ");
            }
            System.out.print(solutions[i].getLength());
            System.out.println();
        }
    }

    /**
     * Only prints the length of all solutions
     */
    public void printLength() {
        for (int i=0; i<solutions.length; i++) {
            System.out.println(solutions[i].getLength());
        }
    }
}
