package com.java.arobjviewer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.dhbw.andarmodelviewer.R;

public class Login extends Activity {

	Button btn_login;
	EditText et_user;
	String Loggeduser = "";
	TextView forgetPassword ;
	EditText password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Log.d("Login page", "++ON CREATE++");

		final SharedPreferences checkLogin = getSharedPreferences(
				MainActivity.PREFS_NAME, 0);
		boolean userHasLogged = checkLogin.getBoolean("status", false);

		if ((userHasLogged)) {
			Log.d("Login page", "Already logged in now opening main activity");
			Intent intent = new Intent(getBaseContext(), MainActivity.class);
			startActivity(intent);
			finish();
		}

		btn_login = (Button) findViewById(R.id.buttonSubmit);
		et_user = (EditText) findViewById(R.id.editText2);
		forgetPassword = (TextView)findViewById(R.id.forgetpassword);
		password = (EditText)findViewById(R.id.pass);
		InputFilter alphaNumericFilter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				for (int k = start; k < end; k++) {
					if (!Character.isLetterOrDigit(source.charAt(k))
							|| Character.isWhitespace(source.charAt(k))) {
						return "";
					}
				}
				return null;
			}
		};
		et_user.setFilters(new InputFilter[]{ alphaNumericFilter});
		
		btn_login.setOnClickListener(new OnClickListener(){
			SharedPreferences.Editor editor = checkLogin.edit();
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Loggeduser = et_user.getText().toString();
					if(!Loggeduser.equals("")){
						if(Loggeduser.equalsIgnoreCase("KASHISH") && password.getText().toString().equals("kashish") ){
							editor.putBoolean("status", true);
							editor.putString("username", Loggeduser);
							editor.commit();
							
							Intent intent = new Intent(getBaseContext(), MainActivity.class);
							startActivity(intent);
							finish();
						}else{
							Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_LONG).show();
							editor.putBoolean("status", false);
							editor.commit();
						}
						
					}else{
						Toast.makeText(getApplicationContext(),"Enter Valid User ID",Toast.LENGTH_LONG).show();
						editor.putBoolean("status", false);
						editor.commit();
					}
				}});

		
		forgetPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getBaseContext(), ForgetPass.class);
				startActivity(intent);
			}
		});
	}

}
