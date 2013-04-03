package com.mikeedwin.xracer;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Shader;

public class Road {
	private List<Tree> treeList;
	private int viewWidth;
	private Boolean blurMode = false;
    private int viewHeight;
    public Paint paint;
    public Path path;
    public float turn = 0;  //the turn of the road, -50 is hard left turn, 50 is hard right turn, 0 is straight
    public float nextTurn = 0;  //the next track point's turn
    public float turnChangeSpeed = 0;  //the rate at which the road is changing
    public float distToNextTrackPt = 0;
    public float compass;  //which way you are facing.  Goes from 0 [north] to 99 [50 is south]
    
    public float hill = 0; //-20 to 20, -20 is full downhill, 20 is full uphill
    public float nextHill = 0;  //the next track point's hill
    
    private int roadwidthFront;
    private int roadwidthHorizon;
    private int roadwidthMid;
    public float road_leftright = 0;  //-30 is a half view left, 30 is a half view right
    private Paint p;
    private Path pth, pth2, pth3, pth4, pth5;
    private PathMeasure measureLeft, measureRight;
    private Bitmap treeBitmap;
    private int offsetChange;
    private double distChange;
    
    private float road_lr;
    private int ctrWidth, horizonHeight, midHorizonHeight;
    private float bezX_adj1, bezX_adj2, bezY_adj1, bezY_adj2;
    private float roadBL_X, roadBL_Y, roadML_X, roadML_Y, roadTL_X, roadTL_Y, roadLbez_X, roadLbez_Y;
    private float roadBR_X, roadBR_Y, roadMR_X, roadMR_Y, roadTR_X, roadTR_Y, roadRbez_X, roadRbez_Y;
    
    private float leftLineBot_X, leftLineBot_Y, leftLineBez_X, leftLineBez_Y, leftLineMid_X, leftLineMid_Y, leftLineTop_X, leftLineTop_Y;
    private float rightLineBot_X, rightLineBot_Y, rightLineBez_X, rightLineBez_Y, rightLineMid_X, rightLineMid_Y, rightLineTop_X, rightLineTop_Y;
    
    private float leftTreeLineBot_X, leftTreeLineBot_Y, leftTreeLineBez_X, leftTreeLineBez_Y, leftTreeLineMid_X, leftTreeLineMid_Y, leftTreeLineTop_X, leftTreeLineTop_Y;
    private float rightTreeLineBot_X, rightTreeLineBot_Y, rightTreeLineBez_X, rightTreeLineBez_Y, rightTreeLineMid_X, rightTreeLineMid_Y, rightTreeLineTop_X, rightTreeLineTop_Y;
    
    private int roadline, roadlineGap, roadOffset;
    
    private float nextTurnAdjuster = 0;
    
	int shaderColor0, shaderColor1, x1,y1,y2,x2, horizonDiff;
	float compassAdj; 
	
	
    
