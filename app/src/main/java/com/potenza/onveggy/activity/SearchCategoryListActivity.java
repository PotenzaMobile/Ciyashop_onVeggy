package com.potenza.onveggy.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.potenza.onveggy.R;
import com.potenza.onveggy.adapter.SearchCategoryAdapter;
import com.potenza.onveggy.customview.GridSpacingItemDecoration;
import com.potenza.onveggy.databinding.ActivitySearchCategoryListBinding;
import com.potenza.onveggy.interfaces.OnItemClickListener;
import com.potenza.onveggy.model.Home;
import com.potenza.onveggy.utils.BaseActivity;
import com.potenza.onveggy.utils.Constant;
import com.potenza.onveggy.utils.RequestParamUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchCategoryListActivity extends BaseActivity implements OnItemClickListener {

    private ActivitySearchCategoryListBinding binding;

    private SearchCategoryAdapter searchCategoryAdapter;
    private Bundle bundle;
    private String from;
    public static int sortPosition;
    private final List<Home.AllCategory> list = new ArrayList<>();
    public static String search, sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchCategoryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setToolbarTheme();
        setScreenLayoutDirection();
        settvTitle(getResources().getString(R.string.all_category));
        getIntentData();
        showSearch();
        showCart();
        showBackButton();
        setSearchAdapter();
        setBottomBar("search", binding.svHome);
    }

    public void getIntentData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            from = bundle.getString(RequestParamUtils.from);
            search = bundle.getString(RequestParamUtils.SEARCH);
            sortBy = bundle.getString(RequestParamUtils.ORDER_BY);
            sortPosition = bundle.getInt(RequestParamUtils.POSITION);
        }
    }

    // intent.putExtra(RequestParamUtils.ORDER_BY, Constant.getSortList().get(sortAdapter.getSelectedPosition()).getSyntext());
    // intent.putExtra(RequestParamUtils.POSITION,sortPosition);

    public void setSearchAdapter() {
        searchCategoryAdapter = new SearchCategoryAdapter(this, this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        binding.rvSearchCategory.setLayoutManager(mLayoutManager);
        binding.rvSearchCategory.setAdapter(searchCategoryAdapter);
        binding.rvSearchCategory.setNestedScrollingEnabled(false);
        searchCategoryAdapter.setFrom(from);
        binding.rvSearchCategory.setNestedScrollingEnabled(false);
        binding.rvSearchCategory.setHasFixedSize(true);
        binding.rvSearchCategory.setItemViewCacheSize(20);
        binding.rvSearchCategory.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        for (int i = 0; i < Constant.MAINCATEGORYLIST.size(); i++) {
            if (Constant.MAINCATEGORYLIST.get(i).parent == 0) {
                list.add(Constant.MAINCATEGORYLIST.get(i));
            }
        }
        searchCategoryAdapter.addAll(list);
    }

    @Override
    public void onItemClick(int position, String value, int outerPos) {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showCart();
    }
}
