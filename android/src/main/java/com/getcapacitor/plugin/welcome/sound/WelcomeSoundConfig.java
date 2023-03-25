package com.getcapacitor.plugin.welcome.sound;

public class WelcomeSoundConfig {

    private boolean enable = false;
    private String fileName = "welcome.aac";

    public boolean isEnabled() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
