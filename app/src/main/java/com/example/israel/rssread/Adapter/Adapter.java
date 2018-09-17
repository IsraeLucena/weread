package com.example.israel.rssread.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.israel.rssread.Model.FeedRss;
import com.example.israel.rssread.R;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<FeedRss> listaFeeds;

    public Adapter(List<FeedRss> lista) {
        this.listaFeeds = lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_lista, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        FeedRss feed = listaFeeds.get( position );
        holder.titulo.setText( feed.getNome() );
        holder.url.setText( feed.getUrl() );

    }

    @Override
    public int getItemCount() {
        return listaFeeds.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView url;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textTitulo);
            url = itemView.findViewById(R.id.textUrl);

        }
    }

}
