package com.ruzz.sqlrussel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etStdnId, etStdnName, etStdnProg;
    Button btAdd, btDelete, btSearch, btView;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etStdnId = findViewById(R.id.Russid);
        etStdnName = findViewById(R.id.Russname);
        etStdnProg = findViewById(R.id.Russprogram);

        btAdd = findViewById(R.id.RussAdd);
        btDelete = findViewById(R.id.RussDelete);
        btSearch = findViewById(R.id.RussSearch);
        btView = findViewById(R.id.RussView);

        btAdd.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        btView.setOnClickListener(this);

        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS student(stdnt_id VARCHAR PRIMARY KEY, stdnt_name VARCHAR NOT NULL, stdnt_program VARCHAR NOT NULL)");

        etStdnId.requestFocus();

    }

    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        etStdnId = (findViewById(R.id.Russid));
        etStdnId.getText().clear();
        etStdnName = (findViewById(R.id.Russname));
        etStdnName.getText().clear();
        etStdnProg = (findViewById(R.id.Russprogram));
        etStdnProg.getText().clear();
    }

    @Override
    public void onClick(View v) {
        if (v == btAdd) {
            db.execSQL("INSERT INTO student VALUES('" + etStdnId.getText() + "','" + etStdnName.getText() + "','" + etStdnProg.getText() + "');");
            showMessage("Success", "Record Added.");
            clearText();
        } else if (v == btDelete) {
            Cursor c = db.rawQuery("DELETE FROM student WHERE stdnt_id= '" + etStdnId.getText() + "'", null);
            if (c.moveToFirst()) {
                db.execSQL("DELETE FROM student WHERE stdnt_id= '" + etStdnId.getText() + "'");
                showMessage("Success", "Record Deleted.");
            }
            clearText();
        } else if (v == btSearch) {
            Cursor c = db.rawQuery("SELECT * FROM student WHERE stdnt_id= '" + etStdnId.getText() + "'", null);
            StringBuffer buffer = new StringBuffer();
            if (c.moveToFirst()) {
                buffer.append("Name: " + c.getString(1) + "\n");
                buffer.append("Program: " + c.getString(2) + "\n\n");
            }
            showMessage("Student Details", buffer.toString());
        } else if (v == btView) {
            Cursor res = db.rawQuery("SELECT * FROM student", null);
            if (res.getCount() == 0) {
                showMessage("ERROR", "No records found.");
            }
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext())
            {
                buffer.append("ID: "+res.getString(0)+"\n");
                buffer.append("Name: "+res.getString(1)+"\n");
                buffer.append("Program: "+res.getString(2)+"\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }
    }
}