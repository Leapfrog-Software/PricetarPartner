package jp.co.lfg.pricetarpartner.Fragment.Tabbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Fragment.Entry.EntryFragment;
import jp.co.lfg.pricetarpartner.Fragment.Home.HomeFragment;
import jp.co.lfg.pricetarpartner.Fragment.Message.MessageFragment;
import jp.co.lfg.pricetarpartner.Fragment.MyPage.MyPageFragment;
import jp.co.lfg.pricetarpartner.Fragment.Search.SearchFragment;
import jp.co.lfg.pricetarpartner.Http.AutoRequester;
import jp.co.lfg.pricetarpartner.R;

public class TabbarFragment extends BaseFragment {

    private HomeFragment mHomeFragment;
    private EntryFragment mEntryFragment;
    private SearchFragment mSearchFragment;
    private MessageFragment mMessageFragment;
    private MyPageFragment mMyPageFragment;

    private AutoRequester mAutoRequester = new AutoRequester();

    public void reload() {

        mHomeFragment.reload();
        mEntryFragment.reload();
        mMessageFragment.reload();
        mSearchFragment.reload();
        mMyPageFragment.reload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_tabbar, null);

        initFragmentController();
        initAction(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAutoRequester.start(new AutoRequester.Callback() {
            @Override
            public void didReceiveData() {
                reload();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        mAutoRequester.stop();
    }

    private void initFragmentController() {

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        mMyPageFragment = new MyPageFragment();
        transaction.add(R.id.tabContainerLayout, mMyPageFragment);

        mMessageFragment = new MessageFragment();
        transaction.add(R.id.tabContainerLayout, mMessageFragment);

        mSearchFragment = new SearchFragment();
        transaction.add(R.id.tabContainerLayout, mSearchFragment);

        mEntryFragment = new EntryFragment();
        transaction.add(R.id.tabContainerLayout, mEntryFragment);

        mHomeFragment = new HomeFragment();
        transaction.add(R.id.tabContainerLayout, mHomeFragment);

        transaction.commitAllowingStateLoss();
    }

    private void initAction(View view) {

        view.findViewById(R.id.tab0Layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(0);
            }
        });

        view.findViewById(R.id.tab1Layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(1);
            }
        });

        view.findViewById(R.id.tab2Layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(2);
            }
        });

        view.findViewById(R.id.tab3Layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(3);
            }
        });

        view.findViewById(R.id.tab4Layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(4);
            }
        });
    }

    private void changeTab(int index) {

        View view = getView();
        if (view == null) {
            return;
        }

        mHomeFragment.getView().setVisibility((index == 0) ? View.VISIBLE : View.GONE);
        mEntryFragment.getView().setVisibility((index == 1) ? View.VISIBLE : View.GONE);
        mSearchFragment.getView().setVisibility((index == 2) ? View.VISIBLE : View.GONE);
        mMessageFragment.getView().setVisibility((index == 3) ? View.VISIBLE : View.GONE);
        mMyPageFragment.getView().setVisibility((index == 4) ? View.VISIBLE : View.GONE);
    }
}
