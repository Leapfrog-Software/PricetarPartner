package jp.co.lfg.pricetarpartner.Fragment.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.lfg.pricetarpartner.Fragment.Home.HomeAdapter;
import jp.co.lfg.pricetarpartner.Http.DataModel.ChatGroupData;
import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.Http.ImageStorage;
import jp.co.lfg.pricetarpartner.Http.Requester.FetchUserRequester;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class MessageAdapter extends ArrayAdapter<ChatGroupData> {

    LayoutInflater mInflater;
    Context mContext;

    public MessageAdapter(Context context){
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatGroupData data = getItem(position);

        convertView = mInflater.inflate(R.layout.adapter_message, parent, false);

        if (data.userId1.equals(SaveData.getInstance().userId)) {
            ImageStorage.getInstance().fetch(UserData.getImageUrl(data.userId2), (ImageView)convertView.findViewById(R.id.userImageView), R.drawable.no_image);

            String userName = "";
            UserData userData = FetchUserRequester.getInstance().query(data.userId2);
            if (userData != null) {
                userName = userData.nickname;
            }
            ((TextView)convertView.findViewById(R.id.nameTextView)).setText(userName);

            int unreadCount = data.unreadCount2;
            if (unreadCount > 99) {
                ((TextView)convertView.findViewById(R.id.unreadCountTextView)).setText("99+");
            } else {
                ((TextView)convertView.findViewById(R.id.unreadCountTextView)).setText(String.valueOf(unreadCount));
            }
        } else {
            ImageStorage.getInstance().fetch(UserData.getImageUrl(data.userId1), (ImageView)convertView.findViewById(R.id.userImageView), R.drawable.no_image);

            String userName = "";
            UserData userData = FetchUserRequester.getInstance().query(data.userId1);
            if (userData != null) {
                userName = userData.nickname;
            }
            ((TextView)convertView.findViewById(R.id.nameTextView)).setText(userName);

            int unreadCount = data.unreadCount1;
            if (unreadCount > 99) {
                ((TextView)convertView.findViewById(R.id.unreadCountTextView)).setText("99+");
            } else {
                ((TextView)convertView.findViewById(R.id.unreadCountTextView)).setText(String.valueOf(unreadCount));
            }
        }

        return convertView;
    }
}
