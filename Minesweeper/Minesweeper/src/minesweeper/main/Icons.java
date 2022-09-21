package minesweeper.main;

import javax.swing.ImageIcon;
import java.awt.Image;

//Class for creating correctly-sized icons
public enum Icons{
        BLANK("src/strohman/assets/blankcell.png"),
        FLAG("src/strohman/assets/flag.jpg"),
        EMPTY("src/strohman/assets/empty.png"),
        ONE("src/strohman/assets/one.png"),
        TWO("src/strohman/assets/two.png"),
        THREE("src/strohman/assets/three.png"),
        FOUR("src/strohman/assets/four.png"),
        FIVE("src/strohman/assets/five.png"),
        SIX("src/strohman/assets/six.png"),
        SEVEN("src/strohman/assets/seven.png"),
        EIGHT("src/strohman/assets/eight.png"),
        MINE("src/strohman/assets/mine.png"),
        PLAYBUTTON("src/strohman/assets/playbutton.png");

        public ImageIcon ICON;
        private String filepath;

        Icons(String filepath){
            this.filepath = filepath;
        }
        //Game Sprites Initialization
        public static void init(int fWidth, int fHeight, int CELLS_HORIZONTAL, int CELLS_VERTICAL){
            for(Icons icon : values()){
                if(icon.equals(PLAYBUTTON)) break;
                int w = fWidth/CELLS_VERTICAL;
                int h = fHeight/CELLS_HORIZONTAL;
                icon.ICON = new ImageIcon(icon.filepath);
                //Gets the icon's image object, scales it, and sets it to the icon image
                icon.ICON.setImage(icon.ICON.getImage().getScaledInstance(w,h,Image.SCALE_SMOOTH));
            }
        }
        //Start Menu Initialization
        public static void sMInit(){
            PLAYBUTTON.ICON = new ImageIcon(PLAYBUTTON.filepath);
            Image image = PLAYBUTTON.ICON.getImage().getScaledInstance(Main.PB_WIDTH,Main.PB_HEIGHT, Image.SCALE_SMOOTH);
            PLAYBUTTON.ICON.setImage(image);
        }
    }
