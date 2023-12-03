import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;

public class RottenApplesGUI extends JFrame {
    private RottenApples rottingApples;
    private JPanel[][] cellPanels;
    private JButton startButton;
    private JTextField timerDisplay;
    private JTextField freshApples;

    private final Color FRESH = new Color(112, 173, 71);
    private final Color ROTTEN = new Color(191, 191, 191);
    private final Color EMPTY = new Color(255, 255, 255);

    public RottenApplesGUI(int[][] matrix) {
        rottingApples = new RottenApples(matrix);
        initializeGUI(matrix.length, matrix[0].length);
        setupStartButton();
        setupCounters();
    }

    private void initializeGUI(int rows, int cols) {
        int cellSize = 75;
        setTitle("Rotting Apples Simulation");
        int frameWidth = cols * cellSize + 300; 
        int frameHeight = rows * cellSize + 50;
        setSize(frameWidth, frameHeight);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        cellPanels = new JPanel[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createLineBorder(Color.black));
                panel.setPreferredSize(new Dimension(cellSize, cellSize)); 
                gridPanel.add(panel);
                cellPanels[i][j] = panel;
            }
        }
        add(gridPanel, BorderLayout.CENTER);
        updateGUI();
    }
    

    private void setupStartButton() {
        startButton = new JButton("Start Simulation");
        startButton.addActionListener(e -> simulate());
        add(startButton, BorderLayout.SOUTH);
    }

    private void setupCounters() {
        timerDisplay = new JTextField();
        freshApples = new JTextField();
        timerDisplay.setEditable(false);
        freshApples.setEditable(false);
        timerDisplay.setText("Time: \n 0");
        freshApples.setText("Fresh Apples: " + rottingApples.getFreshApples());
        add(timerDisplay, BorderLayout.WEST);
        add(freshApples, BorderLayout.EAST);
    }

    public void updateGUI() {
        RottenApples.Cell[][] grid = rottingApples.getCurrentMatrixState();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Color targetColor;
                switch (grid[i][j].state) {
                    case 0: targetColor = EMPTY; break;
                    case 1: targetColor = FRESH; break;
                    case 2: targetColor = ROTTEN; break;
                    default: targetColor = Color.BLACK;
                }

                if (cellPanels[i][j].getBackground() != targetColor) {
                    cellPanels[i][j].setBackground(targetColor);
                }
            }
        }
        repaint();
    }    

    public void simulate() {
        new Thread(() -> {
            startButton.setEnabled(false);
            while (!rottingApples.isProcessingComplete()) {
                rottingApples.processOneWave();
                timerDisplay.setText("Time: " + rottingApples.time);
                freshApples.setText("Fresh Apples: " + rottingApples.getFreshApples());
                updateGUI();
                try {
                    Thread.sleep(1000); // Pause for 1 second between waves
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Simulation complete!");
            System.out.println("Time: " + rottingApples.time);
            System.out.println("Fresh Apples: " + rottingApples.getFreshApples());
            startButton.setEnabled(true);
        }).start();
    }

    public static void main(String[] args) {
        int[][] matrix = {
            {2, 1, 1, 2, 0, 2, 1},
            {1, 0, 1, 2, 0, 1, 0},
            {2, 0, 0, 2, 1, 1, 0}
        };

        SwingUtilities.invokeLater(() -> {
            RottenApplesGUI gui = new RottenApplesGUI(matrix);
            gui.setVisible(true);
        });
    }
}