    public Road(Bitmap _treeBitmap, int vWidth, int vHeight){
    	this.viewHeight = vHeight;
        this.viewWidth = vWidth;
        this.treeBitmap = _treeBitmap;
        
        treeList = new ArrayList<Tree>();
        
        roadwidthFront = (int)(viewWidth*.7);
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
    	//p.setColor(0xff005900);
    	p.setStyle(Paint.Style.FILL);
    	shaderColor0 = 0xff318C00;
    	shaderColor1 = 0xff004600;
    	p.setAntiAlias(true);
    	
    	
    	horizonDiff = viewHeight - horizonHeight;
    	
    	compassAdj = compass-70;  //adjust compass so top-left is 0
    	if(compassAdj < 0)
    		compassAdj += 100;
    	
    	
    	if(compassAdj < 25)  //top quadrant
    	{
    		y1 = horizonHeight;
    		y2 = viewHeight;
    		
    		x2 = (int)((compassAdj%25) * (viewWidth*.04)); 
    		x1 = viewWidth - x2;
    	}
    	else if(compassAdj < 50)  //left quadrant
    	{
    		x1 = 0;
    		x2 = viewWidth;
    		
    		y1 = (int)((compassAdj%25) * (horizonDiff*.04)) + horizonHeight; 
    		y2 = viewHeight - y1 + horizonHeight;
    	}
    	else if(compassAdj < 75)  //bottom quadrant
    	{
    		y2 = horizonHeight;
    		y1 = viewHeight;
    		
    		x1 = (int)((compassAdj%25) * (viewWidth*.04)); 
    		x2 = viewWidth - x1;
    	}
    	else   //right quadrant
    	{
    		x2 = 0;
    		x1 = viewWidth;
    		
    		y2 = (int)((compassAdj%25) * (horizonDiff*.04)) + horizonHeight; 
    		y1 = viewHeight - y2 + horizonHeight;
    	}
    	
    	Shader linearGradientShader = new LinearGradient(x1, y1, x2, y2, 
    		    shaderColor1, shaderColor0, Shader.TileMode.MIRROR);
    	
    	
    	p.setShader(linearGradientShader);
    	
    	
        canvas.drawRect(0, horizonHeight, viewWidth, viewHeight, p);
    	
    	p.setShader(null);  //unsets the shader as gradients [so it doesnt draw the road as a gradient later]
    }
    
   
    
