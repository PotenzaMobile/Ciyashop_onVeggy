package com.potenza.onveggy.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.potenza.onveggy.R;
import com.potenza.onveggy.customview.textview.TextViewLight;
import com.potenza.onveggy.customview.textview.TextViewRegular;
import com.potenza.onveggy.interfaces.OnItemClickListener;
import com.potenza.onveggy.model.Home;
import com.potenza.onveggy.utils.BaseActivity;
import com.potenza.onveggy.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhumi Shah on 11/7/2017.
 */

public class SixReasonAdapter extends RecyclerView.Adapter<SixReasonAdapter.SpecialOfferViewHolder> {

    private List<Home.FeatureBox> list = new ArrayList<>();
    private final Activity activity;
    private final OnItemClickListener onItemClickListener;
    private final int width = 0;
    private final int height = 0;

    public SixReasonAdapter(Activity activity, OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.onItemClickListener = onItemClickListener;
    }

    public void addAll(List<Home.FeatureBox> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SpecialOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_six_reason, parent, false);
        return new SpecialOfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpecialOfferViewHolder holder, int position) {
        Glide.with(activity).load(list.get(position).featureImage).into(holder.ivImage);
        holder.tvDescription.setText(list.get(position).featureContent);
        holder.tvName.setText(list.get(position).featureTitle);
        Drawable unwrappedDrawable = holder.ivImage.getBackground();
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, (Color.parseColor((((BaseActivity) activity).getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)))));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SpecialOfferViewHolder extends RecyclerView.ViewHolder {

        TextViewRegular tvName;
        TextViewLight tvDescription;
        ImageView ivImage;
        LinearLayout llMain;

        public SpecialOfferViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvDescription = view.findViewById(R.id.tvDescription);
            ivImage = view.findViewById(R.id.ivImage);
            llMain = view.findViewById(R.id.llMain);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}