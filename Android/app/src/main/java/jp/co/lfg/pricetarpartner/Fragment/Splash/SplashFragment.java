package jp.co.lfg.pricetarpartner.Fragment.Splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;

public class SplashFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_splash, null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoginLayout();
            }
        }, 1000);

        return view;
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
}
