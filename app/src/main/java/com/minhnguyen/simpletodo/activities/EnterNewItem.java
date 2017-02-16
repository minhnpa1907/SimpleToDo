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
public class EnterNewItem extends AppCompatActivity implements View.OnClickListener {
    EditText editNewContent, editNewSubject;
    TextView txtDate;
    ImageButton btnRed, btnBlue, btnYellow, btnGreen;
    String date, color, status;
    LinearLayout parentNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editNewSubject = (EditText) findViewById(R.id.editNewSubject);
        editNewContent = (EditText) findViewById(R.id.editNewContent);
        btnRed = (ImageButton) findViewById(R.id.btnRed);
        btnBlue = (ImageButton) findViewById(R.id.btnBlue);
        btnYellow = (ImageButton) findViewById(R.id.btnYellow);
        btnGreen = (ImageButton) findViewById(R.id.btnGreen);
        txtDate = (TextView) findViewById(R.id.txtDate);
        date = getIntent().getStringExtra("new_date");
        color = "RED";
        status = "Undone";
        parentNewItem = (LinearLayout) findViewById(R.id.parentNewItem);

        editNewContent.requestFocus();
        editNewContent.setSelection(0);
        txtDate.setText(date);
        btnRed.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
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

                if ("".equalsIgnoreCase(editNewContent.getText().toString()))
                    Toast.makeText(this, "Please enter some content!", Toast.LENGTH_SHORT).show();
                else {
                    Bundle bundle = new Bundle();

                    bundle.putString("new_date", date);
                    if (editNewSubject.getText().toString().equals("")) {
                        bundle.putString("new_subject", "No Subject");
                    } else {
                        bundle.putString("new_subject", editNewSubject.getText().toString());
                    }
                    bundle.putString("new_content", editNewContent.getText().toString());
                    bundle.putString("new_color", color);
                    bundle.putString("new_status", status);

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
            btnRed.setBackgroundResource(R.drawable.color_red_selected);
            parentNewItem.setBackgroundResource(R.drawable.item_shape_red);
            refreshColor(color);
        } else if (v.getId() == R.id.btnBlue) {
            color = "BLUE";
            btnBlue.setBackgroundResource(R.drawable.color_blue_selected);
            parentNewItem.setBackgroundResource(R.drawable.item_shape_blue);
            refreshColor(color);
        } else if (v.getId() == R.id.btnYellow) {
            color = "YELLOW";
            btnYellow.setBackgroundResource(R.drawable.color_yellow_selected);
            parentNewItem.setBackgroundResource(R.drawable.item_shape_yellow);
            refreshColor(color);
        } else if (v.getId() == R.id.btnGreen) {
            color = "GREEN";
            btnGreen.setBackgroundResource(R.drawable.color_green_selected);
            parentNewItem.setBackgroundResource(R.drawable.item_shape_green);
            refreshColor(color);
        }
    }

    public void refreshColor(String color) {
        switch (color) {
            case "RED":
                btnBlue.setBackgroundResource(R.drawable.color_blue);
                btnYellow.setBackgroundResource(R.drawable.color_yellow);
                btnGreen.setBackgroundResource(R.drawable.color_green);
                break;
            case "BLUE":
                btnRed.setBackgroundResource(R.drawable.color_red);
                btnYellow.setBackgroundResource(R.drawable.color_yellow);
                btnGreen.setBackgroundResource(R.drawable.color_green);
                break;
            case "YELLOW":
                btnRed.setBackgroundResource(R.drawable.color_red);
                btnBlue.setBackgroundResource(R.drawable.color_blue);
                btnGreen.setBackgroundResource(R.drawable.color_green);
                break;
            case "GREEN":
                btnRed.setBackgroundResource(R.drawable.color_red);
                btnBlue.setBackgroundResource(R.drawable.color_blue);
                btnYellow.setBackgroundResource(R.drawable.color_yellow);
                break;
            default:
                break;
        }
    }
}
