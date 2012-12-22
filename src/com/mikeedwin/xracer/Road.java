package com.mikeedwin.xracer;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;

public class Road {
	private List<Tree> treeList;
	private int viewWidth;
    private int viewHeight;
    public Paint paint;
    public Path path;
    public int turn = 0;  //the turn of the road, -50 is hard left turn, 50 is hard right turn, 0 is straight
    private int roadwidthFront;
    private int roadwidthHorizon;
    public int hill = 0; //-20 to 20, -20 is full downhill, 20 is full uphill
    public int road_leftright = 0;  //-30 is a half view left, 30 is a half view right
    private Paint p;
    private Path pth, pth2, pth3, pth4, pth5;
    private PathMeasure measure;
    private Bitmap treeBitmap;
    private int currentSpeed, offsetChange;
    
    private int road_lr, ctrWidth, horizonHeight;
    private double bezX_adj1, bezX_adj2, bezY_adj1, bezY_adj2;
    private int roadBL_X, roadBL_Y, roadTL_X, roadTL_Y, roadLbez_X, roadLbez_Y;
    private int roadBR_X, roadBR_Y, roadTR_X, roadTR_Y, roadRbez_X, roadRbez_Y;
    
    private int leftLineBot_X, leftLineBot_Y, leftLineBez_X, leftLineBez_Y, leftLineTop_X, leftLineTop_Y;
    private int rightLineBot_X, rightLineBot_Y, rightLineBez_X, rightLineBez_Y, rightLineTop_X, rightLineTop_Y;
    
    private int leftTreeLineBot_X, leftTreeLineBot_Y, leftTreeLineBez_X, leftTreeLineBez_Y, leftTreeLineTop_X, leftTreeLineTop_Y;
    private int rightTreeLineBot_X, rightTreeLineBot_Y, rightTreeLineBez_X, rightTreeLineBez_Y, rightTreeLineTop_X, rightTreeLineTop_Y;
    
    private int roadline, roadlineGap, roadOffset;
    
    
    public Road(Bitmap _treeBitmap, int vWidth, int vHeight){
    	this.viewHeight = vHeight;
        this.viewWidth = vWidth;
        this.treeBitmap = _treeBitmap;
        
        treeList = new ArrayList<Tree>();
        
        roadwidthFront = (int)(viewWidth*.7);
        roadwidthHorizon = (int)(viewWidth*.3);
        hill = -3;
        roadline = viewHeight/9;
        roadlineGap = viewHeight/9;
        roadOffset = 0;
        
        
        //trees[0] = new Tree(treeBitmap, viewWidth, viewHeight);
    }
    
    public void update(){
    	
    }
    
    private void drawGround(Canvas canvas)
    {
    	p.setColor(0xff005900);
        canvas.drawRect(0, horizonHeight, viewWidth, viewHeight, p);
    }
    
   
    
