package net.kyrptonaught.linkedstorage.block;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

public class StorageBlockEntity extends BlockEntity {
    private int channel = 0;

    private StorageBlockEntity(BlockEntityType<?> blockEntityType_1) {
        super(blockEntityType_1);
    }

    StorageBlockEntity() {
        this(StorageBlock.blockEntity);
    }

    public void fromTag(CompoundTag compoundTag_1) {
        super.fromTag(compoundTag_1);
        if (compoundTag_1.containsKey("channel"))
            this.channel = compoundTag_1.getInt("channel");
    }

    public CompoundTag toTag(CompoundTag compoundTag_1) {
        super.toTag(compoundTag_1);
        compoundTag_1.putInt("channel", channel);
        return compoundTag_1;
    }

    public void setChannel(int channel) {
        this.channel = channel;
        this.markDirty();
    }

    public int getChannel() {
        return channel;
    }

    public void resetChannel() {
        this.channel = 0;
    }
}
