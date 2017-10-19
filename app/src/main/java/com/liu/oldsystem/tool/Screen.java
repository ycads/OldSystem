package com.liu.oldsystem.tool;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

public class Screen {

 public int brightness;
 public int init;
 public void add(Activity activity){
	  brightness+=20;
	  if(brightness>=255){
		  brightness=255;
	  }
	  save(activity,brightness);
  }
  public Screen(Activity activity){
	  init=getScreenBrightness(activity);
	  brightness = init;
  }
  public void dre(Activity activity){
	  brightness-=20;
	  if(brightness<=20){
		  brightness=20;
	  }
		 
	  save(activity,brightness);
  }

	public  int getScreenBrightness(Activity activity) {
	    int value = 0;
	    ContentResolver cr = activity.getContentResolver();
	    try {
	        value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
	    } catch (SettingNotFoundException e) {
	        
	    }
	    return value;
	}

	public void save(Activity activity, int value){
		
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
	    lp.screenBrightness = value/255.0f;  
		activity.getWindow().setAttributes(lp);     
		Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);

	}
}
