package com.java.arobjviewer;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.dhbw.andarmodelviewer.R;

//hi there
//dfs
public class MainActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";
	ImageView logout ;
	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
  //TODO: 1. First function ... 
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		TextView gand = (TextView)findViewById(R.id.textView1);
		gand.setText(" Welcome "+ "\n " + " EXPERIENCE AUGMENTED REALITY");
        Button button = (Button)findViewById(R.id.button1);

		logout = (ImageView)findViewById(R.id.logout);
      //TODO: 2. Assign listerner to start button..
        logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final SharedPreferences checkLogin = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		    	SharedPreferences.Editor editor = checkLogin.edit();
		    	String userStr="";
				editor.putBoolean("status", false);
				editor.putString("username", "");
				editor.commit();
				startActivity(new Intent(getApplicationContext(), Login.class));
		    	finish();
			}
		});
		
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			//TODO: 3. Start button Clicked.....
			public void onClick(View v) {
				
				//load the selected internal file
				//TODO: 2 Start button clicked... Go to ModelChooser
				Intent intent = new Intent(MainActivity.this, ModelChooser.class);
	            intent.setAction(Intent.ACTION_VIEW);
	            startActivity(intent);
			}
		});
        
Button button1 = (Button)findViewById(R.id.aboutUs);
        
        button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//load the selected internal file
				//TODO: 1 About Us button clicked... Go to ModelChooser
				Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
	            startActivity(intent);
			}
		});
        
    }

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    
}
