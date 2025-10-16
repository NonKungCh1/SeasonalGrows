package com.nonkungch.seasonalgrows;

import com.dynamicsurvival.api.DynamicSurvivalAPI;
import com.dynamicsurvival.api.Season;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigManager {

    private final Main plugin;
    private boolean allowUnlistedCrops;
    private final EnumMap<Season, Set<Material>> growthRules = new EnumMap<>(Season.class);

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadConfigValues();
    }

    public void loadConfigValues() {
        plugin.reloadConfig();
        this.allowUnlistedCrops = plugin.getConfig().getBoolean("allow-unlisted-crops-to-grow", true);

        growthRules.clear();
        ConfigurationSection seasonsSection = plugin.getConfig().getConfigurationSection("seasons");
        if (seasonsSection == null) {
            plugin.getLogger().warning("'seasons' section not found in config.yml! No rules will be applied.");
            return;
        }

        for (String seasonKey : seasonsSection.getKeys(false)) {
            try {
                Season season = Season.valueOf(seasonKey.toUpperCase());
                List<String> cropNames = seasonsSection.getStringList(seasonKey);

                Set<Material> allowedCrops = cropNames.stream()
                        .map(materialName -> {
                            try {
                                return Material.valueOf(materialName.toUpperCase());
                            } catch (IllegalArgumentException e) {
                                plugin.getLogger().warning("Invalid material name in config: '" + materialName + "'");
                                return null;
                            }
                        })
                        .filter(material -> material != null && material.isBlock())
                        .collect(Collectors.toSet());

                growthRules.put(season, allowedCrops);

            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid season name in config: '" + seasonKey + "'");
            }
        }
    }

    public boolean canCropGrow(Material cropType, Season currentSeason) {
        Set<Material> allowedCrops = growthRules.get(currentSeason);

        if (allowedCrops == null) {
            // ถ้าฤดูนั้นไม่ได้ถูกตั้งค่าไว้เลย ให้ยึดตามค่า default
            return allowUnlistedCrops;
        }

        // ถ้าพืชอยู่ในรายการของฤดูนั้นๆ จะโตได้เสมอ
        // ถ้าไม่อยู่ ให้ตัดสินตามค่า allow-unlisted-crops-to-grow
        return allowedCrops.contains(cropType) || allowUnlistedCrops;
    }
      }
                  
