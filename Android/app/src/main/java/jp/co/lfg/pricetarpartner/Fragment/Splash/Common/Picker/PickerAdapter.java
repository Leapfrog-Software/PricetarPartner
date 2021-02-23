package jp.co.lfg.pricetarpartner.Fragment.Splash.Common.Picker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import jp.co.lfg.pricetarpartner.R;

public class PickerAdapter extends ArrayAdapter<PickerAdapter.AdapterData> {

    public static class AdapterData {
        boolean selected;
        String title;
    }

    LayoutInflater mInflater;
    Context mContext;

    public PickerAdapter(Context context) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.adapter_picker, parent, false);

        AdapterData data = getItem(position);

        if (data.selected) {
            convertView.findViewById(R.id.selectedImageView).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.selectedImageView).setVisibility(View.INVISIBLE);
        }

        ((TextView) convertView.findViewById(R.id.titleTextView)).setText(data.title);

        return convertView;
    }
}