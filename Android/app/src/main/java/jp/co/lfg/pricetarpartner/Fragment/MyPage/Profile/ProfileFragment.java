package jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.Http.Requester.FetchUserRequester;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class ProfileFragment extends BaseFragment {

    public enum TransitionSource {
        splash,
        register,
        myPage;
    }

    private TransitionSource mTransitionSource;
    private ProfileClientFragment mProfileClientFragment;
    private ProfilePartnerFragment mProfilePartnerFragment;

    public void set(TransitionSource transitionSource) {
        mTransitionSource = transitionSource;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

        if (mTransitionSource == TransitionSource.register) {
            view.findViewById(R.id.backButton).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.backButton).setVisibility(View.VISIBLE);
        }

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        mProfileClientFragment = new ProfileClientFragment();
        mProfilePartnerFragment = new ProfilePartnerFragment();

        UserData myUserData = FetchUserRequester.getInstance().query(SaveData.getInstance().userId);
        if ((myUserData != null) && (myUserData.profileType == UserData.ProfileType.partner)) {
            transaction.add(R.id.profileContainerLayout, mProfileClientFragment);
            transaction.add(R.id.profileContainerLayout, mProfilePartnerFragment);
            changeSegment(view, 1);
        } else {
            transaction.add(R.id.profileContainerLayout, mProfilePartnerFragment);
            transaction.add(R.id.profileContainerLayout, mProfileClientFragment);
            changeSegment(view, 0);
        }
        transaction.commitAllowingStateLoss();
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close(AnimationType.horizontal);
            }
        });

        view.findViewById(R.id.clientButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSegment(getView(), 0);
            }
        });

        view.findViewById(R.id.partnerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSegment(getView(), 1);
            }
        });
    }

    private void changeSegment(View view, int index) {

        if (view == null) {
            return;
        }

        View clientView = mProfileClientFragment.getView();
        View partnerView = mProfilePartnerFragment.getView();
        if ((clientView != null) && (partnerView != null)) {
            clientView.setVisibility((index == 0) ? View.VISIBLE : View.GONE);
            partnerView.setVisibility((index == 1) ? View.VISIBLE : View.GONE);
        }
        Button clientButton = view.findViewById(R.id.clientButton);
        Button partnerButton = view.findViewById(R.id.partnerButton);

        clientButton.setBackgroundResource((index == 0) ? R.drawable.shape_segment_left_on : R.drawable.shape_segment_left_off);
        partnerButton.setBackgroundResource((index == 1) ? R.drawable.shape_segment_right_on : R.drawable.shape_segment_right_off);

        clientButton.setTextColor((index == 0) ? Color.WHITE : Color.rgb(0.6f, 0.6f, 0.6f));
        partnerButton.setTextColor((index == 1) ? Color.WHITE : Color.rgb(0.6f, 0.6f, 0.6f));
    }

    public void close(AnimationType animationType) {

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (mProfileClientFragment != null) {
            transaction.remove(mProfileClientFragment);
        }
        if (mProfilePartnerFragment != null) {
            transaction.remove(mProfilePartnerFragment);
        }
        transaction.commit();

        popFragment(animationType);
    }

    public void didUpdate() {

        // スプラッシュから遷移(ログイン後プロフィール未設定)
        if (mTransitionSource == TransitionSource.splash) {

        }
        // 新規登録画面から遷移(新規登録済みプロフィール未設定)
        else if (mTransitionSource == TransitionSource.register) {

        }
        // マイページから遷移
        else {

        }
    }
}
