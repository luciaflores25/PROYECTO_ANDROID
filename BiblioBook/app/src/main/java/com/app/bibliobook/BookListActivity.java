package com.app.bibliobook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bibliobook.adapter.BooksAdapter;
import com.app.bibliobook.model.Book;
import com.app.bibliobook.service.ApiService;
import com.app.bibliobook.service.SpUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProgressBar mLoadingProgress;
    private RecyclerView rvBooks;

    FirebaseAuth mAuth;
    URL bookUrl;

    // VARIABLES: DATOS DEL LIBRO

    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        // icono en actionbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mAuth = FirebaseAuth.getInstance();

        rvBooks = (RecyclerView) findViewById(R.id.rv_books);
        LinearLayoutManager booksLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(booksLayoutManager);

        mLoadingProgress = (ProgressBar) findViewById(R.id.pb_loading);
        Intent intent = getIntent();
        String query = intent.getStringExtra("query");

        try {
            if (query == null  || query.isEmpty()) {
                bookUrl = ApiService.buildUrl("spyderman");
            }
            else {
                bookUrl = new URL(query);
            }
            new BooksQueryTask().execute(bookUrl);

        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        ArrayList<String> recentList = SpUtil.getQueryList(getApplicationContext());
        int itemNum = recentList.size();
        MenuItem recentMenu;
        for (int i = 0; i<itemNum; i++) {
            recentMenu = menu.add(Menu.NONE, i, Menu.NONE, recentList.get(i));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_advanced_search:
                startActivity(new Intent(BookListActivity.this, SearchActivity.class));
                return true;
            case R.id.action_profile:
                startActivity(new Intent(BookListActivity.this, ProfileActivity.class));
                return true;
            case R.id.action_signout:
                mAuth.signOut();
                startActivity(new Intent(BookListActivity.this, LoginActivity.class));
                Toast.makeText(BookListActivity.this, "La sesión se ha cerrado correctamente", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                int position = item.getItemId() + 1 ;
                String preferenceName = SpUtil.QUERY + String.valueOf(position);
                String query = SpUtil.getPreferenceString(getApplicationContext(), preferenceName);
                String[] prefParams = query.split("\\,");
                String[] queryParams = new String[4];

                for (int i=0; i<prefParams.length;i++) {
                    queryParams[i] = prefParams[i];
                }


                bookUrl = ApiService.buildUrl(
                        (queryParams[0] == null)?"" : queryParams[0],
                        (queryParams[1] == null)?"" : queryParams[1],
                        (queryParams[2] == null)?"" : queryParams[2],
                        (queryParams[3] == null)?"" : queryParams[3]
                );
                new BooksQueryTask().execute(bookUrl);
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookUrl = ApiService.buildUrl(query);
            new BooksQueryTask().execute(bookUrl);
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String result = null;
            try {
                result = ApiService.getJson(searchURL);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            TextView tvError = (TextView) findViewById(R.id.tv_error);
            mLoadingProgress.setVisibility(View.INVISIBLE);
            if (result == null) {
                rvBooks.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            } else {
                rvBooks.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);
            }
            ArrayList<Book> books = ApiService.getBooksFromJson(result);
            String resultString = "";

            BooksAdapter adapter = new BooksAdapter(books);
            rvBooks.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
            Toast.makeText(BookListActivity.this, "Cargando libros...", Toast.LENGTH_SHORT).show();
        }
    }
}

