package com.tiertagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/** Tamamen yerel (istemci) ayar: .minecraft/config/tiertagger.json (Mojang isimleri, 26.x) */
public class TierConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("tiertagger.json");

    public static final String[] MODES = {"none", "crystal", "sword", "axe", "pickaxe", "uhc", "pot", "netherite", "mace"};
    public static final String[] STYLES = {"left", "right"};

    public static class Data {
        public boolean enabled = true;
        public String defaultTier = "";
        public String defaultMode = "crystal";
        public String style = "left";
        public Map<String, String> players = new LinkedHashMap<>();
        public Map<String, String> modes = new LinkedHashMap<>();
    }

    public static Data data = new Data();

    public static void load() {
        if (!Files.exists(FILE)) {
            save();
            return;
        }
        try (Reader r = Files.newBufferedReader(FILE)) {
            Data d = GSON.fromJson(r, Data.class);
            if (d != null) data = d;
            if (data.players == null) data.players = new LinkedHashMap<>();
            if (data.modes == null) data.modes = new LinkedHashMap<>();
            if (data.style == null) data.style = "left";
            if (data.defaultMode == null) data.defaultMode = "crystal";
        } catch (Exception e) {
            System.err.println("[TierTagger] Ayar okunamadi: " + e.getMessage());
        }
    }

    public static void save() {
        try (Writer w = Files.newBufferedWriter(FILE)) {
            GSON.toJson(data, w);
        } catch (IOException e) {
            System.err.println("[TierTagger] Ayar yazilamadi: " + e.getMessage());
        }
    }

    public static String tierFor(String playerName) {
        if (!data.enabled || playerName == null) return null;
        String t = data.players.get(playerName.toLowerCase(Locale.ROOT));
        if (t == null || t.isBlank()) t = data.defaultTier;
        return (t == null || t.isBlank()) ? null : t;
    }

    private static ChatFormatting colorFor(String tier) {
        String t = tier.toUpperCase(Locale.ROOT);
        if (t.endsWith("1")) return ChatFormatting.GOLD;
        if (t.endsWith("2")) return ChatFormatting.YELLOW;
        if (t.endsWith("3")) return ChatFormatting.GREEN;
        if (t.endsWith("4")) return ChatFormatting.AQUA;
        if (t.endsWith("5")) return ChatFormatting.GRAY;
        return ChatFormatting.WHITE;
    }

    private static String iconChar(String mode) {
        if (mode == null) return null;
        switch (mode.toLowerCase(Locale.ROOT)) {
            case "crystal": return "";
            case "sword": return "";
            case "axe": return "";
            case "pickaxe": return "";
            case "uhc": case "heart": return "";
            case "pot": case "potion": return "";
            case "netherite": case "nethpot": case "shield": return "";
            case "mace": return "";
            default: return null;
        }
    }

    private static Component lit(String s, ChatFormatting f) {
        return Component.literal(s).withStyle(f);
    }

    /** left: [ikon] LT1 | Isim   right: Isim | LT1 [ikon]. Cift etiketi engeller. */
    public static Component decorate(Component original, String playerName) {
        String tier = tierFor(playerName);
        if (tier == null || original == null) return original;
        if (original.getString().contains(" | ")) return original;

        String key = playerName.toLowerCase(Locale.ROOT);
        String mode = data.modes.get(key);
        if (mode == null || mode.isBlank()) mode = data.defaultMode;
        String icon = iconChar(mode);

        String t = tier.toUpperCase(Locale.ROOT);
        MutableComponent out = Component.empty();
        if ("right".equalsIgnoreCase(data.style)) {
            out.append(original).append(lit(" | ", ChatFormatting.DARK_GRAY)).append(lit(t, colorFor(t)));
            if (icon != null) out.append(lit(" ", ChatFormatting.WHITE)).append(lit(icon, ChatFormatting.WHITE));
        } else {
            if (icon != null) out.append(lit(icon, ChatFormatting.WHITE)).append(lit(" ", ChatFormatting.WHITE));
            out.append(lit(t, colorFor(t))).append(lit(" | ", ChatFormatting.DARK_GRAY)).append(original);
        }
        return out;
    }

    /** Kendi isim etiketini ucuncu sahis (F5) gorunumunde cizdirir; vanilla bunu gizler. */
    public static void selfLabel(Object entity, CallbackInfoReturnable<Boolean> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || entity != mc.player) return;
        if (tierFor(mc.player.getName().getString()) == null) return;
        if (mc.options.getCameraType().isFirstPerson()) return;
        cir.setReturnValue(true);
    }

    public static String profileName(Object profile) {
        if (profile == null) return null;
        for (String m : new String[]{"name", "getName"}) {
            try {
                Object v = profile.getClass().getMethod(m).invoke(profile);
                if (v instanceof String s) return s;
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return null;
    }
}
