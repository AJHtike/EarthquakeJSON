package android.Biggest_Earthquake_Task2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity
{
    
    //this method is reading the JSON file path which construct ReadQuakeJSONFeedTask
    public String readEarthQuakeFeed(String URL) 
    {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statuscode = statusLine.getStatusCode();
            
            if(statuscode == 200)
            {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine())!= null)
                {
                    stringBuilder.append(line);
                }
                inputStream.close();
                
            } else {
                Log.d("JSON","Failed to download file");
            }
        } 
        catch (Exception e)
        {
            Log.d("readEarthQuakeFeed", e.getLocalizedMessage());
        }
        return stringBuilder.toString();
    }
    
    //this method is making connection between other two methods:readEarthQuakeFeed & OnPostExecute 
    private class ReadQuakeJSONFeedTask extends AsyncTask <String, Void, String>
    {
        protected String doInBackground(String... urls)
        {
            
                return readEarthQuakeFeed(urls[0]);
            
        }
        
        protected void onPostExecute(String result) // this method calling from btnView
        {
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray arr = new JSONArray(jsonObject.getString("earthquakes"));

            for (int a = 0; a < arr.length(); a++) {
                jsonObject = arr.getJSONObject(a);
                
                String location = jsonObject.getString("src");
                String magnitude = jsonObject.getString("magnitude");
                String datetime = jsonObject.getString("datetime");
                
            //Before set the text to EditText, to allocate ID by id name from main.xml
                EditText myLocation = (EditText) findViewById(R.id.txtLocation);
                EditText myMagnitude = (EditText) findViewById(R.id.txtMagnitude);
                EditText myDatetime = (EditText) findViewById(R.id.txtDatetime);
               //test toast message  
//                Toast.makeText(getBaseContext(),location + "-" + magnitude + "-" + datetime, Toast.LENGTH_SHORT).show();
               
//Assign the value from the array to setText in the EditText
                myLocation.setText(location);
                myMagnitude.setText(magnitude);
                myDatetime.setText(datetime);
                
            }
            }
            catch (Exception e)
            {
                Log.d("ReadQuakeJSONFeedTask", e.getLocalizedMessage());
            }
        }
    }
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void btnView(View view)
    {
        EditText txtNorth = (EditText) findViewById(R.id.txtNorth);
        EditText txtWest = (EditText) findViewById(R.id.txtWest);
        EditText txtEast = (EditText) findViewById(R.id.txtEast);
        EditText txtSouth = (EditText) findViewById(R.id.txtSouth);
        
        //this line is getting value from user inputs and passing as a url as JSON services
        new ReadQuakeJSONFeedTask().execute("http://api.geonames.org/earthquakesJSON?north=" + txtNorth.getEditableText().toString() + "&south=" + txtSouth.getEditableText().toString() + "&east=" + txtEast.getEditableText().toString() +"&west=" + txtWest.getEditableText().toString() + "&username=alexhtike");
        
        
        
       
        
    }
    
    public void btnReset(View view)
    {
        
        EditText txtNorth = (EditText) findViewById(R.id.txtNorth);
        EditText txtWest = (EditText) findViewById(R.id.txtWest);
        EditText txtEast = (EditText) findViewById(R.id.txtEast);
        EditText txtSouth = (EditText) findViewById(R.id.txtSouth);
        
        EditText myLocation = (EditText) findViewById(R.id.txtLocation);
        EditText myMagnitude = (EditText) findViewById(R.id.txtMagnitude);
        EditText myDatetime = (EditText) findViewById(R.id.txtDatetime);
        
        txtNorth.setText("");
        txtWest.setText("");
        txtEast.setText("");
        txtSouth.setText("");
        myLocation.setText("");
        myMagnitude.setText("");
        myDatetime.setText("");
    }
    
}
