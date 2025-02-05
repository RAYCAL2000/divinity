package studio.magemonkey.divinity.modules.list.augments;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.magemonkey.codex.config.api.JYML;
import studio.magemonkey.codex.hooks.external.VaultHK;
import studio.magemonkey.codex.manager.api.gui.ContentType;
import studio.magemonkey.codex.manager.api.gui.GuiClick;
import studio.magemonkey.codex.manager.api.gui.GuiItem;
import studio.magemonkey.codex.manager.api.gui.NGUI;
import studio.magemonkey.codex.util.ItemUT;
import studio.magemonkey.codex.util.NumberUT;
import studio.magemonkey.divinity.Divinity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;


class AugmentsGUI extends NGUI<Divinity> {

    private final int[] equipmentSlots = {0,1};
    Set<Integer> contentSlots;    
    private final JYML cfg;

    public AugmentsGUI(@NotNull AugmentsManager augmentsManager) {
        super(augmentsManager.plugin, augmentsManager.getJYML(), "gui.");

        cfg  = augmentsManager.getJYML();
        String path = "gui.";
        contentSlots = new HashSet<Integer>();

        // Sets the size of the GUI to a multiple of 9.
        this.setSize(Math.min(54, ((cfg.getInt(path+"size",54) + 8) / 9) * 9));
        // Sets the title of the GUI.
        this.setTitle(cfg.getString(path+"title", "&4&l<&4&nAugments&4&l>"));

        // GuiClick click = (p, type, e) -> {
        //     if (type == ContentType.ACCEPT) {
        //         Inventory              inv        = e.getInventory();
        //         Map<ItemStack, Double> priceMap   = new HashMap<>();
        //         double                 priceTotal = 0;

        //         for (int slot : itemSlots) {
        //             ItemStack target = inv.getItem(slot);
        //             if (target == null) continue;

        //             double price = plugin.getWorthManager().getItemWorth(target);
        //             if (price <= 0) {
        //                 ItemUT.addItem(p, target);
        //             } else {
        //                 priceTotal += price;
        //                 priceMap.computeIfAbsent(target, price2 -> 0D);
        //                 priceMap.computeIfPresent(target, (itemKey, priceVal) -> priceVal + price);
        //                 //priceMap.compute(target, (itemKey, itemPrice) -> priceMap.computeIfAbsent(target, price2 -> 0D) + price);
        //             }
        //         }
        //         inv.setContents(new ItemStack[]{});

        //         p.closeInventory();
        //     } else if (type == ContentType.EXIT) {
        //         p.closeInventory();
        //     }
        // };

        // Add Content Items
        for (String itemId : cfg.getSection(path + "content")) {
            GuiItem contentItem = cfg.getGuiItem(path + "content." + itemId, ContentType.class);
            if (contentItem == null) continue;
            contentSlots.addAll(Arrays.stream(contentItem.getSlots()).boxed().collect(Collectors.toSet()));
            this.addButton(contentItem);
        }
    
        // Add Augment Slots
        // Overrides any Content Items

    }

    @Override
    protected void onCreate(@NotNull Player player, @NotNull Inventory inv, int page) {
        // Fill GUI with Default Items
        String path = "gui.";
        for (String itemId : cfg.getSection(path + "equipment")) {
            GuiItem equipmentItem = cfg.getGuiItem(path + "equipment." + itemId, ContentType.class);
            // this.equipmentSlots.addAll(equipmentItem.getSlots());
            if (equipmentItem == null) continue;
            this.addButton(equipmentItem);
        }
        // Override Default Items with Player Items
        player.sendMessage("Opening Augments GUI!");
    }

    @Override
    protected void onReady(@NotNull Player player, @NotNull Inventory inv, int page) {
        super.onReady(player, inv, page);
        this.update(inv);
    }

    @Override
    protected boolean ignoreNullClick() {
        return false;
    }

    @Override
    protected boolean cancelClick(int slot) {
        // Cancel Click if Content Item
        if (contentSlots.contains(slot)){ return true; }
        // Cancel Click if Default Equipment Item

        // Do Not Cancel Click otherwise (Player Item)
        return false;
    }

    @Override
    protected boolean cancelPlayerClick() {
        return false;
    }

    @Override
    public void click(Player player, @Nullable ItemStack item, int slot, InventoryClickEvent e) {
        // Recalc items price only if items are being added or removed from the GUI.
        // Delay needs because event called before the item is added/taken.
        if (!this.cancelClick(slot)) {
            plugin.getServer().getScheduler().runTask(plugin, () -> this.update(e.getInventory()));
        }

        super.click(player, item, slot, e);
    }

    @Override
    public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        for (int slot : this.equipmentSlots) {
            ItemStack item = inv.getItem(slot);
            if (item != null) {
                ItemUT.addItem(player, item);
            }
        }
    }

    // Replace placeholders on default GUI items.
    // As these items are loaded into JGUI database,
    // we may just replace them in their slots.
    private void update(@NotNull Inventory inv) {
        // for (GuiItem guiItem : this.getContent().values()) {
        //     ItemStack item = guiItem.getItem();
        //     ItemMeta  meta = item.getItemMeta();
        //     if (meta == null) continue;

        //     if (meta.hasDisplayName()) {
        //     }

        //     List<String> lore = meta.getLore();
        //     if (lore != null) {
        //         meta.setLore(lore);
        //     }
        //     item.setItemMeta(meta);

        //     for (int i : guiItem.getSlots()) {
        //         inv.setItem(i, item);
        //     }
        // }
    }

}
