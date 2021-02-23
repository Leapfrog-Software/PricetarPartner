package jp.co.lfg.pricetarpartner.Fragment.Splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Fragment.Common.Dialog;
import jp.co.lfg.pricetarpartner.Fragment.Common.Loading;
import jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.ProfileFragment;
import jp.co.lfg.pricetarpartner.Http.Requester.RegisterUserRequester;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class RegisterUserFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_register_user, null);

        initAction(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
                popFragment(AnimationType.horizontal);
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

    private void onClickRegister() {

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

        RegisterUserRequester.register(email, password, new RegisterUserRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, String userId) {
                if (result) {
                    SaveData saveData = SaveData.getInstance();
                    saveData.userId = userId;
                    saveData.save();

                    ProfileFragment fragment = new ProfileFragment();
                    fragment.set(ProfileFragment.TransitionSource.register);
                    stackFragment(fragment, AnimationType.horizontal);
                } else {
                    Dialog.show(Dialog.Style.error, "エラー", "通信に失敗しました");
                }
            }
        });
    }
}
