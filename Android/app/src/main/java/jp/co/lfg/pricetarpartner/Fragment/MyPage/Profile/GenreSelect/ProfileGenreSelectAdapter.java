package jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.GenreSelect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import jp.co.lfg.pricetarpartner.R;

public class ProfileGenreSelectAdapter extends ArrayAdapter<ProfileGenreSelectAdapter.AdapterData> {

    public static class AdapterData {
        public boolean isSelected;
        public String genre;
    }

    LayoutInflater mInflater;
    Context mContext;

    ProfileGenreSelectAdapter(Context context) {
        super(context, 0);

        mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AdapterData data = getItem(position);

        convertView = mInflater.inflate(R.layout.adapter_profile_genre_select, parent, false);

        convertView.findViewById(R.id.selectedImageView).setVisibility(data.isSelected ? View.VISIBLE : View.INVISIBLE);
        ((TextView)convertView.findViewById(R.id.genreTextView)).setText(data.genre);

        return convertView;
    }
}
