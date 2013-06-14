package com.ritterliu.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class Mark extends ImageView {
	private Bitmap _bitmap;
	private RectF _out = new RectF(25,70,215,260);
	private RectF _in = new RectF(25,70,215,260);
	private int _index = 0;
	private float w_delta;
	private float h_delta;
	
	public Mark(Context context, boolean zoom_out) {
		super(context);
		DisplayMetrics dm = new DisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		w_delta = (float)screenWidth/240f;
		h_delta = (float)screenHeight/400f;
		
		if(w_delta > h_delta){
			w_delta = h_delta;
		}else{
			h_delta = w_delta;
		}
		if(!zoom_out){
			 _out = new RectF(23*w_delta,113*h_delta,217*w_delta,307*h_delta);
			 _in = new RectF(56*w_delta,146*h_delta,184*w_delta,274*h_delta);
		}else{
			_out = new RectF(13*w_delta,58*h_delta,226*w_delta,271*h_delta);
			_in = new RectF(51*w_delta,96*h_delta,189*w_delta,234*h_delta);
		}
		float x_center = (_out.right-_out.left)/2 + _out.left;
		float screen_x_center = screenWidth/2;
		_out.left += screen_x_center-x_center;
		_out.right += screen_x_center-x_center;
	}
	
	public int endMode;

	public int GetIndex(){
		return _index;
	}
	
	public void SetBitMap(Bitmap bitmap){
		_bitmap = bitmap;
		
	}
	
	public PointF getEdgePoint(){
		PointF point = new PointF();
		float r_1 = (_out.right-_out.left)/2+15*w_delta;
		float start_1_x = (float) (_out.left -15*w_delta + r_1 + (Math.cos(Deg2Rad(15*_index/2f-90)) * r_1));
		float start_1_y = (float) (_out.top -15*w_delta+ r_1 + (Math.sin(Deg2Rad(15*_index/2f-90)) * r_1));
		point.set(start_1_x, start_1_y);
		return point;
	}
	
	public int GetNewPoint(float x,float y){
		for(int i = 0;i<48;i++){
			float r_1 = ((_out.right-_out.left)/2+(_in.right-_in.left)/2)/2;
			float start_1_x = (float) (_out.left + (_in.left - _out.left)/2 + r_1 + (Math.cos(Deg2Rad(15*i/2f-90)) * r_1));
			float start_1_y = (float) (_out.top +(_in.left - _out.left)/2 + r_1 + (Math.sin(Deg2Rad(15*i/2f-90)) * r_1));
			if(Math.abs(x - start_1_x)<10*w_delta && Math.abs(y - start_1_y)<10*h_delta){
				return i;
			}
		}
		return -1;
	}
	
	public boolean InArea(float x,float y){
		float r_1 = ((_out.right-_out.left)/2+(_in.right-_in.left)/2)/2;
		float start_1_x = (float) (_out.left + (_in.left - _out.left)/2 + r_1 + (Math.cos(Deg2Rad(15*_index/2f-90)) * r_1));
		float start_1_y = (float) (_out.top +(_in.left - _out.left)/2 + r_1 + (Math.sin(Deg2Rad(15*_index/2f-90)) * r_1));
		if(Math.abs(x - start_1_x)<10*w_delta && Math.abs(y - start_1_y)<10*w_delta){
			return true;
		}else{
			return false;
		}
	}
	
	private static float Deg2Rad(float deg){
		return (float) (3.14159265358979323846 * deg / 180.0);
	}
	
	public void SetPoint(int index){
		_index = index;
	}
	
	public int getPoint(){
		return _index;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		Paint mypaint  = new Paint();
		canvas.rotate(15*(_index)/2f, _out.left+(_out.right-_out.left)/2, _out.top+(_out.bottom-_out.top)/2);
		canvas.drawBitmap(_bitmap, _out.left+(_out.right-_out.left)/2-7f,_out.top-20, mypaint);
		
		/* 设置画布的颜色 */  
        canvas.drawColor(Color.BLACK);  
  
        /* 设置取消锯齿效果 */  
        mypaint.setAntiAlias(true);  
  
        /* 设置裁剪区域 */  
        canvas.clipRect(10, 10, 280, 260);  
  
        /* 线锁定画布 */  
        canvas.save();  
        /* 旋转画布 */  
        canvas.rotate(45.0f);  
  
        /* 设置颜色及绘制矩形 */  
        mypaint.setColor(Color.RED);  
        canvas.drawRect(new Rect(15, 15, 140, 70), mypaint);  
  
        /* 解除画布的锁定 */  
        canvas.restore();  
  
        /* 设置颜色及绘制另一个矩形 */  
        mypaint.setColor(Color.GREEN);  
        canvas.drawRect(new Rect(150, 75, 260, 120), mypaint);   
	}
}
