package com.subratgupta.epaper;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.pdf.PdfDocument;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PaperAdapter.ItemClickListener {

    private static final String TAG = "MainActivity";
    ArrayList<PageType> pageTypeArrayList = new ArrayList<>();
    PaperAdapter paperAdapter;
    String url;
    WebView webView;
    public static DatabaseReference db;

    String mPaper;
    String mCity;
    String mDate;

    Spinner paperListSpinner;
    List<String> mPaperList = new ArrayList<String>();

    Spinner cityListSpinner;
    List<String> mCityList = new ArrayList<String>();

    Spinner dateListSpinner;
    List<String> mDateList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        webView = (WebView) findViewById(R.id.webview);

        db = FirebaseDatabase.getInstance().getReference();
        paperListSpinner = (Spinner) findViewById(R.id.paper_list_spinner);
        paperListSpinner.setLayoutParams(new LinearLayout.LayoutParams(0,
                120, 1f));
        cityListSpinner = (Spinner) findViewById(R.id.city_list_spinner);
        dateListSpinner = (Spinner) findViewById(R.id.date_list_spinner);
        paperFill();
        paperListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPaper = mPaperList.get((int) id);
                if (position>0){
                    paperListSpinner.setLayoutParams(new LinearLayout.LayoutParams(0,
                            120, 1f));
                    cityListSpinner.setLayoutParams(new LinearLayout.LayoutParams(0,
                            120, 2f));
//                    paperListSpinner.setVisibility(View.GONE);
//                    cityListSpinner.setVisibility(View.VISIBLE);
                }
                cityFill(mPaper);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cityListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCity = mCityList.get((int) id);
                if (position>0){
                    paperListSpinner.setLayoutParams(new LinearLayout.LayoutParams(0,
                            120, 1f));
                    cityListSpinner.setLayoutParams(new LinearLayout.LayoutParams(0,
                            120, 1f));
                    dateListSpinner.setLayoutParams(new LinearLayout.LayoutParams(0,
                            120, 2f));
                }
                dateFill(mPaper, mCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dateListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDate = mDateList.get((int) id);
                if (position>0){
                    paperListSpinner.setLayoutParams(new LinearLayout.LayoutParams(0,
                            120, 1f));
                    cityListSpinner.setLayoutParams(new LinearLayout.LayoutParams(0,
                            120, 1f));
                    dateListSpinner.setLayoutParams(new LinearLayout.LayoutParams(0,
                            120, 1f));
                }

                setPageList(mPaper, mCity, mDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void paperFill() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> paperList = new ArrayList<String>();
                paperList.add("Choose a paper");

                for (DataSnapshot citySnapshot : dataSnapshot.child("papers").getChildren()) {
                    String city = citySnapshot.getKey();
                    paperList.add(city);
                }

                mPaperList = paperList;

                ArrayAdapter<String> papersAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, paperList);
                papersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                paperListSpinner.setAdapter(papersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "databaseError", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void cityFill(final String paper) {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> cityList = new ArrayList<String>();
                cityList.add("Choose a city");

                for (DataSnapshot citySnapshot : dataSnapshot.child("papers").child(paper).getChildren()) {
                    String city = citySnapshot.getKey();
                    cityList.add(city);
                }

                mCityList = cityList;

                ArrayAdapter<String> citysAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, cityList);
                citysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cityListSpinner.setAdapter(citysAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "databaseError", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void dateFill(final String paper, final String city) {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> dateList = new ArrayList<String>();
                dateList.add("Choose a date");

                for (DataSnapshot dateSnapshot : dataSnapshot.child("papers").child(paper).child(city).getChildren()) {
                    String date = dateSnapshot.getKey();
                    dateList.add(date);
                }

                mDateList = dateList;

                ArrayAdapter<String> datesAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, dateList);
                datesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dateListSpinner.setAdapter(datesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "databaseError", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setPageList(final String paper, final String city, final String date) {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pageTypeArrayList.clear();
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array

                for (DataSnapshot page : dataSnapshot.child("papers").child(paper).child(city).child(date).getChildren()) {
                    PageType id = page.getValue(PageType.class);
                    pageTypeArrayList.add(id);
                }

                RecyclerView recyclerView = findViewById(R.id.paper__recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                paperAdapter = new PaperAdapter(MainActivity.this, pageTypeArrayList);
                paperAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(paperAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getApplicationContext(), pageTypeArrayList.get(position).getName(), Toast.LENGTH_LONG).show();
    }

}
