package com.ritterliu.android;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.DisplayMetrics;
import android.view.View;

public class HourRoundBackGround extends View {

	/**
	 * 每半小时图形角度
	 */
	private float angle = 7.5f;
	/**
	 * 相邻两个半小时图形留出一定角度空白，以便让每半小时都有一条分隔线
	 */
	private float angle_space = 0.2f;
	private int _index = 1;
	private int _color = 0XFFFFFFFF; 
	private float skewing = 0.8f;
	public  RectF _out = new RectF(2f+skewing,2f+skewing,188-skewing,188-skewing);
	public  RectF _in = new RectF(38,38,152,152);
	private float w_delta;
	private float h_delta;
	private Path currentPath;
	int screenWidth;
	int screenHeight;
	public HourRoundBackGround(Context context) {
		super(context);
	}
	
	public void resize(int width,int height){
		w_delta = (float)width/186;
		h_delta = (float)height/186;
		_out = new RectF(_out.left*w_delta,_out.top*h_delta,_out.right*w_delta,_out.bottom*h_delta);
		_in = new RectF(_in.left*w_delta,_in.top*h_delta,_in.right*w_delta,_in.bottom*h_delta);
	}
	
	public HourRoundBackGround(Context context , int index , int color) {
		super(context);
		_index = index;
		_color = color;
	}
	
	public void SetRectF(){
		
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
		long bg_color = Long.valueOf(("0xFFFFFF").replace("0x", "FF"),16);
		Paint paint = new Paint();
		paint.setColor((int)bg_color);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);
		
		//外圆半径
		float out_r = (_out.right-_out.left)/2;
		//内圆半径
		float in_r = (_in.right-_in.left)/2;

		canvas.drawCircle(_out.left+out_r, _out.left+out_r, out_r, mypaint);
		//mypaint.setStyle(Style.STROKE);
		//canvas.drawCircle(out_r+2, out_r+2, in_r, paint);
		canvas.restore();
	}
}
