package com.butter.pnuts.aquapolis;

import android.content.Context;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import android.util.Log;

import jp.butter.pnuts.aquapolis.model.TreasureMapType;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ModelTest extends InstrumentationTestCase {
    private static final String TAG = "ModelTest";
//    @Test
//    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//    }

    @Test
    public void test_TreasureMap_getAllMaps() {
        List<String> maps = TreasureMapType.getAllMapTypes();
        TreasureMapType[] treasureMapConstantses = TreasureMapType.values();

        assertTrue(maps.size() == treasureMapConstantses.length);
        for(TreasureMapType treasureMapType : treasureMapConstantses) {
            assertTrue(maps.contains(treasureMapType));
        }
    }

    @Test
    public void test_CsvParser() {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\r\n");
        settings.setHeaderExtractionEnabled(true); // skip first line

        CsvParser csvParser = new CsvParser(settings);
        AssetManager assetManager = getContext().getResources().getAssets();
        try {
            List<String[]> rows = csvParser.parseAll(assetManager.open("mapdata.csv"));

            for(String[] row : rows) {
                Log.i(TAG, Arrays.toString(row));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Context getContext() {
        return this.getInstrumentation().getTargetContext().getApplicationContext();
    }
}