package jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;

public class ProfileFragment extends BaseFragment {

    public enum TransitionSource {
        splash,
        register,
        myPage;
    }

    private TransitionSource mTransitionSource;

    public void set(TransitionSource transitionSource) {
        mTransitionSource = transitionSource;
    }
}
