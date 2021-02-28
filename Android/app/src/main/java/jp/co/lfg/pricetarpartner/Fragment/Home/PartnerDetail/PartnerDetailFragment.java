package jp.co.lfg.pricetarpartner.Fragment.Home.PartnerDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.Http.ImageStorage;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.RoundOutlineProvider;

public class PartnerDetailFragment extends BaseFragment {

    private UserData mUserData;

    public void set(UserData userData) {
        mUserData = userData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_partner_detail, null);

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
    }

    private void initContents(View view) {

        ((TextView)view.findViewById(R.id.headerNameTextView)).setText(mUserData.nickname);

        ImageView userImageView = view.findViewById(R.id.userImageView);
        int imageViewWidth = DeviceUtility.getWindowSize().x - (int)(120 * DeviceUtility.getDeviceDensity());
        userImageView.getLayoutParams().height = imageViewWidth;
        RoundOutlineProvider.setOutline(userImageView, imageViewWidth / 2);
        ImageStorage.getInstance().fetch(UserData.getImageUrl(mUserData.id), userImageView, R.drawable.no_image);

        ((TextView)view.findViewById(R.id.nameTextView)).setText(mUserData.nickname);
        ((TextView)view.findViewById(R.id.statusTextView)).setText(mUserData.partnerStatus);
        ((TextView)view.findViewById(R.id.loginDatetimeTextView)).setText(mUserData.lastLoginString());

        // TODO レビュー

        ((TextView)view.findViewById(R.id.areaTextView)).setText(mUserData.area);
        ((TextView)view.findViewById(R.id.careerTextView)).setText(mUserData.partnerCareer);
        ((TextView)view.findViewById(R.id.messageTextView)).setText(mUserData.partnerMessage);
        ((TextView)view.findViewById(R.id.newPriceTextView)).setText(mUserData.partnerNewPrice);
        ((TextView)view.findViewById(R.id.oldPriceTextView)).setText(mUserData.partnerOldPrice);

        addOptionLayout(view, "危険物商品", mUserData.partnerDangerousPrice, mUserData.partnerDangerousMessage);
        addOptionLayout(view, "大型商品", mUserData.partnerBigPrice, mUserData.partnerBigMessage);
        addOptionLayout(view, "検品作業", mUserData.partnerInspectionPrice, mUserData.partnerInspectionMessage);
    }

    private void addOptionLayout(View view, String title, String price, String message) {

        PartnerDetailOptionLayout layout = new PartnerDetailOptionLayout(getActivity(), null);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.set(title, price, message);
        ((LinearLayout)view.findViewById(R.id.optionBaseLayout)).addView(layout);
    }
}
