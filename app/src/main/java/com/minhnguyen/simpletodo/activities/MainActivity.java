package com.minhnguyen.simpletodo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minhnguyen.simpletodo.R;
import com.minhnguyen.simpletodo.database.ItemDatabase;
import com.minhnguyen.simpletodo.models.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by MINH on 17 Sep 2016.
 */
public class MainActivity extends AppCompatActivity {
    ItemDatabase mHelper;
    ArrayList<Item> items;
    ItemAdapter itemsAdapter;
    RecyclerView rvItems;
    FloatingActionButton fab;
    boolean setGridView;

    int id;

    public final int REQUEST_ADD = 9001;
    public final int REQUEST_VIEW = 9003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        setGridView = mSettings.getBoolean("setGridView", true);

        fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        mHelper = new ItemDatabase(this);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        refreshItems();
        id = mSettings.getInt("setId", 0);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mr.log", "Adding");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d 'at' HH:mm:ss");
                String mDate = simpleDateFormat.format(new Date());
                Intent intent = new Intent(MainActivity.this, EnterNewItem.class);
                intent.putExtra("new_date", mDate);

                startActivityForResult(intent, REQUEST_ADD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String mDate;
        String mSubject;
        String mContent;
        String mColor;
        String mStatus;

        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD) {
            // Add item
            id++;
            mDate = data.getExtras().getString("new_date");
            mSubject = data.getExtras().getString("new_subject");
            mContent = data.getExtras().getString("new_content");
            mColor = data.getExtras().getString("new_color");
            mStatus = data.getExtras().getString("new_status");
            Item item = new Item(id, mDate, mSubject, mContent, mColor, mStatus);

            mHelper.addItem(item, itemsAdapter.getItemCount());

            // Save id
            SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt("setId", id);
            editor.apply();
        }
        refreshItems();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about_me:
                startActivity(new Intent(this, AboutMeActivity.class));
                return true;
            case R.id.action_view:
                SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
                SharedPreferences.Editor editor = mSettings.edit();

                if (setGridView) {
                    setGridView = false;
                    item.setTitle("Grid View");
                } else {
                    setGridView = true;
                    item.setTitle("List View");
                }

                editor.putBoolean("setGridView", setGridView);
                editor.apply();
                refreshItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshItems() {
        items = mHelper.getAllItems();
        itemsAdapter = new ItemAdapter(this, items);
        rvItems.setAdapter(itemsAdapter);
        if (setGridView)
            rvItems.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        else
            rvItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    // Using RecyclerView
    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        int mPosition;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtSubject, txtContent, txtDate;
            LinearLayout btnDone, btnDelete, cellParent;
            ImageView imgViewDone;

            ViewHolder(View v) {
                super(v);

                txtSubject = (TextView) v.findViewById(R.id.txtSubject);
                txtContent = (TextView) v.findViewById(R.id.txtContent);
                txtDate = (TextView) v.findViewById(R.id.txtDate);
                btnDone = (LinearLayout) v.findViewById(R.id.btnDone);
                btnDelete = (LinearLayout) v.findViewById(R.id.btnDelete);
                cellParent = (LinearLayout) v.findViewById(R.id.cellParent);
                imgViewDone = (ImageView) v.findViewById(R.id.imgViewDone);
            }
        }

        private ArrayList<Item> mItems;
        private Context mContext;

        ItemAdapter(Context context, ArrayList<Item> mItems) {
            this.mContext = context;
            this.mItems = mItems;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.cell_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            mHelper = new ItemDatabase(mContext);
            final Item item = mItems.get(position);

            if (item.getStatus().equals("Undone")) {
                switch (item.getColor()) {
                    case "RED":
                        viewHolder.cellParent.setBackgroundResource(R.drawable.item_shape_red);
                        break;
                    case "BLUE":
                        viewHolder.cellParent.setBackgroundResource(R.drawable.item_shape_blue);
                        break;
                    case "YELLOW":
                        viewHolder.cellParent.setBackgroundResource(R.drawable.item_shape_yellow);
                        break;
                    case "GREEN":
                        viewHolder.cellParent.setBackgroundResource(R.drawable.item_shape_green);
                        break;
                    default:
                        break;
                }
                viewHolder.imgViewDone.setImageResource(R.drawable.ic_done_gray);

                viewHolder.cellParent.invalidate();
            } else {
                viewHolder.cellParent.setBackgroundResource(R.drawable.item_shape_done);
                viewHolder.imgViewDone.setImageResource(R.drawable.ic_clear_gray);

                viewHolder.cellParent.invalidate();
            }

            if (item.getSubject().equals("No Subject")) {
                viewHolder.txtSubject.setTypeface(null, Typeface.BOLD_ITALIC);
                viewHolder.txtSubject.setText(item.getSubject());
            } else {
                viewHolder.txtSubject.setText(item.getSubject());
            }
            viewHolder.txtContent.setText(item.getContent());
            viewHolder.txtDate.setText(item.getDate());
            viewHolder.cellParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("mr.log", "Viewing");
                    mPosition = position;
                    Intent intent = new Intent(MainActivity.this, ItemView.class);
                    Bundle bundle = new Bundle();

                    bundle.putInt("id_to_view", mItems.get(position).getId());
                    bundle.putString("date_to_view", mItems.get(position).getDate());
                    bundle.putString("subject_to_view", mItems.get(position).getSubject());
                    bundle.putString("content_to_view", mItems.get(position).getContent());
                    bundle.putString("color_to_view", mItems.get(position).getColor());
                    bundle.putString("status_to_view", mItems.get(position).getStatus());
                    bundle.putInt("position_to_view", position);

                    intent.putExtras(bundle);
                    ((Activity) mContext).startActivityForResult(intent, REQUEST_VIEW);
                }
            });
            viewHolder.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getStatus().equals("Undone")) {
                        Log.d("mr.log", "Done");
                        item.setStatus("Done");
                    } else if (item.getStatus().equals("Done")) {
                        Log.d("mr.log", "Undone");
                        item.setStatus("Undone");
                    }
                    mHelper.updateItem(item, position);

                    notifyItemChanged(position);
                }
            });
            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.d("mr.log", "Deleting");
                    AlertDialog.Builder ask = new AlertDialog.Builder(mContext);
                    ask.setMessage("Are you sure you want to REMOVE this item?" + "\n\n\tSubject: "
                            + item.getSubject()).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mHelper.deleteItem(mItems.get(position).getId(), position);

                            Snackbar.make(v, R.string.deleted_snackbar_text, Snackbar.LENGTH_SHORT).show();
                            refreshItems();
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
        public int getItemCount() {
            return mItems.size();
        }
    }
}