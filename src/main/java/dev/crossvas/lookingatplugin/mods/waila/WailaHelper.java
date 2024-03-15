package dev.crossvas.lookingatplugin.mods.waila;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Map;

public class WailaHelper {

    public static Map<String, String> MAPPED_LANG = new Object2ObjectLinkedOpenHashMap<>();
    public static List<Component> SPEED_COMP = new ObjectArrayList<>();

    static {
        MAPPED_LANG.put("ic2.probe.eu.storage.full.name", of("energy"));
        MAPPED_LANG.put("ic2.probe.barrel.beer.sugar_cane.name", of("sugar_cane"));
        MAPPED_LANG.put("ic2.probe.barrel.whisky.grist.name", of("grist"));
        MAPPED_LANG.put("ic2.probe.barrel.whisky.years.name", of("years"));
        MAPPED_LANG.put("ic2.probe.barrel.beer.redstone.name", of("redstone"));
        MAPPED_LANG.put("ic2.probe.barrel.beer.glowstone.name", of("glowstone"));
        MAPPED_LANG.put("ic2.probe.barrel.beer.wheat.name", of("wheat"));
        MAPPED_LANG.put("ic2.probe.barrel.beer.hops.name", of("hops"));
        MAPPED_LANG.put("ic2.probe.progress.material.name", of("material"));
        MAPPED_LANG.put("info.block.ic2.induction_furnace.heat", "info.block.ic2.induction_furnace.heat");
        MAPPED_LANG.put("info.block.ic2.rotary_macerator.speed", "info.block.ic2.rotary_macerator.speed");
        MAPPED_LANG.put("info.block.ic2.singularity_compressor.pressure", "info.block.ic2.singularity_compressor.pressure");
        MAPPED_LANG.put("info.block.ic2.centrifugal_extractor.speed", "info.block.ic2.centrifugal_extractor.speed");
        MAPPED_LANG.put("info.block.ic2.compacting_recycler.speed", "info.block.ic2.compacting_recycler.speed");
        MAPPED_LANG.put("info.block.ic2.rare_earth_centrifuge.speed", "info.block.ic2.rare_earth_centrifuge.speed");
        MAPPED_LANG.put("ic2.probe.progress.full.name", of("progress"));
        MAPPED_LANG.put("ic2.probe.crop.info.stage", of("crop.stage"));
        MAPPED_LANG.put("ic2.probe.crop.info.points", of("crop.points"));
        MAPPED_LANG.put("ic2.probe.crop.info.growth", of("crop.growth"));
        MAPPED_LANG.put("ic2.probe.crop.info.gain", of("crop.gain"));
        MAPPED_LANG.put("ic2.probe.crop.info.resistance", of("crop.resistance"));
        MAPPED_LANG.put("ic2.probe.crop.info.needs", of("crop.needs"));
        MAPPED_LANG.put("ic2.probe.crop.info.scan", of("crop.scan"));
        MAPPED_LANG.put("ic2.probe.crop.info.fertilizer", of("crop.fertilizer"));
        MAPPED_LANG.put("ic2.probe.crop.info.water", of("crop.water"));
        MAPPED_LANG.put("ic2.probe.crop.info.weedex", of("crop.weedex"));
        MAPPED_LANG.put("ic2.probe.crop.info.nutrients", of("crop.nutrients"));
        MAPPED_LANG.put("ic2.probe.crop.info.humidity", of("crop.humidity"));
        MAPPED_LANG.put("ic2.probe.crop.info.env", of("crop.env"));
        MAPPED_LANG.put("ic2.probe.crop.info.light", of("crop.light"));
    }

    public static Component getWailaComp(Component from) {
        Component toReturn = from;
        JsonObject json = Component.Serializer.toJsonTree(from).getAsJsonObject();
        if (json.has("translate")) {
            String key = json.get("translate").getAsString();
            if (!key.isEmpty()) {
                String newKey = MAPPED_LANG.getOrDefault(key, "");
                if (!newKey.isEmpty()) {
                    toReturn = Component.translatable(newKey);
                }
                if (newKey.equals(key)) {
                    SPEED_COMP.add(from);
                }
            }
        } else {
            // Return an empty component, meaning it has no translation key
            // these components we create them later for Waila to display using the current / max values
            return Component.empty();
        }
        return toReturn;
    }

    public static String of(String info) {
        return "waila.info." + info;
    }
}
