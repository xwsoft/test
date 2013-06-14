package com.ritterliu.android;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class ActivityTestActivity extends Activity {
	private HourWatchScale[] hours = new HourWatchScale[48];
	private HourWatchScaleBg[] hours_bg = new HourWatchScaleBg[48];
	private int day = 0;
	private int selectedMark = -1;
	private int selectHour = -1;
	public boolean selectDispatched = false;
	private boolean inAddMode = false;
	private RelativeLayout rl_canvas;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.main2);
        
        Log.d("avtivity","onCreate()");
        
/*        LayoutInflater inflater = getLayoutInflater();    
        final View main = inflater.inflate(R.layout.main,null);
        final View main2 = inflater.inflate(R.layout.main2,null);
                
        Button btn1=(Button)main.findViewById(R.id.btn1);
        btn1.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("Activity","press btn1");
                setActivityView(main2);
            }

        });
        
        Button btn2=(Button)main2.findViewById(R.id.btn2);
        btn2.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("Activity","press btn2");
                setActivityView(main);
            }
        });*/
        //setContentView(new GameView(this));
        
        //long color_value = Long.valueOf("0xFF8800".replace("0x", "FF"),16);
        //setContentView(new HourInRing(this, 1,(int) color_value));
        setContentView(R.layout.canvas);

        rl_canvas = (RelativeLayout)this.findViewById(R.id.rl_canvas);
    
        long bg_color2 = Long.valueOf(("0xFFFFFF").replace("0x", "FF"),16);
        HourRoundBackGround hourRoundBackGround = new HourRoundBackGround(this, 1,(int) bg_color2);
        hourRoundBackGround.resize(rl_canvas.getLayoutParams().width-4,rl_canvas.getLayoutParams().height-4);
        //rl_canvas.addView(hourRoundBackGround);
        
        long bg_color = Long.valueOf(("0xDADADA").replace("0x", "FF"),16);
        for(int i=0;i<48;i++){
        	hours_bg[i] = new HourWatchScaleBg(this, i,(int) bg_color);
        	hours_bg[i].resize(rl_canvas.getLayoutParams().width,rl_canvas.getLayoutParams().height);
         	rl_canvas.addView(hours_bg[i]);
        }
        
 
        
        for(int i=0;i<48;i++){
        	long color_value = Long.valueOf(("0x5598CB").replace("0x", "FF"),16);
        	if(i<12){
        		color_value = Long.valueOf(("0xf2cf45").replace("0x", "FF"),16);
        	}else if(i>=12 && i<18){
        		color_value = Long.valueOf(("0xff6040").replace("0x", "FF"),16);
        	}else if(i>=18 && i<24){
        		color_value = Long.valueOf(("0xdd99d8").replace("0x", "FF"),16);
        	}else if(i>=24 && i<36){
        		color_value = Long.valueOf(("0xEEEEEE").replace("0x", "FF"),16);
        	}else if(i>=36 && i<42){
        		color_value = Long.valueOf(("0xfa6748").replace("0x", "FF"),16);
        	}else if(i>=42){
        		color_value = Long.valueOf(("0x5598cb").replace("0x", "FF"),16);
        	}
        	hours[i] = new HourWatchScale(this, i,(int) color_value);
        	hours[i].resize(rl_canvas.getLayoutParams().width,rl_canvas.getLayoutParams().height);
         	rl_canvas.addView(hours[i]);
        }
        rl_canvas.findViewById(R.id.btn_apply).bringToFront();
        rl_canvas.findViewById(R.id.btn_reset).bringToFront();
        
        rl_canvas.setOnTouchListener(new OnTouchListener() {
			boolean isZoom;
			private float oldDist;
			public int selectHour2 = -1;
			private PointF midPoint = new PointF();
			private int count = 0;
			private long first = 0;
			private long second = 0;
			private int firstMode = -1;
			private boolean inSplitter = false;
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Log.i("HoursRing onTouch start","arg1.getAction:"+arg1.getAction()+"	x:"+arg1.getX()+"	y:"+arg1.getY());
				int x = (int)arg1.getX();
				int y = (int)arg1.getY();
				if(arg1.getAction()==2){

					//mousePointer.setLocation(x, y);
				}
				switch (arg1.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					Log.i("switch","MotionEvent.ACTION_DOWN");
					break;
				case MotionEvent.ACTION_POINTER_UP:
					Log.i("switch","MotionEvent.ACTION_POINTER_UP");
					isZoom = false;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					Log.i("switch","MotionEvent.ACTION_POINTER_DOWN");
					oldDist = spacing(arg1);
					midPoint(midPoint, arg1);
					isZoom = true;
					break;
				case MotionEvent.ACTION_MOVE:
					Log.i("switch","MotionEvent.ACTION_MOVE");
					if (isZoom) {
						float newDist = spacing(arg1);
						if (newDist + 30 > oldDist) {
							isZoom = false;
						}
						if (newDist + 30 < oldDist) {
							isZoom = false;
						}
					}
					break;
				}
				
				
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					Log.i("Action","MotionEvent.ACTION_DOWN");
					selectedMark = -1;
					for (int i = 0; i < hours.length; i++) {
						if (hours[i].InArea(arg1.getX(), arg1.getY())) {
							selectHour = i;
							break;
						}
					}
					return true;
				} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
					Log.i("Action","MotionEvent.ACTION_UP");
					selectHour = -1;
					return false;
				} else if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
					if(selectHour!=-1){
						for (int i = 0; i < hours.length; i++) {
							if (hours[i].InArea(arg1.getX(), arg1.getY())) {
								Log.i("Action","MotionEvent.ACTION_MOVE");
								hours[i].ChangeColor(hours[selectHour].get_color());
								
								if(i>selectHour2){
									
								}
								selectHour2 = i;
								refresh();
								break;
							}
						}
						return true;
					}
				}
				return true;
			}
        });
        
        //setActivityView(new MyView(this));
    }
    private void refresh(){
    	//rl_canvas.removeAllViews();
    	for(int i=0;i<hours.length;i++){
    		rl_canvas.removeView(hours[i]);
    	}
    	for(int i=0;i<hours.length;i++){
    		rl_canvas.addView(hours[i]);
    	}
    	
    }
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
    public void setActivityView(View view)
    {
        this.setContentView(view);
    }
    
}