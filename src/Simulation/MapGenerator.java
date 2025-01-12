package Game;

import java.awt.*;
import java.util.Random;

public class MapGenerator {
    public int[][] map;
    public int[][][] positions;
    public int brickWidth;
    public int brickHeight;
    private int difficulty;
    private int screenWidth = 692;
    private int screenHeight = 592;

    private Color[][] brickColors;  // Array to hold the color of each brick
    private Random rand = new Random();

    public MapGenerator(int difficulty) {
        this.difficulty = difficulty;
        brickColors = new Color[10][10];  // Max rows and columns set to 10x10
        switch (difficulty) {
            case 2:
                initializeMap(9, 10, "rectangle");
                break;
            case 3:
                initializeMap(7, 8, "circle");
                break;
            default:
                initializeMap(3, 4, "uniform");
                break;
        }
    }

    private void initializeMap(int rows, int cols, String arrangement) {
        map = new int[rows][cols];
        positions = new int[rows][cols][2];
        brickWidth = 540 / cols;
        brickHeight = 150 / rows;

        // Offset for brick positions centered in the screen
        int offsetX = (screenWidth - brickWidth * cols) / 2;
        int offsetY = (screenHeight - brickHeight * rows) / 2;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = 1; // Initialize bricks with value '1'
                setBrickPosition(i, j, rows, cols, arrangement);
                brickColors[i][j] = getBrickColor(i, j);  // Set brick color
            }
        }

        // Apply quicksort or circular arrangement based on difficulty level
        if (arrangement.equals("circle")) {
            quickSortCircular(positions, 0, positions.length - 1);
        }
    }

    private void quickSortCircular(int[][][] positions, int low, int high) {
        if (low < high) {
            int pivot = partitionCircular(positions, low, high);
            quickSortCircular(positions, low, pivot - 1);
            quickSortCircular(positions, pivot + 1, high);
        }
    }

    private int partitionCircular(int[][][] positions, int low, int high) {
        int[] pivot = positions[high][0];
        int i = low - 1;
        double anglePivot = Math.atan2(pivot[1] - screenHeight / 2, pivot[0] - screenWidth / 2);

        for (int j = low; j < high; j++) {
            double angleJ = Math.atan2(positions[j][0][1] - screenHeight / 2, positions[j][0][0] - screenWidth / 2);
            if (angleJ < anglePivot) {
                i++;
                int[] temp = positions[i][0];
                positions[i][0] = positions[j][0];
                positions[j][0] = temp;
            }
        }
        int[] temp = positions[i + 1][0];
        positions[i + 1][0] = positions[high][0];
        positions[high][0] = temp;
        return i + 1;
    }

    private void setBrickPosition(int row, int col, int totalRows, int totalCols, String arrangement) {
        if (arrangement.equals("circle")) {
            double angle = (2 * Math.PI / totalCols) * col;
            int radius = 150;
            int offsetX = -50;
            int offsetY = -30;

            positions[row][col][0] = (int) (screenWidth / 2 + radius * Math.cos(angle)) + offsetX;
            positions[row][col][1] = (int) (screenHeight / 2 + radius * Math.sin(angle)) + offsetY;
        } else {
            positions[row][col][0] = col * brickWidth + 80;
            positions[row][col][1] = row * brickHeight + 50;
        }
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(brickColors[i][j]);
                    g.fillRoundRect(positions[i][j][0], positions[i][j][1], brickWidth, brickHeight, 10, 10);
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRoundRect(positions[i][j][0], positions[i][j][1], brickWidth, brickHeight, 10, 10);
                }
            }
        }
    }

    private Color getBrickColor(int row, int col) {
        Color[] colors = {Color.red, Color.orange, Color.yellow, Color.green, Color.blue};
        return colors[(row + col) % colors.length];
    }
    public class Ball {
        private int x, y, radius, dx, dy;

        // Add get methods for ball properties (position, radius)
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getRadius() {
            return radius;
        }

        public void move() {
            x += dx;
            y += dy;
        }
    }


    // Detect ball collision with bricks
    public void checkBrickCollision(Ball ball) {
        int ballX = ball.getX();
        int ballY = ball.getY();
        int ballRadius = ball.getRadius();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    int brickX = positions[i][j][0];
                    int brickY = positions[i][j][1];

                    // Check if the ball is within the brick's boundaries
                    if (ballX + ballRadius > brickX && ballX - ballRadius < brickX + brickWidth &&
                        ballY + ballRadius > brickY && ballY - ballRadius < brickY + brickHeight) {
                        // Ball has hit the brick, mark the brick as destroyed (set value to 0)
                        map[i][j] = 0;  // Remove the brick
                    }
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

	public int getTotalBricks() {
		// TODO Auto-generated method stub
		return 0;
	}
}
