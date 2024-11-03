import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class BattleshipFrame extends JFrame {
    private static final int ROW = 10;
    private static final int COL = 10;
    private static String[][] board = new String[ROW][COL];
    private static int[] shipCounter = {5, 4, 3, 3, 2};
    private static String[] shipType = {"B", "L", "C1", "C2", "S"};
    private static int miss;
    private static int totalMiss;
    private static int totalHit;
    private static int strike;
    JLabel missLabel;
    JLabel totalMissLabel;
    JLabel totalHitLabel;
    JLabel strikeLabel;
    JPanel mainPnl;
    JPanel gridPnl;
    JPanel statsPnl;
    JLabel title;
    JButton quit;
    ImageIcon waterImg;
    ImageIcon hitImg;
    ImageIcon missImg;
    public BattleshipFrame(){
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        //Title doesn't need its own method
        title = new JLabel("Battleship!", JLabel.CENTER);
        title.setFont(new Font("Impact", Font.PLAIN, 36));
        mainPnl.add(title, BorderLayout.NORTH);

        createGrid();
        mainPnl.add(gridPnl, BorderLayout.CENTER);

        createStatsPanel();
        mainPnl.add(statsPnl, BorderLayout.EAST);

        //quit button also  doesn't need its own method
        quit = new JButton();
        quit.setText("QUIT");
        quit.addActionListener((ActionEvent ae) -> System.exit(0));
        mainPnl.add(quit, BorderLayout.SOUTH);

        System.out.println("Main panel not added yet");

        add(mainPnl);
        setSize(900,950);
        setLocation(0,0);
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        placeShips();
        System.out.println("Ships placed");
    }

    private void createGrid(){
        System.out.println("function createGrid ran");
        gridPnl = new JPanel();
        gridPnl.setLayout(new GridLayout(ROW, COL));


        for(int row=0; row < ROW; row++)
        {
            for(int col=0; col < COL; col++)
            {
                board[row][col] = " ";
                BattleTile tile = new BattleTile(row, col);
                gridPnl.add(tile);

                tile.setFont(new Font("Impact", Font.PLAIN, 40));

                waterImg = new ImageIcon("src/Water.png");

                //making images scaled down
                Image scaledWaterImg = waterImg.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                tile.setIcon(new ImageIcon(scaledWaterImg));

                tile.addActionListener(e -> getClicked(tile.getRow(), tile.getCol(), tile));
                //System.out.println("Everything in createGrid works");
            }
        }
    }

    private void placeShips(){
        int[] shipSizes = {5, 4, 3, 3, 2}; // Ship sizes

        for(int i = 0 ;  i < shipSizes.length ; i++){
            boolean placed = false;
            while (!placed) {
                int randomRow = (int) (Math.random() * 10);
                int randomCol = (int) (Math.random() * 10);
                boolean horizontal = Math.random() < 0.5;
                String type = "";

                type = shipType[i];

                /*if(i == 0){
                    type = "B";
                }
                else if(i == 1){
                    type = "L";
                }
                else if(i == 2){
                    type = "C1";
                }
                else if(i == 3){
                    type = "C2";
                }
                else{
                    type = "S";
                }*/

                if (canPlaceShip(randomRow, randomCol, shipSizes[i], horizontal)) {
                    for (int e = 0; e < shipSizes[i]; e++) {
                        if (horizontal) {
                            board[randomRow][randomCol + e] = type; //change to an image
                            ((JButton) gridPnl.getComponent(randomRow * COL + (randomCol + e))).setText(type);
                        } else {
                            board[randomRow + e][randomCol] = type;
                            ((JButton) gridPnl.getComponent((randomRow + e) * COL + randomCol)).setText(type);
                        }
                    }
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceShip(int row, int col, int size, boolean horizontal) { //method to detect if a ship can be placed
        if (horizontal) {
            if (col + size > COL){
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!board[row][col + i].equals(" ")) return false;
            }
        } else {
            if (row + size > ROW){
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!board[row + i][col].equals(" ")) return false;
            }
        }
        return true;
    }

    private void getClicked(int row, int col, BattleTile tile) {
        if (!tile.isClicked()) {
            String shipType = board[row][col];

            if (!board[row][col].equals(" ")) {
                hitImg = new ImageIcon("src/Hit.png");
                Image scaledHitImg = hitImg.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                tile.setIcon(new ImageIcon(scaledHitImg));

                totalHit += 1;
                totalHitLabel.setText("Total hits: " + totalHit);
                miss = 0;
                missLabel.setText("Misses: " + miss);

                findSunkenShip(row, col, shipType);
            } else {
                missImg = new ImageIcon("src/Miss.png");
                Image scaledMissImg = missImg.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                miss += 1;
                totalMiss += 1;
                missLabel.setText("Misses: " + miss);
                totalMissLabel.setText("Total misses: " + totalMiss);

                if (miss == 5) {
                    strike += 1;
                    strikeLabel.setText("Strike: " + strike);
                    miss = 0;
                    if (strike == 3) {
                        endGame();
                    }
                }

                tile.setIcon(new ImageIcon(scaledMissImg));
            }

            // mark the tile as clicked
            tile.setClicked(true);
        }
    }

    private void findSunkenShip(int row, int col, String type){
        //System.out.println("Hit on " + row + ", " + col + ". Ship type was " + shipType);
        for(int i = 0; i < 5; i++){
            if(shipType[i] == type){
                shipCounter[i] -= 1;
                if(shipCounter[i] == 0){
                    JOptionPane.showMessageDialog(this, "You sunk my ship!");
                }
            }

        }

        //for(int row=0; row < ROW; row++) {
        //    for (int col = 0; col < COL; col++) {
        //        if (board[row][col].equals(shipType)) {
         //           BattleTile tile = (BattleTile) gridPnl.getComponent(row * COL + col);
         //           Icon tileIcon = tile.getIcon();
//
          //          // Check if tile is not hit
          //          if (!tileIcon.toString().contains("Hit")) {
          //
          //          }
          //      }
          //  }
        //}
    }

    private void createStatsPanel(){
        statsPnl = new JPanel();
        statsPnl.setLayout(new BoxLayout(statsPnl, BoxLayout.Y_AXIS));
        statsPnl.setAlignmentY(CENTER_ALIGNMENT);
        missLabel = new JLabel("Misses: 0");
        totalMissLabel = new JLabel("Total misses: 0");
        totalHitLabel = new JLabel("Total hits: 0");
        strikeLabel = new JLabel("Strike: 0");

        missLabel.setFont(new Font("Impact", Font.PLAIN, 20));
        totalMissLabel.setFont(new Font("Impact", Font.PLAIN, 20));
        totalHitLabel.setFont(new Font("Impact", Font.PLAIN, 20));
        strikeLabel.setFont(new Font("Impact", Font.PLAIN, 20));

        statsPnl.add(missLabel);
        statsPnl.add(totalMissLabel);
        statsPnl.add(totalHitLabel);
        statsPnl.add(strikeLabel);
    }

    private void endGame(){
        int res = JOptionPane.showConfirmDialog(this, "Do you want to play again? (This might take a while)","Game Over!", JOptionPane.YES_NO_OPTION);
        if(res == JOptionPane.YES_OPTION){
            miss = 0;
            totalMiss = 0;
            totalHit = 0;
            strike = 0;

            // Update labels
            missLabel.setText("Misses: 0");
            totalMissLabel.setText("Total misses: 0");
            totalHitLabel.setText("Total hits: 0");
            strikeLabel.setText("Strike: 0");

            // Clear the board
            for (int row = 0; row < ROW; row++) {
                for (int col = 0; col < COL; col++) {
                    board[row][col] = " ";
                    BattleTile tile = (BattleTile) gridPnl.getComponent(row * COL + col);

                    // Reset the tile image and clicked state
                    tile.setIcon(new ImageIcon(waterImg.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                    tile.setClicked(false);
                }
            }

            // Place ships again
            placeShips();
        }
        else{
            System.exit(0);
        }
    }

}
