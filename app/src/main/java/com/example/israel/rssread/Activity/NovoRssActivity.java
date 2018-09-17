package com.example.israel.rssread.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.israel.rssread.Common.ConfiguracaoFirebase;
import com.example.israel.rssread.Model.FeedRss;
import com.example.israel.rssread.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class NovoRssActivity extends AppCompatActivity {
    private EditText campoNome, campoRss;
    private Button botaoCadastrar;
    private ProgressBar progressBar;

    private DatabaseReference dataBase;
    private FirebaseAuth autenticacao;

    private FeedRss feedRss;
    private String idFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_rss);

        inicializarComponentes();
        Intent intent = getIntent();
        String vKey = intent.getStringExtra("key");
        String vNome = intent.getStringExtra("nome");
        String vUrl = intent.getStringExtra("url");

        if(!vKey.isEmpty()){
            idFeed = vKey;
            campoNome.setText(vNome);
            campoRss.setText(vUrl);
        }


        progressBar.setVisibility(View.GONE);
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome  = campoNome.getText().toString();
                String textoRss = campoRss.getText().toString();

                    if( !textoNome.isEmpty() ){
                        if( !textoRss.isEmpty() ){

                            feedRss = new FeedRss();
                            feedRss.setNome( textoNome );
                            feedRss.setUrl( textoRss );
                            cadastrar( feedRss );

                        }else{
                            Toast.makeText(NovoRssActivity.this,
                                    "Preencha a senha!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(NovoRssActivity.this,
                                "Preencha o nome!",
                                Toast.LENGTH_SHORT).show();
                    }

            }
        });


    }

    private void cadastrar(FeedRss feedRss) {
        progressBar.setVisibility(View.VISIBLE);
        dataBase = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarios = dataBase.child("usuarios");

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if( autenticacao.getCurrentUser() != null ) {
            String uId = autenticacao.getCurrentUser().getUid();
            if(idFeed.isEmpty())
                usuarios.child(uId).push().setValue(feedRss);
            else
                usuarios.child(uId).child(idFeed).setValue(feedRss);

            progressBar.setVisibility(View.GONE);
            Toast.makeText(NovoRssActivity.this,
                           "Cadastro com sucesso",
                           Toast.LENGTH_SHORT).show();
            startActivity( new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }
   }

    public void inicializarComponentes(){

        campoNome       = findViewById(R.id.editCadastroFeed);
        campoRss      = findViewById(R.id.editCadastroRss);
        botaoCadastrar  = findViewById(R.id.buttonCadastrar);
        progressBar     = findViewById(R.id.progressCadastro);

        campoNome.requestFocus();

    }


}
