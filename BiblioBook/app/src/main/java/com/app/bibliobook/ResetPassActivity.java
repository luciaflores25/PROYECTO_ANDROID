package com.app.bibliobook;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    private EditText mEditTextEmail;
    private Button mButtonResetPass;

    private String email = "";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        // icono en actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mAuth = FirebaseAuth.getInstance();

        mEditTextEmail = (EditText) findViewById(R.id.editTextEmailInResetPass);
        mButtonResetPass = (Button) findViewById(R.id.btnResetPass);

        mButtonResetPass .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = mEditTextEmail.getText().toString();

                if (!email.isEmpty()) {
                    resetPassword();
                }
                else {
                    Toast.makeText(ResetPassActivity.this, "Para restablecer la contraseña debes introducir tu email", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void resetPassword() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPassActivity.this, "Se ha enviado un correo para restablecer tu contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPassActivity.this, "No se ha podido enviar el correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
