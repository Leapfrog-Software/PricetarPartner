package jp.co.lfg.pricetarpartner.Fragment.Message.Chat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Fragment.Common.Dialog;
import jp.co.lfg.pricetarpartner.Fragment.Common.Loading;
import jp.co.lfg.pricetarpartner.Http.DataModel.ChatData;
import jp.co.lfg.pricetarpartner.Http.Requester.FetchChatRequester;
import jp.co.lfg.pricetarpartner.Http.Requester.PostChatImageRequester;
import jp.co.lfg.pricetarpartner.Http.Requester.PostChatMessageRequester;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.BitmapUtility;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.GalleryManager;

public class ChatFragment extends BaseFragment {

    private String mTargetId;
    private boolean mActive = true;
    private ChatAdapter mAdapter;
    private boolean mIsInitialReload = true;
    private boolean mNeedScrollPosition = false;

    public void set(String targetId) {
        mTargetId = targetId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_chat, null);

        initAction(view);
        initListView(view);

        Loading.start();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mActive) {
                    fetch();
                    new Handler().postDelayed(this, 5000);
                }
            }
        };
        new Handler().post(runnable);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActive = false;
                DeviceUtility.hideSoftKeyboard();
                popFragment(AnimationType.horizontal);
            }
        });

        view.findViewById(R.id.cameraButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();

                GalleryManager.getInstance().open(GalleryManager.SourceType.Camera, new GalleryManager.Callback() {
                    @Override
                    public void didSelectImage(Bitmap bitmap) {
                        Bitmap chatBitmap = BitmapUtility.createChatBitmap(bitmap);
                        if (chatBitmap != null) {
                            postImage(chatBitmap);
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtility.hideSoftKeyboard();
                sendMessage();
            }
        });

        ((ListView)view.findViewById(R.id.listView)).setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mNeedScrollPosition = (totalItemCount < firstVisibleItem + visibleItemCount + 2);
            }
        });
    }

    private void initListView(View view) {

        mAdapter = new ChatAdapter(getActivity());

        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
    }

    private void fetch() {

        FetchChatRequester.fetch(mTargetId, new FetchChatRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, ArrayList<ChatData> chats) {
                Loading.stop();

                if (result) {
                    if (chats.size() != mAdapter.getCount()) {
                        reload(chats);
                    }
                }
            }
        });
    }

    private void reload(ArrayList<ChatData> chats) {

        View view = getView();
        if (view == null) {
            return;
        }

        mAdapter.clear();
        for (ChatData chatData : chats) {
            mAdapter.add(chatData);
        }
        mAdapter.notifyDataSetChanged();

        boolean needScroll = mIsInitialReload || mNeedScrollPosition;
        if (needScroll) {
            ListView listView = view.findViewById(R.id.listView);
            listView.smoothScrollToPosition(listView.getCount() - 1);
        }
        mIsInitialReload = false;
    }

    private void sendMessage() {

        View view = getView();
        if (view == null) {
            return;
        }

        String message = ((EditText)view.findViewById(R.id.messageEditText)).getText().toString();
        if (message.length() == 0) {
            return;
        }

        PostChatMessageRequester.post(mTargetId, message, new PostChatMessageRequester.Callback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) {
                    fetch();
                } else {
                    Dialog.show(Dialog.Style.error, "エラー", "通信に失敗しました");
                }
            }
        });

        ((EditText)view.findViewById(R.id.messageEditText)).setText("");
    }

    private void postImage(Bitmap bitmap) {

        PostChatImageRequester.post(mTargetId, bitmap, new PostChatImageRequester.Callback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) {
                    fetch();
                } else {
                    Dialog.show(Dialog.Style.error, "エラー", "通信に失敗しました");
                }
            }
        });
    }
}
