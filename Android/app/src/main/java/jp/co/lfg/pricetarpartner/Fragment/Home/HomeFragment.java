package jp.co.lfg.pricetarpartner.Fragment.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.Http.Requester.FetchUserRequester;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class HomeFragment extends BaseFragment {

    private HomeAdapter mAdapter;

    public void reload() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_home, null);

        initListView(view);
        reloadListView();

        return view;
    }

    private void initListView(View view) {

        mAdapter = new HomeAdapter(getActivity());

        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeAdapter.AdapterData data = (HomeAdapter.AdapterData)parent.getItemAtPosition(position);

            }
        });
    }

    private void reloadListView() {

        if (mAdapter == null) {
            return;
        }
        UserData myUserData = FetchUserRequester.getInstance().query(SaveData.getInstance().userId);
        if (myUserData == null) {
            return;
        }

        mAdapter.clear();

        HomeAdapter.AdapterData newTitleData = new HomeAdapter.AdapterData();
        newTitleData.type = HomeAdapter.AdapterType.title;
        newTitleData.title = "新着ユーザー";
        mAdapter.add(newTitleData);

        for (UserData userData : FetchUserRequester.getInstance().dataList) {
            if ((userData.profileType != UserData.ProfileType.none) && (userData.profileType != myUserData.profileType)) {
                // TODO ソート
                HomeAdapter.AdapterData newUserData = new HomeAdapter.AdapterData();
                newUserData.type = HomeAdapter.AdapterType.user;
                newUserData.userData = userData;
                mAdapter.add(newUserData);
            }
        }

        HomeAdapter.AdapterData popularTitleData = new HomeAdapter.AdapterData();
        popularTitleData.type = HomeAdapter.AdapterType.title;
        popularTitleData.title = "人気ユーザー";
        mAdapter.add(popularTitleData);

        for (UserData userData : FetchUserRequester.getInstance().dataList) {
            if ((userData.profileType != UserData.ProfileType.none) && (userData.profileType != myUserData.profileType)) {
                // TODO ソート
                HomeAdapter.AdapterData popularUserData = new HomeAdapter.AdapterData();
                popularUserData.type = HomeAdapter.AdapterType.user;
                popularUserData.userData = userData;
                mAdapter.add(popularUserData);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
