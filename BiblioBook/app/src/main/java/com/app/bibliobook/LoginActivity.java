package com.app.bibliobook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEditTextEmail;
    private EditText mEditTextPass;
    private Button mButtonLogin;
    private TextView mButtonGoRegister;
    private TextView mButtonForgotPass;

    private String email = "";
    private String pass = "";

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // icono en actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mAuth = FirebaseAuth.getInstance();

        mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextPass = (EditText) findViewById(R.id.editTextPass);
        mButtonLogin = (Button) findViewById(R.id.btnLogin);
        mButtonGoRegister = (TextView) findViewById(R.id.btnGoRegister);
        mButtonForgotPass = (TextView) findViewById(R.id.btnForgotPass);

        mButtonGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEditTextEmail.getText().toString();
                pass = mEditTextPass.getText().toString();


                FirebaseUser user = mAuth.getCurrentUser();

                if (!email.isEmpty() && !pass.isEmpty()){
                        loginUser();

                }
                else {
                    Snackbar.make(mButtonLogin, "Para iniciar sesión debe completar los campos.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        mButtonForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPassActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, BookListActivity.class));
            finish();
            return;
        }
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, BookListActivity.class));
                    Toast.makeText(LoginActivity.this, "Bienvenido a BIBLIOBOOK", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                else {
                    Toast.makeText(LoginActivity.this, "No se pudo iniciar sesión. Comprueba los datos que has introducido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
