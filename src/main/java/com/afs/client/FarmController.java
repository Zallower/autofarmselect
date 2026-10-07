/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1297
 *  net.minecraft.class_1308
 *  net.minecraft.class_1657
 *  net.minecraft.class_1661
 *  net.minecraft.class_1713
 *  net.minecraft.class_1723
 *  net.minecraft.class_1735
 *  net.minecraft.class_1799
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_2596
 *  net.minecraft.class_2680
 *  net.minecraft.class_2828$class_2831
 *  net.minecraft.class_310
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 *  net.minecraft.class_3966
 *  net.minecraft.class_634
 *  net.minecraft.class_746
 *  net.minecraft.class_7923
 */
package com.afs.client;

import com.afs.config.AfsConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1713;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2828;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_634;
import net.minecraft.class_746;
import net.minecraft.class_7923;

public final class FarmController {
    private static final FarmController INSTANCE = new FarmController();
    private boolean running = false;
    private int tickCounter = 0;
    private boolean watering = false;
    private int waterTickCounter = 0;
    private final HashMap<Long, ClickRec> clicked = new HashMap();
    private final HashMap<Integer, Long> wateredAt = new HashMap();
    private static final int WATERED_MAX = 512;
    private final HashMap<Integer, Integer> waterCount = new HashMap();
    private boolean waterAllCooling = false;
    private int forceRotTicks = 0;
    private double forceYaw = 0.0;
    private double forcePitch = 0.0;
    private static final int CLICKED_MAX = 8192;
    private int statHarvest = 0;
    private int statFail = 0;
    private int scannedPositions = 0;
    private long rateWindowStart = 0L;
    private int rateCount = 0;
    private int pauseTicks = 0;
    private String lastFailReason = "\uff08\u8fd8\u6ca1\u5c1d\u8bd5\u8fc7\u52a8\u4f5c\uff09";
    private String lastClientPredict = "-";
    private String lastActionTarget = "-";

    public static FarmController get() {
        return INSTANCE;
    }

    private FarmController() {
    }

    public boolean isWatering() {
        return this.watering;
    }

    public void setWatering(boolean bl) {
        this.watering = bl;
        this.waterTickCounter = 0;
        this.wateredAt.clear();
        this.waterCount.clear();
        this.forceRotTicks = 0;
    }

    private boolean canClick(long l, boolean bl) {
        return this.canClick(l, bl, AfsConfig.get().cropCooldownMs);
    }

    private boolean canClick(long l, boolean bl, int n) {
        ClickRec clickRec = this.clicked.get(l);
        if (clickRec == null) {
            return true;
        }
        if (clickRec.occupied != bl) {
            this.clicked.remove(l);
            return true;
        }
        if (n <= 0 || System.currentTimeMillis() - clickRec.ms >= (long)n) {
            this.clicked.remove(l);
            return true;
        }
        return false;
    }

    private void markClicked(long l, boolean bl) {
        if (this.clicked.size() >= 8192) {
            this.clicked.clear();
        }
        this.clicked.put(l, new ClickRec(bl, System.currentTimeMillis()));
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean bl) {
        this.running = bl;
        this.tickCounter = 0;
        this.clicked.clear();
    }

    public void toggle() {
        this.setRunning(!this.running);
    }

    public String statsLine() {
        return String.format("\u5df2\u6536 %d / \u5931\u8d25 %d", this.statHarvest, this.statFail);
    }

    public String lastFailReason() {
        return this.lastFailReason;
    }

    public int scannedPositions() {
        return this.scannedPositions;
    }

    public int remainingPauseTicks() {
        return this.pauseTicks;
    }

    public void resetStats() {
        this.statHarvest = 0;
        this.statFail = 0;
    }

    private static long key(class_2338 class_23382) {
        return class_2338.method_10064((int)class_23382.method_10263(), (int)class_23382.method_10264(), (int)class_23382.method_10260());
    }

