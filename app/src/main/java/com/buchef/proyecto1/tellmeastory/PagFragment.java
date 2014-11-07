package com.buchef.proyecto1.tellmeastory;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by Cahco on 05-11-2014.
 */
public class PagFragment extends Fragment {
    /* Esta clase se usa para crear objetos Fragment.
    * Cada página del libro es un fragment. En definitiva entonces,
    * esta clase define cada página del libro.
    * El mátodo newInstance es como el constructor.
    * Recibe un Index, que no es más que el número de página.
    * Si el index es 0, se está en la página principal del libro (la que
    * ofrece si quieres leer o escuchar el libro).*/
     public static PagFragment newInstance(int index){
        PagFragment f = new PagFragment();
        //Guardar valor de la página
        Bundle args = new Bundle();
        args.putInt("pagina", index);
        f.setArguments(args);

        return f;
    }
    private LinearLayout crearPagina(LayoutInflater inflater, ViewGroup container){
        /* Este método crea el layout completo de la página, programáticamente, es decir, usando
        * trozos de xml en R, pero además creando y editando elementos en código de Java.
        * El layout dependerá del valor de Index, es decir, de qué página es. Si es la página 0,
        * hay que generar un layout correspondiente al comienzo del libro. Si es 1 o más, hay que
        * crear layout de página del libro, y setear su texto, imagen y música correspondiente. */

        //Obtener número de la página de este fragment
        int nro_pagina = getArguments().getInt("pagina",0);
        //El siguiente será el layout generado y retornado
        LinearLayout layout_pagina;
        //Caso en que es página inicial
        if (nro_pagina == 0){
            layout_pagina= (LinearLayout) inflater.inflate(R.layout.cuento_title, container, false);
            TextView label_titulo = (TextView)inflater.inflate(R.layout.cuento_title_titulo,container, false);
            //titulo se saca de los Extras del activiti padre de los fragments
            String titulo_cuento = getActivity().getIntent().getStringExtra("titulo");
            label_titulo.setText("Titulo del cuento: "+titulo_cuento);
            layout_pagina.addView(label_titulo);
        }
        else {
            layout_pagina= (LinearLayout) inflater.inflate(R.layout.cuento_pagina, container, false);
            TextView label_pagina = (TextView) inflater.inflate(R.layout.cuento_pagina_texto,container, false);
            label_pagina.setText(obtenerPaginaN(nro_pagina));
            layout_pagina.addView(label_pagina);
        }
        return layout_pagina;
    }
    private String obtenerPaginaN(int nro_pagina){
        String archivo = getActivity().getIntent().getStringExtra("archivo");
        final Resources res = getResources();
        InputStream is = res.openRawResource(res.getIdentifier(archivo,"raw",getActivity().getPackageName()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int count=0;
        try {
            String line;
            boolean contando = false;
            while ((line = reader.readLine()) != null){
                if (line.startsWith("----")) {
                    contando = true;
                    continue;
                }
                if (contando && (line.length()>0))
                    count++;
                if (count == nro_pagina){
                    reader.close();
                    return line;
                }
            }
            reader.close();
            return "Error: Página no encontrada";
        }
        catch(IOException e){
            return "EXCEPTION";
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        //para retornar layout definido programáticamente:
        return crearPagina(inflater, container);
        //para retornar layout definido en Layouts:
        //return (LinearLayout)inflater.inflate(R.layout.tab_frag1_layout, container, false);
    }
}