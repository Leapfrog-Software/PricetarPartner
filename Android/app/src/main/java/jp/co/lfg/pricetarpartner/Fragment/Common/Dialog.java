package jp.co.lfg.pricetarpartner.Fragment.Common;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.MainActivity;
import jp.co.lfg.pricetarpartner.R;

public class Dialog extends BaseFragment {

    public static class Action {
        public String title;
        public Callback callback;
        public ActionColor color;
    }

    public enum ActionColor {
        success,
        error,
        cancel;
    }

    public enum Style {
        success,
        error
    }

    private static MainActivity mActivity;
    private Style mStyle;
    private String mTitle;
    private String mMessage;
    private ArrayList<Action> mActions;

    public static void initialize(MainActivity activity) {
        mActivity = activity;
    }

    public static void show(Style style, String title, String message) {

        Action action = new Action();
        action.title = "OK";


        ArrayList<Action> actions = new ArrayList<>();
        actions.add(action);

        Dialog.show(style, title, message, actions);
    }

    public static void show(Style style, String title, String message, Action action) {
        Dialog.show(style, title, message, new ArrayList<Action>(Arrays.asList(action)));
    }

    public static void show(Style style, String title, String message, ArrayList<Action> actions) {

        if (mActivity == null) {
            return;
        }

        Dialog dialog = new Dialog();
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(mActivity.getSubContainerId(), dialog);
        transaction.commitAllowingStateLoss();

        dialog.mStyle = style;
        dialog.mTitle = title;
        dialog.mMessage = message;
        dialog.mActions = actions;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_dialog, null);

        initContents(view);

        return view;
    }

    private void initContents(View view) {

        if (mStyle == Style.success) {
            ((ImageView)view.findViewById(R.id.dialogImageView)).setImageResource(R.drawable.dialog_success);
        } else {
            ((ImageView)view.findViewById(R.id.dialogImageView)).setImageResource(R.drawable.dialog_error);
        }

        ((TextView)view.findViewById(R.id.titleTextView)).setText(mTitle);
        ((TextView)view.findViewById(R.id.messageTextView)).setText(mMessage);

        for (int i = 0; i < mActions.size(); i++) {
            final int fi = i;
            final Action action = mActions.get(i);

            Button button = new Button(mActivity);
            button.setText(action.title);
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            button.setTypeface(Typeface.DEFAULT_BOLD);
            button.setTextColor(Color.WHITE);

            if (action.color == null) {
                if (mStyle == Style.success) {
                    button.setBackgroundResource(R.drawable.shape_dialog_success_button);
                } else {
                    button.setBackgroundResource(R.drawable.shape_dialog_error_button);
                }
            } else {
                if (action.color == ActionColor.success) {
                    button.setBackgroundResource(R.drawable.shape_dialog_success_button);
                } else if (action.color == ActionColor.error) {
                    button.setBackgroundResource(R.drawable.shape_dialog_error_button);
                } else {
                    button.setBackgroundResource(R.drawable.shape_dialog_cancel_button);
                }
            }

            float density = mActivity.getResources().getDisplayMetrics().density;
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(40 * density));
            params.topMargin = (int)(12 * density);
            button.setLayoutParams(params);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (action.callback != null) {
                        action.callback.didClose();
                    }
                    FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
                    transaction.remove(Dialog.this);
                    transaction.commitAllowingStateLoss();
                }
            });
            ((LinearLayout)view.findViewById(R.id.dialogActionBaseLayout)).addView(button);
        }
    }

    public interface Callback {
        void didClose();
    }
}
