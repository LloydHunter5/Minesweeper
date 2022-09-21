package minesweeper.main;

public class Cell {
    private boolean isMine;
    private int dangerLevel;
    public Cell(boolean isMine){
        this.isMine = isMine;
    }
    public void setDanger(int dangerLevel){
        this.dangerLevel = dangerLevel;
    }
    public boolean isMine(){
        return this.isMine;
    }
    public int getDangerLevel(){
        return dangerLevel;
    }
}
