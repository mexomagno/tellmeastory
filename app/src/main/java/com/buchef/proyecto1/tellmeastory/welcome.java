package com.buchef.proyecto1.tellmeastory;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

/* Desde este activity se va a MENU_CUENTOS  */
public class welcome extends ActionBarActivity {
    boolean music_on;
    boolean continueMusic;
    //Keys de configs
    public static final String CONFIGS = "Configs";
    public static final String MusicOn = "musicOnKey";
    SharedPreferences configs;
    String msg = "Android : "; // DEBUG


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        TextView txt = (TextView) findViewById(R.id.nombre_app);
        Typeface font = Typeface.createFromAsset(getAssets(), "Chantelli_Antiqua.ttf");
        txt.setTypeface(font);
        Log.d(msg, "welcome onCreate"); // DEBUG
        configs = getSharedPreferences(CONFIGS, Context.MODE_PRIVATE);
        continueMusic = true;

    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(msg, "welcome onStart"); // DEBUG
    }
    @Override
    protected void onResume(){
        super.onResume();
        continueMusic = false;
        music_on = configs.getBoolean(MusicOn, true);
        if (music_on) {
            MusicManager.start(this, MusicManager.MUSIC_MENU);
            Log.d(msg, "Music on status: " + music_on); // DEBUG
        }
        Log.d(msg, "welcome onResume"); // DEBUG
    }
    @Override
    protected void onPause(){
        super.onPause();
        if (!continueMusic) {
            MusicManager.pause();
        }
        Log.d(msg, "welcome onPause"); // DEBUG
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(msg, "welcome onStop"); // DEBUG
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(msg, "welcome onDestroy"); // DEBUG
    }

    public void accionBoton(View view){
        final Button boton = (Button) findViewById(R.id.boton_ver_libros);
        Intent myIntent = new Intent(welcome.this,menu_cuentos.class);
        welcome.this.startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
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
