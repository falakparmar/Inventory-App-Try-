package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.inventoryapp.Product.ProductAdapter;
import com.example.android.inventoryapp.Product.ProductContract;

import com.example.android.inventoryapp.Product.ProductContract.ProductEntry;
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int Item_Loader = 1;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list_item);
        View empty = findViewById(R.id.empty);
        listView.setEmptyView(empty);
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Edititems.class);
                startActivity(intent);
            }
        });
        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
        getLoaderManager().initLoader(Item_Loader, null, (android.app.LoaderManager.LoaderCallbacks<Object>) this);
        adapter = new ProductAdapter(this, null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                Intent intent = new Intent(MainActivity.this, Edititems.class);
                Uri ItemUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
                intent.setData(ItemUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(Item_Loader, null, (android.app.LoaderManager.LoaderCallbacks<Object>) this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {ProductContract.ProductEntry._ID, ProductEntry.COLUMN_NAME, ProductEntry.COLUMN_QUANTITY,ProductEntry.COLUMN_PRICE};
        return new CursorLoader(this, ProductEntry.CONTENT_URI, projection, null, null, null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getText(R.string.delete));
        builder.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}