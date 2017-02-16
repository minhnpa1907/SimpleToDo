package com.minhnguyen.simpletodo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minhnguyen.simpletodo.R;
import com.minhnguyen.simpletodo.database.ItemDatabase;
import com.minhnguyen.simpletodo.models.Item;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MINH NPA on 24 Sep 2016.
 */

public class ItemView extends AppCompatActivity {
    TextView txtSubject, txtContent, txtDate;
    LinearLayout btnDone, btnEdit, btnDelete, cellParent;
    ImageView imgViewDone;
    String date, subject, content, color, status;
    int id, position;

    ItemDatabase mHelper = new ItemDatabase(this);
    Item item;

    public final int REQUEST_EDIT = 9002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtSubject = (TextView) findViewById(R.id.txtSubject);
        txtContent = (TextView) findViewById(R.id.txtContent);
        txtDate = (TextView) findViewById(R.id.txtDate);
        btnDone = (LinearLayout) findViewById(R.id.btnDone);
        btnEdit = (LinearLayout) findViewById(R.id.btnEdit);
        btnDelete = (LinearLayout) findViewById(R.id.btnDelete);
        cellParent = (LinearLayout) findViewById(R.id.cellParent);
        imgViewDone = (ImageView) findViewById(R.id.imgViewDone);

        id = getIntent().getIntExtra("id_to_view", 0);
        date = getIntent().getStringExtra("date_to_view");
        subject = getIntent().getStringExtra("subject_to_view");
        content = getIntent().getStringExtra("content_to_view");
        color = getIntent().getStringExtra("color_to_view");
        status = getIntent().getStringExtra("status_to_view");
        position = getIntent().getIntExtra("position_to_view", 0);
        txtContent.setMovementMethod(new ScrollingMovementMethod());
        item = new Item(id, date, subject, content, color, status);

        refreshItems();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("Undone")) {
                    item.setStatus("Done");
                } else if (status.equals("Done")) {
                    item.setStatus("Undone");
                }
                mHelper.updateItem(item, position);

                setResult(RESULT_OK);
                finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mr.log", "Updating");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d 'at' HH:mm:ss");
                date = simpleDateFormat.format(new Date());
                Intent intent = new Intent(ItemView.this, EditItemActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("date_to_edit", date);
                if (subject.equals("No Subject")) {
                    bundle.putString("subject_to_edit", "");
                } else {
                    bundle.putString("subject_to_edit", subject);
                }
                bundle.putString("content_to_edit", content);
                bundle.putString("color_to_edit", color);

                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_EDIT);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mr.log", "Deleting");
                AlertDialog.Builder ask = new AlertDialog.Builder(ItemView.this);
                ask.setMessage("Are you sure you want to REMOVE this item?" + "\n\n\tSubject: "
                        + item.getSubject()).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHelper.deleteItem(item.getId(), position);

                        setResult(RESULT_OK);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("mr.log", "Delete Canceled");
                        dialog.cancel();
                    }
                });
                AlertDialog alert = ask.create();
                alert.setTitle("Warning!!!");
                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT) {
            // Edit item
            date = data.getExtras().getString("date_edited");
            subject = data.getExtras().getString("subject_edited");
            content = data.getExtras().getString("content_edited");
            color = data.getExtras().getString("color_edited");
            status = data.getExtras().getString("status_edited");
            item = new Item(id, date, subject, content, color, status);

            mHelper.updateItem(item, position);
            refreshItems();
        }

        setResult(RESULT_OK, data);
    }

    public void refreshItems() {
        if (status.equals("Undone")) {
            switch (color) {
                case "RED":
                    cellParent.setBackgroundResource(R.drawable.item_shape_red);
                    break;
                case "YELLOW":
                    cellParent.setBackgroundResource(R.drawable.item_shape_yellow);
                    break;
                case "BLUE":
                    cellParent.setBackgroundResource(R.drawable.item_shape_blue);
                    break;
                case "GREEN":
                    cellParent.setBackgroundResource(R.drawable.item_shape_green);
                    break;
                default:
                    break;
            }
            imgViewDone.setImageResource(R.drawable.ic_done_gray);

            cellParent.invalidate();
        } else {
            cellParent.setBackgroundResource(R.drawable.item_shape_done);
            imgViewDone.setImageResource(R.drawable.ic_clear_gray);

            cellParent.invalidate();
        }

        if (subject.equals("No Subject")) {
            txtSubject.setText(subject);
            txtSubject.setTypeface(null, Typeface.BOLD_ITALIC);
        } else {
            txtSubject.setText(subject);
        }
        txtContent.setText(content);
        txtDate.setText(date);
    }
}
