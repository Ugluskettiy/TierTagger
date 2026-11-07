package com.tiertagger;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

import java.util.Locale;

public class TierTaggerClient implements ClientModInitializer {
    private static void put(String name, String tier, String mode) {
        String key = name.toLowerCase(Locale.ROOT);
        TierConfig.data.players.put(key, tier);
        if (mode != null) TierConfig.data.modes.put(key, mode);
        TierConfig.save();
    }

    @Override
    public void onInitializeClient() {
        TierConfig.load();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommandManager.literal("tiertagger")
                .then(ClientCommandManager.literal("set")
                    .then(ClientCommandManager.argument("oyuncu", StringArgumentType.word())
                        .then(ClientCommandManager.argument("tier", StringArgumentType.word())
                            .executes(ctx -> {
                                String name = StringArgumentType.getString(ctx, "oyuncu");
                                String tier = StringArgumentType.getString(ctx, "tier");
                                put(name, tier, null);
                                ctx.getSource().sendFeedback(Text.literal("§a" + name + " -> " + tier.toUpperCase(Locale.ROOT)));
                                return 1;
                            })
                            .then(ClientCommandManager.argument("mod", StringArgumentType.word())
                                .suggests((c, b) -> {
                                    for (String m : TierConfig.MODES) b.suggest(m);
                                    return b.buildFuture();
                                })
                                .executes(ctx -> {
                                    String name = StringArgumentType.getString(ctx, "oyuncu");
                                    String tier = StringArgumentType.getString(ctx, "tier");
                                    String mod = StringArgumentType.getString(ctx, "mod");
                                    put(name, tier, mod);
                                    ctx.getSource().sendFeedback(Text.literal("§a" + name + " -> " + tier.toUpperCase(Locale.ROOT) + " (" + mod + ")"));
                                    return 1;
                                })))))
                .then(ClientCommandManager.literal("remove")
                    .then(ClientCommandManager.argument("oyuncu", StringArgumentType.word())
                        .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "oyuncu").toLowerCase(Locale.ROOT);
                            TierConfig.data.players.remove(name);
                            TierConfig.data.modes.remove(name);
                            TierConfig.save();
                            ctx.getSource().sendFeedback(Text.literal("§e" + name + " etiketi kaldirildi"));
                            return 1;
                        })))
                .then(ClientCommandManager.literal("default")
                    .then(ClientCommandManager.argument("tier", StringArgumentType.word())
                        .executes(ctx -> {
                            String tier = StringArgumentType.getString(ctx, "tier");
                            TierConfig.data.defaultTier = tier.equalsIgnoreCase("off") ? "" : tier;
                            TierConfig.save();
                            ctx.getSource().sendFeedback(Text.literal("§aVarsayilan tier: " + (TierConfig.data.defaultTier.isEmpty() ? "kapali" : TierConfig.data.defaultTier.toUpperCase(Locale.ROOT))));
                            return 1;
                        })))
                .then(ClientCommandManager.literal("style")
                    .then(ClientCommandManager.argument("duzen", StringArgumentType.word())
                        .suggests((c, b) -> b.suggest("left").suggest("right").buildFuture())
                        .executes(ctx -> {
                            String s = StringArgumentType.getString(ctx, "duzen").toLowerCase(Locale.ROOT);
                            TierConfig.data.style = s.equals("right") ? "right" : "left";
                            TierConfig.save();
                            ctx.getSource().sendFeedback(Text.literal("§aDuzen: " + TierConfig.data.style));
                            return 1;
                        })))
                .then(ClientCommandManager.literal("toggle")
                    .executes(ctx -> {
                        TierConfig.data.enabled = !TierConfig.data.enabled;
                        TierConfig.save();
                        ctx.getSource().sendFeedback(Text.literal(TierConfig.data.enabled ? "§aAcik" : "§cKapali"));
                        return 1;
                    }))
                .then(ClientCommandManager.literal("list")
                    .executes(ctx -> {
                        if (TierConfig.data.players.isEmpty()) {
                            ctx.getSource().sendFeedback(Text.literal("§7Kayitli etiket yok."));
                        } else {
                            TierConfig.data.players.forEach((p, t) ->
                                ctx.getSource().sendFeedback(Text.literal("§f" + p + " §7-> §e" + t.toUpperCase(Locale.ROOT))));
                        }
                        return 1;
                    }))
            ));
    }
}
