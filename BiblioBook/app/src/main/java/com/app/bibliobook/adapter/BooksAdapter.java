package com.app.bibliobook.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.bibliobook.R;
import com.app.bibliobook.model.Book;
import com.app.bibliobook.BookDetail;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder>{

    ArrayList<Book> books;
    public BooksAdapter(ArrayList<Book> books) {
        this.books = books;
    }
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.book_list_item, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvAuthors;
        TextView tvDate;
        TextView tvPublisher;

        public BookViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvAuthors= (TextView) itemView.findViewById(R.id.tvAuthors);
            tvDate = (TextView) itemView.findViewById(R.id.tvPublishedDate);
            tvPublisher =(TextView) itemView.findViewById(R.id.tvPublisher);
            itemView.setOnClickListener(this);

        }
        public void bind (Book book) {
            tvTitle.setText(book.title);
            String authors="";
            tvAuthors.setText(book.authors.get());
            tvDate.setText(book.publishedDate.get());
            tvPublisher.setText(book.publisher.get());

        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Book selectedBook = books.get(position);
            Log.d("Click", String.valueOf(position));
            //gets the book from the arrayList
            Intent intent = new Intent(view.getContext(), BookDetail.class);
            intent.putExtra("Book", selectedBook);
            view.getContext().startActivity(intent);
        }
    }
}
