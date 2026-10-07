/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
 *  net.minecraft.class_2561
 */
package com.afs.client;

import com.afs.client.FarmController;
import com.afs.config.AfsConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.class_2561;

public final class RateGuard {
    private static final String[] LADDER = new String[]{"safe", "normal", "fast", "turbo", "insane"};
    private static boolean wasRunningOnDisconnect = false;
    private static int strikeCount = 0;
    private static String lastKickServer = "-";

    private RateGuard() {
    }

    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register((class_6342, class_3102) -> {
            if (FarmController.get().isRunning()) {
                wasRunningOnDisconnect = true;
                ++strikeCount;
                lastKickServer = "-";
                RateGuard.autoDowngrade();
            } else {
                wasRunningOnDisconnect = false;
            }
        });
        ClientPlayConnectionEvents.JOIN.register((class_6342, packetSender, class_3102) -> {
            if (wasRunningOnDisconnect) {
                wasRunningOnDisconnect = false;
                class_3102.execute(() -> {
                    if (class_3102.field_1724 != null) {
                        class_3102.field_1724.method_7353((class_2561)class_2561.method_43470((String)("\u00a7e[FarmSelect] \u4e0a\u6b21\u88ab\u65ad\u5f00\uff0c\u5df2\u81ea\u52a8\u964d\u6863\u81f3 \u00a7f" + AfsConfig.get().currentPresetName() + "\u00a7e\uff08\u9891\u7e41\u88ab\u8e22\u8bf7\u7528 /afs mode safe\uff09")), true);
                    }
                });
            }
        });
    }

    private static void autoDowngrade() {
        int n;
        AfsConfig afsConfig = AfsConfig.get();
        String string = afsConfig.currentPresetName();
        int n2 = RateGuard.indexOf(string);
        int n3 = Math.max(0, n2 - (n = strikeCount >= 2 ? 2 : 1));
        if (n3 < n2) {
            afsConfig.applyPreset(LADDER[n3]);
        }
        afsConfig.packetBudgetPerSecond = Math.max(3, afsConfig.packetBudgetPerSecond - 2);
        afsConfig.save();
    }

    private static int indexOf(String string) {
        for (int i = 0; i < LADDER.length; ++i) {
            if (!LADDER[i].equals(string)) continue;
            return i;
        }
        return 1;
    }

    public static int strikeCount() {
        return strikeCount;
    }

    public static String lastKickServer() {
        return lastKickServer;
    }
}

