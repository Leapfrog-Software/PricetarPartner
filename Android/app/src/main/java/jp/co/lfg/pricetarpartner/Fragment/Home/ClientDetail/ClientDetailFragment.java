package jp.co.lfg.pricetarpartner.Fragment.Home.ClientDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.StringJoiner;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Fragment.Message.Chat.ChatFragment;
import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.Http.ImageStorage;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.RoundOutlineProvider;

public class ClientDetailFragment extends BaseFragment {

    private UserData mUserData;

    public void set(UserData userData) {
        mUserData = userData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_client_detail, null);

        initAction(view);
        initContents(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });

        view.findViewById(R.id.messageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatFragment fragment = new ChatFragment();
                fragment.set(mUserData.id);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }

    private void initContents(View view) {

        ((TextView)view.findViewById(R.id.headerNameTextView)).setText(mUserData.nickname);

        ImageView userImageView = view.findViewById(R.id.userImageView);
        int imageViewWidth = DeviceUtility.getWindowSize().x - (int)(120 * DeviceUtility.getDeviceDensity());
        RoundOutlineProvider.setOutline(userImageView, (int)imageViewWidth / 2);
        userImageView.getLayoutParams().height = imageViewWidth;
        ImageStorage.getInstance().fetch(UserData.getImageUrl(mUserData.id), userImageView, R.drawable.no_image);

        ((TextView)view.findViewById(R.id.nameTextView)).setText(mUserData.nickname);
        ((TextView)view.findViewById(R.id.statusTextView)).setText(mUserData.partnerStatus);
        ((TextView)view.findViewById(R.id.loginDatetimeTextView)).setText(mUserData.lastLoginString());
        ((TextView)view.findViewById(R.id.areaTextView)).setText(mUserData.area);
        ((TextView)view.findViewById(R.id.messageTextView)).setText(mUserData.clientMessage);
        ((TextView)view.findViewById(R.id.useFrequencyTextView)).setText(mUserData.clientUseFrequency);
        ((TextView)view.findViewById(R.id.newConditionTextView)).setText(mUserData.clientNewCondition);
        ((TextView)view.findViewById(R.id.oldConditionTextView)).setText(mUserData.clientOldCondition);

        StringJoiner genreJoiner = new StringJoiner("・");
        for (String genre : mUserData.clientGenres) {
            genreJoiner.add(genre);
        }
        ((TextView)view.findViewById(R.id.genreTextView)).setText(genreJoiner.toString());

        StringJoiner optionJoiner = new StringJoiner("\n");
        for (String option : mUserData.clientOptions) {
            optionJoiner.add(option);
        }
        ((TextView)view.findViewById(R.id.optionTextView)).setText(optionJoiner.toString());
    }
}
