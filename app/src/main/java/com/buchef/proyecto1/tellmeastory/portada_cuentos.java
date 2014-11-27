package com.buchef.proyecto1.tellmeastory;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class portada_cuentos extends ActionBarActivity {
    //private static String titulo;
    private static String archivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada_cuentos);
        archivo=getIntent().getStringExtra("archivo");
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
        portada_cuentos.this.startActivity(myIntent);
    }
    public void accionBotonLeertumismo(View view){
        Intent myIntent = new Intent(portada_cuentos.this,Cuento_con_paginas.class);
        myIntent.putExtra("voces_on",false);
        myIntent.putExtra("archivo",archivo);
        portada_cuentos.this.startActivity(myIntent);
    }
}
