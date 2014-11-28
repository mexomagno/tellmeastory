package com.buchef.proyecto1.tellmeastory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

/* Este activity genera fragments de la clase PagFragment */
public class Cuento_con_paginas extends FragmentActivity {
    //****Variables internas
    //manejo de sonidos y música
    boolean sonido_on; //false si esta en esta en silencio
    boolean continueMusic;
    Button sonido;
    SharedPreferences configs;
    //establece si se quiere lectura con voz o no
    boolean voz_on;
    //Información del cuento
    //private static String titulo;
    private static String archivo;
    private int n_paginas;
    private PagerAdapter mPagerAdapter;

    //****Métodos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Este layout debiera depender del título del cuento
        super.setContentView(R.layout.activity_cuento_con_paginas);
        //obtener titulo del cuento
        archivo=getIntent().getStringExtra("archivo");
        Log.d("Cuentos: ", "Activity Cuento_con_paginas recibe texto archivo="+archivo);
        voz_on=getIntent().getBooleanExtra("voz_on", true);
        //titulo=obtenerTituloDesdeArchivo(archivo);
        //Log.d("Cuentos: ", "Activity Cuento_con_paginas tiene titulo="+titulo);
        //obtener cuenta de paginas del cuento
        n_paginas = obtenerNumeroPaginas(archivo);
        //inicializar fragments, páginas del cuento
        this.initialisePaging();
        continueMusic = false;
        //A partir del título, obtener el nombre del archivo
        //Log.d("Cuento: ", "titulo del cuento: " + titulo);
        //Crear boton
        sonido = (Button) findViewById(R.id.bSonido);
        sonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Silenciando
                if (sonido_on) {
                    sonido_on = false;
                    MusicManager.release();
                    sonido.setBackgroundResource(R.drawable.sonido_no);
                    SharedPreferences.Editor editor = configs.edit();
                    editor.putBoolean(welcome.MusicOn, false);
                    editor.apply();

                    //Quitando silencio
                } else {
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
    private String obtenerTituloDesdeArchivo(String archivo){
        final Resources res = getResources();
        InputStream is = res.openRawResource(res.getIdentifier(archivo,"raw",this.getPackageName()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder strb = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null){
                if (line.startsWith("title:")){
                    reader.close();
                    return line.substring(line.indexOf(':')+1);
                }
            }
            return "Error: Archivo no tiene titulo!!!";
        }
        catch(IOException e){
            return "EXCEPTION";
        }
    }
    private int obtenerNumeroPaginas(String archivo){
        Resources res = getResources();
        Scanner br = new Scanner(res.openRawResource(res.getIdentifier(archivo,"raw",this.getPackageName())));//new InputStreamReader(fstream));
        int count=0;
        boolean contando=false;
        while (br.hasNext()) {
            String linea = br.nextLine();
            if (linea.startsWith("----"))
                contando = true;
            if (contando && (linea.length()>0))
                count++;
        }
        br.close();
        return count;
    }
    private void initialisePaging() {
        //
        List<Fragment> fragments = new Vector<Fragment>();
        //Generar páginas del cuento
        for (int i = 0; i < n_paginas; i++) {
            // Generar nuevo fragment para el arreglo
            Fragment newfragment = PagFragment.newInstance(i);
            fragments.add(newfragment);
        }
        this.mPagerAdapter = new com.buchef.proyecto1.tellmeastory.PagerAdapter(super.getSupportFragmentManager(), fragments);
        //Obtener vista del pager
        ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cuento_con_paginas, menu);
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
    protected void onPause() {
        super.onPause();
        if (!continueMusic) {
            MusicManager.pause();
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
    @Override
    public void onDestroy(){
        try {
            PagFragment.mp.stop();
        }catch (NullPointerException e){
            Log.d("Error", "Null Pointer");
        }

        super.onDestroy();
    }
}