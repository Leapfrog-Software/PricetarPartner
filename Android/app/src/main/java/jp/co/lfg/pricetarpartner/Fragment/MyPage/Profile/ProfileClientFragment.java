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
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Fragment.Common.Dialog;
import jp.co.lfg.pricetarpartner.Fragment.Common.Loading;
import jp.co.lfg.pricetarpartner.Fragment.Common.Picker.MultiPickerFragment;
import jp.co.lfg.pricetarpartner.Fragment.Common.Picker.PickerFragment;
import jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.AreaSelect.ProfileAreaSelectFragment;
import jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.GenreSelect.ProfileGenreSelectFragment;
import jp.co.lfg.pricetarpartner.Http.Requester.FetchUserRequester;
import jp.co.lfg.pricetarpartner.Http.Requester.UpdateClientProfileRequester;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.BitmapUtility;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.GalleryManager;
import jp.co.lfg.pricetarpartner.System.RoundOutlineProvider;

public class ProfileClientFragment extends BaseFragment {

    private String mSelectedArea = null;
    private String mSelectedUseFrequency = null;
    private String mSelectedCondition = null;
    private ArrayList<String> mSelectedGenres = new ArrayList<>();
    private ArrayList<String> mSelectedOptions = new ArrayList<>();
    private Bitmap mSelectedBitmap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile_client, null);

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
        view.findViewById(R.id.useFrequencyLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();

                final ArrayList<String> dataList = new ArrayList(Arrays.asList("0 〜 10", "11 〜 30", "31 〜 100", "100 〜"));

                Integer defaultIndex = null;
                if (mSelectedUseFrequency != null) {
                    defaultIndex = dataList.indexOf(mSelectedUseFrequency);
                }
                PickerFragment.show(getProfileFragment(), "月間のご利用見込み数", defaultIndex, dataList, new PickerFragment.Callback() {
                    @Override
                    public void didSelect(int index) {
                        View view = getView();
                        if (view != null) {
                            ((TextView)view.findViewById(R.id.useFrequencyTextView)).setText(dataList.get(index));
                            mSelectedUseFrequency = dataList.get(index);
                        }
                    }
                });
            }
        });
        view.findViewById(R.id.conditionLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();

                final ArrayList<String> dataList = new ArrayList(Arrays.asList("新品", "中古"));

                Integer defaultIndex = null;
                if (mSelectedCondition != null) {
                    defaultIndex = dataList.indexOf(mSelectedCondition);
                }
                PickerFragment.show(getProfileFragment(), "お取り扱い商品のコンディション", defaultIndex, dataList, new PickerFragment.Callback() {
                    @Override
                    public void didSelect(int index) {
                        View view = getView();
                        if (view != null) {
                            ((TextView)view.findViewById(R.id.conditionTextView)).setText(dataList.get(index));
                            mSelectedCondition = dataList.get(index);
                        }
                    }
                });
            }
        });
        view.findViewById(R.id.genreLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();

                ProfileGenreSelectFragment fragment = new ProfileGenreSelectFragment();
                fragment.set(mSelectedGenres, new ProfileGenreSelectFragment.Callback() {
                    @Override
                    public void didSelect(ArrayList<String> genres) {
                        View view = getView();
                        if (view != null) {
                            StringJoiner genresJoiner = new StringJoiner(", ");
                            for (String genre : genres) {
                                genresJoiner.add(genre);
                            }
                            ((TextView)view.findViewById(R.id.genreTextView)).setText(genresJoiner.toString());

                            mSelectedGenres = genres;
                        }
                    }
                });
                getProfileFragment().stackFragment(fragment, AnimationType.horizontal);
            }
        });
        view.findViewById(R.id.optionLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();

                ArrayList<String> dataList = new ArrayList<>(Arrays.asList("SKU設定希望", "危険物商品あり", "備品支給あり", "商品説明文に追加希望"));

                ArrayList<Integer> defaultIndexes = new ArrayList<>();
                for (String option : mSelectedOptions) {
                    int index = dataList.indexOf(option);
                    if (index >= 0) {
                        defaultIndexes.add(index);
                    }
                }
                MultiPickerFragment.show(getProfileFragment(), "追加オプション", defaultIndexes, dataList, new MultiPickerFragment.Callback() {
                    @Override
                    public void didSelect(ArrayList<Integer> indexes) {
                        View view = getView();
                        if (view != null) {
                            StringJoiner optionsJoiner = new StringJoiner(", ");

                            mSelectedOptions.clear();
                            for (Integer index : indexes) {
                                mSelectedOptions.add(dataList.get(index));
                                optionsJoiner.add(dataList.get(index));
                            }
                            ((TextView)view.findViewById(R.id.optionTextView)).setText(optionsJoiner.toString());
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
        if (mSelectedUseFrequency == null) {
            Dialog.show(Dialog.Style.error, "エラー", "月間のご利用見込み数が選択されていません");
            return;
        }
        if (mSelectedCondition == null) {
            Dialog.show(Dialog.Style.error, "エラー", "お取り扱い商品のコンディションが選択されていません");
            return;
        }

        String message = ((EditText)view.findViewById(R.id.messageEditText)).getText().toString();

        Loading.start();

        UpdateClientProfileRequester.update(nickname, mSelectedArea, mSelectedUseFrequency, mSelectedCondition, mSelectedGenres, mSelectedOptions, message, mSelectedBitmap, new UpdateClientProfileRequester.Callback() {
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
