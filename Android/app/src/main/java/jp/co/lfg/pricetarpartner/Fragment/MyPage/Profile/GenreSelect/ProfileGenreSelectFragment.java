package jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.GenreSelect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import jp.co.lfg.pricetarpartner.Fragment.BaseFragment;
import jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.AreaSelect.ProfileAreaSelectAdapter;
import jp.co.lfg.pricetarpartner.Fragment.MyPage.Profile.AreaSelect.ProfileAreaSelectFragment;
import jp.co.lfg.pricetarpartner.R;
import jp.co.lfg.pricetarpartner.System.DeviceUtility;

public class ProfileGenreSelectFragment extends BaseFragment {

    private ProfileGenreSelectAdapter mAdapter;
    private ArrayList<Integer> mSelectedIndexes = new ArrayList<>();
    private ArrayList<String> mGenres = new ArrayList<>(Arrays.asList("メディア商品", "ゲーム機・ゲームソフト", "家電・カメラ・AV機器", "パソコン・オフィス商品", "ホーム・キッチン", "食品・飲料・お酒", "ドラッグストア・ビューティ", "ベビー・おもちゃ・ホビー", "服・シューズ・バッグ・腕時計", "スポーツ＆アウトドア", "車＆バイク・産業・研究開発"));
    private Callback mCallback;

    public void set(ArrayList<String> defaultGenres, Callback callback) {

        mSelectedIndexes.clear();
        for (String genre : defaultGenres) {
            Integer defaultIndex = mGenres.indexOf(genre);
            if ((defaultIndex >= 0) && (defaultIndex < mGenres.size())) {
                mSelectedIndexes.add(defaultIndex);
            }
        }
        mCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_profile_genre_select, null);

        initAction(view);
        initListView(view);
        reloadListView();

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> genres = new ArrayList<>();
                for (int i = 0; i < mSelectedIndexes.size(); i++) {
                    genres.add(mGenres.get(mSelectedIndexes.get(i)));
                }
                mCallback.didSelect(genres);
                popFragment(AnimationType.horizontal);
            }
        });
    }

    private void initListView(View view) {

        mAdapter = new ProfileGenreSelectAdapter(getActivity());

        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = mSelectedIndexes.indexOf(position);
                if (index >= 0) {
                    mSelectedIndexes.remove(position);
                } else {
                    mSelectedIndexes.add(position);
                }
                reloadListView();
            }
        });
    }

    private void reloadListView() {

        if (mAdapter == null) {
            return;
        }
        mAdapter.clear();

        for (int i = 0; i < mGenres.size(); i++) {
            ProfileGenreSelectAdapter.AdapterData data = new ProfileGenreSelectAdapter.AdapterData();
            data.genre = mGenres.get(i);
            data.isSelected = mSelectedIndexes.contains(i);
            mAdapter.add(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    public interface Callback {
        void didSelect(ArrayList<String> genres);
    }
}
