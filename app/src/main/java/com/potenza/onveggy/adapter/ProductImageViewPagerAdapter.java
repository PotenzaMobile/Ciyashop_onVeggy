package com.potenza.onveggy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.potenza.onveggy.R;
import com.potenza.onveggy.activity.ImageViewerActivity;
import com.potenza.onveggy.activity.VideoViewActivity;
import com.potenza.onveggy.model.CategoryList;
import com.potenza.onveggy.utils.BaseActivity;
import com.potenza.onveggy.utils.Constant;
import com.potenza.onveggy.utils.RequestParamUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhumi Shah on 11/9/2017.
 */

public class ProductImageViewPagerAdapter extends PagerAdapter {
    public static List<CategoryList.Image> list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private final Activity activity;
    private final int id;
    private int imageWidth, imageHeight;

    public ProductImageViewPagerAdapter(Activity activity, int id) {
        this.activity = activity;
        this.id = id;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void addAll(List<CategoryList.Image> list) {
        ProductImageViewPagerAdapter.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_top_banner, container, false);
        final ImageView imageView = view.findViewById(R.id.ivBanner);
        final ImageView iv_play = view.findViewById(R.id.iv_play);
        final ProgressBar progress_bar = view.findViewById(R.id.progress_bar);
        container.addView(view);

        imageView.setOnClickListener(v -> {
            Intent intent;
            if (list.get(position).type != null && list.get(position).type.equals("Video")) {
                intent = new Intent(activity, VideoViewActivity.class);
                intent.putExtra(RequestParamUtils.pos, position);
                intent.putExtra(RequestParamUtils.VIDEO_URL, list.get(position).url);
            } else {
                intent = new Intent(activity, ImageViewerActivity.class);
                intent.putExtra(RequestParamUtils.pos, position);
                intent.putExtra(RequestParamUtils.cat_id, id);
            }
            activity.startActivity(intent);
        });

        progress_bar.getIndeterminateDrawable().setColorFilter(Color.parseColor(((BaseActivity) activity).getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)), android.graphics.PorterDuff.Mode.MULTIPLY);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (list.get(position).type != null && list.get(position).type.equals("Video")) {
            String image = list.get(position).src == null ? "test" : list.get(position).src;
            image = image.length() == 0 ? "test" : image;

            Glide.with(activity)
                    .load(image)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progress_bar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
            iv_play.setVisibility(View.VISIBLE);
        } else {
            Glide.with(activity)
                    .load(list.get(position).src)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
            iv_play.setVisibility(View.GONE);
        }

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                imageWidth = imageView.getMeasuredHeight();
                imageHeight = imageView.getMeasuredHeight();
//                Log.e("Product Image  Height: " + imageView.getMeasuredHeight(), "Product Image  Width: " + imageView.getMeasuredWidth());
                return true;
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
