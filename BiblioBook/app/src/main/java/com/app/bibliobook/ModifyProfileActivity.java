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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ModifyProfileActivity extends AppCompatActivity {

    private TextView mEditTextNamePerfil;
    private TextView mEditTextApePerfil;
    private TextView mEditTextEmailPerfil;
    private Button mBtnModifyProfile;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String name = "";
    private String apellidos = "";
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_profile);
        // icono en actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextNamePerfil = (EditText) findViewById(R.id.editTextNameProfile);
        mEditTextApePerfil = (EditText) findViewById(R.id.editTextApeProfile);
        mEditTextEmailPerfil = (EditText) findViewById(R.id.editTextEmailProfile);
        mBtnModifyProfile = (Button) findViewById(R.id.btnModifyProfile);

        getUserInfo();

        mBtnModifyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = mEditTextNamePerfil.getText().toString();
                apellidos = mEditTextApePerfil.getText().toString();
                email = mEditTextEmailPerfil.getText().toString();

                if (!name.isEmpty() && !apellidos.isEmpty() && !email.isEmpty()) {

                        modifyUser();
                }

                else {
                    Toast.makeText(ModifyProfileActivity.this, "Debes rellenar todos los datos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getUserInfo() {
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String apellidos = dataSnapshot.child("apellidos").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    mEditTextNamePerfil.setText(name);
                    mEditTextApePerfil.setText(apellidos);
                    mEditTextEmailPerfil.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void modifyUser() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("apellidos", apellidos);
        userMap.put("email", email);

        String id = mAuth.getCurrentUser().getUid();

        mDatabase.child("Users").child(id).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(ModifyProfileActivity.this, ProfileActivity.class));
                    Toast.makeText(ModifyProfileActivity.this, "Datos actualizados correctamente", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                } else {
                    Toast.makeText(ModifyProfileActivity.this, "Error... No se han podido actualizar los datos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
