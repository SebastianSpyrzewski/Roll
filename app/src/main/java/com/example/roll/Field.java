package com.example.roll;

public class Field {
    public boolean[] edges;
    public boolean hole;
    public boolean finish;
    public boolean visited;
    public int distance;
    public Field(){
        edges = new boolean[4];
        for(int i =0; i<4; i++) edges[i] = true;
        hole = false;
        finish = false;
        visited = false;
        distance = 0;
    }

}
