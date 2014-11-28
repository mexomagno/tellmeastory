package com.buchef.proyecto1.tellmeastory;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


public class TituloCuento extends FragmentActivity {
    boolean sonido_on; //false si esta en esta en silencio
    boolean continueMusic;
    SharedPreferences configs;
    private String titulo;
    Button sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Este layout debiera depender del título del cuento
        setContentView(R.layout.activity_titulo_cuento);
        
        continueMusic = false;
        //Crear boton
        sonido = (Button) findViewById(R.id.bSonido);
        sonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Silenciando
                if (sonido_on){
                    sonido_on = false;
                    MusicManager.release();
                    sonido.setBackgroundResource(R.drawable.sonido_no);
                    SharedPreferences.Editor editor = configs.edit();
                    editor.putBoolean(welcome.MusicOn, false);
                    editor.apply();

                    //Quitando silencio
                }else{
                    sonido_on = true;
                    MusicManager.start(getApplicationContext(), MusicManager.MUSIC_GAME);
                    sonido.setBackgroundResource(R.drawable.sonido);
                    SharedPreferences.Editor editor = configs.edit();
                    editor.putBoolean(welcome.MusicOn, true);
                    editor.apply();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.titulo_cuento, menu);
        return true;
    }/*
    public void accionBotonExit(View view){
        final Button boton = (Button) findViewById(R.id.boton_ver_libros);
        System.exit(0);
        finish();
    }*/

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
    @Override
    protected void onPause() {
        super.onPause();
        if (!continueMusic) {
            MusicManager.pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        continueMusic = true;
        configs = getSharedPreferences(welcome.CONFIGS, Context.MODE_PRIVATE);
        sonido_on = configs.getBoolean(welcome.MusicOn, true);
        if (!sonido_on){
            sonido.setBackgroundResource(R.drawable.sonido_no);
        }
        if (sonido_on){
            sonido.setBackgroundResource(R.drawable.sonido);
            MusicManager.start(this, MusicManager.MUSIC_GAME);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        boolean retorno = super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK){
            continueMusic = true;
        }
        return retorno;
    }
}
