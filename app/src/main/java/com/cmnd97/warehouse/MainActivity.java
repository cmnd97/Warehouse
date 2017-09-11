package com.cmnd97.warehouse;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;

import com.cmnd97.warehouse.data.InventoryDbHelper;
import com.cmnd97.warehouse.data.StockItem;

public class MainActivity extends AppCompatActivity {

    InventoryDbHelper dbHelper;
    StockCursorAdapter adapter;
    int lastVisibleItem = 0;

    String product_name_title;
    String quantity_title;
    String price_title;
    String space_dots;
    SearchView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sv = (SearchView) findViewById(R.id.search_bar);
        dbHelper = new InventoryDbHelper(this);
        // to be used in the cursor adapter
        product_name_title = getString(R.string.product_name_title);
        quantity_title = getString(R.string.quantity_title);
        price_title = getString(R.string.price_title);
        space_dots = getString(R.string.space_dots);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PropsActivity.class);
                startActivity(intent);
            }
        });

        final ListView itemsList = (ListView) findViewById(R.id.items_list);

        itemsList.setEmptyView(findViewById(R.id.empty_view_image));

        Cursor cursor = dbHelper.readStock(sv.getQuery().toString());
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                      @Override
                                      public boolean onQueryTextSubmit(String query) {
                                          return true;
                                      }

                                      @Override
                                      public boolean onQueryTextChange(String newText) {
                                          adapter.swapCursor(dbHelper.readStock(sv.getQuery().toString()));
                                          return true;
                                      }
                                  }
        );

        adapter = new StockCursorAdapter(this, cursor);
        itemsList.setAdapter(adapter);
        itemsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) return;
                final int currentFirstVisibleItem = view.getFirstVisiblePosition();
                if (currentFirstVisibleItem > lastVisibleItem) {
                    fab.show();
                } else if (currentFirstVisibleItem < lastVisibleItem) {
                    fab.hide();
                }
                lastVisibleItem = currentFirstVisibleItem;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.swapCursor(dbHelper.readStock(sv.getQuery().toString()));
    }

    public void clickOnViewItem(long id) {
        Intent intent = new Intent(this, PropsActivity.class);
        intent.putExtra("itemId", id);
        startActivity(intent);
    }

    public void clickOnSale(long id, int quantity) {
        dbHelper.sellOneItem(id, quantity);
        adapter.swapCursor(dbHelper.readStock(sv.getQuery().toString()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_dummy_data:
                addDummyData();

                adapter.swapCursor(dbHelper.readStock(sv.getQuery().toString()));
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDummyData() {
        dbHelper.insertItem(new StockItem("Capacitor",
                "0.6 $",
                100,
                "Bourns GmbH",
                "+49 123 456",
                "sales@bourns.de",
                "android.resource://com.cmnd97.warehouse/drawable/capacitor"));

        dbHelper.insertItem(new StockItem("Quad OpAmp",
                "2 $",
                20,
                "Analog Devices Co",
                "+23 999 999",
                "sales@analog.com",
                "android.resource://com.cmnd97.warehouse/drawable/quad_opamp"));

        dbHelper.insertItem(new StockItem("Multimeter",
                "200 $",
                5,
                "Multicomp Ltd",
                "+00 001 002",
                "customer@multicomp.com",
                "android.resource://com.cmnd97.warehouse/drawable/multimeter"));

        dbHelper.insertItem(new StockItem("Oscilloscope",
                "1999 $",
                1,
                "ETTI SRL",
                "+40 21 212 212",
                "sales@etti.pub.ro",
                "android.resource://com.cmnd97.warehouse/drawable/oscilloscope"));

    }
}