    public static boolean isCropBlock(class_2680 class_26802) {
        if (class_26802.method_26215()) {
            return false;
        }
        String string = FarmController.blockId(class_26802);
        if (string.contains("tripwire")) {
            return true;
        }
        return AfsConfig.get().cropBlockIds.contains(string);
    }

    private static String blockId(class_2680 class_26802) {
        try {
            return class_26802.method_26204().method_63499();
        }
        catch (Throwable throwable) {
            return "";
        }
    }

    public void tick(class_310 class_3102) {
        if (!this.running) {
            return;
        }
        if (class_3102.field_1724 == null || class_3102.field_1687 == null) {
            return;
        }
        if (class_3102.field_1755 != null || !class_3102.field_1729.method_1613()) {
            return;
        }
        AfsConfig afsConfig = AfsConfig.get();
        if (!FarmController.hasAnySeed(class_3102)) {
            this.setRunning(false);
            class_3102.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7c[FarmSelect] \u6ca1\u6709\u79cd\u5b50\uff0c\u5df2\u81ea\u52a8\u5173\u95ed"), true);
            return;
        }
        ++this.tickCounter;
        if (this.pauseTicks > 0) {
            return;
        }
        if (this.tickCounter < afsConfig.actionInterval) {
            return;
        }
        this.tickCounter = 0;
        int n = 0;
        if (afsConfig.doHarvest) {
            n += this.frontAreaHarvest(class_3102, afsConfig);
        }
        this.chargeRate(n, afsConfig);
    }

