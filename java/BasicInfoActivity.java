package edu8557.usc.cs_server.hw9;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.internal.Utility;
import com.facebook.widget.WebDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BasicInfoActivity extends Activity {

    String retcode;
    String imgn;
    String imgp;
    String street;
    String city;
    String state;
    String zipcode;
    String homedetails;
    String type;
    String price;
    String year;
    String soldDate;
    String lot;
    String dat1;
    String amt;
    String finArea;
    String val;
    String bath;
    String low1;
    String high1;
    String dat2;
    String bed;
    String rentamt;
    String assYear;
    String val2;
    String ass;
    String low2;
    String high2;
    String chart1;
    String chart5;
    String chart10;
    String negr;
    String negz;

    private Session.StatusCallback sessionStatusCallback;
    private Session currentSession;

    private Button login;
    private Button logout;
    private Button publishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);

      //  setContentView(R.id.scroll);

        Intent intent2 = getIntent();
        String jsonArray = intent2.getStringExtra("jsonArray1");

        try {
            json = new JSONArray(jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        parseResults();
        showResults();


        // create instace for sessionStatusCallback
        sessionStatusCallback = new Session.StatusCallback() {

            @Override
            public void call(Session session, SessionState state,
                             Exception exception) {
                onSessionStateChange(session, state, exception);

            }
        };


        // publish button
        publishButton = (Button) findViewById(R.id.fshare);
        publishButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Session session = Session.getActiveSession();
                if (session == null) {
                    connectToFB();

                }

                else if (session.isOpened()){

                    publishStory();
                }


            }
        });

    }

    /**
     * Connects the user to facebook
     */

    public void connectToFB() {


            final List<String> permissions = new ArrayList<String>();
            permissions.add("publish_stream");

                 currentSession = new Session.Builder(this).build();
                 currentSession.addCallback(sessionStatusCallback);

                   Session.OpenRequest openRequest = new Session.OpenRequest(BasicInfoActivity.this);
                  openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);

                    openRequest.setRequestCode(Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE);
                    openRequest.setPermissions(permissions);
                    currentSession.openForPublish(openRequest);


        if(currentSession.isOpened()){
            publishStory();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentSession != null) {
            currentSession
                    .onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {
        if (session != currentSession) {
            return;
        }

        if (state.isOpened()) {
            // Log in just happened.
            Toast.makeText(getApplicationContext(), "session opened",Toast.LENGTH_SHORT).show();
            publishStory();
        } else if (state.isClosed()) {
            // Log out just happened. Update the UI.
            Toast.makeText(getApplicationContext(), "session closed",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Publishes story on the logged user's wall
     */
    public void publishStory() {

        int neg = Integer.parseInt(negz);
        if(neg == 1)
            val= "-" + val;
        else
         val = "+"+val;

        String nm = street+", "+city+", "+state+"-"+zipcode;
        String desc = "Last Sold Price: "+price+", 30 Days Overall Change: "+val ;
        Bundle params = new Bundle();
        params.putString("name", nm);
        params.putString("caption", "Property Information from Zillow.com");
        params.putString("description", desc);
        params.putString("link", homedetails);
        params.putString("picture",chart1);

      //  Session session = Session.getActiveSession();

      //  currentSession = Session.getActiveSession();
        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,currentSession, params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                // do some stuff
                                // User clicked the Cancel button
                                Toast.makeText(getApplicationContext(),
                                        "Posted Story, ID: "+postId, Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                // User clicked the Cancel button
                                Toast.makeText(getApplicationContext(),
                                        "Post cancelled", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Toast.makeText(getApplicationContext(),
                                    "Post cancelled", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            // Generic, ex: network error
                            Toast.makeText(getApplicationContext(),
                                    "Error posting story", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                }).setFrom("").build();
        feedDialog.show();

    }



    JSONArray json;


    private void parseResults() {

        try {
            val =  json.getString(0);
            finArea =  json.getString(1);
            rentamt =  json.getString(2);
            street =  json.getString(3);
            state =  json.getString(4);
            ass =  json.getString(5);
            type =  json.getString(6);
            homedetails =  json.getString(7);
            city =  json.getString(8);
            soldDate =  json.getString(9);
            dat1 =  json.getString(10);
            dat2 =  json.getString(11);
            bath =  json.getString(12);
            year =  json.getString(13);
            val2 =  json.getString(14);
            chart10 =  json.getString(15);
            retcode =  json.getString(16);
            bed =  json.getString(17);
            chart5 =  json.getString(18);
            zipcode =  json.getString(19);
            high1 =  json.getString(20);
            high2 =  json.getString(21);
            amt =  json.getString(22);
            price =  json.getString(23);
            assYear =  json.getString(24);
            negr =  json.getString(25);
            negz =  json.getString(26);
            lot =  json.getString(27);
            imgn =  json.getString(28);
            low1 =  json.getString(29);
            imgp =  json.getString(30);
            low2 =  json.getString(31);
            chart1 =  json.getString(32);





        }

        catch (JSONException e) { e.printStackTrace(); }
    }


    private void showResults() {


        TextView textView =(TextView)findViewById(R.id.link);
        String link = street+", "+city+", "+state+"-"+zipcode;
        textView.setClickable(true);
        String text = "<a href='"+homedetails+"'>"+link+" </a>";
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml(text));


        if (type.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.type);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.type);
            street1.setText(type);
        }


        if (year.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.year);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.year);
            street1.setText(year);
        }



        if (lot.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.lot);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.lot);
            String lot1 = lot+" sq.ft.";
            street1.setText(lot1);
        }


        if (finArea.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.finArea);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.finArea);
            String fin = finArea+" sq.ft.";
            street1.setText(fin);
        }

        if (bath.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.bath);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.bath);
            street1.setText(bath);
        }

        if (bed.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.bed);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.bed);
            street1.setText(bed);
        }

        if (assYear.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.assYear);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.assYear);
            street1.setText(assYear);
        }

        if (ass.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.ass);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.ass);
            street1.setText(ass);
        }

        if (price.matches("") || price.matches("$0.00")) {

            TextView street1 = (TextView) findViewById(R.id.price);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.price);
            street1.setText(price);
        }

        if (soldDate.matches("") || soldDate.matches("01-Jan-1970")) {

            TextView street1 = (TextView) findViewById(R.id.soldDate);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.soldDate);
            street1.setText(soldDate);
        }

        if (amt.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.amt);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.amt);
            street1.setText(amt);
        }

        if (val.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.val);
            street1.setText("N/A");

        }

        else {

            int neg = Integer.parseInt(negz);

            if(neg == 1) {
                String MY_URL_STRING = imgn;
                TextView street1 = (TextView) findViewById(R.id.val);
               // new DownloadImageTask3((ImageView) findViewById(R.id.imageVal)).execute(MY_URL_STRING);
                ImageView img = (ImageView) findViewById(R.id.imageVal2);
                img.setImageResource(R.drawable.down_r);
                street1.setText(val);
            }


            else{
                String MY_URL_STRING2 = imgp;
                TextView street1 = (TextView) findViewById(R.id.val);
               // new DownloadImageTask3((ImageView) findViewById(R.id.imageVal)).execute(MY_URL_STRING2);
                ImageView img = (ImageView) findViewById(R.id.imageVal);
                img.setImageResource(R.drawable.up_g);
                street1.setText(val);
            }
        }

        if (low1.matches("") || high1.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.range);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.range);
            String range = low1+" - "+high1;
            street1.setText(range);
        }

        if (rentamt.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.rentamt);
            street1.setText("N/A");

        } else {

            TextView street1 = (TextView) findViewById(R.id.rentamt);
            street1.setText(rentamt);
        }

        if (val2.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.val2);
            street1.setText("N/A");

        }

        else {

            int negrt = Integer.parseInt(negr);

            if(negrt == 1) {
                String MY_URL_STRING = imgn;
                TextView street1 = (TextView) findViewById(R.id.val2);
               // new DownloadImageTask3((ImageView) findViewById(R.id.imageVal2)).execute(MY_URL_STRING);

                ImageView img = (ImageView) findViewById(R.id.imageVal2);
                img.setImageResource(R.drawable.down_r);
                street1.setText(val2);
            }


            else{
                String MY_URL_STRING2 = imgp;
                TextView street1 = (TextView) findViewById(R.id.val2);
                //new DownloadImageTask3((ImageView) findViewById(R.id.imageVal2)).execute(MY_URL_STRING2);
                ImageView img = (ImageView) findViewById(R.id.imageVal2);
                img.setImageResource(R.drawable.up_g);
                street1.setText(val2);
            }
        }

        if (low2.matches("") || high2.matches("")) {

            TextView street1 = (TextView) findViewById(R.id.range2);
            street1.setText("N/A");

        } else {
            TextView street1 = (TextView) findViewById(R.id.range2);
            String range2 = low2+" - "+high2;
            street1.setText(range2);
        }


        TextView disc = (TextView) findViewById(R.id.discl);

        String disclink = "<center>© Zillow, Inc., 2006-2014.";
        String text3 = "Use is subject to";
        disc.setClickable(true);
        String text2 = disclink + "<br/>" + text3 + "<a href='http://www.zillow.com/corp/Terms.htm'> Terms of Use </a><br/><a href='http://www.zillow.com/zestimate/'> What's a Zestimate </a></center>";
        disc.setMovementMethod(LinkMovementMethod.getInstance());
        disc.setText(Html.fromHtml(text2));


        TextView zest = (TextView) findViewById(R.id.zestlabel);
       // TextView zest2 = (TextView) findViewById(R.id.zestlabel2);

        String txt = "Zestimate ® Property Estimate <br/>";
       // zest.setText(txt);

        String txt3, txt2;
        if (dat1.matches("")) {
            txt2 = txt + "as of N/A";

            zest.setText(Html.fromHtml(txt2));
          //  zest2.setText(txt2);
        }
        else
        {
            txt3 = txt+ "as of " + dat1;

            zest.setText(Html.fromHtml(txt3));
           // zest2.setText(txt3);
        }





        TextView rent = (TextView) findViewById(R.id.rentlabel);
       // TextView rent2 = (TextView) findViewById(R.id.rentlabel2);

        String txt4 = "Rent Zestimate ® Valuation <br/>";
        rent.setText(txt4);

        if (dat1.matches("")) {
            String txt5 = txt4+ "as of N/A";
            rent.setText(Html.fromHtml(txt5));

            //rent2.setText(txt5);
        }
        else
        {
            String txt6 = txt4+ "as of " + dat1;
            rent.setText(Html.fromHtml(txt6));

            //rent2.setText(txt6);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.basic_info, menu);
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
}



class DownloadImageTask3 extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask3(ImageView bmImage) {
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