    private void calculateNewRoadVars()
    {
    	road_lr = (road_leftright * viewWidth / -60);
        
        ctrWidth = viewWidth/2;
        horizonHeight = (int)(viewHeight*(((float)hill/100) + .35));
        roadwidthHorizon = (int)(viewWidth*((float)hill/200 + .25));
        
        //bezier modifier variables, depending on turn strength
        bezX_adj1 = Math.abs(turn)*.03 + 2.5;  //2 on straight, 3.5 on full turn
        bezX_adj2 = Math.abs(turn)*-.03 + 1.5;  //2 on straight, .5 on full turn
        
        bezY_adj1 = Math.abs(turn)*-.05 + 2.5;  //3 on straight, .5 on full turn
        bezY_adj2 = Math.abs(turn)*.05 + 3.5;  //3 on straight, 5 on full turn
        
        //--left outer road line
        roadBL_X = ctrWidth-roadwidthFront/2 + road_lr;
        roadBL_Y = viewHeight;

        roadTL_X = ctrWidth + ((turn * viewWidth) /40) - roadwidthHorizon/2 + road_lr/3;
        roadTL_Y = horizonHeight;
        
        roadLbez_X = ((int)(roadBL_X*bezX_adj1)+(int)(roadTL_X*bezX_adj2)) / 4;
        roadLbez_Y = ((int)(roadBL_Y*bezY_adj1)+(int)(roadTL_Y*bezY_adj2)) / 6;

        //--right outer road line
        roadBR_X = ctrWidth+roadwidthFront/2 + road_lr;
        roadBR_Y = viewHeight;

        roadTR_X = ctrWidth + ((turn * viewWidth) /40) + roadwidthHorizon/2 + road_lr/3;
        roadTR_Y = horizonHeight;

        roadRbez_X = ((int)(roadBR_X*bezX_adj1)+(int)(roadTR_X*bezX_adj2)) / 4;  //3-1
        roadRbez_Y = ((int)(roadBR_Y*bezY_adj1)+(int)(roadTR_Y*bezY_adj2)) / 6;  //1-5
        
        
        //----------inner road lines
        leftLineBot_X = (roadBL_X * 2 + roadBR_X)/3;
        leftLineBot_Y = roadBL_Y;
        leftLineBez_X = (roadLbez_X * 2 + roadRbez_X)/3;
        leftLineBez_Y = (roadLbez_Y * 2 + roadRbez_Y)/3;
        leftLineTop_X = (roadTL_X * 2 + roadTR_X)/3; 
        leftLineTop_Y = roadTL_Y;
        
        rightLineBot_X = (roadBL_X + roadBR_X * 2)/3;
        rightLineBot_Y = roadBL_Y;
        rightLineBez_X = (roadLbez_X + roadRbez_X * 2)/3;
        rightLineBez_Y = (roadLbez_Y + roadRbez_Y * 2)/3;
        rightLineTop_X = (roadTL_X + roadTR_X * 2)/3; 
        rightLineTop_Y = roadTL_Y;
        
        
        //----tree lines
        leftTreeLineBot_X = roadBL_X - roadwidthFront/3;
        leftTreeLineBot_Y = roadBL_Y;
        leftTreeLineBez_X = roadLbez_X - (roadwidthFront/3 + roadwidthHorizon/3)/2;
        leftTreeLineBez_Y = roadLbez_Y;
        leftTreeLineTop_X = roadTL_X - roadwidthHorizon/3;
        leftTreeLineTop_Y = roadTL_Y;
        
        rightTreeLineBot_X = roadBR_X + roadwidthFront/3;
        rightTreeLineBot_Y = roadBR_Y;
        rightTreeLineBez_X = roadRbez_X + (roadwidthFront/3 + roadwidthHorizon/3)/2;
        rightTreeLineBez_Y = roadRbez_Y;
        rightTreeLineTop_X = roadTR_X + roadwidthHorizon/3;
        rightTreeLineTop_Y = roadTR_Y;
    }
    
    private void drawRoadBG(Canvas canvas)
    {
    	pth = new Path();
    	pth.moveTo(roadBR_X,roadBR_Y);
        pth.lineTo(roadBL_X,roadBL_Y);
        pth.cubicTo(roadBL_X,roadBL_Y, roadLbez_X, roadLbez_Y, roadTL_X,roadTL_Y);
        pth.lineTo(roadTR_X, roadTR_Y);
        pth.cubicTo(roadTR_X, roadTR_Y, roadRbez_X, roadRbez_Y, roadBR_X,roadBR_Y);
        pth.moveTo(roadBR_X,roadBR_Y);
        p.setColor(0xff999999);
        canvas.drawPath(pth,p);
    }
    
    private void drawOuterRoadLines(Canvas canvas)
    {
    	pth2 = new Path();
    	pth2.moveTo(roadBL_X,roadBL_Y);
        pth2.cubicTo(roadBL_X,roadBL_Y, roadLbez_X, roadLbez_Y, roadTL_X,roadTL_Y);
        pth2.moveTo(roadTR_X, roadTR_Y);
        pth2.cubicTo(roadTR_X, roadTR_Y, roadRbez_X, roadRbez_Y, roadBR_X,roadBR_Y);
        p.setColor(0xFFCCCBCC);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawPath(pth2,p);
    }
    
