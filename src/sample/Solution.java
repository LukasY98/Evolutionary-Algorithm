package sample;

import java.util.ArrayList;

public class Solution {

    private float length;
    private ArrayList<Node> sequence;

    public Solution(ArrayList sequence) {
        this.sequence = sequence;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public ArrayList<Node> getSequence() {
        return sequence;
    }

    public void setSequence(ArrayList<Node> sequence) {
        this.sequence = sequence;
    }
}
