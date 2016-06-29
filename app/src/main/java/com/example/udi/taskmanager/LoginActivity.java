package com.example.udi.taskmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etpassword;
    AutoCompleteTextView etemail;
    Button btnSignIn,btnNewAcount;
    //5.1
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //5.2
        auth= FirebaseAuth.getInstance();
        etemail=(AutoCompleteTextView)findViewById(R.id.etemail);
        etpassword=(EditText)findViewById(R.id.etpassword);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        btnNewAcount=(Button)findViewById(R.id.btnNewAcount);
        btnNewAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etemail.getText().toString();
                String passw=etpassword.getText().toString();
                signIn(email,passw);
            }
        });
    }
    private FirebaseAuth.AuthStateListener authStateListener=new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user=firebaseAuth.getCurrentUser();
            if (user!=null) {
                Toast.makeText(LoginActivity.this, "user is signed in", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MngTaskMain.class);
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(LoginActivity.this,"user is signed out",Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onStart(){
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }
    protected void onStop(){
        super.onStop();
        if (authStateListener!=null)
            auth.removeAuthStateListener(authStateListener);
    }
    private void signIn(String email, String passw) {
        auth.signInWithEmailAndPassword(email, passw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,MngTaskMain.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication fail" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    task.getException().printStackTrace();
                }
            }
        });
    }
}
