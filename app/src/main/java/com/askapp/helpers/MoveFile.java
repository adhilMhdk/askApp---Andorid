package com.askapp.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MoveFile {
    public MoveFile() {
    }

    public void moveFile(File srcFileOrDirectory, File desFileOrDirectory) throws IOException {
        try (FileChannel outputChannel = new FileOutputStream(desFileOrDirectory).getChannel(); FileChannel inputChannel = new FileInputStream(srcFileOrDirectory).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();

        }
    }
}
