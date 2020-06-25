package com.apps.uclup.activity;

import android.content.Intent;
import android.os.Bundle;

import com.apps.uclup.R;
import com.apps.uclup.adapter.ClupAdapter;
import com.apps.uclup.adapter.EventSliderAdapter;
import com.apps.uclup.adapter.OnItemClickListener;
import com.apps.uclup.adapter.PostAdapter;
import com.apps.uclup.model.Clup;
import com.apps.uclup.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> imagesArray;
    List<Post> listEvent = new ArrayList<>();
    List<Post> listPost = new ArrayList<>();
    List<Clup> listClup = new ArrayList<>();

    DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
    DatabaseReference vData,mData;
    FirebaseAuth auth;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.indicator)
    CircleIndicator circleIndicator;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initEventSlider();
        initView();
        loadClup();
        loadData();


    }

    private void initEventSlider() {

        imagesArray= new ArrayList<>();

        vData = mRoot.child("Event/Upcoming");
        vData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Post model = dataSnapshot.getValue(Post.class);
                model.setId(dataSnapshot.getKey());
                listEvent.add(model);
                imagesArray.add(model.getImage());

                viewPager.setAdapter(new EventSliderAdapter(getApplicationContext(), imagesArray, new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
                        intent.putExtra("title", listEvent.get(position).getTitle());
                        intent.putExtra("subtitle", listEvent.get(position).getSubtitle());
                        intent.putExtra("dest", listEvent.get(position).getDesct());
                        intent.putExtra("date", listEvent.get(position).getDate());
                        intent.putExtra("image", listEvent.get(position).getImage());
                        intent.putExtra("logo", listEvent.get(position).getLogo());
                        intent.putExtra("id", listEvent.get(position).getId());
                        startActivity(intent);
                    }
                }));

                circleIndicator.setViewPager(viewPager);
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

    private void initView(){

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView2.setLayoutManager(linearLayoutManager);
        auth = FirebaseAuth.getInstance();

    }

    private void loadClup(){

        mData = mRoot.child("Clup");
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try {
                    Clup model = dataSnapshot.getValue(Clup.class);
                    listClup.add(model);
                    recyclerView.setAdapter(new ClupAdapter(getApplicationContext(),listClup));
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

    private void loadData(){

        vData = mRoot.child("Post");
        vData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try {
                    Post model = dataSnapshot.getValue(Post.class);
                    model.setId(dataSnapshot.getKey());
                    listPost.add(model);
                    recyclerView2.setAdapter(new PostAdapter(getApplicationContext(),listPost));
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

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));
            return true;
        }

        else if (id == R.id.action_exit) {
            auth.signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
            return true;
        }

        else if (id == R.id.action_search) {
            startActivity(new Intent(getApplicationContext(),SearchActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
