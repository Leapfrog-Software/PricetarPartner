package jp.co.lfg.pricetarpartner.Http;

import android.os.Handler;

import java.util.Date;

import jp.co.lfg.pricetarpartner.Http.Requester.FetchChatGroupRequester;
import jp.co.lfg.pricetarpartner.Http.Requester.FetchUserRequester;
import jp.co.lfg.pricetarpartner.System.DateUtility;

public class AutoRequester {

    private Callback mCallback;
    private Date mLastFetchDatetime = null;
    private boolean mIsActive = false;
    private boolean mIsFetching = false;

    public void start(Callback callback) {

        mIsActive = true;
        mCallback = callback;
        mLastFetchDatetime = null;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(this, 1000);

                if (mLastFetchDatetime == null) {
                    fetch();
                } else if (DateUtility.addSecond(new Date(), -60).after(mLastFetchDatetime)) {
                    fetch();
                }
            }
        };
        new Handler().post(runnable);
    }

    private void fetch() {

        if (!mIsActive) {
            return;
        }
        if (mIsFetching) {
            return;
        }
        mIsFetching = true;

        FetchUserRequester.getInstance().fetch(new FetchUserRequester.Callback() {
            @Override
            public void didReceiveData(final boolean resultUser) {
                FetchChatGroupRequester.getInstance().fetch(new FetchChatGroupRequester.Callback() {
                    @Override
                    public void didReceiveData(boolean resultChatGroup) {
                        if (resultUser && resultChatGroup) {
                            mCallback.didReceiveData();
                        }
                        mIsFetching = false;
                    }
                });
            }
        });
        mLastFetchDatetime = new Date();
    }

    public void stop() {
        mIsActive = false;
    }

    public interface Callback {
        void didReceiveData();
    }
}