    public void waterTick(class_310 class_3102) {
        int n;
        if (!this.watering) {
            this.forceRotTicks = 0;
            return;
        }
        if (class_3102.field_1724 == null || class_3102.field_1687 == null) {
            return;
        }
        if (class_3102.field_1755 != null || !class_3102.field_1729.method_1613()) {
            return;
        }
        if (this.pauseTicks > 0) {
            return;
        }
        AfsConfig afsConfig = AfsConfig.get();
        if (!FarmController.hasAnyWateringCan(class_3102)) {
            this.setWatering(false);
            class_3102.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7c[FarmSelect] \u6ca1\u6709\u6d47\u6c34\u7f50\uff0c\u5df2\u81ea\u52a8\u5173\u95ed"), true);
            return;
        }
        if (this.forceRotTicks > 0 && class_3102.method_1562() != null) {
            class_3102.method_1562().method_52787((class_2596)new class_2828.class_2831((float)this.forceYaw, (float)this.forcePitch, class_3102.field_1724.method_24828(), false));
            --this.forceRotTicks;
        }
        if (++this.waterTickCounter < (n = Math.max(1, afsConfig.waterIntervalTicks))) {
            return;
        }
        this.waterTickCounter = 0;
        class_1297 class_12972 = this.findMachineEntity(class_3102, afsConfig);
        if (class_12972 == null) {
            this.lastFailReason = this.waterAllCooling ? "\uff08\u6d47\u6c34\u673a\u90fd\u5728\u51b7\u5374\u4e2d\uff0c\u8ddd\u4e0a\u6b21\u8865\u6c34 < " + afsConfig.waterCooldownMs / 1000 + "s \u540e\u624d\u91cd\u8bd5\uff09" : "\u5468\u56f4 " + String.format("%.1f", afsConfig.waterEntitySearchRadius) + " \u683c\u5185\u6ca1\u626b\u5230\u6d47\u6c34\u673a\uff08interaction/display/\u76d4\u7532\u67b6\uff09\u2014\u2014 \u8d70\u8fd1\u4e00\u70b9\uff1b\u767d\u540d\u5355\u7528 /afs watertype \u770b\uff0c\u9644\u8fd1\u5b9e\u4f53\u7528 /afs here \u770b";
            return;
        }
        double d = class_3102.field_1724.method_55755();
        double d2 = class_3102.field_1724.method_5739(class_12972);
        if (d2 > d) {
            this.lastFailReason = String.format("\u79bb\u6d47\u6c34\u673a\u592a\u8fdc\uff08%.1f > \u5b9e\u4f53\u4ea4\u4e92\u8ddd\u79bb %.1f\uff09\uff0c\u8bf7\u8d70\u8fd1\u4e00\u70b9\u518d\u6309 J", d2, d);
            return;
        }
        class_1268 class_12682 = this.wateringHand(class_3102);
        if (class_12682 == null) {
            this.lastFailReason = "\u624b\u4e0a\u548c\u80cc\u5305\u90fd\u6ca1\u6709\u6d47\u6c34\u7f50\uff08golden_horse_armor\uff09\uff0c\u65e0\u6cd5\u8865\u6c34";
            return;
        }
        class_1799 class_17992 = class_12682 == class_1268.field_5808 ? class_3102.field_1724.method_6047() : class_3102.field_1724.method_6079();
        class_746 class_7462 = class_3102.field_1724;
        class_238 class_2382 = class_12972.method_5829();
        class_243 class_2432 = class_2382 != null ? class_2382.method_1005() : class_12972.method_73189();
        class_243 class_2433 = class_7462.method_33571();
        double d3 = class_2432.field_1352 - class_2433.field_1352;
        double d4 = class_2432.field_1351 - class_2433.field_1351;
        double d5 = class_2432.field_1350 - class_2433.field_1350;
        double d6 = Math.sqrt(d3 * d3 + d5 * d5);
        double d7 = Math.toDegrees(Math.atan2(d5, d3)) - 90.0;
        double d8 = -Math.toDegrees(Math.atan2(d4, d6));
        class_634 class_6342 = class_3102.method_1562();
        if (class_6342 != null) {
            class_6342.method_52787((class_2596)new class_2828.class_2831((float)d7, (float)d8, class_7462.method_24828(), false));
            this.forceYaw = d7;
            this.forcePitch = d8;
            this.forceRotTicks = 4;
        }
        try {
            String string;
            class_238 class_2383 = class_12972.method_5829();
            class_243 class_2434 = class_2383 != null ? class_2383.method_1005() : class_12972.method_73189();
            class_1269 class_12692 = class_3102.field_1761.method_2917((class_1657)class_7462, class_12972, new class_3966(class_12972, class_2434), class_12682);
            class_7462.method_6104(class_12682);
            this.lastActionTarget = string = FarmController.entityIdOf(class_12972);
            this.lastFailReason = "\uff08\u5df2\u8865\u6c34\u53d1\u5305\uff1a\u53f3\u952e\u5b9e\u4f53 " + string + " \u8ddd\u79bb " + String.format("%.1f", d2) + (class_12682 == class_1268.field_5808 ? "\u4e3b\u624b" : "\u526f\u624b") + FarmController.canWaterHint(class_17992) + "\uff0c\u672c\u5730\u9884\u6d4b=" + String.valueOf(class_12692) + "\uff09";
            if (this.wateredAt.size() >= 512) {
                this.wateredAt.clear();
                this.waterCount.clear();
            }
            this.wateredAt.put(class_12972.method_5628(), System.currentTimeMillis());
            this.waterCount.merge(class_12972.method_5628(), 1, Integer::sum);
            this.chargeRate(1, afsConfig);
        }
        catch (Throwable throwable) {
            this.lastFailReason = "\u8865\u6c34\u53d1\u5305\u5f02\u5e38(\u5b9e\u4f53): " + throwable.getClass().getSimpleName();
        }
    }

