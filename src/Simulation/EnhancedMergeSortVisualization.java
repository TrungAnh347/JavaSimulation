package Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnhancedMergeSortVisualization extends JPanel {

    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 400;

    private List<int[]> snapshots;
    private int snapshotIndex = 0;
    private Timer timer;
    private int barWidth;
    private Image backgroundImage;
    private Image cherryTreeImage;

    public EnhancedMergeSortVisualization(List<int[]> snapshots) {
        this.snapshots = snapshots;

        int arraySize = snapshots.get(0).length;
        barWidth = PANEL_WIDTH / (arraySize + 2);  

        backgroundImage = new ImageIcon(getClass().getResource("/Simulation/tet2025.jpg")).getImage();  
        
        cherryTreeImage = new ImageIcon(getClass().getResource("/Simulation/anhcot.jpg")).getImage();  

        timer = new Timer(500, e -> {
            snapshotIndex++;
            if (snapshotIndex >= snapshots.size()) {
                timer.stop();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Ve nen 
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        int[] array = snapshots.get(snapshotIndex >= snapshots.size() ? snapshots.size() - 1 : snapshotIndex);
        int treeHeightMultiplier = (PANEL_HEIGHT - 100) / getMaxValue(array);

        // Ve cot 
        for (int i = 0; i < array.length; i++) {
            int x = i * barWidth;
            int height = array[i] * treeHeightMultiplier;

            int scaledHeight = Math.min(height, cherryTreeImage.getHeight(this));  
            int y = PANEL_HEIGHT - scaledHeight - 50;  // Vị trí của cây anh đào

            g.drawImage(cherryTreeImage, x + 5, y, barWidth - 10, scaledHeight, this);

            // Vẽ border và các thông tin khác
            g.setColor(Color.DARK_GRAY);
            g.drawRoundRect(x + 5, y, barWidth - 10, scaledHeight, 10, 10);

            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(array[i]), x + barWidth / 2 - 8, y - 10);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(String.valueOf(i), x + barWidth / 2 - 8, PANEL_HEIGHT - 40);
        }
    }

    private int getMaxValue(int[] array) {
        int max = array[0];
        for (int val : array) {
            max = Math.max(max, val);
        }
        return max;
    }

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);  // Trộn ngẫu nhiên

        int[] array = numbers.stream().mapToInt(i -> i).toArray();
        System.out.println("Mảng ban đầu (trộn ngẫu nhiên):");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();

        List<int[]> snapshots = new ArrayList<>();
        snapshots.add(array.clone());

        mergeSort(array.clone(), snapshots);

        JFrame frame = new JFrame("Enhanced Merge Sort Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(PANEL_WIDTH, PANEL_HEIGHT);  // Cửa sổ nhỏ gọn hơn
        frame.setResizable(false);
        frame.add(new EnhancedMergeSortVisualization(snapshots));
        frame.setVisible(true);
    }

    public static void mergeSort(int[] array, List<int[]> snapshots) {
        if (array.length > 1) {
            int mid = array.length / 2;
            int[] left = new int[mid];
            int[] right = new int[array.length - mid];

            System.arraycopy(array, 0, left, 0, mid);
            System.arraycopy(array, mid, right, 0, array.length - mid);

            mergeSort(left, snapshots);
            mergeSort(right, snapshots);

            merge(array, left, right, snapshots);
        }
    }

    private static void merge(int[] array, int[] left, int[] right, List<int[]> snapshots) {
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] < right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
            snapshots.add(array.clone());
        }

        while (i < left.length) {
            array[k++] = left[i++];
            snapshots.add(array.clone());
        }

        while (j < right.length) {
            array[k++] = right[j++];
            snapshots.add(array.clone());
        }
    }
}
