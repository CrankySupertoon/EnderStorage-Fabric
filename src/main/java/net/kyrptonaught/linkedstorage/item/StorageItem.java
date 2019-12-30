package net.kyrptonaught.linkedstorage.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.kyrptonaught.linkedstorage.network.ChannelViewers;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class StorageItem extends Item {
    public StorageItem(Settings item$Settings_1) {
        super(item$Settings_1);
        Registry.register(Registry.ITEM, new Identifier(LinkedStorageMod.MOD_ID, "storageitem"), this);
        this.addPropertyGetter(new Identifier("open"), (stack, world, entity) -> {
            String channel = LinkedInventoryHelper.getChannelName(LinkedInventoryHelper.getItemChannelOrDefault(stack));
            return ChannelViewers.getViewersFor(channel) ? 1 : 0;
        });
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient) {
            PlayerEntity playerEntity = context.getPlayer();
            if (playerEntity.isSneaking() && context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof StorageBlock) {
                byte[] channel = LinkedInventoryHelper.getBlockChannel(context.getWorld(), context.getBlockPos());
                LinkedInventoryHelper.setItemChannel(channel, context.getStack());
            } else use(context.getWorld(), context.getPlayer(), context.getHand());
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getStackInHand(hand);
        if (!world.isClient) {
            byte[] channel = LinkedInventoryHelper.getItemChannelOrDefault(stack);
            ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(LinkedStorageMod.MOD_ID, "linkedstorage"), playerEntity, (buf) -> buf.writeByteArray(channel));
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        byte[] channel;
        if (LinkedInventoryHelper.itemHasChannel(itemStack_1))
            channel = LinkedInventoryHelper.getItemChannel(itemStack_1);
        else channel = LinkedInventoryHelper.getDefaultChannel();
        String name = DyeColor.byId(channel[0]).getName() + ", " + DyeColor.byId(channel[1]).getName() + ", " + DyeColor.byId(channel[2]).getName();
        list_1.add(new TranslatableText("text.linkeditem.channel", name).formatted(Formatting.GRAY));

    }
}