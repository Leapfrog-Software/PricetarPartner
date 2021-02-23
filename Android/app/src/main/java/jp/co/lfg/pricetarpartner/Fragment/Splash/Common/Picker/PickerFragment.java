package jp.co.lfg.pricetarpartner.Fragment.Splash.Common.Picker;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;

public class PickerFragment extends BaseFragment {

    private String mTitle;
    private Integer mCurrentIndex;
    private ArrayList<String> mDataList;
    private Callback mCallback;
    private PickerAdapter mAdapter;

    public static void show(BaseFragment fragment, String title, Integer defaultIndex, ArrayList<String> dataList, Callback callback) {

        PickerFragment picker = new PickerFragment();
        picker.mTitle = title;
        picker.mCurrentIndex = defaultIndex;
        picker.mDataList = dataList;
        picker.mCallback = callback;
        fragment.stackFragment(picker, AnimationType.none);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_picker, null);

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

        ListView listView = (ListView) view.findViewById(R.id.listView);
        ViewGroup.LayoutParams listViewParams = listView.getLayoutParams();
        listViewParams.height = getListViewHeight();
        listView.setLayoutParams(listViewParams);

        LinearLayout contentsLayout = (LinearLayout) view.findViewById(R.id.contentsLayout);
        int fromYDelta = getListViewHeight() + (int) (51 * DeviceUtility.getDeviceDensity());
        TranslateAnimation animation = new TranslateAnimation(0, 0, fromYDelta, 0);
        animation.setDuration(200);
        animation.setFillAfter(true);
        contentsLayout.startAnimation(animation);
    }

    private int getListViewHeight() {

        int height = (int) (50 * DeviceUtility.getDeviceDensity()) * mDataList.size();
        if (height > DeviceUtility.getWindowSize().y - 100) {
            return DeviceUtility.getWindowSize().y - 100;
        } else {
            return height;
        }
    }


    private void initContent(View view) {

        ((TextView) view.findViewById(R.id.titleTextView)).setText(mTitle);
    }

    private void initAction(View view) {

        view.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
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
                mCurrentIndex = i;
                reloadListView();
                mCallback.didSelect(i);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        close();
                    }
                }, 200);
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
            data.selected = ((mCurrentIndex != null) && (mCurrentIndex == i));
            mAdapter.add(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void close() {

        View view = getView();
        if (view == null) return;

        LinearLayout contentsLayout = (LinearLayout) view.findViewById(R.id.contentsLayout);
        int toYDelta = getListViewHeight() + (int) (51 * DeviceUtility.getDeviceDensity());
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, toYDelta);
        animation.setDuration(200);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                popFragment(AnimationType.none);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        contentsLayout.startAnimation(animation);
    }

    public interface Callback {
        void didSelect(int index);
    }
}
