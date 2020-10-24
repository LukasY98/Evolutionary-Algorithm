package sample;

import java.util.ArrayList;

public class Node {

    private ArrayList coordinates = new ArrayList<Integer>();

    public Node(ArrayList arrayList) {
        this.coordinates = arrayList;
    }

    public ArrayList getCoordinates() {
        return coordinates;
    }
}
