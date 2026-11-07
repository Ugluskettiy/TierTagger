package com.tiertagger;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Cloth Config ile ayar ekrani: genel ayarlar + oyuncu listesi ("isim tier [mod]"). */
public class TierConfigScreen {

    public static Screen create(Screen parent) {
        TierConfig.Data d = TierConfig.data;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Tier Tagger"))
                .setSavingRunnable(TierConfig::save);
        ConfigEntryBuilder eb = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("Genel"));
        general.addEntry(eb.startBooleanToggle(Text.literal("Etiketler acik"), d.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> d.enabled = v)
                .build());
        general.addEntry(eb.startSelector(Text.literal("Duzen (left: ikon LT1 | isim, right: isim | LT1 ikon)"),
                        TierConfig.STYLES, d.style)
                .setDefaultValue("left")
                .setSaveConsumer(v -> d.style = v)
                .build());
        general.addEntry(eb.startStrField(Text.literal("Herkese varsayilan tier (bos = kapali)"), d.defaultTier)
                .setDefaultValue("")
                .setSaveConsumer(v -> d.defaultTier = v.trim())
                .build());
        general.addEntry(eb.startSelector(Text.literal("Varsayilan mod ikonu"), TierConfig.MODES, d.defaultMode)
                .setDefaultValue("crystal")
                .setSaveConsumer(v -> d.defaultMode = v)
                .build());

        ConfigCategory players = builder.getOrCreateCategory(Text.literal("Oyuncular"));
        List<String> rows = new ArrayList<>();
        for (Map.Entry<String, String> e : d.players.entrySet()) {
            String mode = d.modes.get(e.getKey());
            rows.add(e.getKey() + " " + e.getValue() + (mode == null || mode.isBlank() ? "" : " " + mode));
        }
        players.addEntry(eb.startStrList(Text.literal("Satir: isim tier [mod]  ornek: Steve HT1 sword"), rows)
                .setExpanded(true)
                .setDefaultValue(new ArrayList<>())
                .setTooltip(Text.literal("Modlar: " + String.join(", ", TierConfig.MODES)))
                .setSaveConsumer(list -> {
                    d.players.clear();
                    d.modes.clear();
                    for (String line : list) {
                        String[] p = line.trim().split("\\s+");
                        if (p.length < 2) continue;
                        String key = p[0].toLowerCase(Locale.ROOT);
                        d.players.put(key, p[1]);
                        if (p.length >= 3) d.modes.put(key, p[2]);
                    }
                })
                .build());

        return builder.build();
    }
}
