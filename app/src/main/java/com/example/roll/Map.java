package com.example.roll;

import android.util.Pair;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Map {
    static int TOPEDGE = 0;
    static int RIGHTEDGE = 1;
    static int BOTTOMEDGE = 2;
    static int LEFTEDGE = 3;
    public Field[][] fields;
    public int startingX;
    public int startingY;

    public Map(int type){
        fields = new Field[10][8];
        for(int i =0; i<10; i++){
            for(int j=0; j<8;j++){
                fields[i][j]=new Field();
            }
        }
        for(int i =0; i<8; i++){
            fields[0][i].edges[TOPEDGE] = false;
            fields[9][i].edges[BOTTOMEDGE] = false;
        }
        for(int i =0; i<10; i++){
            fields[i][0].edges[LEFTEDGE] = false;
            fields[i][7].edges[RIGHTEDGE] = false;
        }
        startingX = 25;
        startingY = 25;
        if(type==0){
            fields[6][3].edges[RIGHTEDGE] = false;
            fields[6][4].edges[LEFTEDGE] = false;
            fields[3][5].edges[BOTTOMEDGE] = false;
            fields[4][5].edges[TOPEDGE] = false;
            fields[3][6].edges[BOTTOMEDGE] = false;
            fields[4][6].edges[TOPEDGE] = false;
            fields[0][7].hole = true;
            fields[8][6].hole = true;
            fields[6][5].finish = true;
            startingX = 40;
            startingY = 40;
            return;
        }
        if(type == 1){
            v(0, 1); v(0,4);
            v(1, 1); v(1, 4); v(1,5);
            v(3, 0); v(3,3);
            v(4, 1); v(4, 3);
            v(5,1); v(5, 2); v(5,3); v(5,4); v(5,5); v(5, 6);
            v(6,4); v(6, 6);
            v(7,1); v(7, 3); v(7,5);
            v(8, 3);
            h(0, 3); h(0, 8);
            h(1, 3); h(1, 6); h(1,7); h(1,8);
            h(2, 0); h(2, 3);
            h(3, 3);
            h(4,1); h(4,5); h(4,6); h(4,8);
            h(5, 4); h(5,7);
            h(6, 0); h(6,3); h(6,6); h(6, 8);
            h(7,0); h(7,8);
            fields[2][2].hole = true;
            fields[2][7].hole = true;
            fields[6][1].hole = true;
            fields[4][1].finish = true;
            startingX = 70;
            startingY = 70;
            return;
        }
        if(type == 2){
            v(0,1); v(0,5);
            v(1,1); v(1,3);
            v(2,0); v(2,5);
            v(3,1); v(3,5);
            v(4,1); v(4,4); v(4,5);
            v(5,1); v(5,4); v(5,5);
            v(6,2); v(6,3); v(6,4);
            v(7,0); v(7,1);
            v(8,2); v(8,3); v(8,5);
            v(9,2); v(9,5);
            h(0,6);
            h(1,2); h(1,5);
            h(2,1); h(2,3); h(2,6);
            h(3,0); h(3,1); h(3,4); h(3,6);
            h(4,0); h(4,1); h(4,2); h(4,6);
            h(5,1); h(5,4);
            h(6,2);
            h(7,1); h(7,4); h(7,8);
            fields[4][0].hole = true;
            fields[4][3].hole = true;
            fields[4][5].hole = true;
            fields[6][6].hole = true;
            fields[7][4].hole = true;
            fields[0][2].finish = true;
            startingX = 70;
            startingY = 25;
            return;
        }
        if(type==3){
            v(0,1); v(0,3); v(0,5);
            v(1,1); v(1,4); v(1,5);
            v(2,1); v(2,4); v(2,5);
            v(3,0); v(3,4); v(3,5); v(3,6);
            v(4,0); v(4,1); v(4,6);
            v(5,0); v(5,1); v(5,3); v(5,4); v(5,5);
            v(6,2); v(6,3); v(6,5);
            v(7,2); v(7,3); v(7,4); v(7,6);
            v(8,3);
            v(9,1); v(9,4);
            h(0,6);
            h(1,5); h(1,6); h(1,7); h(1,8);
            h(2,3); h(2,4); h(2,5); h(2,7);
            h(3,0); h(3,1); h(3,2); h(3,3);
            h(4,1); h(4,4);
            h(5,4); h(5,7);
            h(6,1); h(6,4); h(6,7); h(6,8);
            h(7,0);
            fields[4][1].hole = true;
            fields[3][3].hole = true;
            fields[6][7].hole = true;
            fields[8][6].hole = true;
            fields[8][3].hole = true;
            fields[7][7].finish = true;
            startingX = 25;
            startingY = 70;
            return;
        }
        if(type==4){
            v(0,4);
            v(1,2); v(1,4); v(1,5); v(1,6);
            v(2,1); v(2,3); v(2,5); v(2,6);
            v(3,1); v(3,2); v(3,3); v(3,6);
            v(4,5);
            v(5,1); v(5,3);
            v(6,1); v(6,2); v(6,5);
            v(7,2); v(7,4); v(7,5);
            v(8,4); v(8,5);
            v(9,0); v(9,4);
            h(0,0); h(0,2); h(0,5);
            h(1,7);
            h(2,0); h(2,2); h(2,5); h(2,7); h(2,8);
            h(3,1); h(3,3); h(3,4); h(3,7); h(3,8);
            h(4,1); h(4,6);
            h(5,2); h(5,5);
            h(6,0); h(6,5); h(6,7);
            h(7,4);h(7,6);
            fields[4][1].hole = true;
            fields[4][4].hole = true;
            fields[5][5].hole = true;
            fields[8][2].hole = true;
            fields[9][7].hole = true;
            fields[3][7].finish = true;
            startingX = 200;
            startingY = 40;
            return;
        }
        if(type==5){
            v(0,1); v(0,2);
            v(1,4); v(1,6);
            v(2,2); v(2,5);
            v(4,0); v(4,2); v(4,5);
            v(5,0); v(5,1); v(5,2); v(5,3);
            v(6,2); v(6,6);
            v(7,3);
            v(8,4); v(8,5); v(8,6);
            v(9,6);
            h(0,3);
            h(1,1); h(1,2);
            h(2,1);
            h(3,2); h(3,3); h(3,6);
            h(4,2); h(4,7);
            h(5,0); h(5,2); h(5,3); h(6,6); h(5,8);
            h(6,0); h(6,2); h(6,4); h(6,5);
            h(7,1); h(7,2); h(7,4);
            fields[0][0].hole = true;
            fields[1][4].hole = true;
            fields[3][3].hole = true;
            fields[4][2].hole = true;
            fields[6][4].hole = true;
            fields[7][0].hole = true;
            fields[7][2].hole = true;
            fields[9][1].hole = true;
            fields[9][3].hole = true;
            fields[1][7].finish = true;
            startingX = 330;
            startingY = 150;
            return;
        }
        if(type==6){
            v(0,1); v(0,2); v(0,3);
            v(1,3); v(1,4); v(1,6);
            v(2,1); v(2,2); v(2,4); v(2,5);
            v(3,2); v(3,3); v(3,6);
            v(4,1); v(4,5);
            v(5,1); v(5,2); v(5,4);
            v(6,2); v(6,3);
            v(8,0); v(8,2);
            v(9,1); v(9,2);
            h(0,4); h(0,6); h(0,7);
            h(1,0); h(1,2); h(1,4);
            h(2,1); h(2,3); h(2,4); h(2,6);
            h(3,3); h(3,6);
            h(4,2); h(4,4);
            h(5,3);
            h(6,0); h(6,4);
            h(7,0); h(7,2); h(7,4); h(7,5); h(7,6);
            fields[2][5].hole = true;
            fields[3][3].hole = true;
            fields[4][5].hole = true;
            fields[5][1].hole = true;
            fields[6][3].hole = true;
            fields[7][4].hole = true;
            fields[8][6].hole = true;
            fields[9][4].hole = true;
            fields[5][0].finish = true;
            startingX = 70;
            startingY = 200;
            return;
        }
        if(type == 2137) {
            int i, j;
            int walls = getRandomNumber(3, 6);
            for(int k =0; k<walls; k++){
                i = getRandomNumber(0, 10);
                j = getRandomNumber(0, 7);
                v(i, j);
            }
            walls = getRandomNumber(3, 6);
            for(int k =0; k<walls; k++){
                i = getRandomNumber(0, 9);
                j = getRandomNumber(0, 8);
                h(j, i);
            }
            int holes = 4;
            while(holes>0){
                i = getRandomNumber(0, 10);
                j = getRandomNumber(0, 8);
                if(!fields[i][j].hole) {
                    fields[i][j].hole = true;
                    holes--;
                }
            }
            return;
        }
        if(type<0){
            while(true) {
                for(int i =0; i<10; i++){
                    for(int j=0; j<8;j++){
                        fields[i][j]=new Field();
                    }
                }
                for(int i =0; i<8; i++){
                    fields[0][i].edges[TOPEDGE] = false;
                    fields[9][i].edges[BOTTOMEDGE] = false;
                }
                for(int i =0; i<10; i++){
                    fields[i][0].edges[LEFTEDGE] = false;
                    fields[i][7].edges[RIGHTEDGE] = false;
                }
                int level = -type;
                int i=0, j=0;
                int walls = getRandomNumber(20, 40);
                for(int k =0; k<walls; k++){
                    i = getRandomNumber(0, 10);
                    j = getRandomNumber(0, 7);
                    v(i, j);
                }
                walls = getRandomNumber(20, 40);
                for(int k =0; k<walls; k++){
                    i = getRandomNumber(0, 9);
                    j = getRandomNumber(0, 8);
                    h(j, i);
                }
                int holes = 2+ level/2;
                for(int k =0; k<holes; k++){
                    i = getRandomNumber(0, 10);
                    j = getRandomNumber(0, 8);
                    fields[i][j].hole = true;
                }
                for(int a =0; a<10; a++){
                    for(int b=0; b<8;b++){
                        fields[a][b].visited = false;
                    }
                }
                while (fields[i][j].hole) {
                    i = getRandomNumber(0, 10);
                    j = getRandomNumber(0, 8);
                }
                startingX = j * 45 + 22;
                startingY = i * 45 + 22;
                fields[i][j].visited = true;
                fields[i][j].distance = 0;
                Queue<Pair<Integer, Integer>> Q = new ArrayDeque<>();
                Pair<Integer, Integer> p = new Pair<>(i, j);
                Q.add(p);
                while (!Q.isEmpty()) {
                    p = Q.remove();
                    i = p.first;
                    j = p.second;
                    int distance = fields[i][j].distance;
                    if (fields[i][j].edges[RIGHTEDGE] && !fields[i][j + 1].visited && !fields[i][j + 1].hole) {
                        fields[i][j + 1].visited = true;
                        fields[i][j + 1].distance = distance +1;
                        Q.add(new Pair<>(i, j + 1));
                    }
                    if (fields[i][j].edges[LEFTEDGE] && !fields[i][j - 1].visited && !fields[i][j - 1].hole) {
                        fields[i][j - 1].visited = true;
                        fields[i][j - 1].distance = distance +1;
                        Q.add(new Pair<>(i, j - 1));
                    }
                    if (fields[i][j].edges[TOPEDGE] && !fields[i - 1][j].visited && !fields[i - 1][j].hole) {
                        fields[i - 1][j].visited = true;
                        fields[i - 1][j].distance = distance +1;
                        Q.add(new Pair<>(i - 1, j));
                    }
                    if (fields[i][j].edges[BOTTOMEDGE] && !fields[i + 1][j].visited && !fields[i + 1][j].hole) {
                        fields[i + 1][j].visited = true;
                        fields[i + 1][j].distance = distance +1;
                        Q.add(new Pair<>(i + 1, j));
                    }
                }
                if(fields[i][j].distance>12) {
                    fields[i][j].finish = true;
                    break;
                }
            }
        }
    }

    void v(int i, int j){
        fields[i][j].edges[RIGHTEDGE] = false;
        fields[i][j+1].edges[LEFTEDGE] = false;
    }
    void h(int j, int i){
        fields[i][j].edges[BOTTOMEDGE] = false;
        fields[i+1][j].edges[TOPEDGE] = false;
    }
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public Pair<Integer, Integer> getRandomPosition(){
        int i, j;
        do{
            i = getRandomNumber(0, 10);
            j = getRandomNumber(0, 8);
        }while(fields[i][j].hole || fields[i][j].visited);
        fields[i][j].visited = true;
        return new Pair<>(i, j);
    }
}
    /*Callable<Object> task = new Callable<Object>() {
        public Object call() {
            new HighScoresActivity.GetScores().execute();
            return null;
        }
    };
    Future<Object> future = executor.submit(task);
        try{
                future.get(3, TimeUnit.SECONDS);
                }catch (Exception ex){
                }*/