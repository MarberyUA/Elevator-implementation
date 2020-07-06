package com.dev.elevator;

public class Main {
    public static void main(String[] args) {
        int[][] arr = new int[][] {{}, {6,5,2}, {4}, {}, {0,0,0}, {}, {}, {3,6,4,5,6}, {}, {1,10,2}, {1,4,3,2}};
        Elevator.theLift(arr, 5);
    }
}
