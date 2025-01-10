package Simulation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

public class EnhancedQuickSortVisualization extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<int[]> snapshots;
    private int snapshotIndex = 0;
    private Timer timer;
    private int barWidth;
    private Image backgroundImage;
    private Image cherryTreeImage;

    public EnhancedQuickSortVisualization(List<int[]> snapshots) {
        this.snapshots = snapshots;

        int arraySize = snapshots.get(0).length;
        barWidth = 500 / (arraySize + 2);

        backgroundImage = new ImageIcon(getClass().getResource("/Simulation/tet2025.jpg")).getImage();  
        
        cherryTreeImage = new ImageIcon(getClass().getResource("/Simulation/anhcot.jpg")).getImage();  

        timer = new Timer(1000, e -> {
            snapshotIndex++;
            if (snapshotIndex >= snapshots.size()) {
                timer.stop();
            }
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        barWidth = getWidth() / (snapshots.get(0).length + 2); // Dynamic adjustment
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        int[] array = snapshots.get(snapshotIndex >= snapshots.size() ? snapshots.size() - 1 : snapshotIndex);
        int treeHeightMultiplier = (getHeight() - 100) / getMaxValue(array);

        for (int i = 0; i < array.length; i++) {
            int x = i * barWidth;
            int height = array[i] * treeHeightMultiplier;

            int scaledHeight = Math.min(height, cherryTreeImage.getHeight(this));
            int y = getHeight() - scaledHeight - 50;

            g.drawImage(cherryTreeImage, x + 5, y, barWidth - 10, scaledHeight, this);

            g.setColor(Color.DARK_GRAY);
            g.drawRoundRect(x + 5, y, barWidth - 10, scaledHeight, 10, 10);

            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(array[i]), x + barWidth / 2 - 8, y - 10);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(String.valueOf(i), x + barWidth / 2 - 8, getHeight() - 40);
        }
    }

    private int getMaxValue(int[] array) {
        int max = array[0];
        for (int val : array) {
            max = Math.max(max, val);
        }
        return max;
    }

    public void startVisualization() {
        snapshotIndex = 0;
        timer.restart();
    }

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        int[] array = numbers.stream().mapToInt(i -> i).toArray();
        System.out.println("Mảng ban đầu (trộn ngẫu nhiên):");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();

        List<int[]> snapshots = new ArrayList<>();
        snapshots.add(array.clone());

        quickSort(array, 0, array.length - 1, snapshots);

        JFrame frame = new JFrame("Enhanced Quick Sort Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(true);

        EnhancedQuickSortVisualization panel = new EnhancedQuickSortVisualization(snapshots);
        frame.add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Visualization");
        startButton.addActionListener(e -> panel.startVisualization());

        buttonPanel.add(startButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void quickSort(int[] array, int low, int high, List<int[]> snapshots) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, snapshots);

            quickSort(array, low, pivotIndex - 1, snapshots);
            quickSort(array, pivotIndex + 1, high, snapshots);
        }
    }

    private static int partition(int[] array, int low, int high, List<int[]> snapshots) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
                snapshots.add(array.clone());
            }
        }

        swap(array, i + 1, high);
        snapshots.add(array.clone());

        return i + 1;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
