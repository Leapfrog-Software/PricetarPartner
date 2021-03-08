package jp.co.lfg.pricetarpartner.Fragment.Message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Http.DataModel.ChatGroupData;
import jp.co.lfg.pricetarpartner.Http.Requester.FetchChatGroupRequester;
import jp.co.lfg.pricetarpartner.R;

public class MessageFragment extends BaseFragment {

    private MessageAdapter mAdapter;

    public void reload() {

        View view = getView();
        if (view != null) {
            reloadListView(view);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_message, null);

        initListView(view);
        reloadListView(view);

        return view;
    }

    private void initListView(View view) {

        mAdapter = new MessageAdapter(getActivity());

        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatGroupData chatGroupData = (ChatGroupData)parent.getItemAtPosition(position);


            }
        });
    }

    private void reloadListView(View view) {

        if (mAdapter == null) {
            return;
        }
        mAdapter.clear();

        for (ChatGroupData chatGroupData : FetchChatGroupRequester.getInstance().dataList) {
            mAdapter.add(chatGroupData);
        }
        mAdapter.notifyDataSetChanged();

        if (mAdapter.getCount() > 0) {
            view.findViewById(R.id.listView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.listView).setVisibility(View.GONE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.VISIBLE);
        }
    }
}
