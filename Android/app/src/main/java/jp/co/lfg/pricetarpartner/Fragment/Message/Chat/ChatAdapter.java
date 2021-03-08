package jp.co.lfg.pricetarpartner.Fragment.Message.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.lfg.pricetarpartner.Http.DataModel.ChatData;
import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.Http.ImageStorage;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DateUtility;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.RoundOutlineProvider;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class ChatAdapter extends ArrayAdapter<ChatData> {

    LayoutInflater mInflater;
    Context mContext;

    public ChatAdapter(Context context){
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatData data = getItem(position);

        if (data.type == ChatData.ChatType.message) {
            // 自分の投稿
            if (data.senderId.equals(SaveData.getInstance().userId)) {
                convertView = mInflater.inflate(R.layout.adapter_chat_message_right, parent, false);

                ImageView senderImageView = convertView.findViewById(R.id.senderImageView);
                RoundOutlineProvider.setOutline(senderImageView, (int)(17 * DeviceUtility.getDeviceDensity()));
                ImageStorage.getInstance().fetch(UserData.getImageUrl(data.senderId), senderImageView, R.drawable.no_image);

                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(data.message);
                ((TextView)convertView.findViewById(R.id.datetimeTextView)).setText(data.getDisplayDatetime());
            } else {
                convertView = mInflater.inflate(R.layout.adapter_chat_message_left, parent, false);

                ImageView senderImageView = convertView.findViewById(R.id.senderImageView);
                RoundOutlineProvider.setOutline(senderImageView, (int)(17 * DeviceUtility.getDeviceDensity()));
                ImageStorage.getInstance().fetch(UserData.getImageUrl(data.senderId), senderImageView, R.drawable.no_image);

                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(data.message);
                ((TextView)convertView.findViewById(R.id.datetimeTextView)).setText(data.getDisplayDatetime());
            }
        } else {
            // 自分の投稿
            if (data.senderId.equals(SaveData.getInstance().userId)) {
                convertView = mInflater.inflate(R.layout.adapter_chat_image_right, parent, false);

                ImageView senderImageView = convertView.findViewById(R.id.senderImageView);
                RoundOutlineProvider.setOutline(senderImageView, (int)(17 * DeviceUtility.getDeviceDensity()));
                ImageStorage.getInstance().fetch(UserData.getImageUrl(data.senderId), senderImageView, R.drawable.no_image);

                ImageStorage.getInstance().fetch(data.getImageUrl(), (ImageView)convertView.findViewById(R.id.chatImageView), R.drawable.no_image);
                ((TextView)convertView.findViewById(R.id.datetimeTextView)).setText(data.getDisplayDatetime());
            } else {
                convertView = mInflater.inflate(R.layout.adapter_chat_image_left, parent, false);

                ImageView senderImageView = convertView.findViewById(R.id.senderImageView);
                RoundOutlineProvider.setOutline(senderImageView, (int)(17 * DeviceUtility.getDeviceDensity()));
                ImageStorage.getInstance().fetch(UserData.getImageUrl(data.senderId), senderImageView, R.drawable.no_image);

                ImageStorage.getInstance().fetch(data.getImageUrl(), (ImageView)convertView.findViewById(R.id.chatImageView), R.drawable.no_image);
                ((TextView)convertView.findViewById(R.id.datetimeTextView)).setText(data.getDisplayDatetime());
            }
        }
        return convertView;
    }
}
