package com.buchef.proyecto1.tellmeastory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;                             //para obtener el ListView del xml
import android.widget.ArrayAdapter;                         //para usar el ArrayAdapter
import android.widget.AdapterView.OnItemClickListener;      //para usar OnItemClickListener
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;


public class menu_cuentos extends Activity {
    ListView lista_cuentos;
    boolean sonido_on; //false si esta en esta en silencio
    boolean continueMusic;
    SharedPreferences configs;
    String msg = "Android : "; // DEBUG

    Button sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cuentos);
        Log.d(msg, "menu_cuentos onCreate"); // DEBUG
        continueMusic = true;
        //obtener objeto boton desd el xml
        sonido = (Button) findViewById(R.id.bSonido);
        //Definir comportamiento de boton sonido
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
                    MusicManager.start(getApplicationContext(), MusicManager.MUSIC_MENU);
                    sonido.setBackgroundResource(R.drawable.sonido);
                    SharedPreferences.Editor editor = configs.edit();
                    editor.putBoolean(welcome.MusicOn, true);
                    editor.apply();
                }
            }
        });
        //obtener objeto lista de cuentos desde el xml
        lista_cuentos = (ListView) findViewById(R.id.lista_libros);
        //definir arreglo con nombre de los elementos a mostrar
        String[] nombre_cuentos = getTitulosCuentos();
        /* definir Adapter
        Parámetros: Contexto, layout para la fila, ID del textview hacia donde se escribe, el arreglo de los datos
        */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,nombre_cuentos);
        //asignar Adapter al listview
        lista_cuentos.setAdapter(adapter);
        //setear listener para click en elementos de la lista
        lista_cuentos.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //posicion del item clickeado
                int pos_item = position;
                String nombre_item = (String) lista_cuentos.getItemAtPosition(position);
                //mostrar una alerta
                //Toast.makeText(getApplicationContext(),"Posicion: "+pos_item+", Item: " + valor_item, Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(getApplicationContext(),Cuento_con_paginas.class);
                //Añadir parametros al activity, es decir, añadir nombre del cuento.
                myIntent.putExtra("titulo",nombre_item);
                menu_cuentos.this.startActivity(myIntent);
            }
        });
    }
    public String [] getTitulosCuentos(){
        Field[] fields=R.raw.class.getFields();
        String []lista = new String[fields.length];
        int index=0;
        for(int count=0; count < fields.length; count++){
            //Log.i("Raw Asset: ", fields[count].getName());
            lista[index++]=fields[count].getName();
        }
        return lista;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cuentos, menu);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        boolean retorno = super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK){
            continueMusic = true;
        }
        return retorno;
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(msg, "menu_cuentos onStart"); // DEBUG
    }
    @Override
    protected void onResume(){
        super.onResume();
        continueMusic = false;
         //Recuperar configuracion guardada
        configs = getSharedPreferences(welcome.CONFIGS, Context.MODE_PRIVATE);
        sonido_on = configs.getBoolean(welcome.MusicOn, true);
        if (!sonido_on){ //Cambiar icono si venia en silencio
            sonido.setBackgroundResource(R.drawable.sonido_no);
        }
        if (sonido_on) {
            MusicManager.start(this, MusicManager.MUSIC_MENU);
        }
        Log.d(msg, "menu_cuentos onResume"); // DEBUG
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!continueMusic) {
            MusicManager.pause();
        }
        Log.d(msg, "menu_cuentos onPause"); // DEBUG
        
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(msg, "menu_cuentos onStop"); // DEBUG
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(msg, "menu_cuentos onDestroy"); // DEBUG
    }
}
