package com.developer.tracksy;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class HomeScreen extends AppCompatActivity {
    private static final String TAG ="VACCINE_TRACKER";
    BottomSheetBehavior bottomSheetBehavior;
    String txnID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        View view2 = findViewById(R.id.bottom_sheet);
        ImageButton back = view2.findViewById(R.id.backArrow);
        CardView searchScreen = view2.findViewById(R.id.cv_searchScreen);
        CardView certificateScreen = view2.findViewById(R.id.cv_certificate);
        searchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SearchActivity.class));
            }
        });
        certificateScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Certificate.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from(view2);
        bottomSheetBehavior.setHideable(false);
    }

}