package net.ilexiconn.lawnchair.client.gui;

import net.ilexiconn.lawnchair.Lawnchair;
import net.ilexiconn.lawnchair.server.block.entity.FabricatorEntity;
import net.ilexiconn.lawnchair.server.container.FabricatorContainer;
import net.ilexiconn.lawnchair.server.network.ClearFabricatorMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class FabricatorGUI extends GuiContainer {
    public static final ResourceLocation TEXTURE = new ResourceLocation("lawnchair", "textures/gui/fabricator.png");

    private final RenderItem renderItem;
    private final FabricatorContainer container;

    private boolean draggingScrollbar;
    private int scroll;

    public FabricatorGUI(FabricatorContainer container) {
        super(container);
        this.mc = Minecraft.getMinecraft();
        this.renderItem = this.mc.getRenderItem();
        this.container = container;
        this.ySize = 168;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        FabricatorEntity entity = (FabricatorEntity) this.container.player.worldObj.getTileEntity(this.container.pos);
        this.fontRendererObj.drawString(I18n.format(entity.getName()), 8, 6, 0x404040);
        this.fontRendererObj.drawString(this.container.player.inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(FabricatorGUI.TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

        if (!this.container.entity.inventory.isEmpty()) {
            this.drawTexturedModalRect(x + 155, y + 17, 176, 31, 5, 7);
            if (mouseX >= x + 155 && mouseX <= x + 155 + 5 && mouseY >= y + 17 && mouseY <= y + 17 + 7 && !this.draggingScrollbar) {
                this.drawTexturedModalRect(x + 155, y + 17, 181, 31, 5, 7);
                if (Mouse.isButtonDown(0)) {
                    Lawnchair.NETWORK_WRAPPER.sendToServer(new ClearFabricatorMessage(this.container.entity));
                    this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    this.container.entity.inventory.clear();
                }
            }
        }

        if (this.container.entity.getStackInSlot(1) != null) {
            this.drawTexturedModalRect(x + 50, y + 54, 176, 15, 22, 16);
        }

        int i = 0;
        ItemStack hover = null;

        for (ItemStack stack : this.container.entity.inventory) {
            if (stack != null) {
                int stackX = x + 80 + i % 3 * 18;
                int stackY = y + 18 + i / 3 * 18 - this.scroll * 18;
                i++;
                if (stackY < 54 || stackY > 90) {
                    continue;
                }
                RenderHelper.enableGUIStandardItemLighting();
                this.renderItem.renderItemAndEffectIntoGUI(stack, stackX, stackY);
                this.renderItem.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, stack, stackX, stackY, null);
                RenderHelper.disableStandardItemLighting();
                if (mouseX >= stackX && mouseX <= stackX + 16 && mouseY >= stackY && mouseY <= stackY + 16) {
                    hover = stack;
                }
            }
        }

        int maxScroll = this.container.entity.inventory.size() > 0 ? (int) Math.max(0, Math.ceil(this.container.entity.inventory.size() / 3.0F) - 3) : 0;
        int scrollbarY = (y + 18) + (int) (maxScroll == 0 ? 0 : (((float) this.scroll / maxScroll) * (52.0F - 15.0F)));

        if (Mouse.isButtonDown(0) && mouseX >= x + 138 && mouseX <= x + 138 + 12 && mouseY >= scrollbarY && mouseY <= scrollbarY + 15) {
            this.draggingScrollbar = true;
        } else if (!Mouse.isButtonDown(0)) {
            this.draggingScrollbar = false;
        }

        if (this.draggingScrollbar) {
            this.scroll = (int) ((mouseY - (y + 18)) / (52.0F - 15.0F) * maxScroll);
        }

        int scrollWheel = Mouse.getDWheel();
        if (scrollWheel > 0) {
            this.scroll--;
        } else if (scrollWheel < 0) {
            this.scroll++;
        }

        this.scroll = Math.max(0, Math.min(maxScroll, this.scroll));

        this.mc.getTextureManager().bindTexture(FabricatorGUI.TEXTURE);
        if (maxScroll > 0) {
            this.drawTexturedModalRect(x + 138, scrollbarY, 176, 0, 12, 15);
        } else {
            this.drawTexturedModalRect(x + 138, scrollbarY, 188, 0, 12, 15);
        }

        if (hover != null) {
            this.renderToolTip(hover, mouseX, mouseY);
        }
    }
}
