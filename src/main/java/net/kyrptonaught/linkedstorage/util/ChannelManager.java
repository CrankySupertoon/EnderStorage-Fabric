package net.kyrptonaught.linkedstorage.util;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.LevelComponentCallback;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.level.LevelProperties;

import java.util.HashMap;

public class ChannelManager implements StorageManagerComponent {
    private HashMap<String, LinkedInventory> inventories = new HashMap<>();
    private static ComponentType<StorageManagerComponent> CHANNEL_MANAGER;

    public static void init() {
        CHANNEL_MANAGER = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(LinkedStorageMod.MOD_ID, "sman"), StorageManagerComponent.class);
        LevelComponentCallback.EVENT.register((levelProperties, components) -> components.put(CHANNEL_MANAGER, new ChannelManager()));
    }

    public static ChannelManager getManager(LevelProperties props) {
        return CHANNEL_MANAGER.get(props).getValue();
    }

    @Override
    public ChannelManager getValue() {
        return this;
    }

    public LinkedInventory getInv(DyeChannel dyeChannel) {
        String channel = dyeChannel.getChannelName();
        if (!inventories.containsKey(channel))
            inventories.put(channel, new LinkedInventory());
        return inventories.get(channel);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        inventories.clear();
        CompoundTag invs = tag.getCompound("invs");
        for (String key : invs.getKeys()) {
            inventories.put(key, fromList(invs.getCompound(key)));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag invs = new CompoundTag();
        for (String key : inventories.keySet()) {
            if (!inventories.get(key).isInvEmpty())
                invs.put(key, Inventories.toTag(new CompoundTag(), toList(inventories.get(key))));
        }
        tag.put("invs", invs);
        return tag;
    }

    private DefaultedList<ItemStack> toList(Inventory inv) {
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inv.getInvSize(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getInvSize(); i++)
            stacks.set(i, inv.getInvStack(i));
        return stacks;
    }

    private LinkedInventory fromList(CompoundTag tag) {
        LinkedInventory inventory = new LinkedInventory();
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inventory.getInvSize(), ItemStack.EMPTY);
        Inventories.fromTag(tag, stacks);
        for (int i = 0; i < stacks.size(); i++)
            inventory.setInvStack(i, stacks.get(i));
        return inventory;
    }
}