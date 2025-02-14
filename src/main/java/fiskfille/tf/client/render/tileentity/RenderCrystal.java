package fiskfille.tf.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import fiskfille.tf.client.model.tileentity.ModelCrystal;
import fiskfille.tf.common.block.BlockEnergonCrystal;
import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.tileentity.TileEntityCrystal;
import fiskfille.tf.helper.TFRenderHelper;

public class RenderCrystal extends TileEntitySpecialRenderer
{
    private ModelCrystal model;

    public RenderCrystal()
    {
        model = new ModelCrystal();
        ItemRenderer itemRenderer = new ItemRenderer(Minecraft.getMinecraft());
    }

    public void renderAModelAt(TileEntityCrystal tile, double x, double y, double z, float partialTicks)
    {
        BlockEnergonCrystal block = (BlockEnergonCrystal) tile.getBlockType();
        Energon energon = block.getEnergonType();

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);
        adjustRotation(tile, x, y, z, partialTicks);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float[] rgb = TFRenderHelper.hexToRGB(energon.getColor());
        GL11.glColor4f(rgb[0], rgb[1], rgb[2], 0.5F);

        TFRenderHelper.setLighting(61680);
        model.render();
        TFRenderHelper.resetLighting();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    public void adjustRotation(TileEntityCrystal tile, double x, double y, double z, float partialTicks)
    {
        int rot = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);

        if (rot == 2)
        {
            GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
        }
        else if (rot == 3)
        {
            GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
        }
        else if (rot == 4)
        {
            GL11.glRotatef(-90, 0.0F, 1.0F, 0.0F);
        }
        else if (rot == 6)
        {
            GL11.glTranslatef(0.0F, 2.0F, 0.0F);
            GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
        }

        if (rot != 5 && rot != 6)
        {
            GL11.glTranslatef(0.6125F, 0.285F, 0.0F);
            GL11.glRotatef(55, 0.0F, 0.0F, 1.0F);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks)
    {
        renderAModelAt((TileEntityCrystal) tile, x, y, z, partialTicks);
    }
}
