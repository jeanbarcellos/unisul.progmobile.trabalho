package jv.cadastrodelivros;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jv.cadastrodelivros.dao.BookDao;
import jv.cadastrodelivros.model.Book;
import jv.cadastrodelivros.utils.Dialog;

public class BookActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textFilter;

    private BookDao bookDao;

    List<Book> books = new ArrayList<>();

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        this.bookDao = new BookDao(this);

        this.textFilter = ((EditText) findViewById(R.id.editTextFilter));

        // List view
        ListView listViewBooks = findViewById(R.id.listViewBooks);

        this.books = this.bookDao.findAll();

        this.adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                this.books);

        listViewBooks.setAdapter(this.adapter);

        this.adapter.notifyDataSetChanged();

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Book book = (Book) parent.getItemAtPosition(position);

                Intent intent = new Intent(BookActivity.this, BookEditActivity.class);
                Bundle parms = new Bundle();
                parms.putString("code", book.getCode() + "");
                parms.putString("description", book.getDescription());
                parms.putString("title", book.getTitle());
                parms.putString("author", book.getAuthor());
                intent.putExtras(parms);
                startActivityForResult(intent, 0);
            }
        });

        // Apagar
        listViewBooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Book book = (Book) parent.getItemAtPosition(position);

                confirmDelete("Alert", "Delete book: '" + book.getTitle() + "'?", book);

                return true;
            }
        });


        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonFind).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonAdd:
                Intent intent = new Intent(BookActivity.this, BookEditActivity.class);
                Bundle parms = new Bundle();
                intent.putExtras(parms);
                startActivityForResult(intent, 0);
                break;

            case R.id.buttonFind:
                String x = this.textFilter.getText().toString();
                this.books = this.bookDao.findByTitle(x);
                this.adapter.clear();
                this.adapter.addAll(this.books);
                this.adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            boolean result = data.getExtras().getBoolean("result");

            if (result) {
                Dialog.alert("Success!", "Book successfully registered", this);
            } else {
                Dialog.alert("Error!", "Error trying to register Book", this);
            }
        }

        this.reload();
        this.clearForm();
    }

    public void confirmDelete(String title, String content, final Book book) {

        AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        bookDao.delete(book);
                        reload();
                        clearForm();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });

        builder.show();
    }

    private void reload() {
        this.books = this.bookDao.findAll();

        this.adapter.clear();
        this.adapter.addAll(books);

        this.adapter.notifyDataSetChanged();
    }

    private void clearForm() {
    }

}
