package jv.cadastrodelivros;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jv.cadastrodelivros.dao.CategoryDao;
import jv.cadastrodelivros.model.Category;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textCode;
    private EditText textDescription;

    private CategoryDao categoryDao;

    List<Category> categories = new ArrayList<>();

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        this.categoryDao = new CategoryDao(this);

        this.textCode = ((EditText) findViewById(R.id.editTextCode));
        this.textDescription = ((EditText) findViewById(R.id.editTextDescription));

        findViewById(R.id.buttonSave).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);

        textCode.setEnabled(false);

        // List view
        ListView listViewCategories = findViewById(R.id.listViewCategories);

        this.categories = this.categoryDao.findAll();

        this.adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                this.categories);

        listViewCategories.setAdapter(this.adapter);

        this.adapter.notifyDataSetChanged();

        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Category category = (Category) parent.getItemAtPosition(position);

                textCode.setText(category.getCode() + "");
                textDescription.setText(category.getDescription());
            }
        });

        // Apagar
        listViewCategories.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Category category = (Category) parent.getItemAtPosition(position);

                confirmDelete("Alert", "Delete category: '" + category.getDescription() + "'?", category);

                return true;
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonSave:

                if (!textDescription.getText().toString().trim().equals("")) {

                    Category result;

                    if (textCode.getText().toString().trim().equals("")) {
                        Category categoryNew = new Category(0,
                                textDescription.getText().toString()
                        );

                        result = this.categoryDao.insert(categoryNew);
                    } else {
                        Category categoryEdit = new Category(
                                Integer.parseInt(textCode.getText().toString()),
                                textDescription.getText().toString()
                        );

                        result = this.categoryDao.update(categoryEdit);
                    }


                    if (result != null) {
                        this.alert("Success!", "Category successfully registered");
                    } else {
                        this.alert("Error!", "Error trying to register category");
                    }

                    this.reload();
                    this.clearForm();
                }

                break;

            case R.id.buttonClear:
                this.clearForm();
                break;
        }

    }

    private void reload() {
        this.categories = this.categoryDao.findAll();

        this.adapter.clear();
        this.adapter.addAll(categories);

        this.adapter.notifyDataSetChanged();
    }

    public void clearForm() {
        this.textCode.setText("");
        this.textDescription.setText("");
    }

    public void alert(String title, String content) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setNeutralButton(getString(R.string.dialog_alert_button_ok), null);

        builder.show();
    }

    public void confirmDelete(String title, String content, final Category category) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        categoryDao.delete(category);
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
}
