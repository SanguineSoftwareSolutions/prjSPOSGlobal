/*
 * Ritesh 18 Sept 2014
 *
 * 
 */

package com.POSGlobal.controller;

import com.POSGlobal.controller.*;
import java.io.*;
import java.nio.channels.*;

public class clsCreateJPOSInstance {
    private String appName;
    private File file;
    private FileChannel channel;
    private FileLock lock;

    public clsCreateJPOSInstance(String appName) {
        this.appName = appName;
    }

    public boolean isAppActive() {
        try {
            file = new File
                 (System.getProperty("user.home"), appName + ".tmp");
            
            channel = new RandomAccessFile(file, "rw").getChannel();

            try {
                lock = channel.tryLock();
            }
            catch (OverlappingFileLockException e) {
                // already locked
                closeLock();
                return true;
            }

            if (lock == null) {
                closeLock();
                return true;
            }

            Runtime.getRuntime().addShutdownHook(new Thread() {
                    // destroy the lock when the JVM is closing
                    public void run() {
                        closeLock();
                        deleteFile();
                    }
                });
            return false;
        }
        catch (Exception e) {
            closeLock();
            return true;
        }
    }

    private void closeLock() {
        try { lock.release();  }
        catch (Exception e) {  }
        try { channel.close(); }
        catch (Exception e) {  }
    }

    private void deleteFile() {
        try { file.delete(); }
        catch (Exception e) { }
    }
}
