package com.apps.uclup.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.uclup.R;
import com.apps.uclup.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AdminActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.edt_title)
    EditText edtTitle;

    @BindView(R.id.edt_subtitle)
    EditText edtSubTitle;

    @BindView(R.id.edt_dest)
    EditText edtDest;

    @BindView(R.id.edt_date)
    EditText edtDate;

    @BindView(R.id.edt_image)
    TextView edtImage;

    @BindView(R.id.img)
    ImageView image;

    @BindView(R.id.profile_image)
    CircleImageView logo;

    DatabaseReference data,mData;
    Post post;
    String sayac="0";
    String mPath,logoImage;

    private Uri filePath;
    private StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        storageReference = FirebaseStorage.getInstance().getReference();
        logoImage=getIntent().getExtras().getString("send_logo");
        title.setText("Hoşgeldin,\n"+getIntent().getExtras().getString("send_name"));

        Picasso.with(getApplicationContext())
                .load(logoImage)
                .into(logo);

    }

    private boolean validateForm() {

        boolean result = true;

        if (TextUtils.isEmpty(edtTitle.getText().toString())) {
            edtTitle.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            result = false;
        }

        if (TextUtils.isEmpty(edtSubTitle.getText().toString())) {
            edtSubTitle.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            result = false;
        }

        if (TextUtils.isEmpty(edtDest.getText().toString())) {
            edtDest.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            result = false;
        }

        if (TextUtils.isEmpty(edtDate.getText().toString())) {
            edtDate.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            result = false;
        }

        if (edtImage.getText().toString().equals("Paylaşıma ait resim seçiniz >")) {
            edtImage.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            image.setVisibility(View.GONE);
            result = false;
        }

        return result;
    }


    @OnClick(R.id.save)
    public void onclickSave() {

        if (validateForm()) {
            uploadImage();
        }

    }

    @OnClick(R.id.edt_image)
    public void onclickImage(){
        chooseImage();
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seçin"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            sayac="1";

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 1200, 600, true);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bMapScaled.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bMapScaled, "Title", null);
                filePath=Uri.parse(path);

                image.setImageBitmap(bMapScaled);
                image.setVisibility(View.VISIBLE);
                edtImage.setText(filePath.getPath().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            sayac="0";
        }

    }
    private void uploadImage(){

        if(sayac.equals("0")){
            dataUp();
        }
        else{

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Yükleniyor");
            progressDialog.show();

            StorageReference riversRef = storageReference.child("images/"+edtTitle.getText().toString().replace(" ","")+".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            dataUp();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            progressDialog.setMessage("Resim yükleniyor " + ((int) progress) + "%...");
                        }
                    });


        }
    }

    public void dataUp(){

        storageReference.child("images/"+edtTitle.getText().toString().replace(" ","")+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                mPath=uri.toString();
                if(!mPath.equals(null)){

                    String images= uri.toString();
                    try {
                        post = new Post(edtTitle.getText().toString(),
                                        edtSubTitle.getText().toString(),images,
                                        edtDate.getText().toString(),"",
                                        edtDest.getText().toString(),
                                        logoImage);

                        data = FirebaseDatabase.getInstance().getReference("ClupEvent/"+getIntent().getExtras().getString("send_name"));
                        data.push().setValue(post);

                        mData = FirebaseDatabase.getInstance().getReference("Post");
                        mData.push().setValue(post);

                        Toasty.success(getApplicationContext(),
                                "Paylaşım başarıyla yapıldı", Toast.LENGTH_SHORT, true).show();


                        edtTitle.setText("");
                        edtDest.setText("");
                        edtDate.setText("");
                        edtSubTitle.setText("");
                        edtImage.setText("Paylaşıma ait resim seçiniz >");
                        image.setVisibility(View.GONE);

                    }
                    catch (Exception e){

                    }
                }
                else{
                    Toasty.warning(getApplicationContext(), "Profil resmi seçilemedi, tekrar deneyiniz.", Toast.LENGTH_SHORT, true).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toasty.warning(getApplicationContext(), "Profil resmi seçilemedi, tekrar deneyiniz.", Toast.LENGTH_SHORT, true).show();

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }


}
