package com.example.snake;

/**************** Serpiente ****************/
public class SnakePoints {
    private int positionX, positionY;
    /*Constructor*/
    public SnakePoints(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
    /*Getters y Setters*/
    ///////////////////////////////////////////
    public int getPositionX() {
        return positionX;
    }
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }
    ///////////////////////////////////////////
    public int getPositionY() {
        return positionY;
    }
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
