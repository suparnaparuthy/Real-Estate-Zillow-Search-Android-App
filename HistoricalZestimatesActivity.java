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
import android.widget.TabHost;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;


public class HistoricalZestimatesActivity extends Activity {
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


    JSONArray json;
    private TextSwitcher mSwitcher;
    Button btnNext;
    Button btnPrev;
    int currentIndex = 0;
    String textToShow[];
    int messageCount=2;

    private ImageSwitcher imageSwitcher;
    int currentIndex1=0;

    int messageCount1 = 2;

    int numDrawables = 3;
    Drawable[] drawableArray = new Drawable[numDrawables];
    Bitmap bmp1;
    Bitmap bmp5;
    Bitmap bmp10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_zestimates);


        Intent intent3 = getIntent();
        String jsonArray = intent3.getStringExtra("jsonArray2");


            try {
                json = new JSONArray(jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }


             parseResults();
        TextView disc = (TextView) findViewById(R.id.discl);

        String disclink = "<center>© Zillow, Inc., 2006-2014.";
        String text3 = "Use is subject to";
        disc.setClickable(true);
        String text2 = disclink + "<br/>" + text3 + "<a href='http://www.zillow.com/corp/Terms.htm'> Terms of Use </a><br/><a href='http://www.zillow.com/zestimate/'> What's a Zestimate </a></center>";
        disc.setMovementMethod(LinkMovementMethod.getInstance());
        disc.setText(Html.fromHtml(text2));


        String chart1txt1 = "Historical Zestimates for the Past 1 year";
        String chart1txt2 = street+", "+city+", "+state+"-"+zipcode;
        String chart1txt3 = "<b>"+chart1txt1 + "</b> <br/>"+chart1txt2;


        String chart5txt1 = "Historical Zestimates for the Past 5 years";
        String chart5txt2 = street+", "+city+", "+state+"-"+zipcode;
        String chart5txt3 = "<b>"+chart5txt1 + "</b> <br/>"+chart5txt2;


        String chart10txt1 = "Historical Zestimates for the Past 10 year";
        String chart10txt2 = street+", "+city+", "+state+"-"+zipcode;
        String chart10txt3 = "<b>"+chart10txt1 + "</b> <br/>"+chart10txt2;

        textToShow = new String[]{chart1txt3, chart5txt3, chart10txt3};


        btnNext=(Button)findViewById(R.id.buttonNext);
        btnPrev = (Button)findViewById(R.id.buttonPrev);
        mSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);

        mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView myText = new TextView(HistoricalZestimatesActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(15);
                myText.setTextColor(Color.BLACK);
                return myText;
            }
        });

        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        // set the animation type of textSwitcher
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);

        mSwitcher.setText(Html.fromHtml(textToShow[currentIndex]));


        parseCharts();


   /*    imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.FILL_PARENT,ImageSwitcher.LayoutParams.FILL_PARENT));
                imageView.setMinimumHeight(300);
                return imageView;
            }
        });

        Animation in1 = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out1 = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        imageSwitcher.setInAnimation(in1);
        imageSwitcher.setOutAnimation(out1);
*/


      //  ImageView img = (ImageView) findViewById(R.id.image);
        //  img.setImageBitmap(bmp1);

        new DownloadImageTask3((ImageView) findViewById(R.id.image)).execute(chart1);



        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ++currentIndex;

                if(currentIndex == messageCount+1)
                    currentIndex=0;
                mSwitcher.setText(Html.fromHtml(textToShow[currentIndex]));

                ++currentIndex1;
                if(currentIndex1 == messageCount1+1)
                    currentIndex1=0;

                ImageView img = (ImageView) findViewById(R.id.image);

                if(currentIndex1 == 0)
                {
                    img.setImageBitmap(bmp1);
                }

                else if(currentIndex1 == 1)
                {
                    img.setImageBitmap(bmp5);
                }

                else if(currentIndex1 == 2)
                {
                    img.setImageBitmap(bmp10);
                }

                else{

                }
                //imageSwitcher.setImageDrawable(drawableArray[currentIndex1]);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                --currentIndex;

                if(currentIndex == -1)
                    currentIndex=messageCount;
                mSwitcher.setText(Html.fromHtml(textToShow[currentIndex]));


                --currentIndex1;

                if(currentIndex1 == -1)
                    currentIndex1 = messageCount1;

               // imageSwitcher.setImageDrawable(drawableArray[currentIndex1]);

                ImageView img = (ImageView) findViewById(R.id.image);

                if(currentIndex1 == 0)
                {
                    img.setImageBitmap(bmp1);
                }

                else if(currentIndex1 == 1)
                {
                    img.setImageBitmap(bmp5);
                }

                else if(currentIndex1 == 2)
                {
                    img.setImageBitmap(bmp10);
                }

                else{

                }


            }
        });



    }


    class DownloadImageTask2 extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        String urldisplay;

        public DownloadImageTask2() {
            //this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
             urldisplay = urls[0];
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
            //HistoricalZestimatesActivity.bmp = result;
           // bmImage.setImageBitmap(result);

          //  bmp1 =result;
            if(urldisplay == chart1)
                bmp1 = result;

            else if(urldisplay == chart5)
                bmp5 = result;

            else
                bmp10 = result;
        }

    }

    private void parseCharts(){

        String MY_URL_STR1 = chart1;
        new DownloadImageTask2().execute(MY_URL_STR1);
     //   Drawable drawable1 =new BitmapDrawable(bmp1);
       // drawableArray[0] = drawable1;



        String MY_URL_STR5 = chart5;
        new DownloadImageTask2().execute(MY_URL_STR5);
      //  Drawable drawable5 =new BitmapDrawable(bmp5);
        //drawableArray[1] = drawable5;

        String MY_URL_STR10 = chart10;
        new DownloadImageTask2().execute(MY_URL_STR10);
       // Drawable drawable10 =new BitmapDrawable(bmp10);
        //drawableArray[2] = drawable10;
        // bitmaps[2] = bmp;
    }

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.historical_zestimates, menu);
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





class DownloadImageTask4 extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask4(ImageView bmImage) {
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