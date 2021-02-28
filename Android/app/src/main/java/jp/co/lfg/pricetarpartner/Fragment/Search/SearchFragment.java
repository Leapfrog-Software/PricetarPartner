package jp.co.lfg.pricetarpartner.Fragment.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.R;

public class SearchFragment extends BaseFragment {

    public void reload() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_search, null);

        return view;
    }
}
