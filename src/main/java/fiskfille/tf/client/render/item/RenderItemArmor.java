package fiskfille.tf.client.render.item;

import fiskfille.tf.client.model.transformer.ModelTransformerBase;
import fiskfille.tf.helper.TFArmorDyeHelper;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import fiskfille.tf.TransformersAPI;
import fiskfille.tf.client.model.transformer.definition.TFModelRegistry;
import fiskfille.tf.client.model.transformer.definition.TransformerModel;
import fiskfille.tf.client.model.transformer.vehicle.ModelVehicleBase;
import fiskfille.tf.common.transformer.base.Transformer;

public class RenderItemArmor implements IItemRenderer
{
    private Transformer transformer;
    private int armorPiece;

    public RenderItemArmor(Transformer transformer, int armorPiece)
    {
        this.armorPiece = armorPiece;
        this.transformer = transformer;
    }

    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        TransformerModel tfModel = TFModelRegistry.getModel(transformer);
        ModelTransformerBase model = tfModel.getItemInventoryModel();

        switch (armorPiece)
        {
            case 0:
                GL11.glScalef(1.5F, 1.5F, 1.5F);
                break;
            case 1:
                GL11.glScalef(0.8F, 0.8F, 0.8F);
                break;
            case 2:
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                break;
            case 3:
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                break;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        Minecraft.getMinecraft().renderEngine.bindTexture(tfModel.getTexture(null));
        renderArmor(type, model);

        if (TFArmorDyeHelper.isDyed(item))
        {
            float[] primaryColor = TFHelper.hexToRGB(TFArmorDyeHelper.getPrimaryColor(item));
            float[] secondaryColor = TFHelper.hexToRGB(TFArmorDyeHelper.getSecondaryColor(item));

            GL11.glColor4f(primaryColor[0], primaryColor[1], primaryColor[2], 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(tfModel.getTextureDirPrefix(), "textures/models/" + tfModel.getTextureDir() + "_primary.png"));
            renderArmor(type, model);

            GL11.glColor4f(secondaryColor[0], secondaryColor[1], secondaryColor[2], 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(tfModel.getTextureDirPrefix(), "textures/models/" + tfModel.getTextureDir() + "_secondary.png"));
            renderArmor(type, model);
        }
    }

    private void renderArmor(ItemRenderType type, ModelTransformerBase model)
    {
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
            GL11.glPushMatrix();
            GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(210, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(10, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.9F, -1.0F, 0.2F);

            if (armorPiece == 1)
            {
                GL11.glTranslatef(0.0F, -0.5F, 0.0F);
            }
            else if (armorPiece == 0)
            {
                GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            }

            float scale = 1.0F;
            GL11.glScalef(scale, scale, scale);
            model.renderArmorPiece(armorPiece);
            GL11.glPopMatrix();
        }
        else if (type == ItemRenderType.EQUIPPED)
        {
            GL11.glPushMatrix();
            GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-45, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-45, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.5F, -0.5F, 0.0F);

            if (armorPiece == 1)
            {
                GL11.glTranslatef(-0.2F, 0.1F, 0.0F);
            }
            else if (armorPiece == 2)
            {
                GL11.glTranslatef(-0.1F, 0.4F, -0.2F);
                GL11.glRotatef(35, 0.0F, 0.0F, 1.0F);
            }
            else if (armorPiece == 3)
            {
                GL11.glTranslatef(-0.2F, 0.3F, -0.3F);
                GL11.glRotatef(35, 0.0F, 0.0F, 1.0F);
            }

            float scale = 0.7F;
            GL11.glScalef(scale, scale, scale);
            model.renderArmorPiece(armorPiece);
            GL11.glPopMatrix();
        }
        else if (type == ItemRenderType.INVENTORY)
        {
            GL11.glPushMatrix();
            GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(-0.35F, -0.3F, 0.0F);

            if (armorPiece == 0)
            {
                GL11.glTranslatef(0.25F, 0.6F, 0.1F);
            }
            else if (armorPiece == 1)
            {
                GL11.glTranslatef(0.1F, 0.0F, 0.0F);
            }
            else if (armorPiece == 2)
            {
                GL11.glTranslatef(0.2F, -0.3F, 0.0F);
            }
            else if (armorPiece == 3)
            {
                GL11.glTranslatef(0.2F, -0.3F, 0.0F);
            }

            float scale = 1.5F;
            GL11.glScalef(scale, scale, scale);
            model.renderArmorPiece(armorPiece);
            GL11.glPopMatrix();
        }
        else if (type == ItemRenderType.ENTITY)
        {
            GL11.glPushMatrix();
            GL11.glRotatef(180, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-90, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0F, -1.0F, 0.1F);

            if (armorPiece == 0)
            {
                GL11.glTranslatef(0.0F, 1.1F, -0.25F);
            }
            else if (armorPiece == 2)
            {
                GL11.glTranslatef(0.0F, 0.4F, 0.0F);
            }
            else if (armorPiece == 3)
            {
                GL11.glTranslatef(0.0F, 0.25F, 0.0F);
            }
            else if (armorPiece == 1)
            {
                GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            }

            float scale = 1.0F;
            GL11.glScalef(scale, scale, scale);
            model.renderArmorPiece(armorPiece);
            GL11.glPopMatrix();
        }
    }
}