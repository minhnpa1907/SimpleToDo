package com.minhnguyen.simpletodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minhnguyen.simpletodo.R;

/**
 * Created by MINH on 17 Sep 2016.
 */
public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {
    String content, subject, color, date, status;
    EditText editContent, editSubject;
    ImageButton btnRed, btnBlue, btnYellow, btnGreen;
    TextView txtDate;
    LinearLayout parentEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnRed = (ImageButton) findViewById(R.id.btnRed);
        btnBlue = (ImageButton) findViewById(R.id.btnBlue);
        btnYellow = (ImageButton) findViewById(R.id.btnYellow);
        btnGreen = (ImageButton) findViewById(R.id.btnGreen);
        txtDate = (TextView) findViewById(R.id.txtDate);
        editSubject = (EditText) findViewById(R.id.editSubject);
        editContent = (EditText) findViewById(R.id.editContent);
        date = getIntent().getStringExtra("date_to_edit");
        subject = getIntent().getStringExtra("subject_to_edit");
        content = getIntent().getStringExtra("content_to_edit");
        color = getIntent().getStringExtra("color_to_edit");
        status = "Undone";
        parentEditItem = (LinearLayout) findViewById(R.id.parentEditItem);

        txtDate.setText(date);
        editSubject.setText(subject);
        editContent.setText(content);
        editContent.requestFocus();
        editContent.setSelection(editContent.getText().length());
        btnRed.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        refreshColor(color);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miOK:
                Intent data = new Intent();
                if ("".equalsIgnoreCase(editContent.getText().toString()))
                    Toast.makeText(this, "Please enter some content!", Toast.LENGTH_SHORT).show();
                else {
                    Bundle bundle = new Bundle();

                    bundle.putString("date_edited", date);
                    if (editSubject.getText().toString().equals("")) {
                        bundle.putString("subject_edited", "No Subject");
                    } else {
                        bundle.putString("subject_edited", editSubject.getText().toString());
                    }
                    bundle.putString("content_edited", editContent.getText().toString());
                    bundle.putString("color_edited", color);
                    bundle.putString("status_edited", status);

                    data.putExtras(bundle);
                    setResult(RESULT_OK, data);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRed) {
            color = "RED";
            refreshColor(color);
        } else if (v.getId() == R.id.btnBlue) {
            color = "BLUE";
            refreshColor(color);
        } else if (v.getId() == R.id.btnYellow) {
            color = "YELLOW";
            refreshColor(color);
        } else if (v.getId() == R.id.btnGreen) {
            color = "GREEN";
            refreshColor(color);
        }
    }

    public void refreshColor(String color) {
        switch (color) {
            case "RED":
                btnRed.setBackgroundResource(R.drawable.color_red_selected);
                btnBlue.setBackgroundResource(R.drawable.color_blue);
                btnYellow.setBackgroundResource(R.drawable.color_yellow);
                btnGreen.setBackgroundResource(R.drawable.color_green);
                parentEditItem.setBackgroundResource(R.drawable.item_shape_red);
                break;
            case "BLUE":
                btnRed.setBackgroundResource(R.drawable.color_red);
                btnBlue.setBackgroundResource(R.drawable.color_blue_selected);
                btnYellow.setBackgroundResource(R.drawable.color_yellow);
                btnGreen.setBackgroundResource(R.drawable.color_green);
                parentEditItem.setBackgroundResource(R.drawable.item_shape_blue);
                break;
            case "YELLOW":
                btnRed.setBackgroundResource(R.drawable.color_red);
                btnBlue.setBackgroundResource(R.drawable.color_blue);
                btnYellow.setBackgroundResource(R.drawable.color_yellow_selected);
                btnGreen.setBackgroundResource(R.drawable.color_green);
                parentEditItem.setBackgroundResource(R.drawable.item_shape_yellow);
                break;
            case "GREEN":
                btnRed.setBackgroundResource(R.drawable.color_red);
                btnBlue.setBackgroundResource(R.drawable.color_blue);
                btnYellow.setBackgroundResource(R.drawable.color_yellow);
                btnGreen.setBackgroundResource(R.drawable.color_green_selected);
                parentEditItem.setBackgroundResource(R.drawable.item_shape_green);
                break;
            default:
                break;
        }
    }
}
