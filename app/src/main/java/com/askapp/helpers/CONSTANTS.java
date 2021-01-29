package com.askapp.helpers;

import android.os.Environment;

public class CONSTANTS {
    public String rootDirName = Environment.getDataDirectory()+"/data/com.askapp";
    public String UPDATE = "UPDATE";
    public String DELETE = "DELETE";
    public String INSERT = "INSERT";

    public CONSTANTS() {
    }

    public String getRootDirName() {
        return rootDirName;
    }
}
