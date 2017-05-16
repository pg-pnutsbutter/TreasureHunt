package jp.butter.pnuts.aquapolis.model.datasource;

import android.content.Context;

import jp.butter.pnuts.aquapolis.model.TreasureMap;
import jp.butter.pnuts.aquapolis.model.TreasureMapLoader;
import jp.butter.pnuts.aquapolis.model.TreasureMapType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by msahakyan on 22/10/15.
 */
public class ExpandableListDataSource {
    private static final String TAG = "Treasure Expandable";

    /**
     * 地図種類->エリア名リストのMapを取得する
     *
     * @param context
     * @return
     */
    public static Map<String, List<String>> getData(Context context) {
        Map<String, List<String>> expandableListData = new TreeMap<>();

        List<String> mapTypes = TreasureMapType.getAllMapTypes();
        for(String mapType : mapTypes) {
            List<String> subMenuList = getSubMenuList(context, mapType);
            expandableListData.put(mapType, subMenuList);
        }

        return expandableListData;
    }

    /**
     * 指定の地図種類に紐づくエリアリストを取得する
     *
     * @param context Context
     * @param mapType 地図の種類(G1, G2...)
     *
     * @return エリアリスト
     */
    private static List<String> getSubMenuList(Context context, String mapType) {
        TreasureMapType treasureMapType = TreasureMapType.valueOf(mapType);
        List<TreasureMap> treasureMaps = TreasureMapLoader.getTreasureMaps(context, treasureMapType);
        List<String> subMenuList = new ArrayList();
        for(TreasureMap type : treasureMaps) {
            String areaName = type.getAreaName(context);
            if(!subMenuList.contains(areaName)) {
                subMenuList.add(type.getAreaName(context));
            }
        }

        return subMenuList;
    }
}
