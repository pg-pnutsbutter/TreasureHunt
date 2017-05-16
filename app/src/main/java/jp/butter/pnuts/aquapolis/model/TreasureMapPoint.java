package jp.butter.pnuts.aquapolis.model;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by pnuts on 2016/08/16.
 */
public class TreasureMapPoint implements Serializable {
    private float mX;
    private float mY;
    private float mZ;

    public TreasureMapPoint() {}

    public TreasureMapPoint(float x, float y, float z) {
        mX = x;
        mY = y;
        mZ = z;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "X:%.1f, Y:%.1f, Z:%.1f", mX, mY, mZ);
    }
}
