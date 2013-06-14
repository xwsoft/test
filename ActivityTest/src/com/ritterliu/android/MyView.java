package com.ritterliu.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyView extends ViewGroup {
	
	private ImageView mask;
	private static Bitmap bitmap_mask;
	public  RectF _out = new RectF(32,70,287,325);
	public  RectF _in = new RectF(77,115,242,280);
	public MyView(Context context) {
		super(context);
		if (bitmap_mask == null) {
			bitmap_mask = BitmapFactory.decodeResource(getResources(),R.drawable.mask);
		}
		mask = new ImageView(context);
		mask.setImageBitmap(bitmap_mask);
		this.addView(mask);
		
		Mark mark = new Mark(context, true);
		mark.SetPoint(23);
		mark.setVisibility(View.VISIBLE);
		this.addView(mark);
		
        long color_value = Long.valueOf("0xFF8800".replace("0x", "FF"),16);
        this.addView(new HourWatchScale(context, 1,(int) color_value));
		//this.addView(new GameView(context));
	}

/*	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		// 去锯齿
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		
		//paint.setStyle(Paint.Style.FILL);
		//paint.setColor(Color.RED);
		// 设置笔触宽度
		//paint.setStrokeWidth(2);
		// 绘制圆角矩形
		//RectF rectF3 = new RectF(100, 200, 150, 240);
		//canvas.drawRoundRect(rectF3,34, 80, paint);	
		
		canvas.drawCircle(130, 130, 125, paint);
		canvas.drawCircle(130, 130, 80, paint);
		
		

		
		//canvas.drawRoundRect(_out,0, 0, paint);	
		//canvas.drawRoundRect(_in,34, 80, paint);	
		//public  RectF _out = new RectF(32,70,287,325);
		//public  RectF _in = new RectF(77,115,242,280);
		Paint mypaint = new Paint();
		mypaint.setColor(Color.GREEN);
		mypaint.setStyle(Style.FILL_AND_STROKE);
		int _index = 1;
		Path myPath = new Path();
		myPath.moveTo(160, 70);
		myPath.addArc(_out, 15*(_index-1)/2f-90, 15/2f);
		myPath.lineTo(170, 115);
		myPath.addArc(_in, 15*(_index)/2f-90, -15/2f);
		myPath.lineTo(160, 70);
		canvas.drawPath(myPath, mypaint);
			
		
		// 绘制圆形
		canvas.drawCircle(10, 100, 80, paint);
		// 绘制方形
		canvas.drawRect(10, 80, 70, 140, paint);
		// 绘制矩形
		canvas.drawRect(10, 150, 70, 190, paint);
		// 绘制圆角矩形
		RectF rectF1 = new RectF(10, 200, 70, 230);
		canvas.drawRoundRect(rectF1, 15, 15, paint);
		// 绘制椭圆
		RectF rectF2 = new RectF(10, 240, 70, 270);
		

		
		canvas.drawOval(rectF2, paint);	
		// 定义一个path对象，封闭成一个三角形
		Path path1 = new Path();
		path1.moveTo(10, 340);
		path1.lineTo(70, 340);
		path1.lineTo(40, 290);
		path1.close();
		// 根据path进行绘制，绘制三角形
		canvas.drawPath(path1, paint);
		// 定义一个path对象，封闭成一个五角星
		Path path2 = new Path();
		path2.moveTo(26, 360);
		path2.lineTo(54, 360);
		path2.lineTo(70, 392);
		path2.lineTo(40, 420);
		path2.lineTo(10, 392);
		path2.close();
		// 根据path进行绘制，绘制五角星
		canvas.drawPath(path2, paint);

		// 设置填充风格后绘制

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.RED);
		canvas.drawCircle(120, 40, 30, paint);
		// 绘制方形
		canvas.drawRect(90, 80, 150, 140, paint);
		// 绘制矩形
		canvas.drawRect(90, 150, 150, 190, paint);
		// 绘制圆角矩形
		RectF rectF3 = new RectF(90, 200, 150, 230);
		canvas.drawRoundRect(rectF3, 15, 15, paint);
		// 绘制椭圆
		RectF rectF4 = new RectF(90, 240, 150, 270);
		canvas.drawOval(rectF4, paint);
		// 定义一个path对象，封闭成一个三角形
		Path path3 = new Path();
		path3.moveTo(90, 340);
		path3.lineTo(150, 340);
		path3.lineTo(120, 290);
		path3.close();
		// 根据path进行绘制，绘制三角形
		canvas.drawPath(path3, paint);
		// 定义一个path对象，封闭成一个五角星
		Path path4 = new Path();
		path4.moveTo(106, 360);
		path4.lineTo(134, 360);
		path4.lineTo(150, 392);
		path4.lineTo(120, 420);
		path4.lineTo(90, 392);
		path4.close();
		// 根据path进行绘制，绘制五角星
		canvas.drawPath(path4, paint);

		Shader shader = new LinearGradient(0, 0, 40, 60, new int[] {
				Color.BLACK, Color.BLUE, Color.GRAY }, null,
				Shader.TileMode.REPEAT);
		paint.setShader(shader);
	}*/
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int childCount = getChildCount();
		DisplayMetrics dm = new DisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}
			child.layout(0, 0, screenWidth, screenHeight);
		}
		float screen_x_center = screenWidth / 2;
		float w_delta = (float) screenWidth / 240f;
		float h_delta = (float) screenHeight / 400f;
		if (w_delta > h_delta) {
			w_delta = h_delta;
		} else {
			h_delta = w_delta;
		}

			float x_center_out = (230 * w_delta - 10 * w_delta) / 2 + 10 * w_delta;
			float delta = screen_x_center - x_center_out;
			mask.layout((int) (10 * w_delta + delta), (int) (55 * w_delta),
					(int) (230 * w_delta + delta), (int) (275 * w_delta));

	}

}