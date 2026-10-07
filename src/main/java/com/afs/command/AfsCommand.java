/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.DoubleArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
 *  net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
 *  net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1661
 *  net.minecraft.class_1799
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_239
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_2680
 *  net.minecraft.class_310
 *  net.minecraft.class_3965
 *  net.minecraft.class_3966
 *  net.minecraft.class_746
 *  net.minecraft.class_7923
 */
package com.afs.command;

import com.afs.AutoFarmSelect;
import com.afs.client.FarmController;
import com.afs.client.RateGuard;
import com.afs.config.AfsConfig;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.lang.invoke.CallSite;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_746;
import net.minecraft.class_7923;

public final class AfsCommand {
    private AfsCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, class_71572) -> commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)ClientCommandManager.literal((String)"afs").executes(commandContext -> {
            AfsCommand.info((FabricClientCommandSource)commandContext.getSource());
            return 1;
        })).then(ClientCommandManager.literal((String)"start").executes(commandContext -> {
            AfsConfig afsConfig = AfsConfig.get();
            if (!FarmController.hasAnySeed(class_310.method_1551())) {
                ((FabricClientCommandSource)commandContext.getSource()).sendError((class_2561)class_2561.method_43470((String)"\u00a7c[FarmSelect] \u624b\u4e0a\u548c\u80cc\u5305\u90fd\u6ca1\u6709\u79cd\u5b50\uff0c\u65e0\u6cd5\u5f00\u542f\u3002\u00a77\u8bf7\u5148\u62ff\u597d \u00a7fminecraft:sugar \u00a77\uff08\u79cd\u5b50\uff09\u3002"));
                return 0;
            }
            FarmController.get().setRunning(true);
            ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7a[FarmSelect] \u5df2\u5f00\u542f \u00a77(\u6309 H \u4e5f\u53ef\u5f00\u5173) \u00a77\u8303\u56f4: \u00a7f" + afsConfig.frontConeText())));
            return 1;
        }))).then(ClientCommandManager.literal((String)"stop").executes(commandContext -> {
            FarmController.get().setRunning(false);
            ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)"\u00a7c[FarmSelect] \u5df2\u505c\u6b62"));
            return 1;
        }))).then(ClientCommandManager.literal((String)"toggle").executes(commandContext -> {
            boolean bl;
            FarmController farmController = FarmController.get();
            boolean bl2 = bl = !farmController.isRunning();
            if (bl && !FarmController.hasAnySeed(class_310.method_1551())) {
                ((FabricClientCommandSource)commandContext.getSource()).sendError((class_2561)class_2561.method_43470((String)"\u00a7c[FarmSelect] \u624b\u4e0a\u548c\u80cc\u5305\u90fd\u6ca1\u6709\u79cd\u5b50\uff0c\u65e0\u6cd5\u5f00\u542f\u3002\u00a77\u8bf7\u5148\u62ff\u597d \u00a7fminecraft:sugar \u00a77\uff08\u79cd\u5b50\uff09\u3002"));
                return 0;
            }
            farmController.setRunning(bl);
            ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)(farmController.isRunning() ? "\u00a7a[FarmSelect] \u5df2\u5f00\u542f" : "\u00a7c[FarmSelect] \u5df2\u505c\u6b62")));
            return 1;
        }))).then(((LiteralArgumentBuilder)ClientCommandManager.literal((String)"cone").executes(commandContext -> {
            AfsConfig afsConfig = AfsConfig.get();
            ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7b[FarmSelect] \u9762\u524d\u8303\u56f4 = \u00a7f" + afsConfig.frontConeText())));
            ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77\u7528\u6cd5: \u00a7f/afs cone <1-180> \u00a77(\u534a\u89d2\uff0c\u9ed8\u8ba4 60 = \u9762\u524d 120 \u5ea6)"));
            return 1;
        })).then(ClientCommandManager.argument((String)"deg", (ArgumentType)IntegerArgumentType.integer((int)1, (int)180)).executes(commandContext -> {
            int n = IntegerArgumentType.getInteger((CommandContext)commandContext, (String)"deg");
            AfsConfig afsConfig = AfsConfig.get();
            afsConfig.plantConeDegrees = n;
            AfsConfig.save();
            ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7b[FarmSelect] \u9762\u524d\u8303\u56f4 = \u00a7f" + afsConfig.frontConeText())));
            return 1;
        })))).then(ClientCommandManager.literal((String)"plantdist").then(ClientCommandManager.argument((String)"d", (ArgumentType)DoubleArgumentType.doubleArg((double)0.5, (double)8.0)).executes(commandContext -> {
            double d = DoubleArgumentType.getDouble((CommandContext)commandContext, (String)"d");
            AfsConfig afsConfig = AfsConfig.get();
            afsConfig.plantMaxDistance = d;
            AfsConfig.save();
            ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7b[FarmSelect] \u9762\u524d\u8ddd\u79bb = \u00a7f" + d + " \u683c \u00a77(\u5b9e\u9645\u53d7\u4ea4\u4e92\u8ddd\u79bb " + afsConfig.reach + " \u9650\u5236)")));
            return 1;
        })))).then(ClientCommandManager.literal((String)"stat").executes(commandContext -> {
            AfsCommand.info((FabricClientCommandSource)commandContext.getSource());
            return 1;
        }))).then(ClientCommandManager.literal((String)"resetstat").executes(commandContext -> {
            FarmController.get().resetStats();
            ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)"\u00a7b\u5df2\u6e05\u96f6\u7edf\u8ba1"));
            return 1;
        }))).then(ClientCommandManager.literal((String)"mode").then(ClientCommandManager.argument((String)"preset", (ArgumentType)StringArgumentType.word()).executes(commandContext -> {
            String string = StringArgumentType.getString((CommandContext)commandContext, (String)"preset").toLowerCase();
            if (!(string.equals("safe") || string.equals("normal") || string.equals("fast") || string.equals("turbo") || string.equals("insane"))) {
                ((FabricClientCommandSource)commandContext.getSource()).sendError((class_2561)class_2561.method_43470((String)"\u00a7c\u53ef\u9009\u6863\u4f4d: safe / normal / fast / turbo / insane"));
                return 0;
            }
            AfsConfig.get().applyPreset(string);
            AfsConfig afsConfig = AfsConfig.get();
            ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)String.format("\u00a7b[FarmSelect] \u6863\u4f4d=\u00a7f%s \u00a77(\u95f4\u9694 %d tick, \u6bcf\u6279 %d, \u6bcf\u79d2\u4e0a\u9650 %d)", string, afsConfig.actionInterval, afsConfig.actionsPerBatch, afsConfig.packetBudgetPerSecond)));
            if (string.equals("turbo")) {
                ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)"\u00a7e\u6ce8\u610f: turbo \u53d1\u5305\u8f83\u5bc6\uff0c\u53cd\u4f5c\u5f0a\u4e25\u683c\u7684\u670d\u53ef\u80fd\u88ab\u8e22\u3002\u88ab\u8e22\u540e\u6a21\u7ec4\u4f1a\u81ea\u52a8\u964d\u6863\u3002"));
            }
            if (string.equals("insane")) {
                ((FabricClientCommandSource)commandContext.getSource()).sendFeedback((class_2561)class_2561.method_43470((String)"\u00a7c\u8b66\u544a: insane \u6863\u53d1\u5305\u6781\u5bc6\uff0c\u660e\u663e\u8d85\u51fa\u4eba\u624b\u64cd\u4f5c\u8303\u7574\uff0c\u88ab\u8e22/\u88abban\u98ce\u9669\u5f88\u9ad8\u3002\u5efa\u8bae\u53ea\u5728\u5355\u4eba\u5b58\u6863\u6216\u79c1\u670d\u4f7f\u7528\u3002"));
            }
            return 1;
        })))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)ClientCommandManager.literal((String)"water").executes(commandContext -> {
            AfsCommand.waterToggle((FabricClientCommandSource)commandContext.getSource(), !FarmController.get().isWatering());
            return 1;
        })).then(ClientCommandManager.literal((String)"on").executes(commandContext -> {
            AfsCommand.waterToggle((FabricClientCommandSource)commandContext.getSource(), true);
            return 1;
        }))).then(ClientCommandManager.literal((String)"off").executes(commandContext -> {
            AfsCommand.waterToggle((FabricClientCommandSource)commandContext.getSource(), false);
            return 1;
        })))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)ClientCommandManager.literal((String)"watertype").executes(commandContext -> {
            AfsCommand.waterTypeList((FabricClientCommandSource)commandContext.getSource());
            return 1;
        })).then(ClientCommandManager.literal((String)"add").then(ClientCommandManager.argument((String)"id", (ArgumentType)StringArgumentType.string()).executes(commandContext -> {
            AfsCommand.waterTypeEdit((FabricClientCommandSource)commandContext.getSource(), StringArgumentType.getString((CommandContext)commandContext, (String)"id"), true);
            return 1;
        })))).then(ClientCommandManager.literal((String)"del").then(ClientCommandManager.argument((String)"id", (ArgumentType)StringArgumentType.string()).executes(commandContext -> {
            AfsCommand.waterTypeEdit((FabricClientCommandSource)commandContext.getSource(), StringArgumentType.getString((CommandContext)commandContext, (String)"id"), false);
            return 1;
        }))))).then(ClientCommandManager.literal((String)"here").executes(commandContext -> {
            AfsCommand.here((FabricClientCommandSource)commandContext.getSource());
            return 1;
        }))).then(ClientCommandManager.literal((String)"scan").executes(commandContext -> {
            AfsCommand.diagnosticScan((FabricClientCommandSource)commandContext.getSource());
            return 1;
        }))).then(ClientCommandManager.literal((String)"why").executes(commandContext -> {
            AfsCommand.why((FabricClientCommandSource)commandContext.getSource());
            return 1;
        }))));
    }

    private static void info(FabricClientCommandSource fabricClientCommandSource) {
        AfsConfig afsConfig = AfsConfig.get();
        FarmController farmController = FarmController.get();
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a76===== FarmSelect \u72b6\u6001 ====="));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u8fd0\u884c: \u00a7f" + (farmController.isRunning() ? "\u00a7a\u5f00\u542f" : "\u00a7c\u505c\u6b62"))));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u6863\u4f4d: \u00a7f" + afsConfig.currentPresetName() + " \u00a77(\u95f4\u9694 " + afsConfig.actionInterval + " tick, \u6bcf\u6279 " + afsConfig.actionsPerBatch + ", \u6bcf\u79d2\u4e0a\u9650 " + afsConfig.packetBudgetPerSecond + ", \u8d85\u9650\u4f11\u606f " + afsConfig.burstPauseTicks + " tick)")));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u4ea4\u4e92\u8ddd\u79bb: \u00a7f" + afsConfig.reach)));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u52a8\u4f5c: \u00a7f\u53f3\u952e\u4f5c\u7269(tripwire) = \u6536\u83b7 + \u81ea\u52a8\u8865\u79cd" + (afsConfig.doHarvest ? "" : " \u00a77(\u5df2\u5173\u95ed)"))));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u6536\u83b7\u8303\u56f4: \u00a7f" + afsConfig.frontConeText())));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u7edf\u8ba1: \u00a7f" + farmController.statsLine())));
        int n = RateGuard.strikeCount();
        if (n > 0) {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7c\u88ab\u8e22\u8bb0\u5f55: \u672c\u6b21\u4f1a\u8bdd " + n + " \u6b21 \u00a77(\u5df2\u81ea\u52a8\u964d\u6863\u4fdd\u62a4)")));
        }
        if (farmController.remainingPauseTicks() > 0) {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7e\u9650\u6d41\u4f11\u606f\u4e2d\uff0c\u5269 " + farmController.remainingPauseTicks() + " tick")));
        }
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77\u79cd\u5b50\u8bc6\u522b: \u00a7fsugar\uff08\u672c\u670d\u79cd\u5b50\u7edf\u4e00\u4e3a minecraft:sugar\uff09"));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u81ea\u52a8\u8865\u6c34: \u00a7f" + (farmController.isWatering() ? "\u00a7a\u5f00\u542f" : "\u00a7c\u505c\u6b62") + " \u00a77(\u624b\u6301\u6d47\u6c34\u7f50\u53f3\u952e\u51c6\u661f\u6240\u6307\u7684\u6d47\u6c34\u673a, \u6bcf " + afsConfig.waterIntervalTicks + " tick \u4e00\u6b21; \u6309\u952e\u9ed8\u8ba4 J)")));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u8865\u6c34\u515c\u5e95: \u00a7f\u51c6\u661f\u9009\u4e0d\u4e2d\u65f6\u626b\u9644\u8fd1\u767d\u540d\u5355\u5b9e\u4f53 \u00a77(" + afsConfig.waterEntityTypeIds.size() + " \u7c7b, \u534a\u5f84 " + String.format("%.1f", afsConfig.waterEntitySearchRadius) + "; /afs watertype \u67e5\u770b)")));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77\u6307\u4ee4: \u00a7f/afs start | stop | toggle | water | cone | plantdist | mode | stat | resetstat | here | scan | why | watertype"));
    }

    private static void diagnosticScan(FabricClientCommandSource fabricClientCommandSource) {
        class_310 class_3102 = class_310.method_1551();
        AfsConfig afsConfig = AfsConfig.get();
        class_746 class_7462 = class_3102.field_1724;
        if (class_3102.field_1687 == null || class_7462 == null) {
            fabricClientCommandSource.sendError((class_2561)class_2561.method_43470((String)"\u00a7c\u9700\u8981\u5728\u6e38\u620f\u5185\u6267\u884c"));
            return;
        }
        class_243 class_2432 = class_7462.method_33571();
        class_243 class_2433 = class_7462.method_5720();
        double d = Math.sqrt(class_2433.field_1352 * class_2433.field_1352 + class_2433.field_1350 * class_2433.field_1350);
        boolean bl = d < 0.08;
        double d2 = bl ? 0.0 : class_2433.field_1352 / d;
        double d3 = bl ? 0.0 : class_2433.field_1350 / d;
        int n = Math.min(180, Math.max(1, afsConfig.plantConeDegrees));
        double d4 = Math.cos(Math.toRadians(n));
        double d5 = afsConfig.effectivePlantDistance();
        int n2 = (int)Math.ceil(d5);
        int n3 = (int)Math.floor(class_2432.field_1352);
        int n4 = (int)Math.floor(class_2432.field_1351);
        int n5 = (int)Math.floor(class_2432.field_1350);
        int n6 = n3 - n2;
        int n7 = n3 + n2;
        int n8 = n5 - n2;
        int n9 = n5 + n2;
        int n10 = n4 - 1;
        int n11 = n4 + 1;
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<String, Integer>();
        LinkedHashMap<String, Integer> linkedHashMap2 = new LinkedHashMap<String, Integer>();
        LinkedHashMap<Integer, Integer> linkedHashMap3 = new LinkedHashMap<Integer, Integer>();
        ArrayList<CallSite> arrayList = new ArrayList<CallSite>();
        int n12 = 0;
        int n13 = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("=== AutoFarmSelect \u8bca\u65ad\u62a5\u544a ===\n");
        stringBuilder.append("\u6536\u83b7\u8303\u56f4: \u9762\u524d ").append(n).append("\u00b0 \u6247\u5f62 / ").append(String.format("%.1f", d5)).append(" \u683c\u5185\uff08\u4e0e mod \u5b9e\u9645\u8303\u56f4\u4e00\u81f4\uff09\n");
        stringBuilder.append("Y \u7a97\u53e3: ").append(n10).append(" ~ ").append(n11).append('\n');
        stringBuilder.append("\u7ef4\u5ea6: ").append(class_3102.field_1687.method_27983().method_29177()).append('\n');
        stringBuilder.append('\n');
        for (int i = n6; i <= n7; ++i) {
            for (int j = n8; j <= n9; ++j) {
                for (int k = n10; k <= n11; ++k) {
                    double d6;
                    class_2338 class_23382 = new class_2338(i, k, j);
                    if (!class_3102.field_1687.method_8477(class_23382)) continue;
                    class_243 class_2434 = class_243.method_24953((class_2382)class_23382);
                    double d7 = class_2434.field_1352 - class_2432.field_1352;
                    double d8 = class_2434.field_1351 - class_2432.field_1351;
                    double d9 = class_2434.field_1350 - class_2432.field_1350;
                    double d10 = Math.sqrt(d7 * d7 + d8 * d8 + d9 * d9);
                    if (d10 > d5 || !bl && (d6 = Math.sqrt(d7 * d7 + d9 * d9)) > 1.0E-4 && (d7 * d2 + d9 * d3) / d6 < d4) continue;
                    ++n13;
                    class_2680 class_26802 = class_3102.field_1687.method_8320(class_23382);
                    String string = AfsCommand.idOf(class_26802);
                    linkedHashMap.merge(string, 1, Integer::sum);
                    linkedHashMap3.merge(k, 1, Integer::sum);
                    if (!FarmController.isCropBlock(class_26802)) continue;
                    ++n12;
                    linkedHashMap2.merge(string, 1, Integer::sum);
                    if (arrayList.size() >= 20) continue;
                    arrayList.add((CallSite)((Object)(class_23382.method_23854() + " (Y=" + k + ")")));
                }
            }
        }
        stringBuilder.append("\u9762\u524d\u6247\u5f62\u5185\u683c\u6570: ").append(n13).append('\n');
        stringBuilder.append("\u8bc6\u522b\u4e3a\u4f5c\u7269\u7684\u683c\u6570: ").append(n12).append('\n');
        if (n12 > 0) {
            stringBuilder.append("--- \u8bc6\u522b\u4e3a\u4f5c\u7269\u7684\u65b9\u5757\u79cd\u7c7b\uff08\u786e\u8ba4 mod \u8ba4\u51fa\u4e86\u4f60\u7684\u4f5c\u7269\uff09---\n");
            linkedHashMap2.entrySet().stream().sorted((entry, entry2) -> Integer.compare((Integer)entry2.getValue(), (Integer)entry.getValue())).forEach(entry -> stringBuilder.append(String.format("  %-45s %d%n", entry.getKey(), entry.getValue())));
            stringBuilder.append('\n');
            stringBuilder.append("--- \u4f5c\u7269\u5750\u6807\uff08\u6700\u591a 20 \u4e2a\uff09---\n");
            for (String string : arrayList) {
                stringBuilder.append("  ").append(string).append('\n');
            }
            stringBuilder.append('\n');
        } else {
            stringBuilder.append("\uff08\u6ca1\u8bc6\u522b\u5230\u4f5c\u7269\uff01\u8fd9\u8bf4\u660e\u4f60\u7684\u4f5c\u7269\u65b9\u5757\u65e2\u4e0d\u662f tripwire\u3001\u4e5f\u4e0d\u5728 cropBlockIds \u91cc\u3002\n");
            stringBuilder.append(" \u770b\u6e05\u771f\u5b9e ID \u540e\u586b\u8fdb\u914d\u7f6e cropBlockIds \u5373\u53ef\u8bc6\u522b\uff1b\u6216\u8f6c\u8eab\u6362\u4e2a\u65b9\u5411\u518d scan\u3002\uff09\n\n");
        }
        stringBuilder.append("\u3010\u673a\u5236\u8bf4\u660e\u3011\u672c\u670d\u53f3\u952e\u4f5c\u7269(tripwire)\u5373\u540c\u65f6\u5b8c\u6210\u6536\u83b7 + \u81ea\u52a8\u8865\u79cd\uff0c\n");
        stringBuilder.append("\u6a21\u7ec4\u53ea\u9700\u627e\u5230\u4f5c\u7269\u683c\u3001\u53f3\u952e\u5b83\uff0c\u65e0\u9700\u5355\u72ec\u8865\u79cd\u3002\u91cd\u590d\u53f3\u952e\u540c\u4e00\u682a\u7684\u8282\u594f\n");
        stringBuilder.append("\u7531 cropCooldownMs\uff08\u6210\u719f\u5468\u671f\u51b7\u5374\uff09\u63a7\u5236\uff0c\u9ed8\u8ba4 120000ms\uff082 \u5206\u949f\uff09\u3002\n\n");
        stringBuilder.append("--- \u9762\u524d\u6247\u5f62\u5185\u51fa\u73b0\u8fc7\u7684\u6240\u6709\u65b9\u5757\uff08\u524d 30\uff09---\n");
        linkedHashMap.entrySet().stream().sorted((entry, entry2) -> Integer.compare((Integer)entry2.getValue(), (Integer)entry.getValue())).limit(30L).forEach(entry -> stringBuilder.append(String.format("  %-45s %d%n", entry.getKey(), entry.getValue())));
        stringBuilder.append('\n');
        stringBuilder.append("--- Y \u5c42\u5206\u5e03 ---\n");
        linkedHashMap3.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> stringBuilder.append(String.format("  Y=%d: %d \u683c%n", entry.getKey(), entry.getValue())));
        String string = AfsCommand.writeReport(stringBuilder.toString());
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a76===== \u8bca\u65ad\u62a5\u544a ====="));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u9762\u524d\u6247\u5f62\u5185: \u00a7f" + n13 + " \u683c \u00a77\u4f5c\u7269: \u00a7f" + n12)));
        if (n12 > 0) {
            linkedHashMap2.entrySet().stream().sorted((entry, entry2) -> Integer.compare((Integer)entry2.getValue(), (Integer)entry.getValue())).limit(5L).forEach(entry -> fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("  \u00a7e\u4f5c\u7269: " + (String)entry.getKey() + " \u00a77x" + String.valueOf(entry.getValue())))));
        } else {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a7c\u6ca1\u8bc6\u522b\u5230\u4f5c\u7269\uff01\u4f60\u7684\u4f5c\u7269\u65b9\u5757\u65e2\u975e tripwire\u3001\u4e5f\u4e0d\u5728 cropBlockIds\u3002"));
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77\u7528 /afs scan \u770b\u6e05\u4f5c\u7269\u771f\u5b9e ID\uff0c\u586b\u8fdb\u914d\u7f6e cropBlockIds \u5373\u53ef\u8bc6\u522b\u3002"));
        }
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7a\u5b8c\u6574\u62a5\u544a\u5df2\u5199\u5165: \u00a7f" + string)));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77\u628a\u4e0a\u9762\u6587\u4ef6\u53d1\u6211\uff0c\u5c31\u80fd\u786e\u8ba4\u771f\u5b9e\u4f5c\u7269 ID"));
        AutoFarmSelect.LOGGER.debug("===== AUTO FARM SCAN REPORT BEGIN =====\n{}\n===== END =====", (Object)stringBuilder);
    }

    private static String writeReport(String string) {
        try {
            Path path = AfsConfig.dir();
            Files.createDirectories(path, new FileAttribute[0]);
            Path path2 = path.resolve("autofarmselect-scan.log");
            Files.writeString(path2, (CharSequence)string, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return path2.toString();
        }
        catch (Throwable throwable) {
            return "(\u5199\u6587\u4ef6\u5931\u8d25: " + throwable.getClass().getSimpleName() + ")";
        }
    }

    private static String idOf(class_2680 class_26802) {
        try {
            return class_26802.method_26204().method_63499();
        }
        catch (Throwable throwable) {
            return "<\u672a\u77e5>";
        }
    }

    private static String idOf(class_1799 class_17992) {
        try {
            return class_17992.method_7909().method_7876();
        }
        catch (Throwable throwable) {
            return "<\u672a\u77e5>";
        }
    }

    private static void here(FabricClientCommandSource fabricClientCommandSource) {
        class_1297 class_12972;
        class_310 class_3102 = class_310.method_1551();
        class_746 class_7462 = class_3102.field_1724;
        if (class_3102.field_1687 == null || class_7462 == null) {
            fabricClientCommandSource.sendError((class_2561)class_2561.method_43470((String)"\u00a7c\u9700\u8981\u5728\u6e38\u620f\u5185\u6267\u884c"));
            return;
        }
        int n = class_7462.method_31477();
        int n2 = class_7462.method_31478();
        int n3 = class_7462.method_31479();
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a76===== \u4f60\u5468\u56f4\u7684\u65b9\u5757 ====="));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u5750\u6807: \u00a7f" + n + " " + n2 + " " + n3)));
        AfsCommand.layerLine(fabricClientCommandSource, class_3102, "\u811a\u4e0b(py-1) ", n, n2 - 1, n3);
        AfsCommand.layerLine(fabricClientCommandSource, class_3102, "\u7ad9\u7acb\u5c42(py) ", n, n2, n3);
        AfsCommand.layerLine(fabricClientCommandSource, class_3102, "\u5934\u9876(py+1) ", n, n2 + 1, n3);
        class_239 class_2392 = class_3102.field_1765;
        if (class_2392 != null && class_2392.method_17783() == class_239.class_240.field_1332) {
            class_12972 = ((class_3965)class_2392).method_17777();
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u51c6\u661f\u6240\u6307: \u00a7f" + class_12972.method_23854() + " \u00a77\u2192 \u00a7f" + AfsCommand.idOf(class_3102.field_1687.method_8320((class_2338)class_12972)))));
        } else {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77\u51c6\u661f\u6240\u6307: \u00a7f(\u6ca1\u6709\u5bf9\u7740\u65b9\u5757)"));
        }
        class_12972 = class_3102.field_1692;
        if (class_12972 == null && class_2392 instanceof class_3966) {
            class_3966 class_39662 = (class_3966)class_2392;
            class_12972 = class_39662.method_17782();
        }
        if (class_12972 != null) {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7a\u51c6\u661f\u6240\u6307(\u5b9e\u4f53): \u00a7f" + AfsCommand.entityId(class_12972) + " \u00a77\u540d=\u00a7f" + class_12972.method_5477().getString() + " \u00a77\u8ddd=\u00a7f" + String.format("%.1f", Float.valueOf(class_7462.method_5739(class_12972))) + " \u00a77@\u00a7f" + class_12972.method_24515().method_23854())));
            if (!(class_12972 instanceof class_1309)) {
                fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77  \u00a7e\u2191 \u8fd9\u662f\u975e\u751f\u7269\u5b9e\u4f53\uff0c\u591a\u534a\u5c31\u662f\u4f2a\u88c5\u6210\u673a\u5668\u7684\u90a3\u4e2a"));
            }
        } else {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77\u51c6\u661f\u6240\u6307(\u5b9e\u4f53): \u00a78\u65e0"));
        }
        AfsCommand.reportNearbyEntities(fabricClientCommandSource, class_3102, class_7462);
    }

    private static void reportNearbyEntities(FabricClientCommandSource fabricClientCommandSource, class_310 class_3102, class_746 class_7462) {
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a76----- \u9644\u8fd1\u5b9e\u4f53(8\u683c\u5185) -----"));
        try {
            class_238 class_2383 = class_7462.method_5829().method_1014(8.0);
            List list = class_3102.field_1687.method_8333((class_1297)class_7462, class_2383, class_12972 -> !(class_12972 instanceof class_1657));
            list.sort(Comparator.comparingDouble(arg_0 -> ((class_746)class_7462).method_5739(arg_0)));
            if (list.isEmpty()) {
                fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77(8\u683c\u5185\u6ca1\u6709\u5b9e\u4f53)"));
                return;
            }
            int n = 0;
            for (class_1297 class_12973 : list) {
                if (n++ >= 12) {
                    fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77... \u8fd8\u6709\u66f4\u591a(\u5df2\u7701\u7565)"));
                    break;
                }
                fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77- \u00a7f" + AfsCommand.entityId(class_12973) + " \u00a77\u540d=\u00a7f" + class_12973.method_5477().getString() + " \u00a77\u8ddd=\u00a7f" + String.format("%.1f", Float.valueOf(class_7462.method_5739(class_12973))) + " \u00a77@\u00a7f" + class_12973.method_24515().method_23854())));
            }
        }
        catch (Throwable throwable) {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u9644\u8fd1\u5b9e\u4f53\u8bfb\u53d6\u5931\u8d25: \u00a78" + throwable.getClass().getSimpleName())));
        }
    }

    private static String entityId(class_1297 class_12972) {
        try {
            return class_7923.field_41177.method_10221((Object)class_12972.method_5864()).toString();
        }
        catch (Throwable throwable) {
            return String.valueOf(class_12972.method_5864());
        }
    }

    private static void layerLine(FabricClientCommandSource fabricClientCommandSource, class_310 class_3102, String string, int n, int n2, int n3) {
        class_2338 class_23382 = new class_2338(n, n2, n3);
        if (!class_3102.field_1687.method_8477(class_23382)) {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77" + string + ": \u00a78(\u533a\u5757\u672a\u52a0\u8f7d)")));
            return;
        }
        class_2680 class_26802 = class_3102.field_1687.method_8320(class_23382);
        String string2 = FarmController.isCropBlock(class_26802) ? "  \u00a7a[\u8bc6\u522b\u4e3a\u4f5c\u7269]" : "";
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77" + string + ": \u00a7f" + AfsCommand.idOf(class_26802) + string2)));
    }

    private static void waterToggle(FabricClientCommandSource fabricClientCommandSource, boolean bl) {
        FarmController farmController = FarmController.get();
        if (bl && !FarmController.hasAnyWateringCan(class_310.method_1551())) {
            fabricClientCommandSource.sendError((class_2561)class_2561.method_43470((String)"\u00a7c[FarmSelect] \u624b\u4e0a\u548c\u80cc\u5305\u90fd\u6ca1\u6709\u6d47\u6c34\u7f50\uff08\u539f\u578b golden_horse_armor\uff09\uff0c\u65e0\u6cd5\u5f00\u542f\u8865\u6c34\u3002"));
            return;
        }
        farmController.setWatering(bl);
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)(bl ? "\u00a7a[FarmSelect] \u81ea\u52a8\u8865\u6c34\u5df2\u5f00\u542f \u00a77(\u624b\u6301\u6d47\u6c34\u7f50\u5bf9\u51c6\u6d47\u6c34\u673a\uff0c\u6bcf " + AfsConfig.get().waterIntervalTicks + " tick \u4e00\u6b21)" : "\u00a7c[FarmSelect] \u81ea\u52a8\u8865\u6c34\u5df2\u505c\u6b62")));
    }

    private static void waterTypeList(FabricClientCommandSource fabricClientCommandSource) {
        AfsConfig afsConfig = AfsConfig.get();
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a76\u8865\u6c34\u515c\u5e95\u626b\u63cf\u7684\u5b9e\u4f53\u767d\u540d\u5355 \u00a77(\u534a\u5f84 " + String.format("%.1f", afsConfig.waterEntitySearchRadius) + " \u683c)")));
        if (afsConfig.waterEntityTypeIds.isEmpty()) {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77(\u7a7a \u2014\u2014 \u515c\u5e95\u626b\u63cf\u5173\u95ed)"));
        } else {
            for (String string : afsConfig.waterEntityTypeIds) {
                fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77- \u00a7f" + string)));
            }
        }
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77\u589e\u5220: \u00a7f/afs watertype add <\u5b9e\u4f53ID> \u00a77/ \u00a7f/afs watertype del <\u5b9e\u4f53ID>"));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a77\u5b9e\u4f53 ID \u770b /afs here \u7684\u300c\u9644\u8fd1\u5b9e\u4f53\u300d\u90a3\u51e0\u884c"));
    }

    private static void waterTypeEdit(FabricClientCommandSource fabricClientCommandSource, String string, boolean bl) {
        AfsConfig afsConfig = AfsConfig.get();
        Object object = string.toLowerCase(Locale.ROOT);
        if (!((String)object).contains(":")) {
            object = "minecraft:" + (String)object;
        }
        if (bl) {
            if (afsConfig.waterEntityTypeIds.contains(object)) {
                fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7e\u5df2\u5728\u540d\u5355\u91cc: \u00a7f" + (String)object)));
                return;
            }
            afsConfig.waterEntityTypeIds.add((String)object);
        } else if (!afsConfig.waterEntityTypeIds.remove(object)) {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7e\u4e0d\u5728\u540d\u5355\u91cc: \u00a7f" + (String)object)));
            return;
        }
        AfsConfig.save();
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7b[FarmSelect] \u00a7f" + (bl ? "\u5df2\u52a0\u5165" : "\u5df2\u79fb\u9664") + " \u00a77" + (String)object + " \u00a77(\u5171 " + afsConfig.waterEntityTypeIds.size() + " \u9879)")));
    }

    private static void why(FabricClientCommandSource fabricClientCommandSource) {
        Object object;
        class_310 class_3102 = class_310.method_1551();
        AfsConfig afsConfig = AfsConfig.get();
        FarmController farmController = FarmController.get();
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)"\u00a76===== \u4e3a\u4ec0\u4e48\u6ca1\u52a8\uff1f ====="));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u8fd0\u884c\u72b6\u6001: \u00a7f" + (farmController.isRunning() ? "\u00a7a\u5f00\u542f" : "\u00a7c\u505c\u6b62\uff08\u6309 H \u6216 /afs start\uff09"))));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u6536\u83b7\u8303\u56f4: \u00a7f" + afsConfig.frontConeText())));
        fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u6700\u8fd1\u4e00\u6b21\u53d7\u963b\u539f\u56e0: \u00a7e" + farmController.lastFailReason())));
        class_746 class_7462 = class_3102.field_1724;
        if (class_7462 != null) {
            object = class_7462.method_6047();
            String string = object.method_7960() ? "\u7a7a\u624b" : AfsCommand.idOf(object);
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u624b\u6301: \u00a7f" + string)));
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u526f\u624b: \u00a7f" + (class_7462.method_6079().method_7960() ? "\u7a7a" : "\u6709\u7269\u54c1"))));
            class_1661 class_16612 = class_7462.method_31548();
            int n = -1;
            int n2 = -1;
            for (int i = 0; i < 36; ++i) {
                class_1799 class_17992 = class_16612.method_5438(i);
                if (class_17992.method_7960() || !AfsCommand.idOf(class_17992).endsWith("sugar")) continue;
                if (i < 9 && n < 0) {
                    n = i;
                    continue;
                }
                if (i < 9 || n2 >= 0) continue;
                n2 = i;
            }
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a77\u79cd\u5b50\u4f4d\u7f6e: \u00a7f\u5feb\u6377\u680f=" + (n < 0 ? "\u65e0" : String.valueOf(n)) + " \u80cc\u5305=" + (n2 < 0 ? "\u65e0" : String.valueOf(n2)))));
        }
        if (farmController.remainingPauseTicks() > 0) {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7e\u9650\u6d41\u4f11\u606f\u4e2d\uff0c\u5269 " + farmController.remainingPauseTicks() + " tick")));
        }
        object = "";
        object = !farmController.isRunning() ? "\u5148\u6309 H \u5f00\u542f" : "\u53ea\u6536\u9762\u524d\u7684\uff08" + afsConfig.frontConeText() + "\uff09\uff1a\u7ad9\u5230\u7530\u8fb9\u3001\u9762\u671d\u8981\u6536\u7684\u65b9\u5411\u5373\u53ef\uff0c\u4e0d\u5fc5\u9010\u683c\u7784\u51c6";
        if (!object.isEmpty()) {
            fabricClientCommandSource.sendFeedback((class_2561)class_2561.method_43470((String)("\u00a7b\u5efa\u8bae: \u00a7f" + (String)object)));
        }
    }
}

