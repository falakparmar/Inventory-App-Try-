package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.inventoryapp.Product.ProductContract.ProductEntry ;

public class Edititems extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int GET_ITEM = 1;
    private static int Load = 1;
    public int itemPrice = 0;
    public int itemQuantity = 0;
    String item_image = null;
    Uri imageUri;
    private EditText EditName;
    private EditText EditQuantity;
    private EditText EditPrice;
    private ImageView EditImage;
    private boolean hasItemChanged = false;
    private Uri ItemUri = null;
    private View.OnTouchListener touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hasItemChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edititems);
        Intent intent = getIntent();
        ItemUri = intent.getData();
        Button delete = (Button) findViewById(R.id.delete_item);
        if (ItemUri == null) {
            imageUri = Uri.parse("android.resource://" + this.getPackageName() + "/drawable/pic");
            setTitle(getText(R.string.add_item));
            delete.setVisibility(View.GONE);
        } else {
            setTitle(getText(R.string.update_item));
            getLoaderManager().initLoader(Load, null, (android.app.LoaderManager.LoaderCallbacks<Object>) this);
        }

        EditName = (EditText) findViewById(R.id.name);
        EditQuantity = (EditText) findViewById(R.id.quantity_item);
        EditImage = (ImageView) findViewById(R.id.item_image);
        EditPrice = (EditText) findViewById(R.id.price_item);

        EditName.setOnTouchListener(touch);
        EditQuantity.setOnTouchListener(touch);
        EditImage.setOnTouchListener(touch);
        EditPrice.setOnTouchListener(touch);

        Button decrease = (Button) findViewById(R.id.dec);
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity_edit = EditQuantity.getText().toString();
                if (TextUtils.isEmpty(quantity_edit)) {
                    EditQuantity.setText("0");
                }
                int quantity = Integer.parseInt(EditQuantity.getText().toString());
                if (quantity <= 0) {
                    Toast.makeText(Edititems.this, getText(R.string.buy_something), Toast.LENGTH_SHORT).show();
                } else {
                    quantity--;
                }
                EditQuantity.setText("" + quantity);
            }
        });
        Button increase = (Button) findViewById(R.id.inc);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity_edit = EditQuantity.getText().toString();
                if (TextUtils.isEmpty(quantity_edit)) {
                    EditQuantity.setText("0");
                }
                int quantity = Integer.parseInt(EditQuantity.getText().toString());
                quantity++;
                EditQuantity.setText("" + quantity);
            }
        });
        Button save_item = (Button) findViewById(R.id.save_item);
        save_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
                finish();
            }
        });
        Button add_item = (Button) findViewById(R.id.image_item);
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Select Image From"), GET_ITEM);
                }
            }
        });
        Button online_search = (Button) findViewById(R.id.online);
        online_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = EditName.getText().toString();
                String url = getText(R.string.url) + query;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        Button deleteButton = (Button) findViewById(R.id.delete_item);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        Button order_item = (Button) findViewById(R.id.order_item);
        order_item.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                String ItemName = EditName.getText().toString();
                String ItemQuantity = EditQuantity.getText().toString();
                Intent intent1 = new Intent(Intent.ACTION_SENDTO);
                intent1.setData(Uri.parse(getString(R.string.mail)));
                intent1.putExtra(intent1.EXTRA_TEXT, "Order Summary" +"\n"+ "Item Name = " + ItemName + "\n" + "Price : " + itemPrice + "\n" + "Quantity of item : " + ItemQuantity);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onActivityResult(int req, int result, Intent info) {
        if (req == GET_ITEM && result == RESULT_OK) {
            Uri imageUri = info.getData();
            item_image = imageUri.toString();
            EditImage.setImageURI(imageUri);
        }
    }

    @Override
    public void onBackPressed() {
        if (!hasItemChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discard = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
        showUnsavedChangesDialog(discard);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {ProductEntry._ID, ProductEntry.COLUMN_NAME, ProductEntry.COLUMN_QUANTITY, ProductEntry.COLUMN_IMAGE, ProductEntry.COLUMN_PRICE};
        return new CursorLoader(this, ItemUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor info) {
        if (info == null || info.getCount() < 1) {
            return;
        }
        if (info.moveToFirst()) {
            int ItemIndex1 = info.getColumnIndex(ProductEntry.COLUMN_NAME);
            int ItemIndex2 = info.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
            int ItemIndex3 = info.getColumnIndex(ProductEntry.COLUMN_IMAGE);
            int ItemIndex4 = info.getColumnIndex(ProductEntry.COLUMN_PRICE);

            String name = info.getString(ItemIndex1);
            int quantity = info.getInt(ItemIndex2);
            String image = info.getString(ItemIndex3);
            int prices = info.getInt(ItemIndex4);

            EditName.setText(name);
            EditQuantity.setText(Integer.toString(quantity));
            EditPrice.setText(Integer.toString(prices));
            imageUri = Uri.parse(image);
            ImageView imageView = (ImageView) findViewById(R.id.item_image);
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        EditName.setText("");
        EditQuantity.setText("");
        EditImage.setImageResource(R.drawable.im);
        EditPrice.setText("");
    }

    private void showDialog() {
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
                deleteitem();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveItem() {
        String name = EditName.getText().toString().trim();
        String price = EditPrice.getText().toString().trim();
        String quantity = EditQuantity.getText().toString().trim();
        String image = imageUri.toString();
        if (ItemUri == null && TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(quantity) || TextUtils.isEmpty(image)) {
            Toast.makeText(Edititems.this, getText(R.string.enter_details), Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_NAME, name);
        if (!TextUtils.isEmpty(price)) {
            itemPrice = Integer.parseInt(price);
        }
        contentValues.put(ProductEntry.COLUMN_PRICE, itemPrice);
        if (!TextUtils.isEmpty(quantity)) {
            itemQuantity = Integer.parseInt(quantity);
        }
        contentValues.put(ProductEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(ProductEntry.COLUMN_IMAGE, image);
        if (ItemUri == null) {
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
            if (newUri != null) {
                Toast.makeText(this, getText(R.string.item_inserted),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getText(R.string.item_not_inserted),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(ItemUri, contentValues, null, null);
            if (rowsAffected != 0) {
                Toast.makeText(this, getText(R.string.item_inserted),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getText(R.string.item_not_inserted),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getText(R.string.changes_are_not_saved));
        builder.setPositiveButton(getText(R.string.leave), discardButtonClickListener);
        builder.setNegativeButton(getText(R.string.dont_leave), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void orderMore() {
        EditText editName = (EditText) findViewById(R.id.name);
        EditText quantity = (EditText) findViewById(R.id.quantity_item);
        String productName = editName.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:falak.p19@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, productName);
        intent.putExtra(Intent.EXTRA_TEXT, quantity.getText().toString() + " more " + productName + " required.");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void deleteitem() {
        if (ItemUri != null) {
            int rowsDeleted = getContentResolver().delete(ItemUri, null, null);
            if (rowsDeleted != 0) {
                Toast.makeText(this, getText(R.string.deleted),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getText(R.string.not_deleted),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}