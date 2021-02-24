package jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.AreaSelect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;

public class ProfileAreaSelectFragment extends BaseFragment {

    private ProfileAreaSelectAdapter mAdapter;
    private int mSelectedIndex = 0;
    private ArrayList<String> mAreas = new ArrayList<>(Arrays.asList("北海道", "東北", "関東", "中部", "近畿", "中国", "四国", "九州"));
    private Callback mCallback;

    public void set(String defaultArea, Callback callback) {

        if (defaultArea != null) {
            Integer defaultIndex = mAreas.indexOf(defaultArea);
            if ((defaultIndex >= 0) && (defaultIndex < mAreas.size())) {
                mSelectedIndex = defaultIndex;
            }
        }
        mCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile_area_select, null);

        initAction(view);
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

        TranslateAnimation animation = new TranslateAnimation(0, 0, 357 * DeviceUtility.getDeviceDensity(), 0);
        animation.setDuration(200);
        animation.setFillAfter(true);
        view.findViewById(R.id.contentsLayout).startAnimation(animation);
    }

    private void initAction(View view) {

        view.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        view.findViewById(R.id.upButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedIndex >= 1) {
                    mSelectedIndex -= 1;
                    reloadListView();
                }
            }
        });
        view.findViewById(R.id.downButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedIndex <= mAreas.size() - 2) {
                    mSelectedIndex += 1;
                    reloadListView();
                }
            }
        });
        view.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.didSelect(mAreas.get(mSelectedIndex));
                close();
            }
        });
    }

    private void close() {

        View view = getView();
        if (view == null) {
            return;
        }

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 357 * DeviceUtility.getDeviceDensity());
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
        view.findViewById(R.id.contentsLayout).startAnimation(animation);
    }

    private void initListView(View view) {

        mAdapter = new ProfileAreaSelectAdapter(getActivity());

        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedIndex = position;
                reloadListView();
            }
        });
    }

    private void reloadListView() {

        if (mAdapter == null) {
            return;
        }
        mAdapter.clear();

        for (int i = 0; i < mAreas.size(); i++) {
            ProfileAreaSelectAdapter.AdapterData data = new ProfileAreaSelectAdapter.AdapterData();
            data.area = mAreas.get(i);
            data.isSelected = (mSelectedIndex == i);
            mAdapter.add(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    public interface Callback {
        void didSelect(String area);
    }
}
