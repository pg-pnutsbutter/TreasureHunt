package jp.butter.pnuts.aquapolis.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import jp.butter.pnuts.aquapolis.R;
import jp.butter.pnuts.aquapolis.model.TreasureMap;
import jp.butter.pnuts.aquapolis.model.TreasureMapLoader;
import jp.butter.pnuts.aquapolis.model.TreasureMapType;
import jp.butter.pnuts.aquapolis.model.adapter.CustomExpandableListAdapter;
import jp.butter.pnuts.aquapolis.model.datasource.ExpandableListDataSource;
import jp.butter.pnuts.aquapolis.model.fragment.FragmentTreasure;
import jp.butter.pnuts.aquapolis.model.fragment.adapter.PagerAdapter;
import jp.butter.pnuts.aquapolis.model.fragment.navigation.FragmentNavigationManager;
import jp.butter.pnuts.aquapolis.model.fragment.navigation.NavigationManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Treasure Hunt";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;

    private NavigationManager mNavigationManager;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        this.initAdView();
        this.initNavigationDrawer();

        mNavigationManager = FragmentNavigationManager.obtain(this);
        Map<String, List<String>> expandableListData = ExpandableListDataSource.getData(this);
        setExpandableListAdapter(expandableListData);
        setOnAreaSelected();

        String areaName = getResources().getString(R.string.abalathia);
        createTreasureMapViewPager(TreasureMapType.G8, areaName);
        if (savedInstanceState == null) {
            setFirstTitleAsDefault();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_IAPTheme));
            alertDialogBuilder.setMessage(R.string.copyright_body).setPositiveButton(R.string.copyright_ok, null).create().show();
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if(mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if(mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    /**
     * AdMobを初期化する
     */
    private void initAdView() {
        String adId = getResources().getString(R.string.banner_ad_unit_id);
        MobileAds.initialize(getApplicationContext(), adId);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     * Navigation Drawerレイアウトをインスタンス化する
     */
    private void initNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mExpandableListView = (ExpandableListView) findViewById(R.id.navList);
        mDrawerToggle = getDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        // syncStateを呼ばないと、DrawerLayoutを開閉しても左上のアイコンが「←」のまま変わらない
        mDrawerToggle.syncState();
    }

    /**
     * DrawerLayoutのリストの中身（地図種類G8、エリア名一覧）を設定する
     */
    // private void setExpandableListAdapter(List<String> titleList) {
    private void setExpandableListAdapter(Map<String, List<String>> expandableListData) {
        List<String> titleList = new ArrayList(expandableListData.keySet());
        mExpandableListAdapter = new CustomExpandableListAdapter(this, titleList, expandableListData);
        mExpandableListView.setAdapter(mExpandableListAdapter);
    }

    /**
     * リストからエリア名を選択された際に表示するFragmentを更新する
     *
     * @param mapType 地図種類
     * @param areaName 表示するエリア名
     */
    private void updateFragment(TreasureMapType mapType, String areaName) {
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new PagerAdapter(manager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter.destroyAllItem(viewPager);

        List<FragmentTreasure> fragmentTreasureList = this.getFragmentTreasureList(mapType, areaName);

        for (FragmentTreasure fragmentTreasure : fragmentTreasureList) {
            pagerAdapter.addFragment(fragmentTreasure);
        }
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
    }
    /**
     * Navigation Drawerのエリア一覧からエリア名が選択されたときの処理
     */
    private void setOnAreaSelected() {
        mExpandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id)->{
            // ActionBarのタイトルに「エリア名」をセットする
            String areaName = this.getSelectedAreaName(groupPosition, childPosition);
            getSupportActionBar().setTitle(areaName);

            updateFragment(TreasureMapType.G8, areaName);

            mDrawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });

    }

    /**
     * Navigation Drawerが開いた時、閉じた時のListenerを取得する
     * ※ActionBarDrawerToggleは、DrawerLayout.DrawerListenerを実装してる
     *
     * @return
     */
    private ActionBarDrawerToggle getDrawerToggle() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        return drawerToggle;
    }

    /**
     * 地図種類とエリア名のリストから、現在選択されているエリア名を取得する
     *
     * @param groupPosition 地図種類の位置(0...)
     * @param childPosition エリア名の位置(0...)
     * @return エリア名
     */
    private String getSelectedAreaName(int groupPosition, int childPosition) {
        if (mExpandableListAdapter == null) {
            return null;
        }
        String areaName = (String) mExpandableListAdapter.getChild(groupPosition, childPosition);

        return areaName;
    }

    /**
     * アプリ起動時に最初に表示するエリア名をActionBarのタイトルに設定する
     */
    private void setFirstTitleAsDefault() {
        if (mNavigationManager != null) {
            String areaName = this.getSelectedAreaName(0, 0);
            getSupportActionBar().setTitle(areaName);
        }
    }

    /**
     * 指定した地図種類とエリア名から地図情報一覧のViewPagerを生成する
     *
     * @param mapType 地図種類
     * @param areaName エリア名
     */
    private void createTreasureMapViewPager(TreasureMapType mapType, String areaName) {
        List<FragmentTreasure> fragmentTreasureList = this.getFragmentTreasureList(mapType, areaName);
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new PagerAdapter(manager);
        for (FragmentTreasure fragmentTreasure : fragmentTreasureList) {
            pagerAdapter.addFragment(fragmentTreasure);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    /**
     * 指定の地図種類とエリア名を持つFragment一覧を取得する
     *
     * @param mapType 地図種類
     * @param areaName エリア名
     * @return Fragmentのリスト
     */
    private List<FragmentTreasure> getFragmentTreasureList(TreasureMapType mapType, String areaName) {
        // 指定の地図種類、エリア名のFragmentリストを作成
        List<TreasureMap> treasureMapList = TreasureMapLoader.getTreasureMaps(getApplicationContext(), mapType, areaName);

        // 地図情報(種類、エリア名)をBundleに入れて生成した各Fragmentに渡す
        List<FragmentTreasure> fragmentTreasureList = new ArrayList();
        for (TreasureMap treasureMap : treasureMapList) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(FragmentTreasure.FRAGMENT_KEY, treasureMap);

            FragmentTreasure fragmentTreasure = new FragmentTreasure();
            fragmentTreasure.setArguments(bundle);
            fragmentTreasureList.add(fragmentTreasure);
        }

        return fragmentTreasureList;
    }
}
