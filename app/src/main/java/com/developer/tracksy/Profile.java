//package com.developer.tracksy;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//public class Profile extends AppCompatActivity {
//    DatabaseReference reference;
//    FirebaseUser user;
//    TextView fname,email,phone,logOut;
//    RelativeLayout loadData;
//    FirebaseAuth mAuth;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//        fname=findViewById(R.id.profile_fullname);
//        email=findViewById(R.id.profile_email);
//        phone=findViewById(R.id.profile_phone);
//        logOut=findViewById(R.id.log_out);
//        loadData=findViewById(R.id.load_data);
//        mAuth= FirebaseAuth.getInstance();
//        reference=  FirebaseDatabase.getInstance().getReference("Users");
//        user=mAuth.getCurrentUser();
//        Query query=reference.orderByChild("email").equalTo(user.getEmail());
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds: snapshot.getChildren()){
//                    String EMAIL = "" + ds.child("email").getValue().toString();
//                    String PHONE= "" + ds.child("phone").getValue().toString();
//                    String FNAME = "" + ds.child("fullname").getValue().toString();
//                    email.setText(EMAIL);
//                    phone.setText(PHONE);
//                    fname.setText(FNAME);
//                }
//                loadData.setVisibility(View.INVISIBLE);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        logOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                Snackbar.make(v,"Logged out",Snackbar.LENGTH_SHORT).show();
//                Intent startActivity = new Intent(Profile.this, LoginOrSignUp.class);
//                startActivity(startActivity);
//                finish();
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
//}
