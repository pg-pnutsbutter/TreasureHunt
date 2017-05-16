package jp.butter.pnuts.aquapolis.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pnuts on 2016/08/15.
 *
 * シングルトン
 * assets/mapdata.csvから地図情報を一括読み込み
 * 読み込んだ地図情報からTreasureMapのリストを作成
 */
public class TreasureMapLoader {
    private static final String TAG = TreasureMapLoader.class.toString();
    private static final String DATA_FILE = "mapdata.csv";

    private static TreasureMapLoader mInstance;
    private static List<TreasureMap> mTreasureMaps;

    // 地図の項目
    private enum MapItem {
        Type(0),
        Country(1),
        City(2),
        X(3),
        Y(4),
        Z(5),
        Image(6);

        private int mIndex;

        private MapItem(int index) {
            mIndex = index;
        }

        public int index() {
            return mIndex;
        }
    }

    private TreasureMapLoader() {
        mInstance = null;
    }

    /**
     * 下記２つのjarが必要
     * libs/univocity-parsers-2.2.0.jar
     * libs/openbeans-1.0.jar
     *
     * openbeansは、Java Beansが使用できないのでその代用
     *
     * http://www.univocity.com/pages/parsers-tutorial
     *
     *
     * @param context
     */
    private static void loadAllTreasureMap(Context context) {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\r\n");
        settings.setHeaderExtractionEnabled(true); // skip first line

        CsvParser csvParser = new CsvParser(settings);
        AssetManager assetManager = context.getResources().getAssets();
        try {
            List<String[]> rows = csvParser.parseAll(assetManager.open(DATA_FILE));
            List<TreasureMap> mapList = new ArrayList();

            //mDebugTime = System.currentTimeMillis();
            for(String[] row : rows) {
                TreasureMapType mapType = TreasureMapType.valueOf(row[MapItem.Type.index()]);
                Areas.IName area = Areas.valueOf(row[MapItem.Country.index()], row[MapItem.City.index()]);
                float x = Float.parseFloat(row[MapItem.X.index()]);
                float y = Float.parseFloat(row[MapItem.Y.index()]);
                float z = Float.parseFloat(row[MapItem.Z.index()]);
                String imageFileName = row[MapItem.Image.index()];

                TreasureMap treasureMap = new TreasureMap();
                treasureMap.setTreasureType(mapType);
                treasureMap.setArea(area);
                treasureMap.setTreasurePoint(new TreasureMapPoint(x, y, z));
                treasureMap.setTreasureImage(context, imageFileName);

                mapList.add(treasureMap);
            }

            mTreasureMaps = mapList;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定した地図種類に紐づく地図のリストを取得する
     *
     * @param context Context
     * @param type 地図種類
     * @return 地図のリスト
     */
    public static List<TreasureMap> getTreasureMaps(Context context, TreasureMapType type) {
        if(mTreasureMaps == null) {
            loadAllTreasureMap(context);
        }
        List<TreasureMap> treasureMaps = new ArrayList<>();
        for(TreasureMap map : mTreasureMaps) {
            if(type == map.getTreasureType()) {
                treasureMaps.add(map);
            }
        }

        return treasureMaps;
    }

    /**
     * 指定した地図種類とエリア名に紐づく地図のリストを取得する
     *
     * @param context Context
     * @param type 地図種類
     * @param areaName エリア名
     * @return 地図のリスト
     */
    public static List<TreasureMap> getTreasureMaps(Context context, TreasureMapType type, String areaName) {
        if(mTreasureMaps == null) {
            loadAllTreasureMap(context);
        }

        List<TreasureMap> treasureMaps = new ArrayList<>();
        for(TreasureMap map : mTreasureMaps) {
            if(type == map.getTreasureType() && areaName.equals(map.getAreaName(context))) {
                treasureMaps.add(map);
            }
        }

        return treasureMaps;
    }

}
