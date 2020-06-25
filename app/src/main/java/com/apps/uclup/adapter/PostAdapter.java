package com.apps.uclup.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.uclup.R;
import com.apps.uclup.activity.EventDetailActivity;
import com.apps.uclup.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> explore;
    private Context context;

    public PostAdapter(Context applicationContext, List<Post> exploreList) {
        this.context = applicationContext;
        this.explore = exploreList;
        notifyDataSetChanged();
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder viewHolder, int pos) {

        viewHolder.title.setText( explore.get(pos).getTitle());
        viewHolder.subtitle.setText( explore.get(pos).getSubtitle());
        viewHolder.date.setText( explore.get(pos).getDate());

        Picasso.with(context).load(explore.get(pos).getImage()).into(viewHolder.image);
        Picasso.with(context).load(explore.get(pos).getLogo()).into(viewHolder.logo);

    }

    @Override
    public int getItemCount() {
        return explore.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title,subtitle,date;
        private ImageView image;
        private CircleImageView logo;

        public ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.tv_title);
            subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
            date = (TextView) view.findViewById(R.id.tv_date);
            image=(ImageView) view.findViewById(R.id.imageView);
            logo=(CircleImageView) view.findViewById(R.id.profile_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        Post clickedDataItem = explore.get(pos);

                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra("title", clickedDataItem.getTitle());
                        intent.putExtra("subtitle", clickedDataItem.getSubtitle());
                        intent.putExtra("dest", clickedDataItem.getDesct());
                        intent.putExtra("date", clickedDataItem.getDate());
                        intent.putExtra("image", clickedDataItem.getImage());
                        intent.putExtra("logo", clickedDataItem.getLogo());
                        intent.putExtra("id", clickedDataItem.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

}


