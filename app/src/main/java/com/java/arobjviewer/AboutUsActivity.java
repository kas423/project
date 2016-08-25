package com.java.arobjviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.dhbw.andarmodelviewer.R;

public class AboutUsActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);
    
        TextView aboutUsview =(TextView)findViewById(R.id.textView1);
        TextView kashish = (TextView)findViewById(R.id.kashish);
        Button button = (Button)findViewById(R.id.back);
        
        kashish.setText("KASHISH MODI \n 146390316023");
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//load the selected internal file
				Intent intent = new Intent(AboutUsActivity.this, MainActivity.class);
	            startActivity(intent);
			}
		});
        
    }

    



}
