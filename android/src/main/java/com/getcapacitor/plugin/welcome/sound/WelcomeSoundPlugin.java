package com.getcapacitor.plugin.welcome.sound;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.IOException;

@CapacitorPlugin(name = "WelcomeSound")
public class WelcomeSoundPlugin extends Plugin {

    private WelcomeSoundConfig config;
    private Context context;
    final String TAG = "WelcomeSound";

    public void load() {
        context = this.bridge.getContext();
        config = welcomeSoundConfig();
        play();
    }

    @PluginMethod
    public void enable(PluginCall call) {
        requestPermissions(call);
        config.setEnable(true);
        config.setFileName(call.getString("fileName", config.getFileName()));
        setWelcomeSoundConfig();
        call.resolve();
    }

    @PluginMethod
    public void disable(PluginCall call) {
        config.setEnable(false);
        setWelcomeSoundConfig();
        call.resolve();
    }

    private void play(){
        if (!config.isEnabled()) {
            return;
        }
        String fileName = config.getFileName();
        String[] parsed = parseFileName(fileName);
        String name = parsed[0];

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {

            mediaPlayer.setDataSource(context, getUri(name));
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                new Handler().postDelayed(() -> {
                    mp.stop();
                    mp.release();
                }, mp.getDuration());
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            config.setEnable(false);
            setWelcomeSoundConfig();
            e.printStackTrace();
        }
    }

    private String[] parseFileName(String fileName) {
        String[] parts = fileName.split("\\.");
        return new String[]{parts[0], parts[1]};
    }

    private Uri getUri(String name) {
        int id = getResource(name);
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + id);
    }

    private int getResource(String name) {
        return context.getResources().getIdentifier(name, "raw", context.getPackageName());
    }

    private void setWelcomeSoundConfig() {
        SharedPreferences preferences = context.getSharedPreferences("welcomeSound", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("enable", config.isEnabled());
        editor.putString("fileName", config.getFileName());
        editor.apply();
    }

    private WelcomeSoundConfig welcomeSoundConfig() {
        SharedPreferences preferences = context.getSharedPreferences("welcomeSound", MODE_PRIVATE);
        WelcomeSoundConfig config = new WelcomeSoundConfig();
        boolean enable = preferences.getBoolean("enable", getConfig().getBoolean("enable", config.isEnabled()));
        String fileName = preferences.getString("fileName", getConfig().getString("fileName", config.getFileName()));
        config.setEnable(enable);
        config.setFileName(fileName);
        return config;
    }
}
