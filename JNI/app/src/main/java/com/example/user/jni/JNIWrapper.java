package com.example.user.jni;

public class JNIWrapper {
    static {
        System.loadLibrary("native-lib");
    }

    public static native void onsurfacecreated();

    public static native void onsurfacechanged(int width, int height);

    public static native void ondrawframe();
}