package app.rcbu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {
	 public final static String LOGIN = "app.rcbu.login";
	 public final static String PASSWORD = "app.rcbu.password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    public void LoginCredentials(View view) {
    	Intent intent = new Intent(this, Credentials.class);
    	EditText editText1 = (EditText) findViewById(R.id.editText1);
    	EditText editText2 = (EditText) findViewById(R.id.editText2);
    	String message1 = editText1.getText().toString();
    	String message2 = editText2.getText().toString();
    	intent.putExtra(LOGIN, message1);
    	intent.putExtra(PASSWORD, message2);
    	startActivity(intent);
        // Do something in response to button
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
