package com.apps.uclup.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.uclup.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminLogin extends AppCompatActivity {

    @BindView(R.id.edt_title)
    EditText edtName;

    @BindView(R.id.edt_subtitle)
    EditText edtPassword;

    DatabaseReference data = FirebaseDatabase.getInstance().getReference("Admin");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.login)
    public void onclicklogin(){

        if(!edtName.getText().toString().equals("")){

            if(!edtPassword.getText().toString().equals("") && edtPassword.getText().toString().equals("123456")){

                data.child(edtName.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {
                            String temp=dataSnapshot.getValue().toString();
                            String [] args=temp.split(";");

                            Toasty.warning(getApplicationContext(), "Lütfen galeriden resim seçebilmek için uygulama ayarlarından depolama iznini verdiğinize emin olunuz.",
                                    Toast.LENGTH_LONG, true).show();

                            Intent i = new Intent(getApplicationContext(),AdminActivity.class);
                            i.putExtra("send_name",args[0]);
                            i.putExtra("send_logo",args[1]);
                            startActivity(i);
                        }
                        catch (Exception e){
                            Toasty.error(getApplicationContext(), "Kullanıcı adı veya şifresi hatalı.",
                                    Toast.LENGTH_SHORT, true).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
            else {
                edtPassword.setError("Şifreniz hatalı");
            }
        }
        else {
            edtName.setError("Kullanıc adınız hatalı");
        }

    }
}
