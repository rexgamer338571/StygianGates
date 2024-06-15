package dev.ng5m.stygiangates.util;

import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder {
    private final Material material;
    private final int count;
    private String name;
    private Component[] lore;
    private Enchantment[] enchantments;
    private ItemFlag[] itemFlags;
    private String tagKey;
    private String tagValue;

    public ItemBuilder(Material material, int count) {
        this.material = material;
        this.count = count;
    }

    public ItemBuilder(Material material) {
        this.material = material;
        this.count = 1;
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(Component... lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder enchant(Enchantment... enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder flag(ItemFlag... itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        if (glow)
            this.enchantments = new Enchantment[]{new Enchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 1)};

        return this;
    }

    public ItemBuilder tag(String key, String value) {
        this.tagKey = key;
        this.tagValue = value;
        return this;
    }

    public ItemStack build() {
        ItemStack i = new ItemStack(this.material, this.count);
        ItemMeta m = i.getItemMeta();

        assert m != null;
        m.setDisplayName(this.name);

        if (this.lore != null)
            m.lore(Arrays.asList(this.lore));

        if (this.itemFlags != null)
            m.addItemFlags(this.itemFlags);

        if (this.enchantments != null) {
            for (Enchantment e : this.enchantments) {
                m.addEnchant(e.getEnchant(), e.getLevel(), true);
            }
        }

        i.setItemMeta(m);

        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(i);
        CompoundTag tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
        tag.putString(this.tagKey, tagValue);
        nmsItem.setTag(tag);

        i = nmsItem.asBukkitCopy();

        return i;
    }

    public static boolean hasTag(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        return nmsItem.hasTag() && nmsItem.getTag().contains(key);
    }
}
