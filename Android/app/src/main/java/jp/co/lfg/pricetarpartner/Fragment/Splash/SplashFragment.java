package jp.co.lfg.pricetarpartner.Fragment.Splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Fragment.Common.Dialog;
import jp.co.lfg.pricetarpartner.Fragment.Common.Loading;
import jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.ProfileFragment;
import jp.co.lfg.pricetarpartner.Fragment.Tabbar.TabbarFragment;
import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.Http.Requester.FetchUserRequester;
import jp.co.lfg.pricetarpartner.Http.Requester.LoginRequester;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class SplashFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_splash, null);

        initAction(view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetch();
            }
        }, 1000);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.loginLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
                onClickLogin();
            }
        });

        view.findViewById(R.id.registerLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
                stackFragment(new RegisterUserFragment(), AnimationType.horizontal);
            }
        });
    }

    private void fetch() {

        FetchUserRequester.getInstance().fetch(new FetchUserRequester.Callback() {
            @Override
            public void didReceiveData(boolean result) {
                UserData myUserData = FetchUserRequester.getInstance().query(SaveData.getInstance().userId);
                if ((myUserData != null) && (myUserData.profileType != UserData.ProfileType.none)) {
                    stackFragment(new TabbarFragment(), AnimationType.none);
                } else {
                    showLoginLayout();
                }
            }
        });
    }

    private void showLoginLayout() {

        View view = getView();
        if (view == null) {
            return;
        }

        TranslateAnimation logoAnim = new TranslateAnimation(0, 0, 0, (int)(-220 * DeviceUtility.getDeviceDensity()));
        logoAnim.setDuration(600);
        logoAnim.setFillAfter(true);
        view.findViewById(R.id.logoImageView).startAnimation(logoAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View view = getView();
                if (view == null) {
                    return;
                }
                LinearLayout loginLayout = view.findViewById(R.id.loginBaseLayout);
                loginLayout.setVisibility(View.VISIBLE);
                LinearLayout registerLayout = view.findViewById(R.id.registerLayout);
                registerLayout.setVisibility(View.VISIBLE);
                AlphaAnimation visibleAnim = new AlphaAnimation(0.0f, 1.0f);
                visibleAnim.setDuration(400);
                visibleAnim.setFillAfter(true);
                loginLayout.startAnimation(visibleAnim);
                registerLayout.startAnimation(visibleAnim);
            }
        }, 400);
    }

    private void onClickLogin() {

        View view = getView();
        if (view == null) {
            return;
        }
        String email = ((EditText)view.findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)view.findViewById(R.id.passwordEditText)).getText().toString();

        if (email.length() == 0) {
            Dialog.show(Dialog.Style.error, "エラー", "メールアドレスの入力がありません");
            return;
        }
        if (password.length() == 0) {
            Dialog.show(Dialog.Style.error, "エラー", "パスワードの入力がありません");
            return;
        }

        Loading.start();

        LoginRequester.login(email, password, new LoginRequester.Callback() {
            @Override
            public void didReceiveData(boolean resultLogin, String userId) {
                FetchUserRequester.getInstance().fetch(new FetchUserRequester.Callback() {
                    @Override
                    public void didReceiveData(boolean resultFetch) {
                        Loading.stop();

                        if (resultLogin) {
                            UserData myUserData = FetchUserRequester.getInstance().query(userId);
                            if (myUserData != null) {
                                SaveData saveData = SaveData.getInstance();
                                saveData.userId = userId;
                                saveData.save();

                                if (myUserData.profileType != UserData.ProfileType.none) {
                                    stackFragment(new TabbarFragment(), AnimationType.none);
                                } else {
                                    ProfileFragment fragment = new ProfileFragment();
                                    fragment.set(ProfileFragment.TransitionSource.splash);
                                    stackFragment(fragment, AnimationType.horizontal);
                                }
                                return;
                            }
                        }
                        Dialog.show(Dialog.Style.error, "エラー", "ログインに失敗しまいsた");
                    }
                });
            }
        });
    }
}