    private void calculateNewRoadVars()
    {
    	road_lr = (road_leftright * viewWidth / -60);
        
        ctrWidth = viewWidth/2;
        midHorizonHeight = (int)(viewHeight*(((float)hill/100) + .35));
        horizonHeight = (int)(viewHeight*(((float)hill/100) + .35)) - (viewHeight/16);
        roadwidthHorizon = (int)(viewWidth*((float)hill/200 + .25));
        roadwidthMid = (int)(roadwidthHorizon * 1.1);
        
        //bezier modifier variables, depending on turn strength
        bezX_adj1 = (float)(Math.abs(turn)*.03 + 2.5);  //2 on straight, 3.5 on full turn
        bezX_adj2 = (float)(Math.abs(turn)*-.03 + 1.5);  //2 on straight, .5 on full turn
        
        bezY_adj1 = (float)(Math.abs(turn)*-.05 + 2.5);  //3 on straight, .5 on full turn
        bezY_adj2 = (float)(Math.abs(turn)*.05 + 3.5);  //3 on straight, 5 on full turn
        
        //next turn road adjustment
        
        float prevTurnAdjuster = nextTurnAdjuster;
        
        float straightTurnAdjuster = turn/(float)(viewWidth*.001);  //sets the next turn adjuster to line up with current road
       
        
        if(distToNextTrackPt >= .2)
        	nextTurnAdjuster = straightTurnAdjuster;
        
        else if((distToNextTrackPt < .2)&&(distToNextTrackPt >= .1))
        {
        	float alleg = (float)((distToNextTrackPt-.1)*10); //=1 when dist is .2, =0 when dist is .1
        	
        	nextTurnAdjuster = (alleg*straightTurnAdjuster)   +   (1-alleg)*(turn+turnChangeSpeed)/(float)(viewWidth*.002);
        }
        else if((distToNextTrackPt < .1)&&(distToNextTrackPt >= 0))
        {
        	float alleg = (float)(distToNextTrackPt*10);
        	
        	nextTurnAdjuster = alleg*(turn+turnChangeSpeed)/(float)(viewWidth*.002)   +   (1-alleg)*nextTurn/(float)(viewWidth*.001);
        }
        
        
        //prevent choppy next turns
        if(nextTurnAdjuster > (prevTurnAdjuster+(viewWidth*.02)))
        	nextTurnAdjuster = (float)(prevTurnAdjuster+(viewWidth*.02));
        
        else if(nextTurnAdjuster < (prevTurnAdjuster-(viewWidth*.02)))
        	nextTurnAdjuster = (float)(prevTurnAdjuster-(viewWidth*.02));
        
        
        
        //--left outer road line
        roadBL_X = ctrWidth-roadwidthFront/2 + road_lr;
        roadBL_Y = viewHeight;

        roadML_X = ctrWidth + ((turn * viewWidth) /40) - roadwidthMid/2 + road_lr/3;
        roadML_Y = midHorizonHeight;
        
        roadTL_X = roadML_X + nextTurnAdjuster + (roadwidthMid - roadwidthHorizon);
        roadTL_Y = horizonHeight;
        
        roadTR_X = roadMR_X + nextTurnAdjuster - (roadwidthMid - roadwidthHorizon);
        roadTR_Y = horizonHeight;
        
        roadLbez_X = ((roadBL_X*bezX_adj1)+(int)(roadML_X*bezX_adj2)) / 4;
        roadLbez_Y = ((roadBL_Y*bezY_adj1)+(int)(roadML_Y*bezY_adj2)) / 6;

        //--right outer road line
        roadBR_X = ctrWidth+roadwidthFront/2 + road_lr;
        roadBR_Y = viewHeight;

        roadMR_X = ctrWidth + ((turn * viewWidth) /40) + roadwidthMid/2 + road_lr/3;
        roadMR_Y = midHorizonHeight;

        roadRbez_X = ((roadBR_X*bezX_adj1)+(int)(roadMR_X*bezX_adj2)) / 4;  //3-1
        roadRbez_Y = ((roadBR_Y*bezY_adj1)+(int)(roadMR_Y*bezY_adj2)) / 6;  //1-5
        
        
        //----------inner road lines
        leftLineBot_X = (roadBL_X * 2 + roadBR_X)/3;
        leftLineBot_Y = roadBL_Y;
        leftLineBez_X = (roadLbez_X * 2 + roadRbez_X)/3;
        leftLineBez_Y = (roadLbez_Y * 2 + roadRbez_Y)/3;
        leftLineMid_X = (roadML_X * 2 + roadMR_X)/3; 
        leftLineMid_Y = roadML_Y;
        leftLineTop_X = (roadTL_X * 2 + roadTR_X)/3; 
        leftLineTop_Y = horizonHeight;
        
        rightLineBot_X = (roadBL_X + roadBR_X * 2)/3;
        rightLineBot_Y = roadBL_Y;
        rightLineBez_X = (roadLbez_X + roadRbez_X * 2)/3;
        rightLineBez_Y = (roadLbez_Y + roadRbez_Y * 2)/3;
        rightLineMid_X = (roadML_X + roadMR_X * 2)/3; 
        rightLineMid_Y = roadML_Y;
        rightLineTop_X = (roadTL_X + roadTR_X * 2)/3; 
        rightLineTop_Y = horizonHeight;
        
        
        //----tree lines
        leftTreeLineBot_X = roadBL_X - roadwidthFront/4;
        leftTreeLineBot_Y = roadBL_Y;
        leftTreeLineBez_X = roadLbez_X - (roadwidthFront/4 + roadwidthMid/4)/2;
        leftTreeLineBez_Y = roadLbez_Y;
        leftTreeLineMid_X = roadML_X - roadwidthMid/4;
        leftTreeLineMid_Y = roadML_Y;
        leftTreeLineTop_X = roadTL_X - roadwidthHorizon/4;
        leftTreeLineTop_Y = roadTL_Y;
        
        rightTreeLineBot_X = roadBR_X + roadwidthFront/4;
        rightTreeLineBot_Y = roadBR_Y;
        rightTreeLineBez_X = roadRbez_X + (roadwidthFront/4 + roadwidthMid/4)/2;
        rightTreeLineBez_Y = roadRbez_Y;
        rightTreeLineMid_X = roadMR_X + roadwidthMid/4;
        rightTreeLineMid_Y = roadMR_Y;
        rightTreeLineTop_X = roadTR_X + roadwidthHorizon/4;
        rightTreeLineTop_Y = roadTR_Y;
    }
    
