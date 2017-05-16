package jp.butter.pnuts.aquapolis.model;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;

import jp.butter.pnuts.aquapolis.R;

/**
 * Created by pnuts on 2016/08/13.
 *
 * 地図のエリアを管理
 *
 */
public final class Areas {

    interface IName {
        public String getAreaName(Context context);
    }

    private Areas() {}

    public enum Gridania implements IName {
        Central(R.string.gridania_central),
        Eastern(R.string.gridania_eastern),
        South(R.string.gridania_south),
        North(R.string.gridania_north);

        private int mResourceId;
        private Gridania(int resourceId) {
            mResourceId = resourceId;
        }

        public String getAreaName(Context context) {
            return context.getString(mResourceId);
        }
    }

    public enum Limsa implements IName {
        Middle(R.string.limsa_middle),
        Eastern(R.string.limsa_eastern),
        Western(R.string.limsa_western),
        Lower(R.string.limsa_lower),
        Upper(R.string.limsa_upper),
        Outer(R.string.limsa_outer);

        private int mResourceId;
        private Limsa(int resourceId) {
            mResourceId = resourceId;
        }

        public String getAreaName(Context context) {
            return context.getString(mResourceId);
        }
    }

    public enum Uldah implements IName {
        Central(R.string.uldah_central),
        Eastern(R.string.uldah_eastern),
        Western(R.string.uldah_western),
        Southern(R.string.uldah_southern),
        Northern(R.string.uldah_northern);

        private int mResourceId;
        private Uldah(int resourceId) {
            mResourceId = resourceId;
        }

        public String getAreaName(Context context) {
            return context.getString(mResourceId);
        }
    }

    public enum Coerthas implements IName {
        Central(R.string.coerthas_central),
        Western(R.string.coerthas_western);

        private int mResourceId;
        private Coerthas(int resourceId) {
            mResourceId = resourceId;
        }
        public String getAreaName(Context context) {
            return context.getString(mResourceId);
        }
    }

    public enum Mor_Dhona implements IName {
        Mor_Dhona(R.string.mor_dhona);

        private int mResourceId;
        private Mor_Dhona(int resourceId) {
            mResourceId = resourceId;
        }
        public String getAreaName(Context context) {
            return context.getString(mResourceId);
        }
    }

    public enum Abalathia implements IName {
        The_Sea_of_Clouds(R.string.abalathia);

        private int mResourceId;
        private Abalathia(int resourceId) {
            mResourceId = resourceId;
        }
        public String getAreaName(Context context) {
            return context.getString(mResourceId);
        }
    }

    public enum Dravania implements IName {
        The_Dravanian_Forelands(R.string.dravania_forelands),
        The_Churning_Mists(R.string.dravania_mists),
        The_Dravanian_Hinterlands(R.string.dravania_hinterlands);

        private int mResourceId;
        private Dravania(int resourceId) {
            mResourceId = resourceId;
        }
        public String getAreaName(Context context) {
            return context.getString(mResourceId);
        }
    }

    public enum AzysLla implements IName {
        AzysLla(R.string.azys_lla);

        private int mResourceId;
        private AzysLla(int resourceId) {
            mResourceId = resourceId;
        }
        public String getAreaName(Context context) {
            return context.getString(mResourceId);
        }
    }

    /**
     * エリア名一覧を取得する
     *
     * @param mapNames MapAreaインタフェースを実装したenum(e.g. Gridania.values)
     * @param context Context
     * @return エリア名一覧
     */
    public static List<String> getAreaNames(IName[] mapNames, Context context) {
        List<String> areaNames = new ArrayList<>();
        for(IName mapName : mapNames) {
            areaNames.add(mapName.getAreaName(context));
        }

        return areaNames;
    }

    public static IName valueOf(String countryName, String cityName) {
        switch (countryName) {
            case "Gridania": return Areas.Gridania.valueOf(cityName);
            case "Limsa" : return Areas.Limsa.valueOf(cityName);
            case "Uldah" : return Areas.Uldah.valueOf(cityName);
            case "Coerthas" : return Areas.Coerthas.valueOf(cityName);
            case "Mor_Dhona" : return Areas.Mor_Dhona.valueOf(cityName);
            case "Abalathia" : return Areas.Abalathia.valueOf(cityName);
            case "Dravania" : return Areas.Dravania.valueOf(cityName);
            case "AzysLla" : return Areas.AzysLla.valueOf(cityName);
        }
        return null;
    }
}
