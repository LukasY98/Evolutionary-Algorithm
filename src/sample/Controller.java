package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Random;

public class Controller {

    private ArrayList nodes = new ArrayList<Node>();

    private int dimensions;
    private int nodesAmount;

    private int size = 10;

    @FXML
    private Button btn;
    @FXML
    private ChoiceBox dimBox = new ChoiceBox();
    @FXML
    private ChoiceBox nodBox = new ChoiceBox();

    @FXML
    public void initialize() {
        dimBox.getItems().removeAll(dimBox.getItems());
        dimBox.getItems().addAll(FXCollections.observableArrayList(2,3,4,5,6,7,8,9));
        nodBox.getItems().removeAll(dimBox.getItems());
        nodBox.getItems().addAll(FXCollections.observableArrayList(5,10,20,50,100));
    }

    @FXML
    public void onButtonPressed() {
        dimensions = (int) dimBox.getValue();
        nodesAmount = (int) nodBox.getValue();
        for (int i=0; i<nodesAmount; i++) {
            nodes.add(createNode());
        }
        for (int i=0; i<nodes.size(); i++) {

        }

    }



    public double computeDistance() {
        return 0.0;
    }

    public Node createNode() {
        ArrayList coordinates = new ArrayList<>();
        Random r = new Random();
        for (int i=0; i<dimensions; i++) {
            float coordinate = (float) (-size + r.nextDouble() * (size*2));
            coordinates.add(coordinate);
            System.out.println(coordinate);
        }
        return new Node(coordinates);
    }

}
