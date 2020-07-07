import com.dev.elevator.Elevator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ElevatorTests {
    private Elevator elevator;

    public ElevatorTests() {
        elevator = new Elevator(5);
    }

    @Test
    public void testUp() {
        final int[][] queues = {
                new int[0],
                new int[0],
                new int[]{5,5,5},
                new int[0],
                new int[0],
                new int[0],
                new int[0],
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,2,5,0}, result);
    }

    @Test
    public void testDown() {
        final int[][] queues = {
                new int[0],
                new int[0],
                new int[]{1,1},
                new int[0],
                new int[0],
                new int[0],
                new int[0],
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,2,1,0}, result);
    }

    @Test
    public void testUpAndUp() {
        final int[][] queues = {
                new int[0],
                new int[]{3},
                new int[]{4},
                new int[0],
                new int[]{5},
                new int[0],
                new int[0],
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,1,2,3,4,5,0}, result);
    }

    @Test
    public void testDownAndDown() {
        final int[][] queues = {
                new int[0],
                new int[]{0},
                new int[0],
                new int[0],
                new int[]{2},
                new int[]{3},
                new int[0],
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,5,4,3,2,1,0}, result);
    }

    @Test
    public void testUpAndDown() {
        final int[][] queues = {
                new int[]{3},
                new int[]{2},
                new int[]{0},
                new int[]{2},
                new int[0],
                new int[0],
                new int[]{5},
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,1,2,3,6,5,3,2,0}, result);
    }

    @Test
    public void testEnterOnGroundFloor() {
        final int[][] queues = {
                new int[]{1,2,3,4},
                new int[0],
                new int[0],
                new int[0],
                new int[0],
                new int[0],
                new int[0],
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,1,2,3,4,0}, result);
    }

    @Test
    public void testLiftFullUp() {
        final int[][] queues = {
                new int[]{3,3,3,3,3,3},
                new int[0],
                new int[0],
                new int[0],
                new int[0],
                new int[0],
                new int[0],
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,3,0,3,0}, result);
    }

    @Test
    public void testLiftFullDown() {
        final int[][] queues = {
                new int[0],
                new int[0],
                new int[0],
                new int[]{1,1,1,1,1,1,1,1,1,1,1},
                new int[0],
                new int[0],
                new int[0],
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,3,1,3,1,3,1,0}, result);
    }

    @Test
    public void testLiftFullUpAndDown() {
        final int[][] queues = {
                new int[]{3,3,3,3,3,3},
                new int[0],
                new int[0],
                new int[0],
                new int[0],
                new int[]{4,4,4,4,4,4},
                new int[0],
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,3,5,4,0,3,5,4,0}, result);
    }

    @Test
    public void testTricky() {
        final int[][] queues = {
                new int[0],
                new int[]{0,0,0,6},
                new int[0],
                new int[0],
                new int[0],
                new int[]{6,6,0,0,0,6},
                new int[0],
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,1,5,6,5,1,0,1,0}, result);
    }

    @Test
    public void testHighlander() {
        final int[][] queues = {
                new int[0],
                new int[]{2},
                new int[]{3,3,3},
                new int[]{1},
                new int[0],
                new int[0],
                new int[0],
        };
        elevator.setCapacity(1);
        final int[] result = elevator.theLift(queues);
        elevator.setCapacity(5);
        assertArrayEquals(new int[]{0,1,2,3,1,2,3,2,3,0}, result);
    }

    @Test
    public void testWhereAllGoesGroundFloor() {
        final int[][] queues = {
                new int[0],
                new int[]{0,0,0,0},
                new int[]{0,0,0,0},
                new int[]{0,0,0,0},
                new int[]{0,0,0,0},
                new int[]{0,0,0,0},
                new int[]{0,0,0,0},
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0,6,5,4,3,2,1,0,5,4,3,2,1,0,4,3,2,1,0,3,2,1,0,1,0}, result);
    }

    @Test
    public void testEmpty() {
        final int[][] queues = {
                new int[0],
                new int[0],
                new int[0],
                new int[0],
                new int[0],
                new int[0],
                new int[0],
        };
        final int[] result = elevator.theLift(queues);
        assertArrayEquals(new int[]{0}, result);
    }

    @Test
    public void testBugs() {

        final int[][] queues = {
                new int[]{8, 8, 6},
                new int[]{8, 3, 4, 7},
                new int[0],
                new int[]{2, 6, 8, 5},
                new int[0], //4
                new int[]{0, 8, 8, 4, 1},
                new int[0],
                new int[]{3, 2, 2, 3},
                new int[0]
        };
        elevator.setCapacity(9);
        final int[] result = elevator.theLift(queues);
        elevator.setCapacity(5);
        assertArrayEquals(new int[]{0, 1, 3, 4, 5, 6, 7, 8, 7, 5, 4, 3, 2, 1, 0}, result);
    }
}
