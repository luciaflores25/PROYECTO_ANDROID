package com.app.bibliobook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextApe;
    private EditText mEditTextEmail;
    private EditText mEditTextPass;
    private Button mButtonRegister;
    private Button mButtonSendToLogin;

    // VARIABLES: DATOS DEL REGISTRO

    private String name = "";
    private String apellidos = "";
    private String email = "";
    private String pass = "";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        // icono en actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextName = (EditText) findViewById(R.id.editTextName);
        mEditTextApe = (EditText) findViewById(R.id.editTextApe);
        mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextPass = (EditText) findViewById(R.id.editTextPass);
        mButtonRegister = (Button) findViewById(R.id.btnRegister);
        mButtonSendToLogin = (Button) findViewById(R.id.btnSendToLogin);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = mEditTextName.getText().toString();
                apellidos = mEditTextApe.getText().toString();
                email = mEditTextEmail.getText().toString();
                pass = mEditTextPass.getText().toString();

                if (!name.isEmpty() && !apellidos.isEmpty() && !email.isEmpty() && !pass.isEmpty()) {

                    if (pass.length() >= 6){

                        registerUser();
                    }
                    else {
                        Toast.makeText(RegistroActivity.this, "La contraseña debe tener al menos 6 carácteres", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RegistroActivity.this, "Campos vacíos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButtonSendToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("apellidos", apellidos);
                    map.put("email", email);
                    map.put("pass", pass);

                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {

                                startActivity(new Intent(RegistroActivity.this, BookListActivity.class));
                                Toast.makeText(RegistroActivity.this, "Registro completado", Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                            else {
                                Toast.makeText(RegistroActivity.this, "No se han podido crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegistroActivity.this, "No se ha podido registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
