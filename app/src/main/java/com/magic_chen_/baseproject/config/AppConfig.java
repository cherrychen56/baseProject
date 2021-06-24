package com.magic_chen_.baseproject.config;

public class AppConfig {

    private static String mHostUrl;
    public static ContextMode currentContextMode = ContextMode.ONLINE;
    public static boolean IS_SHOW_LOG = true;

    public static enum ContextMode {
        DEV(0), ONLINE(1);

        private int value = 0;

        private ContextMode(int value) {
            this.value = value;
        }

        public static ContextMode valueOf(int value) {
            switch (value) {
                case 0:
                    return DEV;
                case 1:
                    return ONLINE;
                default:
                    return ONLINE;
            }
        }

        public int value() {
            return this.value;
        }
    }

    public static String getHostUrl() {
        switch (currentContextMode) {
            case ONLINE:
                mHostUrl =  "https://webapi.nn.com";
                break;
            case DEV:
                mHostUrl =  "https://webapi.nn.com";
                break;
        }
        return mHostUrl;
    }

    public static boolean isOnline() {
        return currentContextMode == ContextMode.ONLINE;
    }

}
