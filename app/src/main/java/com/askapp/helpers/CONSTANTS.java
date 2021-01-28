package com.askapp.helpers;

import android.os.Environment;

public class CONSTANTS {
    String rootDirName = Environment.getDataDirectory()+"/data/com.askapp";

    public CONSTANTS() {
    }

    public String getRootDirName() {
        return rootDirName;
    }
}
