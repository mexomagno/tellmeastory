package com.buchef.proyecto1.tellmeastory;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class TituloCuento extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean continueMusic = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titulo_cuento);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.titulo_cuento, menu);
        return true;
    }
    public void accionBotonExit(View view){
        final Button boton = (Button) findViewById(R.id.boton_ver_libros);
        System.exit(0);
        finish();
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
