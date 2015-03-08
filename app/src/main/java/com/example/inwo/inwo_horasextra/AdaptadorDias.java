package com.example.inwo.inwo_horasextra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by fer on 07/03/2015.
 */
public abstract class AdaptadorDias extends BaseAdapter {

    private Context context;
    private int idLayout;
    private ArrayList<?> datos;

    public AdaptadorDias(Context context, int id, ArrayList<?>entradas){
        super();
        this.context = context;
        this. idLayout = id;
        this.datos = entradas;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return datos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(idLayout, null);
        }

        onEntrada (datos.get(position), convertView);
        return convertView;

    }
    public abstract void onEntrada (Object entrada, View view);

}
