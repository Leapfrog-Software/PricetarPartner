package jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Fragment.Common.Dialog;
import jp.co.lfg.pricetarpartner.Fragment.Common.Loading;
import jp.co.lfg.pricetarpartner.Fragment.Common.Picker.PickerFragment;
import jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.AreaSelect.ProfileAreaSelectFragment;
import jp.co.lfg.pricetarpartner.Http.Requester.FetchUserRequester;
import jp.co.lfg.pricetarpartner.Http.Requester.UpdatePartnerProfileRequester;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.BitmapUtility;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.GalleryManager;
import jp.co.lfg.pricetarpartner.System.RoundOutlineProvider;

public class ProfilePartnerFragment extends BaseFragment {

    private String mSelectedArea = null;
    private String mSelectedCareer = null;
    private String mSelectedStatus = null;
    private Bitmap mSelectedBitmap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile_partner, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

        RoundOutlineProvider.setOutline(view.findViewById(R.id.userImageView), (int)(80 * DeviceUtility.getDeviceDensity()));
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
                getProfileFragment().stackFragment(fragment, AnimationType.none);
            }
        });

        view.findViewById(R.id.careerLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();

                final ArrayList<String> dataList = new ArrayList(Arrays.asList("1年未満", "1 〜 3年", "3 〜 5年", "5 〜 10年", "10年以上"));

                Integer defaultIndex = null;
                if (mSelectedCareer != null) {
                    defaultIndex = dataList.indexOf(mSelectedCareer);
                }
                PickerFragment.show(getProfileFragment(), "Amazonの販売キャリア・年数", defaultIndex, dataList, new PickerFragment.Callback() {
                    @Override
                    public void didSelect(int index) {
                        View view = getView();
                        if (view != null) {
                            ((TextView)view.findViewById(R.id.careerTextView)).setText(dataList.get(index));
                            mSelectedCareer = dataList.get(index);
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.statusLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();

                final ArrayList<String> dataList = new ArrayList(Arrays.asList("受付中", "休止中"));

                Integer defaultIndex = null;
                if (mSelectedStatus != mSelectedStatus) {
                    defaultIndex = dataList.indexOf(mSelectedCareer);
                }
                PickerFragment.show(getProfileFragment(), "お仕事の受付状況", defaultIndex, dataList, new PickerFragment.Callback() {
                    @Override
                    public void didSelect(int index) {
                        View view = getView();
                        if (view != null) {
                            ((TextView)view.findViewById(R.id.statusTextView)).setText(dataList.get(index));
                            mSelectedStatus = dataList.get(index);
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.userImageLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();

                GalleryManager.getInstance().open(GalleryManager.SourceType.Gallery, new GalleryManager.Callback() {
                    @Override
                    public void didSelectImage(Bitmap bitmap) {
                        View view = getView();
                        if (view != null) {
                            Bitmap userBitmap = BitmapUtility.createUserBitmap(bitmap);
                            if (userBitmap != null) {
                                ((ImageView)view.findViewById(R.id.userImageView)).setImageBitmap(userBitmap);
                                mSelectedBitmap = userBitmap;
                            }
                        }
                    }
                });
            }
        });
        view.findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
                onClickRegister();
            }
        });
    }

    private ProfileFragment getProfileFragment() {

        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof ProfileFragment) {
                return (ProfileFragment) fragment;
            }
        }
        return null;
    }

    private void onClickRegister() {

        View view = getView();
        if (view == null) {
            return;
        }
        String nickname = ((EditText)view.findViewById(R.id.nicknameEditText)).getText().toString();
        if (nickname.length() == 0) {
            Dialog.show(Dialog.Style.error, "エラー", "ニックネームの入力がありません");
            return;
        }

        if (mSelectedArea == null) {
            Dialog.show(Dialog.Style.error, "エラー", "居住エリアが選択されていません");
            return;
        }
        if (mSelectedCareer == null) {
            Dialog.show(Dialog.Style.error, "エラー", "Amazon販売キャリア・年数が選択されていません");
            return;
        }
        if (mSelectedStatus == null) {
            Dialog.show(Dialog.Style.error, "エラー", "お仕事の受付状況が選択されていません");
            return;
        }

        String newPrice = ((EditText)view.findViewById(R.id.newPriceEditText)).getText().toString();
        String oldPrice = ((EditText)view.findViewById(R.id.oldPriceEditText)).getText().toString();
        if ((newPrice.length() == 0) || (oldPrice.length() == 0)) {
            Dialog.show(Dialog.Style.error, "エラー", "基本の作業料金の入力がありません");
            return;
        }

        String dangerousPrice = ((EditText)view.findViewById(R.id.dangerousPriceEditText)).getText().toString();
        String dangerousMessage = ((EditText)view.findViewById(R.id.dangerousMessageEditText)).getText().toString();
        String bigPrice = ((EditText)view.findViewById(R.id.bigPriceEditText)).getText().toString();
        String bigMessage = ((EditText)view.findViewById(R.id.bigMessageEditText)).getText().toString();
        String inspectionPrice = ((EditText)view.findViewById(R.id.inspectionPriceEditText)).getText().toString();
        String inspectionMessage = ((EditText)view.findViewById(R.id.inspectionMessageEditText)).getText().toString();
        String message = ((EditText)view.findViewById(R.id.messageEditText)).getText().toString();

        Loading.start();

        UpdatePartnerProfileRequester.update(nickname, mSelectedArea, mSelectedCareer, mSelectedStatus, newPrice, oldPrice, dangerousPrice, dangerousMessage, bigPrice, bigMessage, inspectionPrice, inspectionMessage, message, mSelectedBitmap, new UpdatePartnerProfileRequester.Callback() {
            @Override
            public void didReceiveData(boolean resultUpdate) {
                FetchUserRequester.getInstance().fetch(new FetchUserRequester.Callback() {
                    @Override
                    public void didReceiveData(boolean resultFetch) {
                        Loading.stop();

                        if (resultUpdate) {
                            ProfileFragment profileFragment = getProfileFragment();
                            if (profileFragment != null) {
                                profileFragment.didUpdate();
                            }
                        } else {
                            Dialog.show(Dialog.Style.error, "エラー", "通信に失敗しました");
                        }
                    }
                });
            }
        });


    }
}
