package jp.co.lfg.pricetarpartner.Fragment.Common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.fragment.app.FragmentTransaction;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.MainActivity;
import jp.co.lfg.pricetarpartner.R;

public class Loading extends BaseFragment {

    private static MainActivity mActivity;
    private static Loading mLoading;

    public static void initialize(MainActivity activity) {
        mActivity = activity;
    }

    public static void start() {

        if ((mLoading != null) || (mActivity == null)) {
            return;
        }

        Loading loading = new Loading();
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(mActivity.getSubContainerId(), loading);
        transaction.commitAllowingStateLoss();

        mLoading = loading;
    }

    public static void stop() {

        if (mLoading == null) {
            return;
        }

        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.remove(mLoading);
        transaction.commitAllowingStateLoss();

        mLoading = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_loading, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.loadingImageView);
        RotateAnimation animation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1200);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        imageView.startAnimation(animation);

        return view;
    }
}
