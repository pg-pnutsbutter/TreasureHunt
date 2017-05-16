package jp.butter.pnuts.aquapolis.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by pnuts on 2016/08/14.
 *
 * 以下を管理
 *
 * ・地図の種類
 * ・宝があるエリア
 * ・宝の座標
 */
public class TreasureMap implements Serializable {
    private static final String TAG = "TreasureMap";

    private TreasureMapType mTreasureType;
    private Areas.IName mAreaName;
    private TreasureMapPoint mPoint;
    private byte[] mTreasureBitmap;
    public TreasureMap() {}

    public TreasureMapType getTreasureType() {
        return mTreasureType;
    }

    public String getAreaName(Context context) {
        return mAreaName.getAreaName(context);
    }

    public String getPoint() {
        return mPoint.toString();
    }
    
    public Bitmap getTreasureImage() {
        return BitmapFactory.decodeByteArray(mTreasureBitmap, 0, mTreasureBitmap.length);
    }


    public void setTreasureType(TreasureMapType type) {
        mTreasureType = type;
    }

    public void setArea(Areas.IName areaName) {
        mAreaName = areaName;
    }

    public void setTreasurePoint(TreasureMapPoint point) {
        mPoint = point;
    }

    public void setTreasureImage(Context context, String fileName) {
        if(mTreasureBitmap != null) {
            return;
        }

        Resources resource = context.getResources();
        int resourceId = resource.getIdentifier(fileName, "drawable", context.getPackageName());
        if (resourceId == 0) {
            return;
        }

        InputStream is = resource.openRawResource(resourceId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];

        try {
            int readBytes = 0;
            while((readBytes = is.read(buffer)) > 0) {
                stream.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mTreasureBitmap = stream.toByteArray();
    }
}
