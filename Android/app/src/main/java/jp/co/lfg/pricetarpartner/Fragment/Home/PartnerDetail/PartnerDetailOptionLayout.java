package jp.co.lfg.pricetarpartner.Fragment.Home.PartnerDetail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import jp.co.lfg.pricetarpartner.R;

public class PartnerDetailOptionLayout extends LinearLayout {

    public PartnerDetailOptionLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_partner_detail_option, this, true);
    }

    public void set(String title, String price, String message) {

        ((TextView)findViewById(R.id.titleTextView)).setText(title);
        ((TextView)findViewById(R.id.priceTextView)).setText(price);
        ((TextView)findViewById(R.id.messageTextView)).setText(message);
    }
}
