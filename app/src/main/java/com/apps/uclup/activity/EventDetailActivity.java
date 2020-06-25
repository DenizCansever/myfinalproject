package com.apps.uclup.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.uclup.R;
import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.tv_subtitle)
    TextView subtitle;

    @BindView(R.id.tv_dest)
    TextView dest;

    @BindView(R.id.tv_date)
    TextView date;

    @BindView(R.id.img_banner)
    ImageView image;

    @BindView(R.id.profile_image)
    CircleImageView logo;

    @OnClick(R.id.img_back)
    void onclickBack(){
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        title.setText(getIntent().getExtras().getString("title"));
        subtitle.setText(getIntent().getExtras().getString("subtitle"));
        dest.setText(getIntent().getExtras().getString("dest"));
        date.setText(getIntent().getExtras().getString("date"));

        Picasso.with(getApplicationContext())
                .load(getIntent().getExtras().getString("image"))
                .into(image);

        Picasso.with(getApplicationContext())
                .load(getIntent().getExtras().getString("logo"))
                .into(logo);
    }

}
