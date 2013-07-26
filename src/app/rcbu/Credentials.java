package app.rcbu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Credentials extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credentials);
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        String message1 = intent.getStringExtra(MainActivity.LOGIN);
        String message2 = intent.getStringExtra(MainActivity.PASSWORD);
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(30);
        textView.setText("LOGIN :"+message1+"\nPASSWORD:"+message2);
        JSONObject user_pass = new JSONObject();
        JSONObject passwordCredentials = new JSONObject();
        JSONObject auth = new JSONObject();
        try {
			user_pass.put("username", message1);
			user_pass.put("password", message2);
			passwordCredentials.put("passwordCredentials", user_pass);
			auth.put("auth", passwordCredentials);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Log.i("Request",auth.toString());
        sendJson(auth.toString(),message1,message2);
      
        setContentView(textView);
    }
	protected void sendJson(final String auth,final String username, final String password) {
        Thread t = new Thread() {

            public void run() {
                
//                JSONObject json = new JSONObject();

                try {
                	Looper.prepare();
                	HttpParams myParams = new BasicHttpParams();
                	BasicHttpContext localContext = new BasicHttpContext();
                	HttpConnectionParams.setSoTimeout(myParams, 10000);
                	HttpConnectionParams.setConnectionTimeout(myParams, 10000);//For Preparing Message Pool for the child Thread
                    HttpClient client = new DefaultHttpClient(myParams);
//                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                    HttpResponse response;
                    HttpPost post = new HttpPost("https://identity.api.rackspacecloud.com/v2.0/tokens");
                    StringEntity se = new StringEntity(auth);  
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
              
//                    UsernamePasswordCredentials defaultcreds = new UsernamePasswordCredentials("username", "password");
//                    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username,password);
//                    client.getState().setCredentials(AuthScope.ANY, credentials);
                    post.setEntity(se);
                    response = client.execute(post,localContext);
                    Log.i("Response Status Code", response.getStatusLine().toString());
//                    HttpEntity entity = response.getEntity();
//                    response.getEntity().
//                    Log.i("Response",response.getEntity().getContent().toString());
                    /*Checking response */
                    if(response!=null){
                        InputStream in = response.getEntity().getContent();
                        try {

                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(in));
                            // do something useful with the response
                            Log.i("Body",reader.readLine());
//                            JSONObject response_body = new JSONObject(reader.readLine());
//                            JSONObject response_access = new JSONObject();
//                            JSONObject response_token = new JSONObject();
//                            JSONObject response_id = new JSONObject();
//                            response_access = (JSONObject) response_body.get("access");
//                            response_token = (JSONObject) response_access.get("token");
//                            response_id = (JSONObject) response_token.get("id");
//                            Log.i("Body",response_body.toString());
                            

                        }
                        catch(Exception e)
                        {
                        	
                        }
//                        Log.i("Response",in.toString());//Get the data in the entity
                    }

                } catch(Exception e) {
                    e.printStackTrace();
//                    onCreateDialog("Error", "Cannot Estabilish Connection");
                }

                Looper.loop(); //Loop in the message queue
            }
        };

        t.start();      
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.credentials, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}