    private class_1297 findMachineEntity(class_310 class_3102, AfsConfig afsConfig) {
        try {
            if (class_3102.field_1724 == null || class_3102.field_1687 == null) {
                return null;
            }
            double d = Math.min(afsConfig.waterEntitySearchRadius, afsConfig.reach);
            class_238 class_2382 = class_3102.field_1724.method_5829().method_1014(d + 1.0);
            List list = class_3102.field_1687.method_8333((class_1297)class_3102.field_1724, class_2382, class_12972 -> !(class_12972 instanceof class_1657) && !(class_12972 instanceof class_1308));
            double d2 = class_3102.field_1724.method_55755();
            long l = afsConfig.waterCooldownMs;
            boolean bl = false;
            class_1297 class_12973 = null;
            double d3 = Double.MAX_VALUE;
            class_1297 class_12974 = null;
            double d4 = Double.MAX_VALUE;
            for (class_1297 class_12975 : list) {
                int n;
                double d5;
                String string = FarmController.entityIdOf(class_12975);
                boolean bl2 = afsConfig.waterEntityTypeIds != null && afsConfig.waterEntityTypeIds.contains(string);
                if (!bl2 || (d5 = (double)class_3102.field_1724.method_5739(class_12975)) > d2) continue;
                bl = true;
                Integer n2 = this.waterCount.get(class_12975.method_5628());
                int n3 = n = n2 == null ? 0 : n2;
                if (n >= afsConfig.waterRefillCount) {
                    Long l2 = this.wateredAt.get(class_12975.method_5628());
                    if (l2 != null && System.currentTimeMillis() - l2 < l) continue;
                    this.waterCount.put(class_12975.method_5628(), 0);
                }
                if ("minecraft:interaction".equals(string)) {
                    if (!(d5 < d3)) continue;
                    d3 = d5;
                    class_12973 = class_12975;
                    continue;
                }
                if (!(d5 < d4)) continue;
                d4 = d5;
                class_12974 = class_12975;
            }
            this.waterAllCooling = bl && class_12973 == null && class_12974 == null;
            return class_12973 != null ? class_12973 : class_12974;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static String entityIdOf(class_1297 class_12972) {
        try {
            return class_7923.field_41177.method_10221((Object)class_12972.method_5864()).toString();
        }
        catch (Throwable throwable) {
            return String.valueOf(class_12972.method_5864());
        }
    }

    public void rateTick(class_310 class_3102) {
        if (this.pauseTicks > 0) {
            --this.pauseTicks;
            if (this.pauseTicks % 20 == 0 && class_3102.field_1724 != null) {
                class_3102.field_1724.method_7353((class_2561)class_2561.method_43470((String)("\u00a7e[FarmSelect] \u9650\u6d41\u4f11\u606f\u4e2d\u2026 \u5269 " + this.pauseTicks + " tick")), true);
            }
        }
    }

    private void chargeRate(int n, AfsConfig afsConfig) {
        long l = System.currentTimeMillis();
        if (this.rateWindowStart == 0L || l - this.rateWindowStart >= 1000L) {
            this.rateWindowStart = l;
            this.rateCount = 0;
        }
        this.rateCount += n;
        if (this.rateCount > afsConfig.packetBudgetPerSecond) {
            this.pauseTicks = afsConfig.burstPauseTicks;
            this.rateWindowStart = 0L;
            this.rateCount = 0;
        }
    }

    private int frontAreaHarvest(class_310 class_3102, AfsConfig afsConfig) {
        int n;
        int n2;
        int n3;
        class_746 class_7462 = class_3102.field_1724;
        if (class_7462 == null || class_3102.field_1687 == null) {
            return 0;
        }
        class_243 class_2432 = class_7462.method_33571();
        class_243 class_2433 = class_7462.method_5720();
        double d = Math.sqrt(class_2433.field_1352 * class_2433.field_1352 + class_2433.field_1350 * class_2433.field_1350);
        boolean bl = d < 0.08;
        double d2 = bl ? 0.0 : class_2433.field_1352 / d;
        double d3 = bl ? 0.0 : class_2433.field_1350 / d;
        double d4 = Math.min(180, Math.max(1, afsConfig.plantConeDegrees));
        double d5 = Math.cos(Math.toRadians(d4));
        double d6 = afsConfig.effectivePlantDistance();
        int n4 = (int)Math.ceil(d6);
        int n5 = (int)Math.floor(class_2432.field_1352);
        int n6 = (int)Math.floor(class_2432.field_1351);
        int n7 = (int)Math.floor(class_2432.field_1350);
        int n8 = n6 - 1;
        int n9 = n6 + 1;
        ArrayList<Candidate> arrayList = new ArrayList<Candidate>();
        int n10 = 0;
        for (n3 = n5 - n4; n3 <= n5 + n4; ++n3) {
            for (n2 = n7 - n4; n2 <= n7 + n4; ++n2) {
                for (n = n8; n <= n9; ++n) {
                    double d7;
                    class_2338 class_23382 = new class_2338(n3, n, n2);
                    if (afsConfig.onlyLoadedChunks && !class_3102.field_1687.method_8477(class_23382)) continue;
                    ++n10;
                    if (!FarmController.isCropBlock(class_3102.field_1687.method_8320(class_23382))) continue;
                    Candidate candidate3 = class_243.method_24953((class_2382)class_23382);
                    double d8 = ((class_243)candidate3).field_1352 - class_2432.field_1352;
                    double d9 = ((class_243)candidate3).field_1351 - class_2432.field_1351;
                    double d10 = ((class_243)candidate3).field_1350 - class_2432.field_1350;
                    double d11 = Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
                    if (d11 > d6) continue;
                    double d12 = 0.0;
                    if (!bl && (d7 = Math.sqrt(d8 * d8 + d10 * d10)) > 1.0E-4) {
                        double d13 = (d8 * d2 + d10 * d3) / d7;
                        if (d13 < d5) continue;
                        d12 = Math.toDegrees(Math.acos(Math.min(1.0, Math.max(-1.0, d13))));
                    }
                    arrayList.add(new Candidate(class_23382, class_23382, d11, d12, FarmController.key(class_23382)));
                }
            }
        }
        this.scannedPositions = n10;
        if (arrayList.isEmpty()) {
            this.lastFailReason = "\u9762\u524d\u6ca1\u6709\u53ef\u6536\u7684\u4f5c\u7269\uff08" + afsConfig.frontConeText() + "\uff0c\u626b\u4e86 " + n10 + " \u683c\uff09";
            return 0;
        }
        arrayList.sort((candidate, candidate2) -> {
            int n = Double.compare(candidate.deg, candidate2.deg);
            return n != 0 ? n : Double.compare(candidate.dist, candidate2.dist);
        });
        n3 = 0;
        n2 = Math.max(1, afsConfig.actionsPerBatch);
        n = 0;
        for (Candidate candidate3 : arrayList) {
            if (n3 >= n2 || n >= n2 * 3 + 2) break;
            ++n;
            if (!this.canClick(candidate3.key, true)) continue;
            if (this.doAction(class_3102, new Target(candidate3.soil, candidate3.above, true), true, false)) {
                this.markClicked(candidate3.key, true);
                ++n3;
                ++this.statHarvest;
                continue;
            }
            ++this.statFail;
        }
        return n3;
    }

    private boolean doAction(class_310 class_3102, Target target, boolean bl) {
        return this.doAction(class_3102, target, bl, true);
    }

    private boolean doAction(class_310 class_3102, Target target, boolean bl, boolean bl2) {
        class_746 class_7462 = class_3102.field_1724;
        if (class_7462 == null) {
            return false;
        }
        AfsConfig afsConfig = AfsConfig.get();
        class_2338 class_23382 = target.crop;
        if (!this.isInReach(class_3102, class_23382)) {
            this.lastFailReason = String.format("\u591f\u4e0d\u7740 %s\uff08\u8ddd\u79bb %.1f > %.1f\uff09\uff0c\u8bf7\u8d70\u8fd1\u4e00\u70b9", class_23382.method_23854(), this.eyeDistance(class_3102, class_23382), afsConfig.reach);
            return false;
        }
        if (!this.ensureSeedInHand(class_3102)) {
            this.lastFailReason = "\u624b\u4e0a\u548c\u80cc\u5305\u90fd\u6ca1\u6709\u79cd\u5b50\uff08\u627e\u4e0d\u5230 minecraft:sugar\uff09\uff0c\u5df2\u505c\u6b62\u64cd\u4f5c";
            return false;
        }
        if (afsConfig.faceTarget && bl2) {
            this.faceBlock(class_7462, class_23382);
        }
        class_3965 class_39652 = this.buildHitResult(class_3102, class_23382);
        try {
            class_1269 class_12692 = class_3102.field_1761.method_2896(class_7462, class_1268.field_5808, class_39652);
            class_7462.method_6104(class_1268.field_5808);
            this.lastClientPredict = String.valueOf(class_12692);
            this.lastActionTarget = class_23382.method_23854();
            this.lastFailReason = "\uff08\u5df2\u53d1\u5305\uff1a\u76ee\u6807 " + class_23382.method_23854() + "\uff0c\u672c\u5730\u9884\u6d4b=" + String.valueOf(class_12692) + "\uff09";
            return true;
        }
        catch (Throwable throwable) {
            this.lastFailReason = "\u53d1\u5305\u5f02\u5e38: " + throwable.getClass().getSimpleName();
            return false;
        }
    }

    private class_3965 buildHitResult(class_310 class_3102, class_2338 class_23382) {
        class_2350 class_23502;
        class_746 class_7462 = class_3102.field_1724;
        class_243 class_2432 = class_7462.method_33571();
        class_243 class_2433 = class_243.method_24953((class_2382)class_23382);
        try {
            class_23502 = class_3102.field_1687.method_17742(new class_3959(class_2432, class_2433, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)class_7462));
            if (class_23502 != null && class_23502.method_17783() == class_239.class_240.field_1332 && class_23502.method_17777().equals((Object)class_23382)) {
                return new class_3965(class_23502.method_17784(), class_23502.method_17780(), class_23382, false);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        class_23502 = this.dominantFace(class_2432, class_23382);
        class_243 class_2434 = this.surfacePoint(class_23382, class_23502);
        return new class_3965(class_2434, class_23502, class_23382, false);
    }

    private class_2350 dominantFace(class_243 class_2432, class_2338 class_23382) {
        class_243 class_2433 = class_243.method_24953((class_2382)class_23382);
        double d = class_2432.field_1352 - class_2433.field_1352;
        double d2 = class_2432.field_1351 - class_2433.field_1351;
        double d3 = class_2432.field_1350 - class_2433.field_1350;
        double d4 = Math.abs(d);
        double d5 = Math.abs(d2);
        double d6 = Math.abs(d3);
        if (d5 >= d4 && d5 >= d6) {
            return d2 >= 0.0 ? class_2350.field_11036 : class_2350.field_11033;
        }
        if (d4 >= d6) {
            return d >= 0.0 ? class_2350.field_11034 : class_2350.field_11039;
        }
        return d3 >= 0.0 ? class_2350.field_11035 : class_2350.field_11043;
    }

    private class_243 surfacePoint(class_2338 class_23382, class_2350 class_23502) {
        class_243 class_2432 = class_243.method_24953((class_2382)class_23382);
        double d = 0.4999;
        return switch (class_23502) {
            case class_2350.field_11036 -> class_2432.method_1031(0.0, d, 0.0);
            case class_2350.field_11033 -> class_2432.method_1031(0.0, -d, 0.0);
            case class_2350.field_11034 -> class_2432.method_1031(d, 0.0, 0.0);
            case class_2350.field_11039 -> class_2432.method_1031(-d, 0.0, 0.0);
            case class_2350.field_11035 -> class_2432.method_1031(0.0, 0.0, d);
            case class_2350.field_11043 -> class_2432.method_1031(0.0, 0.0, -d);
            default -> class_2432;
        };
    }

    private void faceBlock(class_746 class_7462, class_2338 class_23382) {
        try {
            class_243 class_2432 = class_7462.method_33571();
            class_243 class_2433 = class_243.method_24953((class_2382)class_23382);
            double d = class_2433.field_1352 - class_2432.field_1352;
            double d2 = class_2433.field_1351 - class_2432.field_1351;
            double d3 = class_2433.field_1350 - class_2432.field_1350;
            double d4 = Math.sqrt(d * d + d3 * d3);
            class_7462.method_36456((float)(Math.toDegrees(Math.atan2(d3, d)) - 90.0));
            class_7462.method_36457((float)(-Math.toDegrees(Math.atan2(d2, d4))));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private boolean isInReach(class_310 class_3102, class_2338 class_23382) {
        return this.eyeDistance(class_3102, class_23382) <= AfsConfig.get().reach;
    }

    private double eyeDistance(class_310 class_3102, class_2338 class_23382) {
        try {
            return class_3102.field_1724.method_33571().method_1022(class_243.method_24953((class_2382)class_23382));
        }
        catch (Throwable throwable) {
            return 999.0;
        }
    }

    private boolean ensureSeedInHand(class_310 class_3102) {
        int n;
        class_746 class_7462 = class_3102.field_1724;
        if (class_7462 == null) {
            return false;
        }
        class_1661 class_16612 = class_7462.method_31548();
        if (this.isSeed(class_7462.method_6047())) {
            return true;
        }
        for (n = 0; n < 9; ++n) {
            if (!this.isSeed(class_16612.method_5438(n))) continue;
            class_16612.method_61496(n);
            return true;
        }
        n = class_16612.method_67532();
        for (int i = 9; i < 36; ++i) {
            if (!this.isSeed(class_16612.method_5438(i)) || !this.moveToHotbar(class_3102, i, n)) continue;
            return true;
        }
        return this.isSeed(class_7462.method_6079());
    }

    public static boolean hasAnySeed(class_310 class_3102) {
        class_746 class_7462;
        if (class_3102 == null || class_3102.field_1724 == null) {
            return false;
        }
        FarmController farmController = FarmController.get();
        if (farmController.isSeed((class_7462 = class_3102.field_1724).method_6047()) || farmController.isSeed(class_7462.method_6079())) {
            return true;
        }
        class_1661 class_16612 = class_7462.method_31548();
        for (int i = 0; i < 36; ++i) {
            if (!farmController.isSeed(class_16612.method_5438(i))) continue;
            return true;
        }
        return false;
    }

    private boolean isSeed(class_1799 class_17992) {
        if (class_17992 == null || class_17992.method_7960()) {
            return false;
        }
        try {
            String string = class_17992.method_7909().method_7876();
            return string.endsWith("sugar");
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private boolean isWateringCan(class_1799 class_17992) {
        if (class_17992 == null || class_17992.method_7960()) {
            return false;
        }
        try {
            return class_17992.method_7909().method_7876().endsWith("golden_horse_armor");
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private class_1268 wateringHand(class_310 class_3102) {
        class_746 class_7462 = class_3102.field_1724;
        if (class_7462 == null) {
            return null;
        }
        if (!this.ensureCanInHand(class_3102)) {
            return null;
        }
        if (this.isWateringCan(class_7462.method_6047())) {
            return class_1268.field_5808;
        }
        if (this.isWateringCan(class_7462.method_6079())) {
            return class_1268.field_5810;
        }
        return null;
    }

    private static String canWaterHint(class_1799 class_17992) {
        try {
            int n = class_17992.method_7936();
            int n2 = class_17992.method_7919();
            if (n <= 0) {
                return "";
            }
            if (n2 >= n) {
                return "\uff08\u7f50\u8010\u4e45\u7528\u5c3d=\u7a7a\uff0c\u8bf7\u5148\u88c5\u6c34\u518d\u6309 J\uff09";
            }
            return "\uff08\u7f50\u8010\u4e45 " + (n - n2) + "/" + n + "\uff0c\u8d8a\u6ee1\u6c34\u8d8a\u591a\uff09";
        }
        catch (Throwable throwable) {
            return "";
        }
    }

    private boolean ensureCanInHand(class_310 class_3102) {
        int n;
        class_746 class_7462 = class_3102.field_1724;
        if (class_7462 == null) {
            return false;
        }
        class_1661 class_16612 = class_7462.method_31548();
        if (this.isWateringCan(class_7462.method_6047())) {
            return true;
        }
        for (n = 0; n < 9; ++n) {
            if (!this.isWateringCan(class_16612.method_5438(n))) continue;
            class_16612.method_61496(n);
            return true;
        }
        n = class_16612.method_67532();
        for (int i = 9; i < 36; ++i) {
            if (!this.isWateringCan(class_16612.method_5438(i)) || !this.moveToHotbar(class_3102, i, n)) continue;
            return true;
        }
        return this.isWateringCan(class_7462.method_6079());
    }

    public static boolean hasAnyWateringCan(class_310 class_3102) {
        class_746 class_7462;
        if (class_3102 == null || class_3102.field_1724 == null) {
            return false;
        }
        FarmController farmController = FarmController.get();
        if (farmController.isWateringCan((class_7462 = class_3102.field_1724).method_6047()) || farmController.isWateringCan(class_7462.method_6079())) {
            return true;
        }
        class_1661 class_16612 = class_7462.method_31548();
        for (int i = 0; i < 36; ++i) {
            if (!farmController.isWateringCan(class_16612.method_5438(i))) continue;
            return true;
        }
        return false;
    }

    private boolean moveToHotbar(class_310 class_3102, int n, int n2) {
        class_746 class_7462 = class_3102.field_1724;
        if (class_7462 == null || class_3102.field_1761 == null) {
            return false;
        }
        try {
            class_1723 class_17232 = class_7462.field_7498;
            int n3 = class_17232.field_7763;
            int n4 = -1;
            int n5 = -1;
            for (int i = 0; i < class_17232.field_7761.size(); ++i) {
                class_1735 class_17352 = (class_1735)class_17232.field_7761.get(i);
                if (!(class_17352.field_7871 instanceof class_1661)) continue;
                int n6 = class_17352.method_34266();
                if (n6 == n) {
                    n4 = i;
                    continue;
                }
                if (n6 != n2) continue;
                n5 = i;
            }
            if (n4 < 0 || n5 < 0) {
                return false;
            }
            class_3102.field_1761.method_2906(n3, n4, 0, class_1713.field_7790, (class_1657)class_7462);
            class_3102.field_1761.method_2906(n3, n5, 0, class_1713.field_7790, (class_1657)class_7462);
            class_3102.field_1761.method_2906(n3, n4, 0, class_1713.field_7790, (class_1657)class_7462);
            class_7462.method_31548().method_61496(n2);
            return true;
        }
        catch (Throwable throwable) {
            this.lastFailReason = "\u642c\u7269\u54c1\u5931\u8d25(\u80cc\u5305\u6ee1?): " + throwable.getClass().getSimpleName();
            return false;
        }
    }

    private static final class ClickRec {
        final boolean occupied;
        final long ms;

        ClickRec(boolean bl, long l) {
            this.occupied = bl;
            this.ms = l;
        }
    }

    private static final class Candidate {
        final class_2338 soil;
        final class_2338 above;
        final double dist;
        final double deg;
        final long key;

        Candidate(class_2338 class_23382, class_2338 class_23383, double d, double d2, long l) {
            this.soil = class_23382;
            this.above = class_23383;
            this.dist = d;
            this.deg = d2;
            this.key = l;
        }
    }

    private static final class Target {
        final class_2338 soil;
        final class_2338 crop;
        final boolean occupied;

        Target(class_2338 class_23382, class_2338 class_23383, boolean bl) {
            this.soil = class_23382;
            this.crop = class_23383;
            this.occupied = bl;
        }
    }
}

