package com.ritterliu.android;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.View;

public class HourWatchScale extends View {

	/**
	 * 每半小时图形角度
	 */
	private float angle = 7.5f;
	/**
	 * 相邻两个半小时图形留出一定角度空白，以便让每半小时都有一条分隔线
	 */
	private float angle_space = 0.4f;
	
	private int _index = 1;
	private int _color = 0XFFFFFFFF; 
	public  RectF _out = new RectF(0f,0f,186,186);
	public  RectF _in = new RectF(36,36,150,150);
	private float skewing = 0.7f;
	private float w_delta;
	private float h_delta;
	private Path currentPath;
	int screenWidth;
	int screenHeight;
	public HourWatchScale(Context context) {
		super(context);
	}
	
	public void resize(int width,int height){
		height = width;
		w_delta = (float)width/186;
		h_delta = (float)height/186;
		//将圆形尺寸按宽度比例缩放
		_out = new RectF(_out.left*w_delta,_out.top*h_delta,_out.right*w_delta,_out.bottom*h_delta);
		_in = new RectF(_in.left*w_delta,_in.top*h_delta,_in.right*w_delta,_in.bottom*h_delta);
		//比例缩小，以例显示出另外背景边框效果
		_out = new RectF(_out.left+skewing,_out.top+skewing,_out.right-skewing,_out.bottom-skewing);
		_in = new RectF(_in.left+skewing,_in.top+skewing,_in.right-skewing,_in.bottom-skewing);

	}
	
	public HourWatchScale(Context context , int index , int color) {
		super(context);
		_index = index;
		_color = color;
	}
	
	public void ChangeColor(int color){
		_color = color;
	}
	
	public int get_color() {
		return _color;
	}
	
	public PointF getCentralPoint(int index){
		PointF point = new PointF();
		float r_1 = ((_out.right-_out.left)/2+(_in.right-_in.left)/2)/2;
		float start_1_x = (float) (_out.left + (_in.left - _out.left)/2 + r_1 + (Math.cos(AngleRadians(15*index/2f-90)) * r_1));
		float start_1_y = (float) (_out.top +(_in.left - _out.left)/2 + r_1 + (Math.sin(AngleRadians(15*index/2f-90)) * r_1));
		point.set(start_1_x, start_1_y);
		return point;
	}
	
	/**
	 * 判断坐标是否在当前半小时内
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean InArea(float x,float y){
		RectF rectF = new RectF();
		currentPath.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(currentPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        if (region.contains((int)x, (int)y)) {
        	return true;
        }else{
        	return false;
        }
	}
	
	/**
	 * 计算当前半小时段的角度
	 * @param iHour 要计算的半小时段，一天分48个段
	 * @return
	 */
	public float getAngle(int iHour){
		return angle*(iHour-1)-90;
	}
	
	/**
	 * 按角度计算弧度
	 * @param _angle
	 * @return
	 */
	private float AngleRadians(float _angle){
		return (float) (_angle * Math.PI / 180.0);
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		Paint mypaint = new Paint();
		mypaint.setColor(_color);
		mypaint.setStyle(Style.FILL_AND_STROKE);
		mypaint.setAntiAlias(true);
		//外圆半径
		float out_r = (_out.right-_out.left)/2;
		//内圆半径
		float in_r = (_in.right-_in.left)/2;
		//当前绘制的起始点角度
		float start_angle = getAngle(_index-1);
		//当前绘制的终止点角度
		float stop_angle = getAngle(_index);
		
		//当前绘制半小时图形的起始点正弦
		float start_cos = (float)Math.cos(AngleRadians(start_angle));
		//当前绘制半小时图形的起始点余弦
		float start_sin = (float)Math.sin(AngleRadians(start_angle));
		
		//正弦
		float stop_cos = (float)Math.cos(AngleRadians(stop_angle));
		//余弦
		float stop_sin = (float)Math.sin(AngleRadians(stop_angle));
		
		//外圆角度在圆上对应的左边横坐标
		float start_out_x_left = (float) (_out.left + out_r + (start_cos * out_r));
		//外圆角度在圆上对应的左边纵坐标
		float start_out_y_left = (float) (_out.top + out_r + (start_sin * out_r));
		
		//内圆角度在圆上对应的右边横坐标
		float stop_in_x_right = (float) (_in.left + in_r + (stop_cos * in_r));
		//内圆角度在圆上对应的右边纵坐标
		float stop_in_y_right = (float) (_in.top + in_r + (stop_sin * in_r));

		Path myPath = new Path();
		myPath.moveTo(start_out_x_left, start_out_y_left);
		myPath.addArc(_out, start_angle, angle-angle_space);
		myPath.lineTo(stop_in_x_right, stop_in_y_right);
		myPath.addArc(_in, stop_angle, -angle+angle_space);
		myPath.lineTo(start_out_x_left, start_out_y_left);
		
		//mypaint.setAlpha(250);
		//去锯齿效果，与paint.setAntiAlias(true)效果相同，这里备用
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		canvas.drawPath(myPath, mypaint);
		currentPath = myPath;
		canvas.restore();
	}
}
