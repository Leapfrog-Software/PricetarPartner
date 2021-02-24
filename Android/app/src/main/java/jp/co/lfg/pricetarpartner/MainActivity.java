package jp.co.lfg.pricetarpartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import jp.co.lfg.pricetarpartner.Fragment.Common.Dialog;
import jp.co.lfg.pricetarpartner.Fragment.Common.Loading;
import jp.co.lfg.pricetarpartner.Fragment.Splash.SplashFragment;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.GalleryManager;
import jp.co.lfg.pricetarpartner.System.PermissionManager;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Loading.initialize(this);
        Dialog.initialize(this);
        SaveData.getInstance().initialize(this);
        DeviceUtility.initialize(this);
        GalleryManager.initialize(this);
        PermissionManager.initialize(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rootContainer, new SplashFragment());
        transaction.commitAllowingStateLoss();
    }

    public int getSubContainerId() {
        return R.id.subContainer;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ギャラリー
        GalleryManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.getInstance().onRequestPermissionsResult(requestCode);
    }
}