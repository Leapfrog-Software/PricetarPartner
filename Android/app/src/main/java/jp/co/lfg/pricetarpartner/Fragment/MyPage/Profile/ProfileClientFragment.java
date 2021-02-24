package jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.AreaSelect.ProfileAreaSelectFragment;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;

public class ProfileClientFragment extends BaseFragment {

    private String mSelectedArea = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile_client, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

    }

    private void initAction(View view) {

        view.findViewById(R.id.areaLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();

                ProfileAreaSelectFragment fragment = new ProfileAreaSelectFragment();
                fragment.set(mSelectedArea, new ProfileAreaSelectFragment.Callback() {
                    @Override
                    public void didSelect(String area) {
                        View view = getView();
                        if (view != null) {
                            ((TextView)view.findViewById(R.id.areaTextView)).setText(area);
                            mSelectedArea = area;
                        }
                    }
                });
                stackFragment(fragment, AnimationType.none);
            }
        });
        view.findViewById(R.id.useFrequencyLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
            }
        });
        view.findViewById(R.id.conditionLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
            }
        });
        view.findViewById(R.id.genreLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
            }
        });
        view.findViewById(R.id.optionLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
            }
        });
        view.findViewById(R.id.userImageLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
            }
        });
        view.findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
            }
        });
    }


}
