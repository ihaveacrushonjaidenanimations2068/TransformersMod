package fiskfille.tf.client.render.entity;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.transformer.ModelTransformer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTransformer extends RenderLiving
{
    public RenderTransformer()
    {
        super(new ModelTransformer(), 0.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return new ResourceLocation(TransformersMod.modid + ":textures/models/purge/purge.png");
    }
}
