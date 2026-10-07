/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
 *  net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
 *  net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents
 *  net.minecraft.class_11908
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_304
 *  net.minecraft.class_304$class_11900
 *  net.minecraft.class_310
 *  net.minecraft.class_3675$class_307
 *  net.minecraft.class_408
 *  net.minecraft.class_437
 */
package com.afs.client;

import com.afs.client.FarmController;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.class_11908;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import net.minecraft.class_408;
import net.minecraft.class_437;

public final class Keybinds {
    public static class_304 toggleKey;
    public static class_304 waterKey;

    private Keybinds() {
    }

    public static void register() {
        class_304.class_11900 class_119002 = class_304.class_11900.method_74698((class_2960)class_2960.method_60655((String)"autofarmselect", (String)"main"));
        toggleKey = KeyBindingHelper.registerKeyBinding((class_304)new class_304("key.autofarmselect.toggle", class_3675.class_307.field_1668, 72, class_119002));
        waterKey = KeyBindingHelper.registerKeyBinding((class_304)new class_304("key.autofarmselect.water", class_3675.class_307.field_1668, 74, class_119002));
        Keybinds.registerScreenHook();
    }

    private static void registerScreenHook() {
        ScreenEvents.AFTER_INIT.register((class_3102, class_4373, n, n2) -> {
            if (class_4373 instanceof class_408) {
                return;
            }
            ScreenKeyboardEvents.allowKeyPress((class_437)class_4373).register((class_4372, class_119082) -> !Keybinds.onKeyInScreen(class_3102, class_119082));
        });
    }

    public static void poll(class_310 class_3102) {
        if (toggleKey != null) {
            while (toggleKey.method_1436()) {
                Keybinds.toggle(class_3102);
            }
        }
        if (waterKey != null) {
            while (waterKey.method_1436()) {
                Keybinds.toggleWater(class_3102);
            }
        }
    }

    public static boolean onKeyInScreen(class_310 class_3102, class_11908 class_119082) {
        if (class_3102.field_1755 instanceof class_408) {
            return false;
        }
        if (toggleKey != null && toggleKey.method_1417(class_119082)) {
            Keybinds.toggle(class_3102);
            return true;
        }
        if (waterKey != null && waterKey.method_1417(class_119082)) {
            Keybinds.toggleWater(class_3102);
            return true;
        }
        return false;
    }

    private static void toggle(class_310 class_3102) {
        boolean bl;
        boolean bl2 = bl = !FarmController.get().isRunning();
        if (bl && class_3102.field_1724 != null && !FarmController.hasAnySeed(class_3102)) {
            class_3102.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7c[FarmSelect] \u6ca1\u6709\u79cd\u5b50\uff0c\u65e0\u6cd5\u5f00\u542f"), true);
            return;
        }
        FarmController.get().setRunning(bl);
        if (class_3102.field_1724 != null) {
            class_3102.field_1724.method_7353((class_2561)class_2561.method_43470((String)("\u00a7b[FarmSelect] \u00a7f" + (bl ? "\u00a7a\u5df2\u5f00\u542f" : "\u00a7c\u5df2\u505c\u6b62"))), true);
        }
    }

    private static void toggleWater(class_310 class_3102) {
        boolean bl;
        boolean bl2 = bl = !FarmController.get().isWatering();
        if (bl && class_3102.field_1724 != null && !FarmController.hasAnyWateringCan(class_3102)) {
            class_3102.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7c[FarmSelect] \u6ca1\u6709\u6d47\u6c34\u7f50\uff0c\u65e0\u6cd5\u5f00\u542f"), true);
            return;
        }
        FarmController.get().setWatering(bl);
        if (class_3102.field_1724 != null) {
            class_3102.field_1724.method_7353((class_2561)class_2561.method_43470((String)("\u00a7b[FarmSelect] \u8865\u6c34\u00a7f" + (bl ? "\u00a7a\u5df2\u5f00\u542f" : "\u00a7c\u5df2\u505c\u6b62"))), true);
        }
    }
}

