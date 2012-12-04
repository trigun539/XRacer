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
    private int roadwidthFront;
    private int roadwidthHorizon;
    public int hill; //-20 to 20, -20 is full downhill, 20 is full uphill
    
    
    public Road(int vWidth, int vHeight){
    	this.viewHeight = vHeight;
        this.viewWidth = vWidth;
        
        roadwidthFront = (int)(viewWidth*.7);
        roadwidthHorizon = (int)(viewWidth*.3);
        hill = 0;
    }
    
    public void update(){
    	
    }
    
    @SuppressLint({ "DrawAllocation", "DrawAllocation", "DrawAllocation" })
    public void onDraw(Canvas canvas){
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        Path pth = new Path();
        
        
        
        int ctrWidth = viewWidth/2;
        int ctrHeight = (int)(viewHeight*.35);
        
        double bezX_adj1 = Math.abs(turn)*.03 + 2.5;  //2 on straight, 3.5 on full turn
        double bezX_adj2 = Math.abs(turn)*-.03 + 1.5;  //2 on straight, .5 on full turn
        
        double bezY_adj1 = Math.abs(turn)*-.05 + 2.5;  //3 on straight, .5 on full turn
        double bezY_adj2 = Math.abs(turn)*.05 + 3.5;  //3 on straight, 5 on full turn
        
        int roadBL_X = ctrWidth-roadwidthFront/2;
        int roadBL_Y = viewHeight;

        int roadTL_X = ctrWidth + ((turn * viewWidth) /40) - roadwidthHorizon/2;
        int roadTL_Y = ctrHeight;
        
        int roadLbez_X = ((int)(roadBL_X*bezX_adj1)+(int)(roadTL_X*bezX_adj2)) / 4;
        int roadLbez_Y = ((int)(roadBL_Y*bezY_adj1)+(int)(roadTL_Y*bezY_adj2)) / 6;

        int roadBR_X = ctrWidth+roadwidthFront/2;
        int roadBR_Y = viewHeight;

        int roadTR_X = ctrWidth + ((turn * viewWidth) /40) + roadwidthHorizon/2;
        int roadTR_Y = ctrHeight;

        int roadRbez_X = ((int)(roadBR_X*bezX_adj1)+(int)(roadTR_X*bezX_adj2)) / 4;  //3-1
        int roadRbez_Y = ((int)(roadBR_Y*bezY_adj1)+(int)(roadTR_Y*bezY_adj2)) / 6;  //1-5
        
        
        pth.moveTo(roadBR_X,roadBR_Y);
        pth.lineTo(roadBL_X,roadBL_Y);
        pth.cubicTo(roadBL_X,roadBL_Y, roadLbez_X, roadLbez_Y, roadTL_X,roadTL_Y);
        pth.lineTo(roadTR_X, roadTR_Y);
        pth.cubicTo(roadTR_X, roadTR_Y, roadRbez_X, roadRbez_Y, roadBR_X,roadBR_Y);
        pth.moveTo(roadBR_X,roadBR_Y);
        p.setColor(0xffaaaaaa);
        canvas.drawPath(pth,p);
        
    }
    
}
