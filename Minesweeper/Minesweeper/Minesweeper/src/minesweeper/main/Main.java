package minesweeper.main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/*
    @author Evan Strohman
 */

/*
    This project recreates the game Minesweeper
    The recreation isn't 100% accurate, especially when it comes to spawning mines
 */

public class Main {
    public static int PB_WIDTH;
    public static int PB_HEIGHT;
    public static int SM_WIDTH;
    public static int SM_HEIGHT;
    public static JFrame popup;

    public static void main(String[] args) {
        createStartMenu();
    }

    public static void createStartMenu(){

        PB_WIDTH = 400;
        PB_HEIGHT = 200;
        SM_WIDTH = 400;
        SM_HEIGHT = 500;

        //Start Menu
        Icons.sMInit();
        Icons.init(200,200,10,10);
        Game.init();

        popup = new JFrame("Minesweeper");
        FlowLayout popupLayout = new FlowLayout(FlowLayout.CENTER, 0,0);
        JPanel settings = new JPanel();
        BoxLayout settingsLayout = new BoxLayout(settings,BoxLayout.Y_AXIS);
        settings.setLayout(settingsLayout);
        popup.setLayout(popupLayout);
        JButton playButton = new JButton(Icons.PLAYBUTTON.ICON);
        JTextField verticalCells = new JTextField(Game.CELLS_HORIZONTAL + "");
        JTextField horizontalCells = new JTextField(Game.CELLS_VERTICAL + "");

        playButton.setBorder(null);
        playButton.setBackground(new Color(255,255,255));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String vCellsText = verticalCells.getText();
                if(vCellsText.matches(".*\\D+.*") || vCellsText.length() == 0){
                    verticalCells.setText(Game.CELLS_HORIZONTAL + "");
                }else{
                    Game.CELLS_HORIZONTAL = Integer.parseInt(vCellsText);
                }
                String hCellsText = horizontalCells.getText();
                if(hCellsText.matches(".*\\D+.*") || hCellsText.length() == 0){
                    horizontalCells.setText(Game.CELLS_VERTICAL + "");
                }else{
                    Game.CELLS_VERTICAL = Integer.parseInt(hCellsText);
                }

                Game.generateBoard();
                popup.setVisible(false);
            }
        });
        JSlider scale = new JSlider(JSlider.HORIZONTAL,10,75,20);
        scale.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Game.SCALE = scale.getValue();
            }
        });
        Font settingsFont = new Font(Font.DIALOG_INPUT,Font.BOLD,24);
        JLabel scaleLabel = new JLabel("Scale:");
        scaleLabel.setFont(settingsFont);
        settings.add(scaleLabel);
        settings.add(scale);

        JSlider difficulty = new JSlider(JSlider.HORIZONTAL, 1,5,3);
        difficulty.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Game.DIFFICULTY = difficulty.getValue();
            }
        });
        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyLabel.setFont(settingsFont);
        settings.add(difficultyLabel);
        settings.add(difficulty);


        JLabel gridSizeLabel = new JLabel("Grid Size:");
        gridSizeLabel.setFont(settingsFont);

        horizontalCells.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField source = (JTextField) e.getSource();
                if(e.getActionCommand().matches(".*\\D+.*") || e.getActionCommand().length() == 0){
                    source.setText(Game.CELLS_VERTICAL + "");
                }else{
                    Game.CELLS_VERTICAL = Integer.parseInt(e.getActionCommand());
                }
            }
        });
        verticalCells.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField source = (JTextField) e.getSource();
                if(e.getActionCommand().matches(".*\\D+.*") || e.getActionCommand().length() == 0){
                    source.setText(Game.CELLS_HORIZONTAL + "");
                }else{
                    Game.CELLS_HORIZONTAL = Integer.parseInt(e.getActionCommand());
                }
            }
        });
        settings.add(gridSizeLabel);
        settings.add(horizontalCells);
        settings.add(verticalCells);
        popup.add(playButton);
        popup.add(settings);
        popup.setSize(SM_WIDTH,SM_HEIGHT);
        popup.setLocationRelativeTo(null);
        popup.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        popup.setVisible(true);
    }

}

