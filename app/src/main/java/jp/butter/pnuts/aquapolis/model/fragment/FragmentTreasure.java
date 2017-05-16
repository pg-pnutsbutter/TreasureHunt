package jp.butter.pnuts.aquapolis.model.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jp.butter.pnuts.aquapolis.R;
import jp.butter.pnuts.aquapolis.model.TreasureMap;

/**
 * Created by pnuts on 2016/08/23.
 */
public class FragmentTreasure extends Fragment {
    private static final String TAG = "FragmentTreasure";
    public static final String FRAGMENT_KEY = "KEY";

    private Context mContext;

    public FragmentTreasure() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle == null) {
            Log.e(TAG, "[onCreateView] bundle is NULL");
            return null;
        }

        TreasureMap treasureMap = (TreasureMap) bundle.getSerializable(FRAGMENT_KEY);

        View treasurePositionView = inflater.inflate(R.layout.fragment_treasure_position, container, false);

        ImageView treasureMapImage = (ImageView) treasurePositionView.findViewById(R.id.treasureMap);
        TextView treasurePoint = (TextView) treasurePositionView.findViewById(R.id.treasurePosition);

        treasureMapImage.setImageBitmap(treasureMap.getTreasureImage());
        treasurePoint.setText(treasureMap.getPoint());

        return treasurePositionView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
