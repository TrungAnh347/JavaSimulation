package Game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BrickBreakerQuickSort extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int highScore = 0;
    private int totalBricks;
    private int playerX = 310;
    private int playerX2 = 310; // Player 2 paddle
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballDirX = -1;
    private int ballDirY = -2;
    private Timer timer;
    private int delay = 8;
    private MapGenerator map;
    private boolean isPaused = false; // Pause state
    private int speed = 1; // Speed of the ball, default is 1
    private long startTime; // Store the start time to track when to increase speed
    private boolean isSinglePlayer; // Single player mode flag

    public BrickBreakerQuickSort(int difficulty, boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
        adjustDifficulty(difficulty);
        map = new MapGenerator(difficulty);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        startTime = System.currentTimeMillis(); // Track start time to increase ball speed
    }

    private void adjustDifficulty(int difficulty) {
        switch (difficulty) {
        	case 1: 
        		 totalBricks = 21;
                 break;
            case 2:
                totalBricks = 28;
                break;
            case 3:
                totalBricks = 8;
                break;
            default:
                totalBricks = 21;
                break;
        }
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        map.draw((Graphics2D) g); // Draw the bricks map

        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Display high score and current score
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("High Score: " + highScore, 20, 30);
        g.drawString("Score: " + score, 500, 30);

        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8); // Player paddle

        if (!isSinglePlayer) {
            g.setColor(Color.red);
            g.fillRect(playerX2, 550, 100, 8); // Player 2 paddle
        }

        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20); // Ball

        if (ballPosY > 570) { // Game Over Condition
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Your Score: " + score, 150, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        if (isPaused) { // Game Paused Condition
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Paused", 250, 300);
            return;
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            if (elapsedTime >= 30) { // Mỗi 30 giây, tăng tốc độ bóng
                speed++;
                startTime = System.currentTimeMillis();
            }

            // Kiểm tra va chạm với thanh người chơi
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballDirY = -ballDirY;
            }

            if (!isSinglePlayer && new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX2, 550, 100, 8))) {
                ballDirY = -ballDirY;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) { 
                        int brickX = map.positions[i][j][0];
                        int brickY = map.positions[i][j][1];
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        
                    
                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j); 
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickWidth) {
                                ballDirX = -ballDirX; 
                            } else {
                                ballDirY = -ballDirY; 
                            }

                            repaint();
                            break A; 
                        }
                    }
                }
            }

        
            ballPosX += ballDirX * speed;
            ballPosY += ballDirY * speed;

 
            if (ballPosX < 0) {
                ballDirX = -ballDirX;
            }
            if (ballPosY < 0) {
                ballDirY = -ballDirY;
            }
            if (ballPosX > 670) {
                ballDirX = -ballDirX;
            }
        }

        if (score > highScore) {
            highScore = score;
        }

        repaint();
    }



    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
    	    
    	    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
    	       
    	        isPaused = !isPaused;

    	        if (isPaused) {
    	            stopBall();
    	        } else {

    	            speed = 1;
    	            ballDirX = -1;
    	            ballDirY = -2;
    	        }
    	        repaint();
    	    } else if (e.getKeyCode() == KeyEvent.VK_C) {

    	        if (isPaused) {
    	            isPaused = false;
    	            speed = 1;  
    	            ballDirX = -1;
    	            ballDirY = -2;
    	            repaint();
    	        }
    	    } else if (e.getKeyCode() == KeyEvent.VK_S) {

    	        speed *= 3; 
    	        repaint();
    	    } else {
    	        
    	        if (isPaused) {
    	            return; 
    	        }

 
    	        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    	            if (!play) {
    	                startNewGame();
    	            }
    	        }

    	        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
    	            if (playerX >= 600) {
    	                playerX = 600;
    	            } else {
    	                moveRight();
    	            }
    	        }
    	        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
    	            if (playerX <= 10) {
    	                playerX = 10;
    	            } else {
    	                moveLeft();
    	            }
    	        }
    	        if (e.getKeyCode() == KeyEvent.VK_A) {
    	            if (playerX2 > 10 && !isSinglePlayer) {
    	                playerX2 -= 20;
    	            }
    	        }
    	        if (e.getKeyCode() == KeyEvent.VK_D) {
    	            if (playerX2 < 600 && !isSinglePlayer) {
    	                playerX2 += 20;
    	            }
    	        }
    	        if (e.getKeyCode() == KeyEvent.VK_N) {
    	            if (isPaused) {
    	                isPaused = false;
    	            } else {
    	                isPaused = true;
    	                stopBall();
    	                showGameMenu();
    	            }
    	        }
    	    }


    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    public void stopBall() {
        // Stop the ball by setting its velocity to zero
        ballDirX = 0;
        ballDirY = 0;
    }

    public void startNewGame() {
        // Reset the game state as if it's a fresh new game, except high score
        play = true;
        ballPosX = 120;
        ballPosY = 350;
        ballDirX = -1;
        ballDirY = -2;
        playerX = 310;
        playerX2 = 310; // Reset the player 2 paddle position
        score = 0;
        totalBricks = map.getDifficulty() == 1 ? 21 : (map.getDifficulty() == 2 ? 28 : 8); // Adjust based on difficulty

        // Reinitialize the brick map with the correct difficulty
        map = new MapGenerator(map.getDifficulty());
        speed = 1; // Reset speed
        startTime = System.currentTimeMillis(); // Reset the timer for speed increase

        isPaused = false; // Ensure the game is not paused when starting a new game

        repaint();
    }

    public void showGameMenu() {
        String[] options = {"1 Player", "2 Players", "Exit"};
        int choice = JOptionPane.showOptionDialog(this,
            "Game Paused - Choose Game Mode",
            "Game Menu",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null, options, options[0]);

        if (choice == 2) {
            System.exit(0);
        }

        String difficultyInput = JOptionPane.showInputDialog(
            this,
            "Choose Difficulty:\n1. Easy\n2. Medium\n3. Hard"
        );
        int difficulty = Integer.parseInt(difficultyInput);

        boolean isSinglePlayer = choice == 0;

        // Reset the game with updated settings
        this.isSinglePlayer = isSinglePlayer;
        map = new MapGenerator(difficulty);
        startNewGame(); // Reset game parameters after updating the mode and difficulty
    }

    public static void main(String[] args) {
        JFrame obj = new JFrame("Brick Breaker - Menu");
        String[] options = {"1 Player", "2 Players", "Exit"};
        int choice = JOptionPane.showOptionDialog(obj,
            "Choose Game Mode", 
            "Game Menu", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, options, options[0]);

        if (choice == 2) {
            System.exit(0);
        }

        String difficultyInput = JOptionPane.showInputDialog(
            obj,
            "Choose Difficulty:\n1. Easy\n2. Medium\n3. Hard"
        );
        int difficulty = Integer.parseInt(difficultyInput);
        
        boolean isSinglePlayer = choice == 0;

        JFrame gameFrame = new JFrame();
        BrickBreakerQuickSort game = new BrickBreakerQuickSort(difficulty, isSinglePlayer);
        gameFrame.setBounds(10, 10, 700, 600);
        gameFrame.setTitle("Brick Breaker Game");
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.add(game);
        gameFrame.setVisible(true);
    }
}
