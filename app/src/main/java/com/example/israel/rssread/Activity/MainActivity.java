package com.example.israel.rssread.Activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.israel.rssread.Common.ConfiguracaoFirebase;
import com.example.israel.rssread.Model.FeedRss;
import com.example.israel.rssread.R;
import com.example.israel.rssread.Adapter.Adapter;
import com.example.israel.rssread.Common.RecyclerItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<FeedRss> listaFeed = new ArrayList<>();
    private FirebaseAuth autenticacao;
    private DatabaseReference reference;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cache
        Paper.init(this);

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WeRead");
        setSupportActionBar( toolbar );

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            final CharSequence textToPaste = clipboard.getPrimaryClip().getItemAt(0).getText();
            if(URLUtil.isHttpsUrl(textToPaste.toString()) || URLUtil.isHttpUrl(textToPaste.toString())){
                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Url na área de transferência")
                        .setMessage("Vocẽ deseja adicionar como um feed?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, NovoRssActivity.class);
                                intent.putExtra("url", textToPaste.toString());
                                MainActivity.this.startActivity(intent);

                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
            Log.d("teste", textToPaste.toString());
            Toast.makeText(MainActivity.this, textToPaste, Toast.LENGTH_SHORT);
        } catch (Exception e) {
            return;
        }


        //configuracoes de objetos
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NovoRssActivity.class));
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        //Configurar adapter
        Adapter adapter = new Adapter( listaFeed );


        //Configurar Recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration( new DividerItemDecoration(this, LinearLayout.VERTICAL));

        //evento de click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                FeedRss feed = listaFeed.get( position );
                                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                                intent.putExtra("url", feed.getUrl());
                                startActivity(intent);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                FeedRss feed = listaFeed.get( position );
                                Intent intent = new Intent(MainActivity.this, NovoRssActivity.class);
                                intent.putExtra("url", feed.getUrl());
                                intent.putExtra("nome", feed.getNome());
                                intent.putExtra("key", feed.getKey());
                                startActivity(intent);

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        String cache = Paper.book().read("cache");
        if(cache != null && !cache.isEmpty() && !cache.equals("null")) // if have cache
        {
            List<FeedRss> lstFeedRss = (List<FeedRss>) new Gson().fromJson(cache, FeedRss.class);// Convert cache from Json to Object
            adapter = new Adapter(lstFeedRss);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);

        }else{
            //Listagem de feeds
            String uId = autenticacao.getCurrentUser().getUid();
            reference = ConfiguracaoFirebase.getFirebase().child("usuarios").child(uId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FeedRss feed = snapshot.getValue(FeedRss.class);
                        feed.setKey(snapshot.getKey());
                        listaFeed.add(feed);
                    }
                    recyclerView.setAdapter( new Adapter( listaFeed ) );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_sair :
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){
        try{
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}