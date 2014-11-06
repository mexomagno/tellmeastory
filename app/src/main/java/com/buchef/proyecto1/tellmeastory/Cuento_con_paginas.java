package com.buchef.proyecto1.tellmeastory;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Vector;


public class Cuento_con_paginas extends FragmentActivity {
    //Variables internas
    boolean sonido_on; //false si esta en esta en silencio
    boolean continueMusic;
    SharedPreferences configs;
    private String titulo;
    private PagerAdapter mPagerAdapter;
    Button sonido;

    //Métodos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Este layout debiera depender del título del cuento
        super.setContentView(R.layout.activity_cuento_con_paginas);
        //inicializar fragments, páginas del cuento
        this.initialisePaging();
        continueMusic = false;
        //obtener titulo del cuento
        titulo=getIntent().getStringExtra("titulo");
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

    private void initialisePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        //Generar páginas del cuento
        for (int i = 0; i < 10; i++) {
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
}