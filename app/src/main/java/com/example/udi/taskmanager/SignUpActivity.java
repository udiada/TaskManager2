package com.example.udi.taskmanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    EditText etEmail,etFullName,etPassw1,etPassw2;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etFullName=(EditText)findViewById(R.id.etFulName);
        etPassw1=(EditText)findViewById(R.id.etPassw1);
        etPassw2=(EditText)findViewById(R.id.etPassw2);
        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        auth=FirebaseAuth.getInstance();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String passw=etPassw1.getText().toString();
                creatAcount(email,passw);
            }
        });
    }
    private FirebaseAuth.AuthStateListener authStateListener=new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user=firebaseAuth.getCurrentUser();
            if (user!=null)
                Toast.makeText(SignUpActivity.this,"user is signed in",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(SignUpActivity.this,"user is signed out",Toast.LENGTH_SHORT).show();
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
    private  void creatAcount(String email,String passw){
        auth.createUserWithEmailAndPassword(email,passw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this, "Authentication successful",Toast.LENGTH_SHORT).show();
                            updateUserProfile(task.getResult().getUser());
                         //   Intent intent = new Intent(SignUpActivity.this,MngTaskMain.class);
                         //  startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "Authentication fail"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                        }


                    }
                }
        );

    }
    private void updateUserProfile(FirebaseUser user){
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        builder.setDisplayName(etFullName.getText().toString());
        UserProfileChangeRequest profileUpdate = builder.build();
        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(SignUpActivity.this, "profileUpdate successful",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "profileUpdate fail"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    task.getException().printStackTrace();
                }
            }
        });
    }
}
