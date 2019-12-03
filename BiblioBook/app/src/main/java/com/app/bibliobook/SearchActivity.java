package com.app.bibliobook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.bibliobook.service.ApiService;
import com.app.bibliobook.service.SpUtil;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // icono en actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        Button button = (Button)findViewById(R.id.btnSearch);
        final EditText etTitle = (EditText) findViewById(R.id.etTitle);
        final EditText etAuthor = (EditText) findViewById(R.id.etAuthor);
        final EditText etPublisher = (EditText) findViewById(R.id.etPublisher);
        final EditText etIsbn = (EditText) findViewById(R.id.etISBN);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String publisher = etPublisher.getText().toString().trim();
                String isbn = etIsbn.getText().toString().trim();
                if(title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
                    String message = getString(R.string.no_search_data);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
                else {
                    URL queryURL = ApiService.buildUrl(title, author, publisher, isbn);
                    Context context = getApplicationContext();
                    //sharedPreferences
                    int position = SpUtil.getPreferenceInt(context, SpUtil.POSITION);
                    if (position == 0 || position == 5) {
                        position=1;
                    }
                    else {
                        position++;
                    }
                    Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                    intent.putExtra("query", queryURL.toString());
                    startActivity(intent);
                }
            }
        });
    }
}