package jp.co.lfg.pricetarpartner.Fragment.MyPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.ProfileFragment;
import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.Http.ImageStorage;
import jp.co.lfg.pricetarpartner.Http.Requester.FetchUserRequester;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.RoundOutlineProvider;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class MyPageFragment extends BaseFragment {

    public void reload() {

        View view = getView();
        if (view != null) {
            reloadContents(view);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage, null);

        initAction(view);
        initContents(view);
        reloadContents(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragment = new ProfileFragment();
                fragment.set(ProfileFragment.TransitionSource.myPage);
                getTabbar().stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }

    private void initContents(View view) {

        ImageView userImageView = view.findViewById(R.id.userImageView);
        RoundOutlineProvider.setOutline(userImageView, (int)(10 * DeviceUtility.getDeviceDensity()));
    }

    private void reloadContents(View view) {

        UserData myUserData = FetchUserRequester.getInstance().query(SaveData.getInstance().userId);
        if (myUserData == null) {
            return;
        }

        ImageStorage.getInstance().fetch(UserData.getImageUrl(myUserData.id), (ImageView)view.findViewById(R.id.userImageView), R.drawable.no_image);
        ((TextView)view.findViewById(R.id.profileTypeTextView)).setText(myUserData.profileType.toText());
        ((TextView)view.findViewById(R.id.nameTextView)).setText(myUserData.nickname);
    }
}
