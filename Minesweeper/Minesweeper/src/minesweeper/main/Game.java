package minesweeper.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Game {
    public static int CELLS_HORIZONTAL;
    public static int CELLS_VERTICAL;
    public static int SCALE;
    public static int DIFFICULTY;

    public static int REVEAL_CELL_EVENT;
    public static int PLACE_FLAG_EVENT;
    private static boolean firstClick;
    private static int totalCells;
    private static int cellsRevealed;
    private static ArrayList<JButton> mines;
    private static JFrame f;

    public static void init(){
        CELLS_HORIZONTAL = 50;
        CELLS_VERTICAL = 50;
        SCALE = 20;
        DIFFICULTY = 3;
        firstClick = true;
        cellsRevealed = 0;
        mines = new ArrayList<>(); //for highlighting all mines at endgame
    }

    //Decreases the probability of a mine the closer it is to the initial click
    private static void placeMines(Cell[][] cells,JButton[][] cellButtons, ArrayList<JButton> mines, int firstClickX, int firstClickY){
        for(int i = 0; i < CELLS_HORIZONTAL; i++){
            for(int j = 0; j < CELLS_VERTICAL; j++){
                double distFromClick = Math.sqrt(Math.pow((firstClickY-j),2) + Math.pow((firstClickX-i),2));
                double mineProbability = 0.1*DIFFICULTY * (1 - (0.0008*CELLS_VERTICAL*CELLS_HORIZONTAL + 1)/distFromClick);
                boolean isMine = Math.random() < mineProbability;
                cells[i][j] = new Cell(isMine);
                JButton b = cellButtons[i][j];
                if(cells[i][j].isMine()) mines.add(b);
            }
        }
        //Sets the danger levels of each cell (numbers on the tiles)
        for (int i = 0; i < CELLS_HORIZONTAL; i++) {
            for (int j = 0; j < CELLS_VERTICAL; j++) {
                int count = 0;
                for (int a = -1; a < 2; a++) {
                    for (int b = -1; b < 2; b++) {
                        if (isValidBounds(a + i, b + j)) {
                            if (cells[i + a][b + j].isMine()) count++;
                        }
                    }
                }
                if (cells[i][j].isMine()) count--;
                cells[i][j].setDanger(count);
            }
        }
    }
    public static void generateBoard() {
        firstClick = true;
        REVEAL_CELL_EVENT = 16;
        PLACE_FLAG_EVENT = 18;
        //Jframe & menu setup
        f = new JFrame("Minesweeper");
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        JPanel main2 = new JPanel();
        main2.setLayout(new BoxLayout(main2, BoxLayout.X_AXIS));
        FlowLayout g = new FlowLayout(FlowLayout.LEADING,0,0); //swapped inputs bc of bug
        JPanel game = new JPanel(g);
        JPanel menu = new JPanel();
        JButton reset = new JButton("RESET");
        menu.add(reset);
        f.add(menu);
        main.add(main2);
        main2.add(game);

        f.add(main);

        Dimension gameSize = new Dimension(CELLS_VERTICAL*SCALE,CELLS_HORIZONTAL*SCALE);
        game.setSize(gameSize);
        game.setMinimumSize(gameSize);
        game.setMaximumSize(gameSize);
        f.setSize(game.getWidth()+20, game.getHeight() + 40);
        f.setMinimumSize(gameSize);


        //Logic Starts
        Cell[][] cells = new Cell[CELLS_HORIZONTAL][CELLS_VERTICAL];
        JButton[][] cellButtons = new JButton[CELLS_HORIZONTAL][CELLS_VERTICAL];
        Icons.init(game.getWidth(), game.getHeight(), CELLS_HORIZONTAL, CELLS_VERTICAL);
        totalCells = CELLS_HORIZONTAL*CELLS_VERTICAL;
        cellsRevealed = 0;
        mines.clear();
        // Create mine array & buttons that correspond to it
        double mineProbability = 0.1;
        for (int i = 0; i < CELLS_HORIZONTAL; i++) {
            for (int j = 0; j < CELLS_VERTICAL; j++) {
                cellButtons[i][j] = new JButton(Icons.BLANK.ICON);
                JButton b = cellButtons[i][j];
                b.setBorder(null);
                b.setBorderPainted(false);
                b.setFocusPainted(false);
                b.setName(((Integer) (1000 * i + j)).toString());
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton source = (JButton) e.getSource();
                        if (e.getModifiers() == PLACE_FLAG_EVENT) {
                            if (source.getIcon().equals(Icons.FLAG.ICON)) {
                                source.setIcon(Icons.BLANK.ICON);
                            } else {
                                source.setIcon(Icons.FLAG.ICON);
                            }
                        } else if (e.getModifiers() == REVEAL_CELL_EVENT) {
                            int xy = Integer.parseInt(source.getName());
                            int x = xy / 1000;
                            int y = xy % 1000;
                            if(firstClick){
                                firstClick = false;
                                placeMines(cells, cellButtons,mines,x,y);
                            }
                            if (cells[x][y].isMine()) {
                                source.setIcon(Icons.MINE.ICON);
                                for(JButton b : mines){
                                    b.setIcon(Icons.MINE.ICON);
                                }
                                //Clicking cells now does nothing
                                REVEAL_CELL_EVENT = -1;
                                PLACE_FLAG_EVENT = -1;
                            }
                            else if (cells[x][y].getDangerLevel() == 0) {
                                cellsRevealed++;
                                source.setIcon(Icons.EMPTY.ICON);
                                revealEmptyCells(cells, cellButtons, x, y);
                                if(endGame()){
                                    return;
                                }
                            }else{
                                cellsRevealed++;
                                int dlvl = cells[x][y].getDangerLevel();
                                source.setIcon(getNumberIcon(dlvl));
                                if(endGame()){
                                    return;
                                }

                            }
                        }

                    }
                });
                game.add(cellButtons[i][j]);
            }
        }

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Main.popup.setVisible(true);
            }
        });
        f.setLocationRelativeTo(null);
        f.setResizable(true);
        f.setVisible(true);
    }
    public static boolean isValidBounds(int x, int y) {
        return x >= 0 && y >= 0 && x <= CELLS_HORIZONTAL - 1 && y <= CELLS_VERTICAL - 1;
    }

    public static void revealEmptyCells(Cell[][] cells, JButton[][] cellButtons, int x, int y) {
        for (int a = -1; a < 2; a++) {
            for (int b = -1; b < 2; b++) {
                if (isValidBounds(a + x, b + y) && (a != 0 || b != 0) && cellButtons[a+x][b+y].getIcon().equals(Icons.BLANK.ICON) && !cells[a+x][b+y].isMine()) {
                    int dlvl = cells[x + a][y + b].getDangerLevel();
                    cellsRevealed++;
                    cellButtons[a + x][b + y].setIcon(getNumberIcon(dlvl));
                    if(endGame()) {
                        return;
                    }
                    if (cells[a + x][b + y].getDangerLevel() == 0){
                        revealEmptyCells(cells, cellButtons, a + x, b + y);
                    }
                }
            }
        }
    }
    public static ImageIcon getNumberIcon(int dlvl) {
        switch (dlvl) {
            case 1:
                return Icons.ONE.ICON;
            case 2:
                return Icons.TWO.ICON;
            case 3:
                return Icons.THREE.ICON;
            case 4:
                return Icons.FOUR.ICON;
            case 5:
                return Icons.FIVE.ICON;
            case 6:
                return Icons.SIX.ICON;
            case 7:
                return Icons.SEVEN.ICON;
            case 8:
                return Icons.EIGHT.ICON;
        }
        return Icons.EMPTY.ICON;
    }
    private static boolean endGame(){
        boolean ended = totalCells - cellsRevealed == mines.size();
        if(ended) {
            JFrame endDialog = new JFrame();
            endDialog.setLayout(new FlowLayout());
            endDialog.add(new JLabel("You Did It!"));
            JButton reset = new JButton("Play Again?");
            reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    endDialog.dispose();
                    f.dispose();
                    Game.generateBoard();
                }
            });
            JButton menu = new JButton("Main Menu");
            menu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    endDialog.dispose();
                    f.dispose();
                    Main.popup.setVisible(true);
                }
            });
            endDialog.add(reset);
            endDialog.add(menu);
            endDialog.setSize(150,150);
            endDialog.setLocationRelativeTo(f);
            endDialog.setAlwaysOnTop(true);
            endDialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            endDialog.setVisible(true);

        }
        return ended;
    }
}
