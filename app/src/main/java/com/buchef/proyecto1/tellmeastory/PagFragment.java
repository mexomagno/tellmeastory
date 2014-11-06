package com.buchef.proyecto1.tellmeastory;

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

/**
 * Created by Cahco on 05-11-2014.
 */
public class PagFragment extends Fragment {
    public static PagFragment newInstance(int index){
        // setear variable legible por el fragment
        PagFragment f = new PagFragment();
        Bundle args = new Bundle();
        args.putInt("pagina", index);
        f.setArguments(args);
        // retornar nuevo fragment
        return f;
    }
    private LinearLayout crearPagina(LayoutInflater inflater, ViewGroup container){
        int nro_pagina = getArguments().getInt("pagina",0);
        Log.d("Android: ", "Fragment n°" + nro_pagina);
        //crear layout del fragment, dependiendo del valor de su index
        /*LinearLayout fraglayout = (LinearLayout) inflater.inflate(R.layout.tab_frag1_layout, container, false);
        fraglayout.setBackgroundColor(Color.argb(255, 0, index * 10, index * 10));

        TextView label = new TextView(getActivity());
        label.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        label.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
        label.setText("Index = " + index);
        //label.setId();
        label.setGravity(Gravity.CENTER_VERTICAL);

        //fraglayout.addView(label);
        TextView inflatedlabel = (TextView)inflater.inflate(R.layout.texto, container, false);
        inflatedlabel.setText("Inflated Index = " + index);
        fraglayout.addView(inflatedlabel);*/
        LinearLayout layout_pagina;
        if (nro_pagina == 0){
            layout_pagina= (LinearLayout) inflater.inflate(R.layout.cuento_title, container, false);
            TextView label_titulo = (TextView)inflater.inflate(R.layout.cuento_title_titulo,container, false);
            //titulo se saca de los Extras del activiti padre de los fragments
            label_titulo.setText("Titulo del cuento: "+getActivity().getIntent().getStringExtra("titulo"));
            layout_pagina.addView(label_titulo);
        }
        else {
            layout_pagina= (LinearLayout) inflater.inflate(R.layout.cuento_pagina, container, false);
            TextView label_pagina = (TextView) inflater.inflate(R.layout.cuento_pagina_texto,container, false);
            label_pagina.setText("Este es el texto que tiene que ir en la página numero: "+nro_pagina);
            layout_pagina.addView(label_pagina);
        }
        return layout_pagina;
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