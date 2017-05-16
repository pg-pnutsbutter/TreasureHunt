package jp.butter.pnuts.aquapolis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pnuts on 2016/08/13.
 */
public enum TreasureMapType {
    G8(1);

    private int mIndex;

    private TreasureMapType(int index) {
        mIndex = index;
    }

    private int getIndex() {
        return mIndex;
    }

    /**
     * 全地図種類を返す
     *
     * @return 地図リスト
     */
    public static List<String> getAllMapTypes() {
        List<String> types = new ArrayList<String>();
        for (TreasureMapType type : TreasureMapType.values()) {
            types.add(type.toString());
        }

        return types;
    }
}
