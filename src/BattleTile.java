import javax.swing.JButton;

public class BattleTile extends JButton {
    private int row;
    private int col;
    private boolean clicked = false; // Add a clicked flag for each tile
    public BattleTile(int row, int col) {
        super();
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}