package jv.cadastrodelivros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jv.cadastrodelivros.dao.BookDao;
import jv.cadastrodelivros.dao.CategoryDao;
import jv.cadastrodelivros.model.Book;
import jv.cadastrodelivros.model.Category;
import jv.cadastrodelivros.utils.Dialog;

public class BookEditActivity extends AppCompatActivity implements View.OnClickListener {

    // DAOSs
    private CategoryDao categoryDao;
    private BookDao bookDao;

    // Elementos
    private EditText textCode;
    private EditText textAuthor;
    private EditText textTitle;
    private EditText textDescription;
    private Category comboCategory;

    List<Category> categories = new ArrayList<>();

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        this.categoryDao = new CategoryDao(this);
        this.bookDao = new BookDao(this);

        this.textCode = ((EditText) findViewById(R.id.editTextCode));
        this.textTitle = ((EditText) findViewById(R.id.editTextTitle));
        this.textDescription = ((EditText) findViewById(R.id.editTextDescription));
        this.textAuthor = ((EditText) findViewById(R.id.editTextAuthor));

        this.textCode.setEnabled(false);

        findViewById(R.id.buttonSave).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);
        //

        this.categories = this.categoryDao.findAll();

        // Combobox
        this.adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                this.categories);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                comboCategory = (Category) adapterView.getItemAtPosition(position);


//                if (item != null) {
//                    Toast.makeText(BookEditActivity.this, item.toString(),
//                            Toast.LENGTH_SHORT).show();
//                }
//                Toast.makeText(BookEditActivity.this, "Selected",
//                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Pegando os parametros
        Bundle args = getIntent().getExtras();
        if (getIntent().hasExtra("code")) {
            textCode.setText(args.getString("code"));
            textDescription.setText(args.getString("description"));
            textTitle.setText(args.getString("title"));
            textAuthor.setText(args.getString("author"));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonSave:

                Book result;

                if (comboCategory == null) {
                    Dialog.alert("Alert!", "Required category", this);
                } else if (textTitle.getText().toString().trim().equals("")) {
                    Dialog.alert("Alert!", "Required title", this);
                    textTitle.requestFocus();
                } else if (textTitle.getText().toString().trim().equals("")) {
                    Dialog.alert("Alert!", "Required title", this);
                    textTitle.requestFocus();
                } else if (textAuthor.getText().toString().trim().equals("")) {
                    Dialog.alert("Alert!", "Required author", this);
                    textAuthor.requestFocus();
                } else if (textDescription.getText().toString().trim().equals("")) {
                    Dialog.alert("Alert!", "Required description", this);
                    textDescription.requestFocus();
                } else {

                    if (textCode.getText().toString().trim().equals("")){

                        Book book = new Book();
                        book.setCode(0);
                        book.setCategory(this.comboCategory);
                        book.setTitle(this.textTitle.getText().toString());
                        book.setDescription(this.textDescription.getText().toString());
                        book.setAuthor(this.textAuthor.getText().toString());

                        result = this.bookDao.insert(book);

                    } else {

                        Book book = new Book();
                        book.setCode(Integer.parseInt(textCode.getText().toString()));
                        book.setCategory(this.comboCategory);
                        book.setTitle(this.textTitle.getText().toString());
                        book.setDescription(this.textDescription.getText().toString());
                        book.setAuthor(this.textAuthor.getText().toString());

                        result = this.bookDao.update(book);
                    }

                    Intent data = new Intent();
                    Bundle parameters = new Bundle();

                    if (result != null) {
                        this.clearForm();
                        parameters.putBoolean("result", true);
                        //Dialog.alert("Success!", "Book successfully registered", this);
                    } else {
                        parameters.putBoolean("result", false);
                        //Dialog.alert("Error!", "Error trying to register Book", this);
                    }

                    data.putExtras(parameters);
                    setResult(RESULT_OK, data);
                    finish();

                }

                break;

            case R.id.buttonClear:
                this.clearForm();
                break;
        }
    }

    // Limpar o fomularios
    public void clearForm() {
        this.textTitle.setText("");
        this.textAuthor.setText("");
        this.textDescription.setText("");
    }

}
