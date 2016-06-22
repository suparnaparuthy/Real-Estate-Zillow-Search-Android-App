package edu8557.usc.cs_server.hw9;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends Activity {

    private Context context;
    ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();
    ListView lv ;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String array_spinner[];

        array_spinner=new String[55];
        array_spinner[0] = "Choose State";
        array_spinner[1] = "AK";
        array_spinner[2] = "AL";
        array_spinner[3] = "AR";
        array_spinner[4] = "AZ";
        array_spinner[5] = "CA";

        array_spinner[6]="CO";
        array_spinner[7]="CT";
        array_spinner[8]="DC";
        array_spinner[9]="DE";

        array_spinner[10]="FL";
        array_spinner[11]="GA";
        array_spinner[12]="HI";
        array_spinner[13]="IA";

        array_spinner[14]="ID";
        array_spinner[15]="IL";
        array_spinner[16]="IN";
        array_spinner[17]="KS";

        array_spinner[18]="KY";
        array_spinner[19]="LA";
        array_spinner[20]="MA";
        array_spinner[21]="MD";

        array_spinner[22]="ME";
        array_spinner[23]="MI";
        array_spinner[24]="MN";
        array_spinner[25]="MO";

        array_spinner[26]="MS";
        array_spinner[27]="MT";
        array_spinner[28]="NC";
        array_spinner[29]="ND";

        array_spinner[30]="NE";
        array_spinner[31]="NH";
        array_spinner[32]="NJ";
        array_spinner[33]="NM";

        array_spinner[34]="NV";
        array_spinner[35]="NY";
        array_spinner[36]="OH";
        array_spinner[37]="OK";
        array_spinner[38]="OR";
        array_spinner[39]="PA";
        array_spinner[40]="RI";
        array_spinner[41]="SC";

        array_spinner[42]="SD";
        array_spinner[43]="TN";
        array_spinner[44]="TX";
        array_spinner[45]="UT";

        array_spinner[46]="VA";
        array_spinner[47]="VT";
        array_spinner[48]="WA";
        array_spinner[49]="WI";

        array_spinner[50]="WV";
        array_spinner[51]="WY";


        Spinner s = (Spinner) findViewById(R.id.state);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);



        String MY_URL_STRING = "http://www.zillow.com/widgets/GetVersionedResource.htm?path=/static/logos/Zillowlogo_150x40_rounded.gif";
        new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(MY_URL_STRING);
        // new ProgressTask(MainActivity.this).execute();
       //ImageView img = (ImageView) findViewById(R.id.imageView2);
        //img.setImageResource(R.drawable.rounded);

        EditText streetTo = (EditText)findViewById(R.id.street);
        streetTo.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                TextView streeterrorEditText = (TextView) findViewById(R.id.streeterror);
                streeterrorEditText.setText("");
            }

        });

        EditText cityTo = (EditText)findViewById(R.id.city);
        cityTo.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                TextView cityerrorEditText = (TextView) findViewById(R.id.cityerror);
                cityerrorEditText.setText("");
            }

        });


       final Spinner stateEditText = (Spinner) findViewById(R.id.state);


        stateEditText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                 String stateTo = stateEditText.getSelectedItem().toString().trim();

                if(stateTo != "Choose State")
               {
                   TextView stateerrorEditText = (TextView) findViewById(R.id.stateerror);
                   stateerrorEditText.setText("");
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public class ProgressTask extends AsyncTask<String,Void , JSONArray> {
        private ProgressDialog dialog;
        private Activity activity;


        JSONArray json;

        Bundle bundle = new Bundle();

        public ProgressTask()
        {
        }

        private Context context;

        String retcode;

        protected void onPreExecute()
        {
        }


        @Override
        protected void onPostExecute(JSONArray success) {


            try {
                retcode=json.getString(16);
            }
            catch (JSONException e) { e.printStackTrace(); }

            int ret = Integer.parseInt(retcode);
            if( ret == 0) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("jsonArray", json.toString());
                startActivity(intent);
            }

            else
            {

              //  TextView textView =(TextView)findViewById(R.id.nomatch);
                TextView textView =(TextView)findViewById(R.id.discla);

                String text = "No exact match found -- Verify that the given address is correct.";

                textView.setText(Html.fromHtml(text));

            }
        }

        protected JSONArray doInBackground(final String... args) {
            JSONParser jParser = new JSONParser();
            json = jParser.getJSONFromUrl(url);

            return json;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void submit(View view) {

        EditText streetEditText = (EditText) findViewById(R.id.street);
        String street;
        street = streetEditText.getText().toString();

        EditText cityEditText = (EditText) findViewById(R.id.city);
        String city;
        city = cityEditText.getText().toString();


        Spinner stateEditText = (Spinner) findViewById(R.id.state);
        String state = stateEditText.getSelectedItem().toString().trim();


        if((!street.matches("")) && (!city.matches("")) && (state != "Choose State"))
        {
            TextView streeterrorEditText = (TextView) findViewById(R.id.streeterror);
            streeterrorEditText.setText("");

            TextView cityerrorEditText = (TextView) findViewById(R.id.cityerror);
            cityerrorEditText.setText("");

            TextView stateerrorEditText = (TextView) findViewById(R.id.stateerror);
            stateerrorEditText.setText("");

            String street1 =  street.replaceAll("\\s+","%20");
            String city1 = city.replaceAll("\\s+","%20");

            url = "http://appnov-env.elasticbeanstalk.com/?streetInput="+street1+"&cityInput="+city1+"&stateInput="+state;


            new ProgressTask().execute();

            return;

        }

        else {
            if (street.matches("")) {


                TextView streeterrorEditText = (TextView) findViewById(R.id.streeterror);
                streeterrorEditText.setText("This field is required");
                // Toast.makeText(this, "This field is required.", Toast.LENGTH_SHORT).show();

                //usernameEditText.setError("This field is required.");

            }

            else{
                TextView streeterrorEditText = (TextView) findViewById(R.id.streeterror);
                streeterrorEditText.setText("");

            }

            if (city.matches("")) {


                TextView cityerrorEditText = (TextView) findViewById(R.id.cityerror);
                cityerrorEditText.setText("This field is required");
            }

            else{


                TextView cityerrorEditText = (TextView) findViewById(R.id.cityerror);
                cityerrorEditText.setText("");
            }


            if (state == "Choose State") {

                TextView stateerrorEditText = (TextView) findViewById(R.id.stateerror);
                stateerrorEditText.setText("This field is required");

            }

            else {
                TextView stateerrorEditText = (TextView) findViewById(R.id.stateerror);
                stateerrorEditText.setText("");
            }

            return;
        }
    }


}


class JSONParser {
    static InputStream iStream = null;
    static JSONArray jarray = null;
    static String json = "";

    public JSONParser() { }

    public JSONArray getJSONFromUrl(String url) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }

            else {
                Log.e("==>", "Failed to download file");
            }
        }

        catch (ClientProtocolException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject res;

        JSONArray jsonArray = new JSONArray();

        try {
           // jarray = new JSONArray( builder.toString());
            res = new JSONObject(builder.toString());
            Iterator x = res.keys();

            while (x.hasNext()){
                String key = (String) x.next();
                jsonArray.put(res.get(key));
            }
        }


        catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jsonArray;
    }
}


class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
