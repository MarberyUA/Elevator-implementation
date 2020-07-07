package com.dev.elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Elevator {
    private int stage;
    private int direction;
    private int capacity;
    private List<List<Integer>> peopleOnFloorsLists;
    private List<Integer> inLift;
    private List<Integer> moves;

    public Elevator() {
        stage = -1;
        direction = 1;
        capacity = 5;
    }

    public Elevator(int inputCapacity) {
        stage = -1;
        direction = 1;
        capacity = inputCapacity > 0 ? inputCapacity : 1;
    }

    public void setCapacity(int newCapacity) {
        capacity = newCapacity;
    }

    public int[] deliver(final int[][] queues) {
        moves = new ArrayList<>();
        peopleOnFloorsLists = covertDimensionalArrayToList(queues);
        inLift = new ArrayList<>();
        moves.add(0);

        while (isStillPeopleToBeLifted() || !liftIsEmpty()) {
            stage += direction;
            if (somePeopleInAreWantToGoOut() || someGuysWaitingSameWayAreAtThatFloor(stage)) {
                liftStopAtThisStage();
                somePeopleInAreGoOut();
                takeInIfAllPeopleGoingTheSameWay();
            }

            System.out.println("" + stage + " | Inside: " + inLift + " ; " + moves);

            /* Decide what the lift has to do now, considering that people inside have the priority:
             *
             * No change of direction in these cases :
             * - If the lift contains people going in the same direction: They have the priority...
             * - If there are people further that want to go in the same direction...
             * - If the lift is Empty and that some people further want
             *   to go in the other direction, the lift goes as far as it can
             *   before taking in some people again (who wanna go to in the other direction).
             *
             * In other cases, the lift begins to move the other way.
             * For the simplicity of the implementation, the lift is shifted one step backward,
             * so it will be able to managed the presence of people at the current floor
             * (before update) who want to go in the other direction.
             */

            if (!(anyPeopleInAreGoingSameWay()
                    || areSomeGuysFurtherWaitingForSameWay()
                    || (liftIsEmpty()
                    && areSomeGuysFurtherWantingForTheOtherWay()))) {
                direction *= -1;
                stage -= direction;
            }
        }
        liftStopAtThatStage(0); // return to the ground if needed
        System.out.println("" + stage + " | Inside: " + inLift + " ; " + moves);
        return moves.stream().mapToInt(i -> i.intValue()).toArray();
    }

    private List<List<Integer>> covertDimensionalArrayToList(final int[][] queues) {
        List<List<Integer>> list = new ArrayList<>();
        for (int n = 0; n < queues.length; n++) {
            list.add(Arrays.stream(queues[n])
                    .boxed()
                    .collect(Collectors.toList()));
        }
        return list;
    }

    private boolean isStillPeopleToBeLifted() {
        return !peopleOnFloorsLists.stream().allMatch(l -> l.isEmpty());
    }

    private boolean liftIsEmpty() {
        return inLift.isEmpty();
    }

    private boolean somePeopleInAreWantToGoOut() {
        return inLift.contains(stage);
    }

    private void somePeopleInAreGoOut() {
        inLift = inLift.stream().filter(i -> i != stage).collect(Collectors.toList());
    }

    private void liftStopAtThisStage() {
        liftStopAtThatStage(stage);
    }

    private void liftStopAtThatStage(int lvl) {
        if (moves.get(moves.size() - 1) != lvl) {
            moves.add(lvl);
        }
    }

    private boolean anyPeopleInAreGoingSameWay() {
        return inLift.stream().anyMatch(guy -> guy * direction > stage * direction);
    }

    private boolean areSomePeopleAtThisFloor(int lvl) {
        return !peopleOnFloorsLists.get(lvl).isEmpty();
    }

    private boolean someGuysWaitingSameWayAreAtThatFloor(int lvl) {
        return areSomePeopleAtThisFloor(lvl)
                && peopleOnFloorsLists.get(lvl)
                .stream()
                .anyMatch(i -> i * direction > lvl * direction);
    }

    private void takeInIfAllPeopleGoingTheSameWay() {
        List<Integer> peopleGoingIn = new ArrayList<>();
        List<Integer> peopleThere = peopleOnFloorsLists.get(stage);

        // Get all people who can enter the lift, according to its
        // capacity and the queue order:
        for (int n = 0; n < peopleThere.size(); n++) {
            if (peopleGoingIn.size() + inLift.size() == capacity) {
                break;
            }
            if (peopleThere.get(n) * direction > stage * direction) {
                peopleGoingIn.add(peopleThere.get(n));
            }
        }

        inLift.addAll(peopleGoingIn); // update the lift content
        // Remove the new people in the lift from this floor:
        while (!peopleGoingIn.isEmpty()) {
            peopleOnFloorsLists.get(stage).remove(peopleGoingIn.remove(0));
        }
    }

    // are some guys further
    private boolean areSomeGuysFurtherWaitingForSameWay() {

        for (int i = stage + direction; 0 <= i && i < peopleOnFloorsLists.size(); i += direction) {
            if (someGuysWaitingSameWayAreAtThatFloor(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean areSomeGuysFurtherWantingForTheOtherWay() {
        for (int i = stage + direction; 0 <= i && i < peopleOnFloorsLists.size(); i += direction) {
            if (areSomePeopleAtThisFloor(i)) {
                return true;
            }
        }
        return false;
    }
}
