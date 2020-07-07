package com.dev.elevator;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int randomFlatNums = (int)(Math.random() * (16) + 5);
        int[][] arr = new int[randomFlatNums][10];
        for (int i = 0; i < randomFlatNums; i++) {
            int peopleAmount = (int)(Math.random() * 11);
            arr[i] = new int[peopleAmount];
            for (int j = 0; j < peopleAmount; j++) {
                arr[i][j] = i < (arr.length / 2)
                        ? (int) ((Math.random()
                        * (arr.length - (arr.length / 2) - 1)) + arr.length / 2)
                        : (int) (Math.random() * ((arr.length / 2) - 1));
            }
        }
        System.out.println("Do you wanna see the queues at the each floor? Y - yes, N - no");
        Scanner scanner = new Scanner(System.in);
        if ((scanner.nextLine().strip().toLowerCase()).equals("y")) {
            for (int k = 0; k < arr.length; k++) {
                System.out.print("[");
                for (int b = 0; b < arr[k].length; b++) {
                    System.out.print(arr[k][b] + ", ");
                }
                System.out.println("]; Floor number: " + k
                        + " , People are waiting: " + arr[k].length);
            }
        }
        TimeUnit.SECONDS.sleep(5);
        Elevator elevator = new Elevator();
        elevator.deliver(arr);
    }
}