    private void drawRoadBG(Canvas canvas)
    {
    	pth = new Path();
    	pth.moveTo(roadBR_X,roadBR_Y);
        pth.lineTo(roadBL_X,roadBL_Y);
        pth.cubicTo(roadLbez_X, roadLbez_Y, roadML_X,roadML_Y, roadTL_X, roadTL_Y);
        pth.lineTo(roadTR_X, roadTR_Y);
        pth.cubicTo(roadMR_X, roadMR_Y, roadRbez_X, roadRbez_Y, roadBR_X,roadBR_Y);
        pth.moveTo(roadBR_X,roadBR_Y);
        p.setColor(0xff999999);
        canvas.drawPath(pth,p);
    }
    
    private void drawOuterRoadLines(Canvas canvas)
    {
    	pth2 = new Path();
    	pth2.moveTo(roadBL_X,roadBL_Y);
        pth2.cubicTo(roadLbez_X, roadLbez_Y, roadML_X,roadML_Y, roadTL_X, roadTL_Y);
        pth2.moveTo(roadTR_X, roadTR_Y);
        pth2.cubicTo(roadMR_X, roadMR_Y, roadRbez_X, roadRbez_Y, roadBR_X,roadBR_Y);
        p.setColor(0xFFCCCBCC);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawPath(pth2,p);
    }
    
    private void drawInnerRoadLines(Canvas canvas)
    {
    	pth3 = new Path();
    	pth3.moveTo(leftLineBot_X,leftLineBot_Y);
        pth3.cubicTo(leftLineBez_X, leftLineBez_Y, leftLineMid_X,leftLineMid_Y, leftLineTop_X,leftLineTop_Y);
        pth3.moveTo(rightLineBot_X,rightLineBot_Y);
        pth3.cubicTo(rightLineBez_X, rightLineBez_Y, rightLineMid_X, rightLineMid_Y, rightLineTop_X, rightLineTop_Y);
        p.setColor(0xFFCCCBCC);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(4);
        p.setStrokeCap(Paint.Cap.BUTT);
        p.setPathEffect(new DashPathEffect(new float[] {roadline,roadlineGap}, roadOffset));
        canvas.drawPath(pth3,p);
    }
    
    private void drawInnerRoadLinesBlurMode(Canvas canvas)
    {
    	pth3 = new Path();
    	pth3.moveTo(leftLineBot_X,leftLineBot_Y);
        pth3.cubicTo(leftLineBez_X, leftLineBez_Y, leftLineMid_X,leftLineMid_Y, leftLineTop_X,leftLineTop_Y);
        pth3.moveTo(rightLineBot_X,rightLineBot_Y);
        pth3.cubicTo(rightLineBez_X, rightLineBez_Y, rightLineMid_X, rightLineMid_Y, rightLineTop_X, rightLineTop_Y);
        p.setColor(0xCCCCCBCC);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setStrokeCap(Paint.Cap.BUTT);
        p.setPathEffect(new DashPathEffect(new float[] {roadline,roadlineGap}, roadOffset));
        canvas.drawPath(pth3,p);
        
        pth3 = new Path();
    	pth3.moveTo(leftLineBot_X,leftLineBot_Y);
        pth3.cubicTo(leftLineBez_X,leftLineBez_Y, leftLineMid_X, leftLineMid_Y, leftLineTop_X,leftLineTop_Y);
        pth3.moveTo(rightLineBot_X,rightLineBot_Y);
        pth3.cubicTo(rightLineBez_X,rightLineBez_Y, rightLineMid_X, rightLineMid_Y, rightLineTop_X, rightLineTop_Y);
        p.setColor(0x44CCCBCC);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setStrokeCap(Paint.Cap.SQUARE);
        p.setPathEffect(null);
        canvas.drawPath(pth3,p);
    }
    
    
    
