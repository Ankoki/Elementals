package com.ankoki.elementals.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Version {
    v1_8_R1(true),
    v1_9_R1(true),
    v1_10_R1(true),
    v1_11_R1(true),
    v1_12_R1(true),
    v1_12_R2(false),
    v1_13_R1(false),
    v1_14_R1(false),
    v1_15_R1(false),
    v1_16_R1(false),
    v1_16_R2(false),
    v1_16_R3(false),
    v1_16_R4(false),
    UNKNOWN(false);

    @Getter
    private final boolean isLegacy;
}
