package com.potenza.onveggy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.potenza.onveggy.R;
import com.potenza.onveggy.customview.textview.TextViewBold;
import com.potenza.onveggy.customview.textview.TextViewRegular;
import com.potenza.onveggy.model.Intro;

import java.util.List;


public class IntroViewPagerAdapter extends PagerAdapter {
    private List<Intro> list;
    private LayoutInflater layoutInflater;
    private final Activity activity;
    private int length;

    public IntroViewPagerAdapter(Activity activity) {
        this.activity = activity;
        list = new Intro().getIntroList(activity);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_intro_pager, container, false);
        container.addView(view);
        ImageView ivIntro = view.findViewById(R.id.ivIntro);
        TextViewRegular tvTitle = view.findViewById(R.id.tvTitle);
        TextViewBold tvDescription = view.findViewById(R.id.tvDescription);
        ivIntro.setImageResource(list.get(position).image);
        tvDescription.setText(list.get(position).description);
        tvTitle.setText(list.get(position).title);
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
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