    private void drawTreeLines(Canvas canvas)
    {
    	pth4 = new Path();
    	pth4.moveTo(leftTreeLineTop_X,leftTreeLineTop_Y);
        pth4.cubicTo(leftTreeLineMid_X,leftTreeLineMid_Y, leftTreeLineBez_X,leftTreeLineBez_Y, leftTreeLineBot_X,leftTreeLineBot_Y);
        
        pth5 = new Path();
        pth5.moveTo(rightTreeLineTop_X,rightTreeLineTop_Y);
        pth5.cubicTo(rightTreeLineMid_X,rightTreeLineMid_Y, rightTreeLineBez_X,rightTreeLineBez_Y, rightTreeLineBot_X,rightTreeLineBot_Y);
        
    }
    
    
    private void moveAndDrawTrees(Canvas canvas)
    {
    	measureLeft = new PathMeasure();
    	measureLeft.setPath(pth4, false);
    	measureRight = new PathMeasure();
    	measureRight.setPath(pth5, false);
    	
        float[] xyLeft = new float[2];
        int pathlengthLeft = (int)measureLeft.getLength();
        float[] xyRight = new float[2];
        int pathlengthRight = (int)measureRight.getLength();
        
        
        Iterator<Tree> it = treeList.iterator();
        
        while (it.hasNext()) {
        	Tree tree = it.next();
        	
        	if(!tree.distanceSet)  //if tree distance not initialized yet, then initalize it
        	{
        		if(tree.isLeft)
        			tree.distance = pathlengthLeft;
        		
        		else
        			tree.distance = pathlengthRight;
        		
        		tree.distanceSet = true;
        	}
        	
        	//tree.offset = (int)(pathlength - tree.distance);
        	
        	
        	if(tree.isLeft)
        	{
	        	measureLeft.getPosTan(tree.offset, xyLeft, null);
	        	tree.x = (int)xyLeft[0];
	        	tree.y = (int)xyLeft[1];
	        	tree.distance = (pathlengthLeft - tree.offset);
	        	tree.currentSize = xyLeft[1]/3;
	        	
	        	offsetChange = (int)((this.distChange * pathlengthLeft) * .0268);
	        	
	        	if(tree.distance < 250)
	        		offsetChange = (int)(((this.distChange * pathlengthLeft) * .0268) * (1+((tree.distance-250)*-.007)));
	        	
        	}
        	else
        	{
	        	measureRight.getPosTan(tree.offset, xyRight, null);
	        	tree.x = (int)xyRight[0];
	        	tree.y = (int)xyRight[1];
	        	tree.distance = (pathlengthRight - tree.offset);
	        	tree.currentSize = xyRight[1]/3;
	        	
	        	offsetChange = (int)((this.distChange * pathlengthRight) * .0268);
	        	
	        	if(tree.distance < 250)
	        		offsetChange = (int)(((this.distChange * pathlengthRight) * .0268) * (1+((tree.distance-250)*-.007)));
	        	
        	}
        	
        	
        	if(tree.distance <= 0)
        		it.remove();
        	
        	
        	
        	tree.offset += offsetChange;
        	
        	
			
        }
        
        for (Tree tree : treeList) {
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
        
        if(!blurMode)
        	drawInnerRoadLines(canvas);
        else
        	drawInnerRoadLinesBlurMode(canvas);
        
        drawTreeLines(canvas);
        moveAndDrawTrees(canvas);
        
        
    }
    
    public void moveCarForward(double _distChange)  //moves the car forward [actually moves road] a certain amount depending on distance changed
    {
    	double offsetChange = (viewWidth*distChange)*.053;  //.0008
    	distChange = _distChange;
    	
    	if(offsetChange > roadline*1.25)
    	{
    		blurMode = true;
    		roadOffset += roadline*1.25;
    	}
    	else
    		roadOffset += offsetChange;
    	
    	
    	float roadMovement = ((float)distChange * turn)*(float).133;  //.002
    	
    	road_leftright -= roadMovement;
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
    	treeList.add(new Tree(treeBitmap, viewWidth, viewHeight, isLeft));
    }
    
}
