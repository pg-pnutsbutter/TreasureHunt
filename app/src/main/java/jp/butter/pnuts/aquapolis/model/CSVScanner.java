package jp.butter.pnuts.aquapolis.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by pnuts on 2016/08/15.
 *
 */
public class CSVScanner {
    Scanner mScanner;

    public CSVScanner(File source) {
        try {
            mScanner = new Scanner(source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public CSVScanner(InputStream source) {
        mScanner = new Scanner(source);
    }

    public ArrayList<String[]> read() {
        ArrayList<String[]> csvList = new ArrayList<>();

        synchronized (mScanner) {
            while (mScanner.hasNext()) {
                String[] line = mScanner.nextLine().split(",");
                csvList.add(line);
            }
        }
        return csvList;
    }

}