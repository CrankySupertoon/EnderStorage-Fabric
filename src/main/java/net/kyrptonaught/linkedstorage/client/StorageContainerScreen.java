package net.kyrptonaught.linkedstorage.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class StorageContainerScreen extends ContainerScreen<Container> {
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/generic_54.png");
    private final int rows = 3;
    public byte[] channel;

    public StorageContainerScreen(Container container, PlayerInventory inventory, byte[] channel) {
        super(container, inventory, new TranslatableText("container.linkedstorage"));
        this.passEvents = true;
        this.containerHeight = 114 + this.rows * 18;
        this.channel = channel;
    }

    public void render(int int_1, int int_2, float float_1) {
        this.renderBackground();
        super.render(int_1, int_2, float_1);
        this.drawMouseoverTooltip(int_1, int_2);
    }

    protected void drawForeground(int int_1, int int_2) {
        this.font.draw(this.title.asFormattedString(), 8.0F, 6.0F, 4210752);
        this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float) (this.containerHeight - 96 + 2), 4210752);
    }

    protected void drawBackground(float float_1, int int_1, int int_2) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int int_3 = (this.width - this.containerWidth) / 2;
        int int_4 = (this.height - this.containerHeight) / 2;
        this.blit(int_3, int_4, 0, 0, this.containerWidth, this.rows * 18 + 17);
        this.blit(int_3, int_4 + this.rows * 18 + 17, 0, 126, this.containerWidth, 96);
    }
}