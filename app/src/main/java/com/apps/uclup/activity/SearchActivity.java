package com.apps.uclup.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.uclup.R;
import com.apps.uclup.adapter.ClupAdapter;
import com.apps.uclup.model.Clup;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mData;
    List<Clup> listClup = new ArrayList<>();
    List<Clup> listTemp;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.edt_search)
    EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        loadClup();

        edtSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(s.length()>=2){
                    initView(s.toString());
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void loadClup(){

        mData = mRoot.child("Clup");
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try {
                    Clup model = dataSnapshot.getValue(Clup.class);
                    listClup.add(model);
                }
                catch (Exception e){
                    Toasty.error(getApplicationContext(), "Sunucu bağlantı hatası.",
                            Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initView(String temp){

        listTemp=new ArrayList<>();
        temp=uppercase(temp);

        for(int i =0;i<listClup.size();i++){

            if(listClup.get(i).getName().contains(temp) || listClup.get(i).getShortName().contains(temp.toUpperCase())){
                listTemp.add(listClup.get(i));

            }
            recyclerView.setAdapter(new ClupAdapter(getApplicationContext(),listTemp));

        }

    }

    String uppercase(String str) {

        char c = Character . toUpperCase ( str . charAt ( 0 ));
        str = c + str . substring ( 1 );
        String x=" ";

        for (int i = 1 ; i<str.length();i++) {
            if (str.charAt(i)==' ') {
                c=Character.toUpperCase(str.charAt (i+1));
                str = str.substring(0,i)+x +c  + str.substring(i+2);
            }
        }
        return str;
    }
}
