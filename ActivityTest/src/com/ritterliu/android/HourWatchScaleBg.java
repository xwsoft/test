package com.ritterliu.android;



import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.view.View;

public class HourWatchScaleBg extends View {

	/**
	 * ÿ��Сʱͼ�νǶ�
	 */
	private float angle = 7.5f;
	/**
	 * ����������Сʱͼ������һ���Ƕȿհף��Ա���ÿ��Сʱ����һ���ָ���
	 */
	private float angle_space = 0.4f;
	private int _index = 1;
	private int _color = 0XFFFFFFFF; 
	private float skewing = 0.7f;
	
	public  RectF _out = new RectF(0f,0f,186,186);
	public  RectF _in = new RectF(36,36,150,150);
	/**
	 * ʱ��̶�Բ�ι켣
	 */
	public  RectF _in_watch = new RectF(43,43,143,143);
	
	private float w_delta;
	private float h_delta;
	private Path currentPath;
	int screenWidth;
	int screenHeight;
	public HourWatchScaleBg(Context context) {
		super(context);
	}
	
	public void resize(int width,int height){
		height = width;
		w_delta = (float)width/186;
		h_delta = (float)height/186;
		
		//��Բ�γߴ簴��ȱȱ�����
		_out = new RectF(_out.left*w_delta,_out.top*h_delta,_out.right*w_delta,_out.bottom*h_delta);
		_in = new RectF(_in.left*w_delta,_in.top*h_delta,_in.right*w_delta,_in.bottom*h_delta);
		_in_watch = new RectF(_in_watch.left*w_delta,_in_watch.top*h_delta,_in_watch.right*w_delta,_in_watch.bottom*h_delta);
		//���ڸ�����Ʊ�����Χ�߿�Ч�����������Ϊ���������ʾ��ʱ��̶ȵ���
		_in_watch = new RectF(_in_watch.left+skewing,_in_watch.top+skewing,_in_watch.right-skewing,_in_watch.bottom-skewing);
	}
	
	public HourWatchScaleBg(Context context , int index , int color) {
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
	 * �ж������Ƿ��ڵ�ǰ��Сʱ��
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
	 * ���㵱ǰ��Сʱ�εĽǶ�
	 * @param iHour Ҫ����İ�Сʱ�Σ�һ���48����
	 * @return
	 */
	public float getAngle(int iHour){
		return angle*(iHour-1)-90;
	}
	
	/**
	 * ���Ƕȼ��㻡��
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
		//��Բ�뾶
		float out_r = (_out.right-_out.left)/2;
		//��Բ�뾶
		float in_r = (_in.right-_in.left)/2;
		//��ǰ���Ƶ���ʼ��Ƕ�
		float start_angle = getAngle(_index-1);
		//��ǰ���Ƶ���ֹ��Ƕ�
		float stop_angle = getAngle(_index);
		
		//��ǰ���ư�Сʱͼ�ε���ʼ������
		float start_cos = (float)Math.cos(AngleRadians(start_angle));
		//��ǰ���ư�Сʱͼ�ε���ʼ������
		float start_sin = (float)Math.sin(AngleRadians(start_angle));
		
		//����
		float stop_cos = (float)Math.cos(AngleRadians(stop_angle));
		//����
		float stop_sin = (float)Math.sin(AngleRadians(stop_angle));
		
		//��Բ�Ƕ���Բ�϶�Ӧ����ߺ�����
		float start_out_x_left = (float) (_out.left + out_r + (start_cos * out_r));
		//��Բ�Ƕ���Բ�϶�Ӧ�����������
		float start_out_y_left = (float) (_out.top + out_r + (start_sin * out_r));
		
		//��Բ�Ƕ���Բ�϶�Ӧ���ұߺ�����
		float stop_in_x_right = (float) (_in.left + in_r + (stop_cos * in_r));
		//��Բ�Ƕ���Բ�϶�Ӧ���ұ�������
		float stop_in_y_right = (float) (_in.top + in_r + (stop_sin * in_r));

		Path myPath = new Path();
		myPath.moveTo(start_out_x_left, start_out_y_left);
		myPath.addArc(_out, start_angle, angle-angle_space);
		myPath.lineTo(stop_in_x_right, stop_in_y_right);
		myPath.addArc(_in, stop_angle, -angle+angle_space);
		myPath.lineTo(start_out_x_left, start_out_y_left);
		
		/*���ý���ɫ*/
		long bg_color1 = Long.valueOf(("0x787878").replace("0x", "FF"),16);
		long bg_color2 = Long.valueOf(("0x787878").replace("0x", "FF"),16);
        Shader mShader=new LinearGradient(0,0,out_r*2,out_r*2,
                new int[]{(int)bg_color1,(int)bg_color2},
                null,Shader.TileMode.REPEAT);
        mypaint.setShader(mShader);
        mypaint.setAlpha(160);
        
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        canvas.drawPath(myPath, mypaint);
        if(_index%2==0){
        	 Paint textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);  
    	     textPaint.setTextSize(10); 
    	     textPaint.setAntiAlias(true);
    	     textPaint.setColor(Color.BLACK);
    	     textPaint.setStyle(Style.FILL_AND_STROKE);

    	     float _in_watch_r = (_in_watch.right-_in_watch.left)/2;
    	     float x = (float) (_in_watch.left + _in_watch_r + (start_cos * _in_watch_r));
    	     float baseY = (float) (_in_watch.top + _in_watch_r + (start_sin * _in_watch_r));
    	   //FontMetrics����  
    	     FontMetrics fontMetrics = textPaint.getFontMetrics(); 
    	     float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;  
    	     float offY = fontTotalHeight / 2 - fontMetrics.bottom;  
    	     float newY = baseY + offY;  
    	     int space_txt = 4;
    	     if(_index/2<10){
    	    	 space_txt = 2;
    	     }
    	     //canvas.drawCircle(x, baseY, 1, textPaint);
        	canvas.drawText(_index/2+"", x-space_txt, baseY+space_txt, textPaint);
        }
		currentPath = myPath;
		canvas.restore();
	}
}
