package com.java.arobjviewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.dhbw.andarmodelviewer.R;

public class ForgetPass extends Activity {
	
	EditText secPass;
	Button submit;
	TextView hint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotpassword);
		
		secPass = (EditText)findViewById(R.id.secPass);
		submit = (Button)findViewById(R.id.button1);
		hint = (TextView)findViewById(R.id.textView3);
		
		submit.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(secPass.getText().toString().equalsIgnoreCase("9033883188")){
					hint.setText("Password: 'kashish' ");
					hint.setVisibility(View.VISIBLE);;
				}else{
					hint.setText("incorrect ans");
					hint.setVisibility(View.VISIBLE);;
				}
			}
		});
	}

}
