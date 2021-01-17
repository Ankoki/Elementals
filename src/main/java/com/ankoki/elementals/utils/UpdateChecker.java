package com.ankoki.elementals.utils;

import com.ankoki.elementals.Elementals;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@RequiredArgsConstructor
public class UpdateChecker {
    private final Elementals plugin;

    public String getLatestTag() {
        String redirectedURL = "";
        try {
            URLConnection con = new URL("https://github.com/Ankoki-Dev/Elementals/releases/latest").openConnection();
            con.connect();
            InputStream is = con.getInputStream();
            redirectedURL = con.getURL().toString();
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            return redirectedURL.split("releases/tag/")[1];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return "UNKNOWN";
        }
    }

    public boolean isLatest() {
        return getLatestTag().equalsIgnoreCase(plugin.getDescription().getVersion());
    }
}