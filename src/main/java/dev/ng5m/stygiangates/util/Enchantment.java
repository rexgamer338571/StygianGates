package dev.ng5m.stygiangates.util;

public class Enchantment {
    private final org.bukkit.enchantments.Enchantment enchantment;
    private final int level;

    public Enchantment(org.bukkit.enchantments.Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public org.bukkit.enchantments.Enchantment getEnchant() {
        return this.enchantment;
    }

    public int getLevel() {
        return this.level;
    }
}
