package com.cmnd97.warehouse;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmnd97.warehouse.data.StockContract;

public class StockCursorAdapter extends CursorAdapter {


    private final MainActivity activity;

    public StockCursorAdapter(MainActivity context, Cursor c) {
        super(context, c, 0);
        this.activity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.product_quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.product_price);
        ImageView sale = (ImageView) view.findViewById(R.id.product_sale);
        ImageView image = (ImageView) view.findViewById(R.id.product_image);

        String name = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_NAME));
        final int quantity = cursor.getInt(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_QUANTITY));
        String price = cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_PRICE));

        image.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(StockContract.StockEntry.COLUMN_IMAGE))));


        nameTextView.setText("" + activity.product_name_title + activity.space_dots + name);
        quantityTextView.setText("" + activity.quantity_title + activity.space_dots + quantity);
        priceTextView.setText("" + activity.price_title + activity.space_dots + price);

        final long id = cursor.getLong(cursor.getColumnIndex(StockContract.StockEntry._ID));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickOnViewItem(id);
            }
        });

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickOnSale(id,
                        quantity);
            }
        });
    }
}