    private void drawInnerRoadLines(Canvas canvas)
    {
    	pth3 = new Path();
    	pth3.moveTo(leftLineBot_X,leftLineBot_Y);
        pth3.cubicTo(leftLineBot_X,leftLineBot_Y, leftLineBez_X, leftLineBez_Y, leftLineTop_X,leftLineTop_Y);
        pth3.moveTo(rightLineBot_X,rightLineBot_Y);
        pth3.cubicTo(rightLineBot_X,rightLineBot_Y, rightLineBez_X, rightLineBez_Y, rightLineTop_X, rightLineTop_Y);
        p.setColor(0xFFCCCBCC);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setStrokeCap(Paint.Cap.SQUARE);
        p.setPathEffect(new DashPathEffect(new float[] {roadline,roadlineGap}, roadOffset));
        canvas.drawPath(pth3,p);
    }
    
    private void drawTreeLines(Canvas canvas)
    {
    	pth4 = new Path();
    	pth4.moveTo(leftTreeLineTop_X,leftTreeLineTop_Y);
        pth4.cubicTo(leftTreeLineTop_X,leftTreeLineTop_Y, leftTreeLineBez_X,leftTreeLineBez_Y, leftTreeLineBot_X,leftTreeLineBot_Y);
        
        pth5 = new Path();
        pth5.moveTo(rightTreeLineTop_X,rightTreeLineTop_Y);
        pth5.cubicTo(rightTreeLineTop_X,rightTreeLineTop_Y, rightTreeLineBez_X,rightTreeLineBez_Y, rightTreeLineBot_X,rightTreeLineBot_Y);
        
    }
    
    private void moveAndDrawTrees(Canvas canvas)
    {
    	measure = new PathMeasure();
    	measure.setPath(pth4, false);
        float[] xy = new float[2];
        float pathlength = measure.getLength();
        
        Iterator<Tree> it = treeList.iterator();
        
        while (it.hasNext()) {
        	Tree tree = it.next();
        	
        	if(!tree.distanceSet)  //if tree distance not initialized yet, then initalize it
        	{
        		
        		tree.distance = (int)(pathlength);
        		tree.distanceSet = true;
        	}
        	
        	tree.distance -= 4;
        	tree.offset = (int)(pathlength - tree.distance);
        	
        	measure.getPosTan(tree.offset, xy, null);
        	tree.x = (int)xy[0];
        	tree.y = (int)xy[1];
        	tree.distance = (int)(pathlength - tree.offset);
        	if(tree.distance <= 0)
        		it.remove();
        	
        	
        	offsetChange = this.currentSpeed / 16;
        	
        	if(tree.distance < 200)
        		offsetChange = (int)((this.currentSpeed / 16) * (1+((tree.distance-200)*-.0035)));
        	
        	tree.offset += 4;
        	
        	
			tree.onDraw(canvas);
        }
    	
    }
    
    
    @SuppressLint({ "DrawAllocation", "DrawAllocation", "DrawAllocation" })
    public void onDraw(Canvas canvas){
        p = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        
        
        calculateNewRoadVars();
        drawGround(canvas);
        drawRoadBG(canvas);
        drawOuterRoadLines(canvas);
        drawInnerRoadLines(canvas);
        drawTreeLines(canvas);
        moveAndDrawTrees(canvas);
        
        
        
    }
    
    public void moveCarForward(int speed)  //moves the car forward [actually moves road] a certain amount depending on car speed
    {
    	roadOffset += speed/4;
    	currentSpeed = speed;
    }
    	
    public void preventBoundaries()  //makes sure internal values don't exceed boundaries
    {
    	if(hill < -20)
    		hill = -20;
    	
    	if(hill > 20)
    		hill = 20;
    	
    	if(turn > 50)
    		turn = 50;
    	
    	if(turn < -50)
    		turn = -50;
    }
    
    public void makeTree(boolean isLeft)
    {
    	treeList.add(new Tree(treeBitmap, viewWidth, viewHeight));
    }
    
}
