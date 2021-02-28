package jp.co.lfg.pricetarpartner.Fragment.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.Http.ImageStorage;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;
import jp.co.lfg.pricetarpartner.System.RoundOutlineProvider;

public class HomeAdapter extends ArrayAdapter<HomeAdapter.AdapterData> {

    public enum AdapterType {
        title,
        user;
    }

    public static class AdapterData {
        public AdapterType type;
        public String title;
        public UserData userData;
    }

    LayoutInflater mInflater;
    Context mContext;

    public HomeAdapter(Context context){
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AdapterData data = getItem(position);

        if (data.type == AdapterType.title) {
            convertView = mInflater.inflate(R.layout.adapter_home_title, parent, false);
            ((TextView)convertView.findViewById(R.id.titleTextView)).setText(data.title);
        } else {
            convertView = mInflater.inflate(R.layout.adapter_home_user, parent, false);

            RoundOutlineProvider.setOutline(convertView.findViewById(R.id.userImageView), (int)(50 * DeviceUtility.getDeviceDensity()));
            ImageStorage.getInstance().fetch(UserData.getImageUrl(data.userData.id), (ImageView)convertView.findViewById(R.id.userImageView), R.drawable.no_image);

            if (data.userData.profileType == UserData.ProfileType.client) {
                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(data.userData.clientMessage);
            } else {
                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(data.userData.partnerMessage);
            }
        }

        return convertView;
    }
}
