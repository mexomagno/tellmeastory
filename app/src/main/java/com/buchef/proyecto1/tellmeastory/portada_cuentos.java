package com.buchef.proyecto1.tellmeastory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
/* desde este activity se va a CUENTO_CON_PAGINAS */

public class portada_cuentos extends ActionBarActivity {
    //private static String titulo;
    boolean sonido_on; //false si esta en esta en silencio
    boolean continueMusic;
    SharedPreferences configs;
    Button sonido;
    public static String archivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada_cuentos);
        archivo=getIntent().getStringExtra("archivo");
        Log.d("Cuentos: ", "Activity portada_cuentos recibe archivo="+archivo);
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
        getMenuInflater().inflate(R.menu.portada_cuentos, menu);
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

    /* Acciones de los botones */
    public void accionBotonEscuchar(View view){
        //final Button boton_escuchar = (Button) findViewById(R.id.boton_escuchar);
        //final Button boton_leertumismo = (Button) findViewById(R.id.boton_leertumismo);
        Intent myIntent = new Intent(portada_cuentos.this,Cuento_con_paginas.class);
        myIntent.putExtra("voces_on",true);
        myIntent.putExtra("archivo",archivo);
        Log.d("Cuentos: ", "Activity portada_cuentos pasa a Cuento_con_paginas texto archivo="+archivo);
        portada_cuentos.this.startActivity(myIntent);
    }
    public void accionBotonLeertumismo(View view){
        Intent myIntent = new Intent(portada_cuentos.this,Cuento_con_paginas.class);
        myIntent.putExtra("voces_on",false);
        myIntent.putExtra("archivo",archivo);
        Log.d("Cuentos: ", "Activity portada_cuentos pasa a Cuento_con_paginas texto archivo="+archivo);
        portada_cuentos.this.startActivity(myIntent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!continueMusic) {
            MusicManager.pause();
            Log.d("MUSICA", "PAUSANDO MUSICA ACTUAL");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        continueMusic = false;
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
