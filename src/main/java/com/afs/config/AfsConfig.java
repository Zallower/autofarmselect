/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  net.fabricmc.loader.api.FabricLoader
 */
package com.afs.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;

public class AfsConfig {
    public boolean doHarvest = true;
    public int plantConeDegrees = 60;
    public double plantMaxDistance = 4.0;
    public int cropCooldownMs = 120000;
    public List<String> cropBlockIds = new ArrayList<String>();
    public int waterIntervalTicks = 3;
    public int waterCooldownMs = 30000;
    public int waterRefillCount = 2;
    public List<String> waterEntityTypeIds = AfsConfig.defaultWaterEntityTypes();
    public double waterEntitySearchRadius = 5.0;
    public int actionInterval = 12;
    public int actionsPerBatch = 1;
    public int packetBudgetPerSecond = 6;
    public int burstPauseTicks = 60;
    public double reach = 4.4;
    public boolean faceTarget = true;
    public boolean onlyLoadedChunks = true;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static AfsConfig INSTANCE = null;

    private static List<String> defaultWaterEntityTypes() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("minecraft:interaction");
        arrayList.add("minecraft:block_display");
        arrayList.add("minecraft:item_display");
        arrayList.add("minecraft:armor_stand");
        return arrayList;
    }

    public static AfsConfig get() {
        if (INSTANCE == null) {
            AfsConfig.load();
        }
        return INSTANCE;
    }

    public static Path dir() {
        return FabricLoader.getInstance().getConfigDir().resolve("autofarmselect");
    }

    private static Path file() {
        return AfsConfig.dir().resolve("autofarmselect.json");
    }

    public static void load() {
        Path path = AfsConfig.file();
        if (Files.exists(path, new LinkOption[0])) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);){
                AfsConfig afsConfig = (AfsConfig)GSON.fromJson((Reader)bufferedReader, AfsConfig.class);
                if (afsConfig != null) {
                    afsConfig.normalize();
                    INSTANCE = afsConfig;
                    return;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        INSTANCE = new AfsConfig();
        AfsConfig.save();
    }

    public static void save() {
        if (INSTANCE == null) {
            return;
        }
        try {
            Path path = AfsConfig.file();
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, new OpenOption[0]);){
                GSON.toJson((Object)INSTANCE, (Appendable)bufferedWriter);
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public double effectivePlantDistance() {
        return Math.min(this.plantMaxDistance, this.reach);
    }

    public String frontConeText() {
        if (this.plantConeDegrees >= 180) {
            return "\u9762\u524d\u4e0d\u9650\u65b9\u5411\uff08\u8ddd\u79bb " + String.format("%.1f", this.effectivePlantDistance()) + " \u683c\u5185\uff09";
        }
        return String.format("\u9762\u524d %d\u00b0 \u6247\u5f62\uff08\u7ea6\u6b63\u524d\u65b9 %d \u683c\u5bbd\uff09/ %.1f \u683c\u5185", this.plantConeDegrees, Math.max(1, (int)Math.round(Math.tan(Math.toRadians(this.plantConeDegrees)) * this.effectivePlantDistance() * 2.0)), this.effectivePlantDistance());
    }

    private void normalize() {
        if (this.plantConeDegrees <= 0 || this.plantConeDegrees > 180) {
            this.plantConeDegrees = 60;
        }
        if (this.plantMaxDistance <= 0.0) {
            this.plantMaxDistance = 4.0;
        }
        if (this.cropCooldownMs <= 0) {
            this.cropCooldownMs = 120000;
        }
        if (this.cropBlockIds == null) {
            this.cropBlockIds = new ArrayList<String>();
        }
        if (this.waterIntervalTicks <= 0) {
            this.waterIntervalTicks = 3;
        }
        if (this.waterEntityTypeIds == null) {
            this.waterEntityTypeIds = AfsConfig.defaultWaterEntityTypes();
        }
        if (this.waterEntitySearchRadius <= 0.0) {
            this.waterEntitySearchRadius = 5.0;
        }
        if (this.waterCooldownMs <= 0) {
            this.waterCooldownMs = 30000;
        }
        if (this.waterRefillCount <= 0) {
            this.waterRefillCount = 2;
        }
        if (this.reach <= 0.0) {
            this.reach = 4.4;
        }
        if (this.actionInterval <= 0) {
            this.actionInterval = 12;
        }
        if (this.actionsPerBatch <= 0) {
            this.actionsPerBatch = 1;
        }
        if (this.packetBudgetPerSecond <= 0) {
            this.packetBudgetPerSecond = 6;
        }
    }

    public String currentPresetName() {
        if (this.packetBudgetPerSecond <= 4) {
            return "safe";
        }
        if (this.packetBudgetPerSecond <= 6) {
            return "normal";
        }
        if (this.packetBudgetPerSecond <= 8) {
            return "fast";
        }
        if (this.packetBudgetPerSecond <= 12) {
            return "turbo";
        }
        return "insane";
    }

    public void applyPreset(String string) {
        switch (string) {
            case "safe": {
                this.actionInterval = 20;
                this.actionsPerBatch = 1;
                this.packetBudgetPerSecond = 4;
                this.burstPauseTicks = 80;
                break;
            }
            case "normal": {
                this.actionInterval = 12;
                this.actionsPerBatch = 1;
                this.packetBudgetPerSecond = 6;
                this.burstPauseTicks = 60;
                break;
            }
            case "fast": {
                this.actionInterval = 8;
                this.actionsPerBatch = 2;
                this.packetBudgetPerSecond = 8;
                this.burstPauseTicks = 60;
                break;
            }
            case "turbo": {
                this.actionInterval = 6;
                this.actionsPerBatch = 2;
                this.packetBudgetPerSecond = 12;
                this.burstPauseTicks = 80;
                break;
            }
            case "insane": {
                this.actionInterval = 3;
                this.actionsPerBatch = 3;
                this.packetBudgetPerSecond = 28;
                this.burstPauseTicks = 100;
                break;
            }
        }
        this.plantConeDegrees = 60;
        AfsConfig.save();
    }
}

