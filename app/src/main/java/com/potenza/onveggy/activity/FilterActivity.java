package com.potenza.onveggy.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ciyashop.library.apicall.PostApi;
import com.ciyashop.library.apicall.URLS;
import com.ciyashop.library.apicall.interfaces.OnResponseListner;
import com.potenza.onveggy.R;
import com.potenza.onveggy.adapter.ColorAdapter;
import com.potenza.onveggy.adapter.FilterTypeAdapter;
import com.potenza.onveggy.customview.EqualSpacingItemDecoration;
import com.potenza.onveggy.customview.rangeseekbar.interfaces.widgets.CrystalRangeSeekbar;
import com.potenza.onveggy.databinding.ActivityFilterBinding;
import com.potenza.onveggy.databinding.ToolbarBinding;
import com.potenza.onveggy.interfaces.OnItemClickListener;
import com.potenza.onveggy.javaclasses.FilterSelectedList;
import com.potenza.onveggy.model.FilterColorOption;
import com.potenza.onveggy.model.FilterOtherOption;
import com.potenza.onveggy.model.PriceFilter;
import com.potenza.onveggy.utils.BaseActivity;
import com.potenza.onveggy.utils.Config;
import com.potenza.onveggy.utils.Constant;
import com.potenza.onveggy.utils.RequestParamUtils;
import com.potenza.onveggy.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends BaseActivity implements OnItemClickListener, OnResponseListner {

    private ActivityFilterBinding binding;
    private ToolbarBinding toolbarBinding;

    private ColorAdapter colorAdapter;
    private FilterTypeAdapter filterTypeAdapter;
    private Bundle bundle;
    private String categoryId;
    private final List<FilterOtherOption> filterOtherOptionList = new ArrayList<>();
    private final List<FilterColorOption> filterColorOptions = new ArrayList<>();

    private String priceSymbol;
    public static boolean clearFilter = false;
    private PriceFilter priceFilterRider;
    public int minPrice, maxPrice;
    private final int REQUEST_CODE = 101;
    private boolean onSale = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        toolbarBinding = ToolbarBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());
        setClickEvent();
        setToolbarTheme();
        ivSearch = findViewById(R.id.ivSearch);
        ivSearch.setVisibility(View.GONE);
        setScreenLayoutDirection();
        showBackButton();
        if (Config.IS_RTL) {
            binding.priceseekbar.setRotation(180);
        }

        toolbarBinding.ivNotification.setVisibility(View.VISIBLE);
        toolbarBinding.ivNotification.setImageResource(R.drawable.ic_right_white);
        toolbarBinding.ivNotification.setColorFilter(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        settvTitle(getResources().getString(R.string.filter));
        getIntentData();

        if (!categoryId.equals("")) {
            getFilterData(categoryId);
        }
        setColorAdapter();
        setFilterTypeAdapter();
        setColorTheme();
    }

    public void getIntentData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            categoryId = bundle.getString(RequestParamUtils.CATEGORY);
            onSale = bundle.getBoolean(RequestParamUtils.onSale);
        }
    }

    public void getFilterData(String categoryId) {
        if (Utils.isInternetConnected(this)) {
            showProgress("");
            PostApi postApi = new PostApi(FilterActivity.this, RequestParamUtils.getFilterData, this, getlanuage());
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(RequestParamUtils.CATEGORY, categoryId);
                postApi.callPostApi(new URLS().ATTRIBUTES, jsonObject.toString());
            } catch (Exception e) {
                Log.e("Json Exception", e.getMessage());
            }
        } else {
            Toast.makeText(this, R.string.internet_not_working, Toast.LENGTH_LONG).show();
        }
    }

    private void setRangePrice() {
        Drawable mDrawable = ContextCompat.getDrawable(this, R.drawable.price_select_dumb);
        if (mDrawable != null) {
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.ADD));
        }

        if (FilterActivity.clearFilter) {
            binding.priceseekbar
                    .setCornerRadius(10f)
                    .setBarColor(Color.parseColor("#b1b1b1"))
                    .setBarHighlightColor(Color.parseColor("#000000"))
                    .setMinValue(Integer.parseInt(priceFilterRider.minPrice))
                    .setMaxValue(Integer.parseInt(priceFilterRider.maxPrice))
                    .setMinStartValue(Integer.parseInt(priceFilterRider.minPrice))
                    .setMaxStartValue(Integer.parseInt(priceFilterRider.maxPrice))
                    .setSteps(1)
                    .setLeftThumbDrawable(mDrawable)
                    .setLeftThumbHighlightDrawable(mDrawable)
                    .setRightThumbDrawable(mDrawable)
                    .setRightThumbHighlightDrawable(mDrawable)
                    .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                    .apply();
        } else {
            binding.priceseekbar
                    .setCornerRadius(10f)
                    .setBarColor(Color.parseColor("#b1b1b1"))
                    .setBarHighlightColor(Color.parseColor("#000000"))
                    .setMinValue(minPrice)
                    .setMaxValue(maxPrice)
                    .setMinStartValue(FilterSelectedList.minPrice)
                    .setMaxStartValue(FilterSelectedList.maxPrice)
                    .setSteps(1)
                    .setLeftThumbDrawable(mDrawable)
                    .setLeftThumbHighlightDrawable(mDrawable)
                    .setRightThumbDrawable(mDrawable)
                    .setRightThumbHighlightDrawable(mDrawable)
                    .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                    .apply();
        }

        // set listener
        binding.priceseekbar.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            if (Integer.parseInt(minValue + "") != minPrice || Integer.parseInt(maxValue + "") != maxPrice) {
                if (clearFilter) {
                    clearFilter = false;
                }
            }

            if (!clearFilter) {
                FilterSelectedList.minPrice = Integer.parseInt(minValue + "");
                FilterSelectedList.maxPrice = Integer.parseInt(maxValue + "");
            }
            binding.tvMin.setText(String.format("%s  %s", priceSymbol, minValue));
            binding.tvMax.setText(String.format("%s  %s", priceSymbol, maxValue));
        });
        setColorTheme();
    }

    public void setColorTheme() {
        setTextViewDrawableColors(binding.tvcolorTitle, Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        binding.tvcolorTitle.setTextColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));
        Drawable mDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.price_select_dumb, null);
        if (mDrawable != null) {
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)), PorterDuff.Mode.OVERLAY));
        }
        binding.tvPriceTitle.setTextColor(Color.parseColor(getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)));

        Drawable unwrappedDrawable = binding.tvClearFilter.getBackground();
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, (Color.parseColor((getPreferences().getString(Constant.SECOND_COLOR, Constant.SECONDARY_COLOR)))));
    }

    public void setColorAdapter() {
        colorAdapter = new ColorAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvColor.setLayoutManager(mLayoutManager);
        binding.rvColor.setAdapter(colorAdapter);
        binding.rvColor.addItemDecoration(new EqualSpacingItemDecoration(dpToPx(10), EqualSpacingItemDecoration.HORIZONTAL)); // 16px. In practice, you'll want to use getDimensionPixelSize
        binding.rvColor.setNestedScrollingEnabled(false);
    }

    public void setFilterTypeAdapter() {
        filterTypeAdapter = new FilterTypeAdapter(this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvFilterType.setLayoutManager(mLayoutManager);
        binding.rvFilterType.setAdapter(filterTypeAdapter);
        binding.rvFilterType.setNestedScrollingEnabled(false);
    }

    @Override
    public void onItemClick(int position, String value, int outerPos) {

        FilterOtherOption filterOtherOption = FilterSelectedList.selectedColorOptionList.get(outerPos);
        if (value.equals(RequestParamUtils.strtrue)) {
            filterOtherOption.options.set(position, colorAdapter.getList().get(position).colorName);
        } else {
            filterOtherOption.options.set(position, "");
        }
        colorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponse(String response, String methodName) {
        if (methodName.equals(RequestParamUtils.getFilterData)) {
            if (response != null && response.length() > 0) {
                if (FilterSelectedList.cat_id.equals("") || !FilterSelectedList.cat_id.equals(categoryId)) {
                    FilterSelectedList.cat_id = categoryId;
                    FilterSelectedList.clearFilter();
                }
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("filters");

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.getJSONObject(i).getString("name").equalsIgnoreCase("Color".toLowerCase())) {
                                FilterColorOption filterOptionRiderRider;
                                try {
                                    filterOptionRiderRider = new Gson().fromJson(
                                            jsonArray.getJSONObject(i).toString(), new TypeToken<FilterColorOption>() {
                                            }.getType());
                                    FilterOtherOption filterColorOption = new FilterOtherOption();
                                    filterColorOption.id = filterOptionRiderRider.id;
                                    filterColorOption.name = filterOptionRiderRider.name;
                                    List<String> option = new ArrayList<>();
                                    for (int j = 0; j < filterOptionRiderRider.options.size(); j++) {
                                        option.add("");
                                    }
                                    filterColorOption.options = option;
                                    if (FilterSelectedList.selectedColorOptionList.size() == 0) {
                                        FilterSelectedList.selectedColorOptionList.add(0, filterColorOption);
                                    }
                                    filterColorOptions.add(filterOptionRiderRider);
                                } catch (Exception e) {
                                    FilterOtherOption filterOtherOptionRider = new Gson().fromJson(
                                            jsonArray.getJSONObject(i).toString(), new TypeToken<FilterOtherOption>() {
                                            }.getType());
                                    filterOptionRiderRider = new FilterColorOption();
                                    filterOptionRiderRider.id = filterOtherOptionRider.id;
                                    filterOptionRiderRider.name = filterOtherOptionRider.name;
                                    List<FilterColorOption.Option> colorList = new ArrayList<>();
                                    for (int j = 0; j < filterOtherOptionRider.options.size(); j++) {
                                        FilterColorOption.Option option1 = new FilterColorOption().getOption();
                                        option1.colorCode = "";
                                        option1.colorName = filterOtherOptionRider.options.get(j);
                                        colorList.add(option1);
                                    }
                                    filterOptionRiderRider.options = colorList;

                                    filterColorOptions.add(filterOptionRiderRider);

                                    List<String> option = new ArrayList<>();
                                    for (int j = 0; j < filterOtherOptionRider.options.size(); j++) {
                                        option.add("");
                                    }
                                    filterOtherOptionRider.options = option;
                                    if (FilterSelectedList.selectedColorOptionList.size() == 0) {
                                        FilterSelectedList.selectedColorOptionList.add(0, filterOtherOptionRider);
                                    }
                                }
                            } else {
                                FilterOtherOption filterOtherOptionRider = new Gson().fromJson(
                                        jsonArray.getJSONObject(i).toString(), new TypeToken<FilterOtherOption>() {
                                        }.getType());
                                filterOtherOptionList.add(filterOtherOptionRider);
                                FilterOtherOption filterOtherOption = new FilterOtherOption();
                                filterOtherOption.id = filterOtherOptionRider.id;
                                filterOtherOption.name = filterOtherOptionRider.name;
                                List<String> option = new ArrayList<>();
                                for (int j = 0; j < filterOtherOptionRider.options.size(); j++) {
                                    option.add("");
                                }
                                filterOtherOption.options = option;
                                filterOtherOption.variation = filterOtherOptionRider.variation;
                                filterOtherOption.visible = filterOtherOptionRider.visible;

                                FilterSelectedList.selectedOtherOptionList.add(filterOtherOption);
                            }
                        }
                        filterTypeAdapter.setRatingEnableOrNot(object.has("rating_filters_status") && object.getString("rating_filters_status").equals("enable"));

                        filterTypeAdapter.addAll(filterOtherOptionList);
                        if (filterColorOptions.size() > 0) {
                            colorAdapter.addAll(filterColorOptions.get(0).options);
                            if (filterColorOptions.get(0).options.size() > 0) {
                                binding.llColor.setVisibility(View.VISIBLE);
                            } else {
                                binding.llColor.setVisibility(View.GONE);
                            }
                        } else {
                            binding.llColor.setVisibility(View.GONE);
                        }
                    }

                    priceFilterRider = new Gson().fromJson(
                            object.getJSONObject("price_filter").toString(), new TypeToken<PriceFilter>() {
                            }.getType());
                    minPrice = Integer.parseInt(priceFilterRider.minPrice);
                    maxPrice = Integer.parseInt(priceFilterRider.maxPrice);
                    if (FilterSelectedList.maxPrice == 0) {
                        FilterSelectedList.maxPrice = maxPrice;
                    }

                    if (FilterSelectedList.minPrice == 0) {
                        FilterSelectedList.minPrice = minPrice;
                    }
                    priceSymbol = priceFilterRider.currencySymbol;
                    setRangePrice();

                    if (FilterSelectedList.maxPrice != 0) {
                        binding.llPrice.setVisibility(View.VISIBLE);
                    } else {
                        binding.llPrice.setVisibility(View.GONE);
                    }
                    dismissProgress();
                } catch (Exception e) {
                    dismissProgress();
                    Log.e(methodName + "Gson Exception is ", e.getMessage());
                }
            }
        }
    }

    public void setClickEvent() {
        binding.tvClearFilter.setOnClickListener(v -> {
            clearFilter = true;
            minPrice = Integer.parseInt(priceFilterRider.minPrice);
            maxPrice = Integer.parseInt(priceFilterRider.maxPrice);
            setRangePrice();
            colorAdapter.notifyDataSetChanged();
            filterTypeAdapter.notifyDataSetChanged();
        });

        toolbarBinding.ivNotification.setOnClickListener(v -> {
            if (clearFilter) {
                clearFilter();
                clearFilter = false;
            }
            applyFilter();
            FilterSelectedList.isFilterCalled = true;
            finish();
        });
    }

    public void clearFilter() {
        for (int i = 0; i < FilterSelectedList.selectedOtherOptionList.size(); i++) {
            FilterOtherOption filterOtherOption = FilterSelectedList.selectedOtherOptionList.get(i);
            List<String> option = filterOtherOption.options;
            for (int j = 0; j < option.size(); j++) {
                option.set(j, "");
            }
        }
        filterTypeAdapter.notifyDataSetChanged();
        if (FilterSelectedList.selectedColorOptionList.size() > 0) {
            for (int k = 0; k < FilterSelectedList.selectedColorOptionList.get(0).options.size(); k++) {
                FilterSelectedList.selectedColorOptionList.get(0).options.set(k, "");
            }
            colorAdapter.notifyDataSetChanged();
        }
        FilterSelectedList.minPrice = Integer.parseInt(priceFilterRider.minPrice);
        FilterSelectedList.maxPrice = Integer.parseInt(priceFilterRider.maxPrice);
        minPrice = Integer.parseInt(priceFilterRider.minPrice);
        maxPrice = Integer.parseInt(priceFilterRider.maxPrice);
        setRangePrice();
        FilterSelectedList.isFilterCalled = false;
    }

    public void applyFilter() {
        StringBuilder rating = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray attributeArray = new JSONArray();
            for (int i = 0; i < FilterSelectedList.selectedOtherOptionList.size(); i++) {
                if (!FilterSelectedList.selectedOtherOptionList.get(i).name.equalsIgnoreCase("rating")) {
                    JSONObject attributeObject = new JSONObject();
                    attributeObject.put(RequestParamUtils.ID, FilterSelectedList.selectedOtherOptionList.get(i).id);
                    attributeObject.put(RequestParamUtils.name, FilterSelectedList.selectedOtherOptionList.get(i).name);
                    JSONArray optionArray = new JSONArray();
                    for (int j = 0; j < FilterSelectedList.selectedOtherOptionList.get(i).options.size(); j++) {
                        if (!FilterSelectedList.selectedOtherOptionList.get(i).options.get(j).equals("")) {
                            optionArray.put(FilterSelectedList.selectedOtherOptionList.get(i).options.get(j));
                        }
                    }
                    if (optionArray.length() > 0) {
                        attributeObject.put(RequestParamUtils.options, optionArray);
                        attributeArray.put(attributeObject);
                    }
                } else {
                    for (int j = 0; j < FilterSelectedList.selectedOtherOptionList.get(i).options.size(); j++) {
                        if (!FilterSelectedList.selectedOtherOptionList.get(i).options.get(j).equals("")) {
                            if (rating.toString().equals("")) {
                                rating.append(FilterSelectedList.selectedOtherOptionList.get(i).options.get(j));
                            } else {
                                rating.append(",").append(FilterSelectedList.selectedOtherOptionList.get(i).options.get(j));
                            }
                        }
                    }
                }
            }
            JSONObject colorAttributeObject = new JSONObject();
            if (FilterSelectedList.selectedColorOptionList.size() > 0) {
                colorAttributeObject.put(RequestParamUtils.ID, FilterSelectedList.selectedColorOptionList.get(0).id);
                colorAttributeObject.put(RequestParamUtils.name, FilterSelectedList.selectedColorOptionList.get(0).name);
                JSONArray optionArray = new JSONArray();
                for (int k = 0; k < FilterSelectedList.selectedColorOptionList.get(0).options.size(); k++) {
                    if (!FilterSelectedList.selectedColorOptionList.get(0).options.get(k).equals("")) {
                        optionArray.put(FilterSelectedList.selectedColorOptionList.get(0).options.get(k));
                    }
                }
                if (optionArray.length() > 0) {
                    colorAttributeObject.put(RequestParamUtils.options, optionArray);
                    attributeArray.put(colorAttributeObject);
                }
            }
            jsonObject.put(RequestParamUtils.attribute, attributeArray);
            jsonObject.put(RequestParamUtils.CATEGORY, categoryId);
            jsonObject.put(RequestParamUtils.maxPrice, FilterSelectedList.maxPrice);
            jsonObject.put(RequestParamUtils.minPrice, FilterSelectedList.minPrice);
            if (onSale) {
                jsonObject.put(RequestParamUtils.onSale, 1);
            }

            jsonObject.put(RequestParamUtils.page, 1);
            if (!rating.toString().equals("")) {
                jsonObject.put(RequestParamUtils.RATING_FILTER, rating.toString());
            }

            FilterSelectedList.filterJson = jsonObject.toString();
        } catch (JSONException e) {
            Log.e("JSONException is", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearFilter = false;
        FilterSelectedList.isFilterCalled = false;
    }
}
