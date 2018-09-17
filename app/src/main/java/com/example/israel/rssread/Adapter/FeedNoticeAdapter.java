package com.example.israel.rssread.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.israel.rssread.Activity.DetailActivity;
import com.example.israel.rssread.Model.FeedNotice;
import com.example.israel.rssread.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class FeedNoticeAdapter extends RecyclerView.Adapter<FeedNoticeAdapter.FeedModelViewHolder> {
    private List<FeedNotice> mRssFeedModels;
    private Context context;

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }

    public FeedNoticeAdapter(List<FeedNotice> rssFeedModels, Context context) {
        mRssFeedModels = rssFeedModels;
        this.context = context;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss_feed, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedModelViewHolder holder, final int position) {
        final FeedNotice rssFeedModel = mRssFeedModels.get(position);

        if(rssFeedModel.title.length() > 65)
            ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title.substring(0,65)+"...");
        else
            ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title);

        String desc = rssFeedModel.description.toString();
        Document doc = Jsoup.parse(desc);

        if(doc.getElementsByTag("p").first() != null) {
            String description = doc.getElementsByTag("p").first().text();
            if(description.length() > 100)
                ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText)).setText(description.substring(0,100)+"...");
            else
                ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText)).setText(description);
        }

        if(doc.getElementsByTag("img").first() != null) {
            Element img = doc.getElementsByTag("img").first();
            String src = img.attr("src");
            Picasso.with(context)
                .load(src)
                .into((ImageView) holder.rssFeedView.findViewById(R.id.feed_image));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(context, DetailActivity.class);
                detail.putExtra("webURL", mRssFeedModels.get(position).link);
                context.startActivity(detail);
            }
        });

//        holder.rssFeedView.findViewById(R.id.card_view).setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }
}

