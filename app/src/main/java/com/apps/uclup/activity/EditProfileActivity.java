package com.apps.uclup.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.uclup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    String uid,temp;
    String args[];

    FirebaseAuth auth;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference("Users");

    @BindView(R.id.txt1)
    EditText edtName;

    @BindView(R.id.txt2)
    TextView edtMail;

    @BindView(R.id.txt3)
    EditText edtPhone;

    @BindView(R.id.txt4)
    EditText edtEdu;

    @BindView(R.id.profile_image)
    CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        data.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temp=dataSnapshot.getValue().toString();
                args=temp.split(";");

                edtName.setText(args[0].replace(" ",""));
                edtMail.setText(auth.getCurrentUser().getEmail());
                edtPhone.setText(args[1].replace(" ",""));
                edtEdu.setText(args[2].replace(" ",""));

                Picasso.with(getApplicationContext())
                        .load(auth.getCurrentUser().getPhotoUrl())
                        .error(R.drawable.ic_user_photo)
                        .into(profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.save)
    public void onclickSave(){

        if(!edtName.getText().toString().equals("")){

            if(!edtPhone.getText().toString().equals("")){

                if(!edtEdu.getText().toString().equals("")){
                    data.child(auth.getCurrentUser().getUid()).setValue(edtName.getText().toString()+";"+edtPhone.getText().toString()+";"+edtEdu.getText().toString());
                    startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    edtEdu.setError("");
                }
            }
            else {
                edtPhone.setError("");
            }
        }
        else {
            edtName.setError("");
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}
