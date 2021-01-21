package com.ankoki.elementals.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Lore {
    @Getter
    private final List<String> l = new ArrayList<>();

    public Lore(String... lines) {
        for (String line : lines) {
            l.add(Utils.coloured(line));
        }
    }
}