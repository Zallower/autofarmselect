/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.afs;

import com.afs.client.FarmController;
import com.afs.client.Keybinds;
import com.afs.client.RateGuard;
import com.afs.command.AfsCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoFarmSelect
implements ClientModInitializer {
    public static final String MOD_ID = "autofarmselect";
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"AutoFarmSelect");

    public void onInitializeClient() {
        LOGGER.debug("[FarmSelect] \u521d\u59cb\u5316\u4e2d\u2026");
        Keybinds.register();
        RateGuard.register();
        AfsCommand.register();
        ClientTickEvents.END_CLIENT_TICK.register(class_3102 -> {
            Keybinds.poll(class_3102);
            FarmController.get().rateTick(class_3102);
            FarmController.get().tick(class_3102);
            FarmController.get().waterTick(class_3102);
        });
        LOGGER.debug("[FarmSelect] \u521d\u59cb\u5316\u5b8c\u6210");
    }
}

