package com.buchef.proyecto1.tellmeastory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Scanner;

/* Desde este activity se va a PORTADA_CUENTO */
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
        TextView txt = (TextView) findViewById(R.id.titulo_menu_cuentos);
        Typeface font = Typeface.createFromAsset(getAssets(), "Chantelli_Antiqua.ttf");
        txt.setTypeface(font);
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
        //obtener listado de nombre de archivo de los cuentos
        final String [] nombre_archivos = getArchivosCuentos();
        //definir arreglo con nombre de los elementos a mostrar
        final String[] nombre_cuentos = getTitulosCuentos(nombre_archivos);

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
                Intent myIntent = new Intent(getApplicationContext(),portada_cuentos.class);
                //Añadir parametros al activity, es decir, añadir nombre del cuento.
                String nom_archivo = nombre_archivos[getIndexOfStringArray(nombre_cuentos,nombre_item)];
                myIntent.putExtra("archivo",nom_archivo);
                myIntent.putExtra("titulo",nombre_item);
                //Log.d("Cuentos: ", "Activity menu_cuentos pasa a portada_cuentos texto 'archivo'="+nom_archivo);
                menu_cuentos.this.startActivity(myIntent);
            }
        });
    }
    private String [] getTitulosCuentos(String [] archivos){
        String [] titulos = new String[archivos.length];
        int index=0;
        for (String arch : archivos){
            titulos[index++]=obtenerTituloDesdeArchivo(arch);
        }
        return titulos;
    }
    private String [] getArchivosCuentos(){
        Field[] fields=R.raw.class.getFields();
        String []lista = new String[fields.length];
        int index=0;
        for(Field field : fields){
            //si el archivo es un txt con un cuento, debe comenzar con "c_" + el título
            if (field.getName().startsWith("c_"))
                //Obtener el título del cuento y añadirlo al arreglo
                lista[index++]=field.getName();//obtenerTitulo(field.getName());//.substring(2).replace("_"," ");
        }
        //Arreglar tamaño del arreglo de strings
        String [] resp = new String[index];
        for (int i=0;lista[i]!=null;i++)
            resp[i]=lista[i];
        return resp;
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
            reader.close();
            return "Error: Archivo no tiene titulo!!!";
        }
        catch(IOException e){
            return "EXCEPTION";
        }
    }
    private int getIndexOfStringArray(String [] arreglo, String buscado){
        int cnt = 0;
        int resp= -1;
        for (String s : arreglo){
            Log.d("Cuento: ", "comparando "+s+" con "+buscado);
            Log.d("Cuento: ", "Compare to da: "+s.compareTo(buscado));
            Log.d("Cuento: ", "equals da "+s.equals(buscado));
            Log.d("Cuento: ", "contentequals da "+s.contentEquals(buscado));
            if (s.equals(buscado)){
                resp = cnt;
                break;
            }
            else{
                Log.d("Cuento: ", "No entró al IF!!!");
            }
            cnt++;
        }
        return resp;
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
            sonido.setBackgroundResource(R.drawable.sonido);
            MusicManager.start(this, MusicManager.MUSIC_MENU, true);
            Log.d("MUSICA", "MUSICA ACTUAL MENU");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!continueMusic) {
            MusicManager.pause();
        }
    }
}
