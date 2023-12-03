import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class RottenApples {
    public class Cell {
        public int state; // 0: empty, 1: fresh, 2: rotten
        public int x;
        public int y;

        public Cell(int state, int x, int y) {
            this.state = state;
            this.x = x;
            this.y = y;
        }
    }

    private Cell[][] grid;
    private Queue<Cell> queue = new LinkedList<>();
    private Set<Cell> rotten = new HashSet<>();
    public int time = 0;


    public RottenApples(int[][] matrix) {
        int M = matrix.length;
        int N = matrix[0].length;
        grid = new Cell[M][N];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = new Cell(matrix[i][j], i, j);
                if (grid[i][j].state == 2) {
                    rotten.add(grid[i][j]);
                    queue.add(grid[i][j]);
                }
            }
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    public int getFreshApples() {
        int fresh = 0;
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.state == 1) {
                    fresh++;
                }
            }
        }
        return fresh;
    } 

    private Set<Cell> getFreshNeighbors(Cell cell) {
        Set<Cell> neighbors = new HashSet<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] d : directions) {
            int newX = cell.x + d[0];
            int newY = cell.y + d[1];

            if (isValid(newX, newY) && grid[newX][newY].state == 1) {
                neighbors.add(grid[newX][newY]);
            }
        }
        return neighbors;
    }

    public boolean processOneWave() {
        if (queue.isEmpty()) {
            return false;
        }

        int size = queue.size();
        for (int i = 0; i < size; i++) {
            Cell rottenCell = queue.poll();
            for (Cell neighbor : getFreshNeighbors(rottenCell)) {
                neighbor.state = 2; // Rot the fresh apple
                queue.add(neighbor);
                rotten.add(neighbor);
            }
        }
        if (!queue.isEmpty()) {
            time += 24;
            System.out.println("Time: " + time);
            return true;
        } return false;
    }

    public Cell[][] getCurrentMatrixState() {
        return grid;
    }

    public boolean isProcessingComplete() {
        return queue.isEmpty();
    }
}
