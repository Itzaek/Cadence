package me.andrewpeng.cadence.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import me.andrewpeng.cadence.core.Renderer;
import me.andrewpeng.cadence.managers.SpinnerManager;

public class Spinner{
    public String[] list;
    public int x1, x2, y1, y2, height, width, yOffset;
    public int queuedSpin = 0;
    public int tickOff;
    public int position = 0;

    public String[] getList() {
        return list;
    }
    public void setList(String[] list) {
        this.list = list;
    }
    public int getX1() {
        return x1;
    }
    public void setX1(int x1) {
        this.x1 = x1;
    }
    public int getX2() {
        return x2;
    }
    public void setX2(int x2) {
        this.x2 = x2;
    }
    public int getY1() {
        return y1;
    }
    public void setY1(int y1) {
        this.y1 = y1;
    }
    public int getY2() {
        return y2;
    }
    public void setY2(int y2) {
        this.y2 = y2;
    }
    public int getPosition(){
        return position;
    }

    public Spinner(int x1, int y1, int x2, int y2, String[] list) {

        // Bound coordinates
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;

        // Set list, height, width, initial offset, and the amount each tick should travel
        this.list = list;
        height = y2 - y1;
        width = x2 - x1;
        yOffset = 0;
        tickOff = (height / 75);
        SpinnerManager.spinners.add(this);

    }
    public void tick(){
        // Moving down
        if (queuedSpin > 0){
            yOffset += tickOff;
            queuedSpin -= tickOff;

            if (queuedSpin < 0)
                queuedSpin = 0;

            // Moving up
        }else if (queuedSpin < 0){
            yOffset -= tickOff;
            queuedSpin += tickOff;

            if (queuedSpin > 0)
                queuedSpin = 0;

        }
    }
    public void spin(float yVel){
        // yVel is actually flipped, so it must be subtracted
        if (queuedSpin == 0){
            // Moving down the list
            if (yVel > 0 && position < list.length - 1){
                queuedSpin -= height / 5;
                position += 1;

                // Tell the renderer that it's been spun
                Renderer.updateSpinner();

                // Moving up the list
            }else if (yVel < 0 && position > 0){
                queuedSpin += height / 5;
                position -= 1;

                // Tell the renderer that it's been spun
                Renderer.updateSpinner();

            }
        }
    }
    public void render(Canvas graphics, Paint paint) {
        // Calculate middle of bounds
        int middle = (x1 + x2) / 2;

        // Loop through each item in the list
        for (int i = 0; i < list.length; i++){
            // It should only fit 4-5 items on the screen, and add the yOffset
            int yPos = y1 + ((i + 1) * (height / 5)) + yOffset;

            // Only render the text if it is within the bounds
            if (yPos > y1 && yPos < y2){
                int color = Color.WHITE;
                String text = list[i];
                // If the text is the current one selected, make it green and add brackets
                if (position == i){
                    color = Color.GREEN;
                    text = "> " + text + " <";
                }
                // Finally draw
                Renderer.centerText(text, graphics, middle, yPos, paint, 20, color, 255);
            }
        }
    }

}
