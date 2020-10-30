package sample;

import java.util.ArrayList;

public class Node {

    private ArrayList<Float> coordinates = new ArrayList();

    public Node(ArrayList arrayList) {
        this.coordinates = arrayList;
    }

    public ArrayList getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Float> coordinates) {
        this.coordinates = coordinates;
    }
}
