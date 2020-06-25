package com.apps.uclup.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.uclup.R;
import com.apps.uclup.adapter.PostAdapter;
import com.apps.uclup.model.Post;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ClupDetailActivity extends AppCompatActivity {

    List<Post> listPost = new ArrayList<>();
    DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
    DatabaseReference vData;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.tv_subtitle)
    TextView subtitle;

    @BindView(R.id.tv_dest)
    TextView dest;

    @BindView(R.id.profile_image)
    CircleImageView logo;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @OnClick(R.id.img_back)
    void onclickBack(){
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clup_detail);
        ButterKnife.bind(this);

        initView();
        loadData();
    }

    private void initView(){

        title.setText(getIntent().getExtras().getString("name"));
        subtitle.setText(getIntent().getExtras().getString("shortname"));
        dest.setText(getIntent().getExtras().getString("dest"));

        Picasso.with(getApplicationContext())
                .load(getIntent().getExtras().getString("logo"))
                .into(logo);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void loadData(){

        vData = mRoot.child("ClupEvent/"+getIntent().getExtras().getString("shortname"));
        vData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try {
                    Post model = dataSnapshot.getValue(Post.class);
                    model.setId(dataSnapshot.getKey());
                    listPost.add(model);
                    recyclerView.setAdapter(new PostAdapter(getApplicationContext(),listPost));
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


    @OnClick(R.id.img_facebook)
    public void onFacebook(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getIntent().getExtras().getString("facebook")));
        startActivity(i);
    }

    @OnClick(R.id.img_insta)
    public void onInstagram(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getIntent().getExtras().getString("instagram")));
        startActivity(i);
    }

    @OnClick(R.id.img_whatsapp)
    public void onWhatsapp(){
        String toNumber = getIntent().getExtras().getString("whatsapp");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber + "&text="+"Merhabalar,\n"));
        startActivity(intent);

    }
}
