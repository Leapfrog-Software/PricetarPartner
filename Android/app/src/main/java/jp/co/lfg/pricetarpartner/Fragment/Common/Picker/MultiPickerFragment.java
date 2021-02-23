package jp.co.lfg.pricetarpartner.Fragment.Common.Picker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;

public class MultiPickerFragment extends BaseFragment {

    private String mTitle;
    private ArrayList<Integer> mCurrentIndexes;
    private ArrayList<String> mDataList;
    private Callback mCallback;
    private PickerAdapter mAdapter;

    public static void show(BaseFragment fragment, String title, ArrayList<Integer> defaultIndexes, ArrayList<String> dataList, MultiPickerFragment.Callback callback) {

        MultiPickerFragment picker = new MultiPickerFragment();
        picker.mTitle = title;
        picker.mCurrentIndexes = defaultIndexes;
        picker.mDataList = dataList;
        picker.mCallback = callback;
        fragment.stackFragment(picker, AnimationType.none);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_multi_picker, null);

        initAction(view);
        initContent(view);
        initListView(view);
        reloadListView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view == null) {
            return;
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        ViewGroup.LayoutParams listViewParams = listView.getLayoutParams();
        listViewParams.height = getListViewHeight();
        listView.setLayoutParams(listViewParams);

        LinearLayout contentsLayout = (LinearLayout)view.findViewById(R.id.contentsLayout);
        int fromYDelta = getListViewHeight() + (int)(51 * DeviceUtility.getDeviceDensity());
        TranslateAnimation animation = new TranslateAnimation(0, 0, fromYDelta, 0);
        animation.setDuration(200);
        animation.setFillAfter(true);
        contentsLayout.startAnimation(animation);
    }

    private int getListViewHeight() {

        int height = (int)(50 * DeviceUtility.getDeviceDensity()) * mDataList.size();
        if (height > DeviceUtility.getWindowSize().y - 100) {
            return DeviceUtility.getWindowSize().y - 100;
        } else {
            return height;
        }
    }

    private void initContent(View view) {
        ((TextView)view.findViewById(R.id.titleTextView)).setText(mTitle);
    }

    private void initAction(View view) {

        ((Button)view.findViewById(R.id.okButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.didSelect(mCurrentIndexes);
                close();
            }
        });

        ((Button)view.findViewById(R.id.cancelButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
    }

    private void initListView(View view) {

        mAdapter = new PickerAdapter(getActivity());

        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = mCurrentIndexes.indexOf(i);
                if (index >= 0) {
                    mCurrentIndexes.remove(index);
                } else {
                    mCurrentIndexes.add(i);
                }
                reloadListView();
            }
        });
    }

    private void reloadListView() {

        if (mAdapter == null) {
            return;
        }
        mAdapter.clear();

        for (int i = 0; i < mDataList.size(); i++) {
            PickerAdapter.AdapterData data = new PickerAdapter.AdapterData();
            data.title = mDataList.get(i);
            data.selected = mCurrentIndexes.contains(i);
            mAdapter.add(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void close() {

        View view = getView();
        if (view == null) return;

        LinearLayout contentsLayout = (LinearLayout)view.findViewById(R.id.contentsLayout);
        int toYDelta = getListViewHeight() + (int)(51 * DeviceUtility.getDeviceDensity());
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, toYDelta);
        animation.setDuration(200);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                popFragment(AnimationType.none);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        contentsLayout.startAnimation(animation);
    }

    public interface Callback {
        void didSelect(ArrayList<Integer> indexes);
    }
}
