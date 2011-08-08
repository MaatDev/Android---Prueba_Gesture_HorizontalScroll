package com.exa.ges;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
//http://blog.global-eng.co.jp/android/2011/02/11/gesturedetector%E3%81%A7%E3%82%B9%E3%82%AF%E3%83%AD%E3%83%BC%E3%83%AB%E3%81%AE%E5%88%B6%E5%BE%A1/
public class Prueba_GestureActivity extends Activity {
	
	private final String TAG = getClass().getSimpleName();
	
	private int page = 0;
	private int pageCount = 0;
	private HorizontalScrollView hsv;
	private LinearLayout ll_images;
	
	//Tamaño de pantalla
	int displayWidth;
	int displayHeight;
	//Estados del scroll
	
	private final int SCROLL_NONE = 0;
	private final int SCROLL_RIGHT = 1;
	private final int SCROLL_LEFT = 2;
	private int slideLimitFlg = SCROLL_NONE; 
	
	//Listener
	private OnGestureListener listener;
	
	//Checkeo de scroll
	
	boolean scrollFlg = false;
	
	//Arreglo de imagenes
		private int [] images = {R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,
				R.drawable.a5,R.drawable.a6,R.drawable.a7};
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        this.hsv = (HorizontalScrollView) findViewById(R.id.hsv_images);
        
        this.setCustomScrollView();
        
        //Colocar las imagenes en el LinearLayout
        
        this.ll_images = (LinearLayout) findViewById(R.id.ll_addImages);
        
        int idCounter = 1;
        for(int i: images){
        	ImageView img = new ImageView(this);
        	img.setLayoutParams(new LinearLayout.LayoutParams(displayWidth,displayWidth));
        	img.setImageResource(i);
        	img.setId(idCounter++);        	
        	ll_images.addView(img);
        }
        
               
        
    }
    
    //Función para customizar el HorizontalScrollView
    
    public HorizontalScrollView setCustomScrollView(){
    	
    	 WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    	 Display display = wm.getDefaultDisplay();
    	
    	 this.displayHeight = display.getHeight();
    	 this.displayWidth = display.getWidth();
    	 
    	 //Definir el tamaño del arreglo de las imagenes
    	 this.pageCount = images.length-1;
    	 
    	    	
    	this.listener = new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				
				Log.v(TAG, "Estoy en "+TAG+": onScroll");
				
				scrollFlg = true;
				
				//Cálculo de rango 
				
				int rangeX = (int) (event1.getRawX() - event2.getRawX());
				
				//Porcentaje de la pantalla
				
				double displayPercentage = 0.6;
				
				if(rangeX < -displayWidth * displayPercentage){
					
					//Slide hacia derecha
					
					slideLimitFlg = SCROLL_RIGHT;
					
				}else if(rangeX > displayWidth * displayPercentage){
					
					//Slide hacia izquierda
					
					slideLimitFlg = SCROLL_LEFT;
					
				}else{
					
					//Nada
					
					slideLimitFlg = SCROLL_NONE;
					
				}
				
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
					float velocityY) {
				// TODO Auto-generated method stub
				
				Log.v(TAG, "Estoy en "+TAG+": onFling");
				
				if(slideLimitFlg == SCROLL_NONE){
					if(velocityX < 0){
						
						//Fling hacia izquierda
						
						setPage(true); Log.v(TAG, "Fling hacia izquierda");
						
					}else if(velocityX > 0){
						
						//Fling hacia derecha
						
						setPage(false); Log.v(TAG, "Fling hacia derecha");
						
					}
					
					hsv.scrollTo(page * displayWidth, displayHeight);
					
				}
				
				return true;
			}
			
			@Override
			public boolean onDown(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		};
    	
    	final GestureDetector gDetector = new GestureDetector(getApplicationContext(), listener);
    	
    	//Colocar el listener para capturar toques en la pantalla
    	
    	hsv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				
				Log.v(TAG, "Estoy en "+TAG+": onTouch");
				
				boolean result = gDetector.onTouchEvent(event);
				
				if((event.getAction() == MotionEvent.ACTION_UP) && scrollFlg){
					switch(slideLimitFlg){
					
						case SCROLL_NONE:
							
							//No hacer nada
							
							break;
						
						case SCROLL_LEFT:
							setPage(true);
							break;
						case SCROLL_RIGHT:
							setPage(false);
							break;
																
					}
					
					hsv.scrollTo(page * displayWidth, displayHeight);
					
				}
				return result;
			
			}
		});
    	
    	return hsv;
    }
    
    private void setPage(boolean check){
    	Log.v(TAG, "Estoy en "+TAG+": setPage");
    	
    	if(check){
    		if(page<pageCount){
    			page++;
    		}
    	}else{
    		if(page > 0){
    			page--;
    		}
    	}
    	
    }
    
    
    
}