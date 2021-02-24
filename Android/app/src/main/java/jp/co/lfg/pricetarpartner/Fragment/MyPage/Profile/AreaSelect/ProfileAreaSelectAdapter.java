package jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.AreaSelect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.lfg.pricetarpartner.R;

public class ProfileAreaSelectAdapter extends ArrayAdapter<ProfileAreaSelectAdapter.AdapterData> {

    public static class AdapterData {
        public boolean isSelected;
        public String area;
    }

    LayoutInflater mInflater;
    Context mContext;

    ProfileAreaSelectAdapter(Context context) {
        super(context, 0);

        mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AdapterData data = getItem(position);

        convertView = mInflater.inflate(R.layout.adapter_profile_area_select, parent, false);

        convertView.findViewById(R.id.selectedView).setVisibility(data.isSelected ? View.VISIBLE : View.GONE);
        ((TextView)convertView.findViewById(R.id.areaTextView)).setText(data.area);

        return convertView;
    }
}
