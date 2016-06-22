package edu8557.usc.cs_server.hw9;

        import android.app.Activity;
        import android.app.TabActivity;
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
        import org.json.JSONObject;

        import java.io.InputStream;
        import java.lang.String;

public class ResultActivity extends TabActivity {


    JSONArray json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("jsonArray");

        try {
            json = new JSONArray(jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //tab1.setContent(new Intent(this,Tab1Activity.class));


        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);


        TabHost.TabSpec tab1 = tabHost.newTabSpec("Basic Info");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Historical Zestimates");


        Intent intent2 = new Intent(ResultActivity.this, BasicInfoActivity.class);
        intent2.putExtra("jsonArray1", json.toString());

        tab1.setIndicator("Basic Info");
        tab1.setContent(intent2);


        Intent intent3 = new Intent(ResultActivity.this, HistoricalZestimatesActivity.class);
        intent3.putExtra("jsonArray2", json.toString());

        tab2.setIndicator("Historical Zestimates");
        tab2.setContent(intent3);


        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
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

