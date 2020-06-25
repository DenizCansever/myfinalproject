package com.apps.uclup.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.uclup.R;
import com.apps.uclup.activity.ClupDetailActivity;
import com.apps.uclup.activity.EventDetailActivity;
import com.apps.uclup.model.Clup;
import com.apps.uclup.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClupAdapter extends RecyclerView.Adapter<ClupAdapter.ViewHolder> {

    private List<Clup> explore;
    private Context context;

    public ClupAdapter(Context applicationContext, List<Clup> exploreList) {
        this.context = applicationContext;
        this.explore = exploreList;
        notifyDataSetChanged();
    }

    @Override
    public ClupAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_clup, viewGroup, false);
        return new ClupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClupAdapter.ViewHolder viewHolder, int i) {

        viewHolder.title.setText( explore.get(i).getShortName());
        Picasso.with(context).load(explore.get(i).getImage()).into(viewHolder.logo);

    }

    @Override
    public int getItemCount() {
        return explore.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private CircleImageView logo;

        public ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.tv_name);
            logo=(CircleImageView) view.findViewById(R.id.profile_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        Clup clickedDataItem = explore.get(pos);

                        Intent intent = new Intent(context, ClupDetailActivity.class);
                        intent.putExtra("name", clickedDataItem.getName());
                        intent.putExtra("shortname", clickedDataItem.getShortName());
                        intent.putExtra("logo", clickedDataItem.getImage());
                        intent.putExtra("dest", clickedDataItem.getDest());
                        intent.putExtra("whatsapp", clickedDataItem.getWhatsapp());
                        intent.putExtra("facebook", clickedDataItem.getFacebook());
                        intent.putExtra("instagram", clickedDataItem.getInstagram());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

}


