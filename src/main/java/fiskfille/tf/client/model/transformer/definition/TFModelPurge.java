package fiskfille.tf.client.model.transformer.definition;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.transformer.ModelPurge;
import fiskfille.tf.client.model.transformer.ModelTransformerBase;
import fiskfille.tf.client.model.transformer.vehicle.ModelPurgeVehicle;
import fiskfille.tf.client.model.transformer.vehicle.ModelVehicleBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TFModelPurge extends TransformerModel
{
    private ModelPurge model;
    private ModelPurge modelItem;
    private ModelPurgeVehicle vehicle;

    public TFModelPurge()
    {
        model = new ModelPurge();
        modelItem = new ModelPurge();
        vehicle = new ModelPurgeVehicle();
    }

    @Override
    public ModelTransformerBase getMainModel()
    {
        return model;
    }

    @Override
    public ModelVehicleBase getVehicleModel()
    {
        return vehicle;
    }

    @Override
    public ModelRenderer getLowerArm()
    {
        return model.lowerArmR;
    }

    @Override
    public ModelRenderer getUpperArm()
    {
        return model.upperArmR;
    }

    @Override
    public ModelRenderer getBody()
    {
        return model.chestplate1;
    }

    @Override
    public ModelRenderer getHead()
    {
        return model.headbase;
    }

    @Override
    public void renderItem(EntityPlayer player, ItemStack stack)
    {
        GL11.glTranslatef(0.05F, 0.0F, 0.1F);
    }

    @Override
    public void renderCape(EntityPlayer player)
    {
        GL11.glTranslatef(0, -0.2F, 0.1F);
    }

    @Override
    public void renderFirstPersonArm(EntityPlayer player)
    {
        GL11.glTranslatef(0, 0.05F, 0.15F);
    }

    @Override
    public ResourceLocation getTexture(Entity entity)
    {
        return new ResourceLocation(TransformersMod.modid, "textures/models/purge/purge.png");
    }

    @Override
    public String getTextureDir()
    {
        return "purge/purge";
    }

    @Override
    public ModelTransformerBase getItemInventoryModel()
    {
        return modelItem;
    }
}
