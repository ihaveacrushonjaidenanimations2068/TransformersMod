package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.container.ContainerAssemblyTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAssemblyTable extends GuiContainer
{
    private ResourceLocation texture = new ResourceLocation(TransformersMod.modid, "textures/gui/container/assembly_table.png");

    public GuiAssemblyTable(InventoryPlayer inventoryPlayer, World world, int x, int y, int z)
    {
        super(new ContainerAssemblyTable(inventoryPlayer, world, x, y, z));
        this.ySize = 222;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;

        String s = StatCollector.translateToLocal("gui.assembly_table");
        fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
    }
}
