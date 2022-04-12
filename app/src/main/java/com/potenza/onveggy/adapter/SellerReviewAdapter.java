package com.potenza.onveggy.adapter;


/**
 * Created by Kaushal on 12-12-2017.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potenza.onveggy.R;
import com.potenza.onveggy.customview.textview.TextViewLight;
import com.potenza.onveggy.customview.textview.TextViewRegular;
import com.potenza.onveggy.interfaces.OnItemClickListener;
import com.potenza.onveggy.model.SellerData;
import com.potenza.onveggy.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhumi Shah on 11/7/2017.
 */

public class SellerReviewAdapter extends RecyclerView.Adapter<SellerReviewAdapter.ReviewHolder> {

    private List<SellerData.SellerInfo.ReviewList> list = new ArrayList<>();
    private final Activity activity;
    private OnItemClickListener onItemClickListener;

    public SellerReviewAdapter(Activity activity, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.onItemClickListener = onItemClickListener;
    }

    public void addAll(List<SellerData.SellerInfo.ReviewList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.tvRatting.setText(Constant.setDecimalTwo((double) list.get(position).rating));
        holder.tvName.setText(list.get(position).commentAuthor);
        holder.tvReview.setText(list.get(position).commentContent);
        holder.tvTime.setText(list.get(position).commentDate);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ReviewHolder extends RecyclerView.ViewHolder {

        TextViewLight tvTime;
        TextViewRegular tvName, tvRatting, tvReview;

        public ReviewHolder(View view) {
            super(view);
            tvTime = view.findViewById(R.id.tvTime);
            tvName = view.findViewById(R.id.tvName);
            tvRatting = view.findViewById(R.id.tvRatting);
            tvReview = view.findViewById(R.id.tvReview);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}