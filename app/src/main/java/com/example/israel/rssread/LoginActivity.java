package com.example.israel.rssread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializaComponentes();

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if( !email.isEmpty() ){
                    if( !senha.isEmpty() ){

                        //Verifica estado do switch
                        if( tipoAcesso.isChecked() ){//Cadastro
//
//                            autenticacao.createUserWithEmailAndPassword(
//                                    email, senha
//                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if( task.isSuccessful() ){
//
//                                        Toast.makeText(MainActivity.this,
//                                                "Cadastro realizado com sucesso!",
//                                                Toast.LENGTH_SHORT).show();
//
//                                        //Direcionar para a tela principal do App
//
//                                    }else {
//
//                                        String erroExcecao = "";
//
//                                        try{
//                                            throw task.getException();
//                                        }catch (FirebaseAuthWeakPasswordException e){
//                                            erroExcecao = "Digite uma senha mais forte!";
//                                        }catch (FirebaseAuthInvalidCredentialsException e){
//                                            erroExcecao = "Por favor, digite um e-mail válido";
//                                        }catch (FirebaseAuthUserCollisionException e){
//                                            erroExcecao = "Este conta já foi cadastrada";
//                                        } catch (Exception e) {
//                                            erroExcecao = "ao cadastrar usuário: "  + e.getMessage();
//                                            e.printStackTrace();
//                                        }
//
//                                        Toast.makeText(MainActivity.this,
//                                                "Erro: " + erroExcecao ,
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });

                        }else {//Login
                            if(email.equals("admin") && senha.equals("admin")){
                                Toast.makeText(LoginActivity.this,
                                        "Logado com sucesso",
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                            }else{
                                Toast.makeText(LoginActivity.this,"Erro ao fazer login",
                                        Toast.LENGTH_SHORT).show();
                            }
//                            autenticacao.signInWithEmailAndPassword(
//                                    email, senha
//                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                    if( task.isSuccessful() ){
//
//                                        Toast.makeText(MainActivity.this,
//                                                "Logado com sucesso",
//                                                Toast.LENGTH_SHORT).show();
//
//                                    }else {
//                                        Toast.makeText(MainActivity.this,
//                                                "Erro ao fazer login : " + task.getException() ,
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }
//                            });
                        }

                    }else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha a senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha o E-mail!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void inicializaComponentes(){
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcessar = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);
    }

}