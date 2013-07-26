package app.rcbu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
public class Credentials extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayList<String> All_resp = new ArrayList();
		String total_resp ="";
		StrictMode.ThreadPolicy policy = new StrictMode.
		ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
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
        TextView textView1 = new TextView(this);
        textView1.setTextSize(10);
       
        textView1.setText("Connection Established!");
        setContentView(textView1);
        try {
			All_resp = get_user_activity();
			for (int i=0;i<All_resp.size();i++)
			{
				total_resp = total_resp + All_resp.get(i) + '\n';
		        
			}
			TextView textView = new TextView(this);
	        textView.setTextSize(20);
	        textView.setText(total_resp);
	        setContentView(textView);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	 ArrayList<String> get_user_activity() throws JSONException
	    {
		 ArrayList<String> All_resp = new ArrayList();
	    	HttpResponse response = null;
	    	try {   
	    			
	    			String Resp_Type = "";
	       	        HttpClient client = new DefaultHttpClient();
	    	        HttpGet request = new HttpGet();
	    	        request.setURI(new URI("http://curryord.drivesrvr.com/ORD,SYD,IAD,LON/v1.0/activity"));
	    	        request.setHeader("Content-Type", "application/json");
	    	        request.setHeader("X-Auth-Token", "8347f17f79124ccd96fe214bcb4817c6");
	    	        //request.setURI(new URI("https://www.google.com/"));
	    	        System.out.println("Request built");
	    	        response = client.execute(request);
	    	        //response = client.execute(request, localContext);
	        		System.out.println("Response: " + response.getStatusLine().toString());
	                HttpEntity entity = response.getEntity();

	                if (entity != null) {

	                    InputStream instream = entity.getContent();
	                    // String result= convertStreamToString(instream);
	                    InputStreamReader is = new InputStreamReader(instream);
	                    StringBuilder sb = new StringBuilder();
	                    BufferedReader br = new BufferedReader(is);
	                    String read = br.readLine();
	                    while(read != null)
	                    {
	                    	JSONArray blah = new JSONArray(read);
	                    	for (int i=0;i<5;i++)
	                    	{
	                    		JSONObject blah_blah = new JSONObject(blah.get(i).toString());
	                    		if(!blah_blah.get("Type").toString().equalsIgnoreCase("Backup") && !blah_blah.get("CurrentState").toString().equalsIgnoreCase("Completed"))
	                    				{
	                    					continue;
	                    				}
	                    		else
	                    		{
	                    			All_resp.add("Source_Machine:" + blah_blah.get("SourceMachineName").toString());
	                    			All_resp.add("Time of Activity:" + blah_blah.get("TimeOfActivity").toString());
	              
	                    		}
	                    	}
	                    	
	                    	System.out.println(read);
	                    	sb.append(read);
	                    	read = br.readLine();
	                    }
	                    // System.out.println("Final: " + sb.toString());
	                    instream.close();
	                }
	                else
	                {
	                	System.out.println("Entity was null");
	                }
	    	        
	    	        
	    	    } catch (URISyntaxException e) {
	    	        e.printStackTrace();
	    	    } catch (ClientProtocolException e) {
	    	        // TODO Auto-generated catch block
	    	        e.printStackTrace();
	    	    } catch (IOException e) {
	    	        // TODO Auto-generated catch block
	    	        e.printStackTrace();
	    	    }  
	    	return All_resp;
	    }
	    
	protected void sendJson(final String auth,final String username, final String password) {
        Thread t = new Thread() {

            public void run() {
                

                try {
                	Looper.prepare();
                	HttpParams myParams = new BasicHttpParams();
                	BasicHttpContext localContext = new BasicHttpContext();
                	HttpConnectionParams.setSoTimeout(myParams, 10000);
                	HttpConnectionParams.setConnectionTimeout(myParams, 10000);//For Preparing Message Pool for the child Thread
                    HttpClient client = new DefaultHttpClient(myParams);
                    HttpResponse response;
                    HttpPost post = new HttpPost("https://identity.api.rackspacecloud.com/v2.0/tokens");
                    StringEntity se = new StringEntity(auth);  
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
              

                    post.setEntity(se);
                    response = client.execute(post,localContext);
                    Log.i("Response Status Code", response.getStatusLine().toString());

                    /*Checking response */
                    if(response!=null){
                        InputStream in = response.getEntity().getContent();
                        try {

                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(in));
                            // do something useful with the response
                            String resp = reader.readLine().substring(0, 60);
//                            Log.i("Body",resp);
                            String token = resp.substring(26, 58);
                            Log.i("Body",token);
                              AuthToken.authtoken = token;
                            

                        }
                        catch(Exception e)
                        {
                        	
                        }

                    }

                } catch(Exception e) {
                    e.printStackTrace();
               
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


