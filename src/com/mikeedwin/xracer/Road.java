package com.mikeedwin.xracer;


import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Road {
	private int viewWidth;
    private int viewHeight;
    public Paint paint;
    public Path path;
    public int turn = 0;  //the turn of the road, -50 is hard left turn, 50 is hard right turn, 0 is straight
	
    public Road(int vWidth, int vHeight){
    	this.viewHeight = vHeight;
        this.viewWidth = vWidth;
    }
    
    public void update(){
    	
    }
    
    @SuppressLint({ "DrawAllocation", "DrawAllocation", "DrawAllocation" })
    public void onDraw(Canvas canvas){
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        Path pth = new Path();
        int ctrWidth = viewWidth/2;
        int ctrHeight = viewHeight/2;
        
        pth.moveTo(viewWidth-70,viewHeight);
        pth.lineTo(70, viewHeight);
        
        int cubiX = ((70) + ((70+ctrWidth)/2))/2;
        int cubiY = (viewHeight + ctrHeight)/2;
        pth.cubicTo(70, viewHeight, 80, 100, (70+ctrWidth)/2+(turn*3), ctrHeight);
        pth.lineTo((viewWidth-70+ctrWidth)/2+(turn*3), ctrHeight);
        pth.moveTo(viewWidth-70,viewHeight);
        p.setColor(0xffaaaaaa);
        canvas.drawPath(pth,p);
        
    }
    
}
