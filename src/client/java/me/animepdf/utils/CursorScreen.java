package me.animepdf.utils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CursorScreen extends Screen {
    private int originX = 0;
    private int originY = 0;

    private int cursorX = 0;
    private int cursorY = 0;

    private int cursorFallbackX = 0;
    private int cursorFallbackY = 0;

    private int lineHeight = 20;

    private int curColumn = 0;
    private int numColumns = 3;

    public void MoveCursor(int x, int y) {
        cursorX += x;
        cursorY += y;
    }

    public void InitCursor(int x, int y) {
        this.originX = this.cursorFallbackX = this.cursorX = x;
        this.originY =this.cursorFallbackY = this.cursorY = y;
    }
    public void SetLineHeight(int height) {
        this.lineHeight = height;
    }
    public void SetColumns(int numColumns) {
        this.numColumns = numColumns;
    }
    public void SetCurColumn(int column) {
        this.curColumn = column;
    }

    public void UpdateOrigin() {
        this.originX = this.cursorX;
        this.originY = this.cursorY;
    }
    public void UpdateFallbacks() {
        this.cursorFallbackX = this.cursorX;
        this.cursorFallbackY = this.cursorY;
    }
    public void ResetCursor(boolean hard) {
        if(hard) {
            this.cursorFallbackX = this.originX;
            this.cursorFallbackY = this.originY;
        }

        this.curColumn = 0;
        this.cursorX = this.cursorFallbackX;
        this.cursorY = this.cursorFallbackY;
    }
    public void NextColumn(int width) {
        this.curColumn++;
        this.cursorX = (width/this.numColumns)*this.curColumn;
        this.cursorY = this.cursorFallbackY;
    }
    public void NextLine() {
        this.cursorX = this.cursorFallbackX;
        this.cursorY += this.lineHeight;
    }

    protected CursorScreen(Text title) {
        super(title);
    }

    public int getCursorX() {
        return cursorX;
    }
    public int getCursorY() {
        return cursorY;
    }
}
