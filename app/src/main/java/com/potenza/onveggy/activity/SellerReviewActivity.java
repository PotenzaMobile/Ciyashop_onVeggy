package com.potenza.onveggy.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.potenza.onveggy.adapter.SellerReviewAdapter;
import com.potenza.onveggy.databinding.ActivitySellerReviewBinding;
import com.potenza.onveggy.interfaces.OnItemClickListener;
import com.potenza.onveggy.model.SellerData;
import com.potenza.onveggy.utils.BaseActivity;
import com.potenza.onveggy.utils.RequestParamUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SellerReviewActivity extends BaseActivity implements OnItemClickListener {

    private ActivitySellerReviewBinding binding;
    private SellerReviewAdapter sellerReviewAdapter;
    List<SellerData.SellerInfo.ReviewList> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySellerReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settvTitle(RequestParamUtils.vendorReview);
        showBackButton();
        setToolbarTheme();

        String listData = getIntent().getExtras().getString(RequestParamUtils.sellerInfo);
        SellerData sellerDataRider = new Gson().fromJson(
                listData, new TypeToken<SellerData>() {
                }.getType());

        list.addAll(sellerDataRider.sellerInfo.reviewList);
        setReviewData();
    }

    public void setReviewData() {
        sellerReviewAdapter = new SellerReviewAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvReview.setLayoutManager(mLayoutManager);
        binding.rvReview.setAdapter(sellerReviewAdapter);
        binding.rvReview.setNestedScrollingEnabled(false);
        sellerReviewAdapter.addAll(list);
    }

    @Override
    public void onItemClick(int position, String value, int outerPos) {
    }
}
