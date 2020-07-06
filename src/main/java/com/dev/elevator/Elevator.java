package com.dev.elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Elevator {
    private static List<Integer> moves;
    private static int stage;
    private static int moving; // moving : 1 = up ; -1 = down
    private static int capacity;
    private static List<List<Integer>> peopleOnFloorsLists;
    private static List<Integer> inLift;
    private static List<List<Integer>> outLift;
    private static boolean PRINTING = true;

    private static List<List<Integer>> cstrStacks(final int[][] queues) {

        List<List<Integer>> stks = new ArrayList<>();
        for (int n = 0; n < queues.length; n++) {
            stks.add(Arrays.stream(queues[n])
                    .boxed()
                    .collect(Collectors.toList()));
        }
        return stks;
    }

    public static int[] theLift(final int[][] queues, final int inputCapacity) {
        stage = -1;
        moving = 1;
        capacity = inputCapacity;
        peopleOnFloorsLists = cstrStacks(queues);
        inLift = new ArrayList<>();
        outLift = new ArrayList<>();
        for (int i = 0; i < queues.length; i++) {
            outLift.add(new ArrayList<>());
        }
        moves = new ArrayList<>();
        moves.add(0);

        while (isStillPeopleToBeLifted() || !liftIsEmpty()) {
            stage += moving;
            if (somePeopleInAreWantToGoOut() || someGuysWaitingSameWayAreAtThatFloor(stage)) {
                liftStopAtThisStage();
                somePeopleInAreGoOut();
                takeInIfAllPeopleGoingTheSameWay();
            }

            if (PRINTING) {
                System.out.println("" + stage + " | Inside: " + inLift + " ; " + moves);
            }
            /* Decide what the lift has to do now, considering that people inside have the priority:
            *
            * No change of direction in these cases :
            * - If the lift contains people going in the same direction: They have the priority...
            * - If there are people further that want to go in the same direction...
            * - If the lift is Empty and that some people further want to go in the other direction,
            *   the lift goes as far as it can before taking in some people again (who wana go to
            *   in the other direction).
            *
            * In other cases, the lift begins to move the other way.
            * For the simplicity of the implementation, the lift is shifted one step backward, so it
            * will be able to managed the presence of people
            * at the current floor (before update) who
            * want to go in the other direction.
            */

            if (!(anyPeopleInAreGoingSameWay()
                    || areSomeGuysFurtherWaitingForSameWay()
                    || (liftIsEmpty()
                    && areSomeGuysFurtherWantingForTheOtherWay()))) {
                moving *= -1;
                stage -= moving;
            }
        }
        liftStopAtThatStage(0); // return to the ground if needed
        if (PRINTING) {
            System.out.println("" + stage + " | Inside: " + inLift + " ; " + moves);
        }
        return moves.stream().mapToInt(i -> i.intValue()).toArray();
    }

    private static boolean isStillPeopleToBeLifted() {
        return !peopleOnFloorsLists.stream().allMatch(l -> l.isEmpty());
    }

    private static boolean liftIsEmpty() {
        return inLift.isEmpty();
    }

    private static boolean somePeopleInAreWantToGoOut() {
        return inLift.contains(stage);
    }

    private static void somePeopleInAreGoOut() {
        List<Integer> list = inLift.stream().filter(i -> i == stage).collect(Collectors.toList());
        list.forEach(i -> outLift.get(stage).add(i));
        inLift = inLift.stream().filter(i -> i != stage).collect(Collectors.toList());
    }

    private static void liftStopAtThisStage() {
        liftStopAtThatStage(stage);
    }

    private static void liftStopAtThatStage(int lvl) {
        if (moves.get(moves.size() - 1) != lvl) {
            moves.add(lvl);
        }
    }

    private static boolean anyPeopleInAreGoingSameWay() {
        return inLift.stream().anyMatch(guy -> guy * moving > stage * moving);
    }

    private static boolean areSomePeopleAtThisFloor(int lvl) {
        return !peopleOnFloorsLists.get(lvl).isEmpty();
    }

    private static boolean someGuysWaitingSameWayAreAtThatFloor(int lvl) {
        return areSomePeopleAtThisFloor(lvl)
                && peopleOnFloorsLists.get(lvl)
                .stream()
                .anyMatch(i -> i * moving > lvl * moving);
    }

    private static void takeInIfAllPeopleGoingTheSameWay() {
        List<Integer> peopleGoingIn = new ArrayList<>();
        List<Integer> peopleThere = peopleOnFloorsLists.get(stage);

        // Get all people who can enter the lift, according to its
        // capacity and the queue order:
        for (int n = 0; n < peopleThere.size(); n++) {
            if (peopleGoingIn.size() + inLift.size() == capacity) {
                break;
            }
            if (peopleThere.get(n) * moving > stage * moving) {
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
    private static boolean areSomeGuysFurtherWaitingForSameWay() {

        for (int i = stage + moving; 0 <= i && i < peopleOnFloorsLists.size(); i += moving) {
            if (someGuysWaitingSameWayAreAtThatFloor(i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean areSomeGuysFurtherWantingForTheOtherWay() {
        for (int i = stage + moving; 0 <= i && i < peopleOnFloorsLists.size(); i += moving) {
            if (areSomePeopleAtThisFloor(i)) {
                return true;
            }
        }
        return false;
    }
}
