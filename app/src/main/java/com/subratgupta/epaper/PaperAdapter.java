package com.subratgupta.epaper;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.subratgupta.epaper.R;

import java.util.List;

public class PaperAdapter extends RecyclerView.Adapter<PaperAdapter.ViewHolder> {
    private List<PageType> mData;
    private LayoutInflater mInflater;
    private Context context;
    private PaperAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    PaperAdapter(Context context, List<PageType> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public PaperAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.page_row, parent, false);
        return new PaperAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(PaperAdapter.ViewHolder holder, int position) {
        PageType pageType = mData.get(position);
//        holder.myNameView.setText("Name: "+restaurent.getName());
//        holder.myPageView.setText("Phone: "+restaurent.getPhone());
        Glide.with(context)
                .asBitmap()
                .load(pageType.getLink())
                .into(holder.myPageView);
//        Glide.with(context).load(pageType.getLink()).into(holder.myPageView);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        TextView myNameView;
        ImageView myPageView;

        ViewHolder(View itemView) {
            super(itemView);
//            myNameView = itemView.findViewById(R.id.restaurent_name);
            myPageView = itemView.findViewById(R.id.page);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    PageType getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(PaperAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
