package fiskfille.tf.client.model.transformer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import fiskfille.tf.client.event.ClientEventHandler;
import fiskfille.tf.common.data.TFDataManager;
import fiskfille.tf.common.item.TFItems;

public class ModelChildBase
{
    public static class Base extends ModelBase
    {
        public float pi = (float) Math.PI;

        protected void addChildTo(ModelRenderer child, ModelRenderer parent)
        {
            child.rotationPointX -= parent.rotationPointX;
            child.rotationPointY -= parent.rotationPointY;
            child.rotationPointZ -= parent.rotationPointZ;

            child.rotateAngleX -= parent.rotateAngleX;
            child.rotateAngleY -= parent.rotateAngleY;
            child.rotateAngleZ -= parent.rotateAngleZ;
            parent.addChild(child);
        }

        protected void addChildToWithoutPoint(ModelRenderer child, ModelRenderer parent)
        {
            child.rotateAngleX -= parent.rotateAngleX;
            child.rotateAngleY -= parent.rotateAngleY;
            child.rotateAngleZ -= parent.rotateAngleZ;
            parent.addChild(child);
        }
    }

    public static class Biped extends ModelBiped
    {
        public float pi = (float) Math.PI;

        protected void addChildTo(ModelRenderer child, ModelRenderer parent)
        {
            child.rotationPointX -= parent.rotationPointX;
            child.rotationPointY -= parent.rotationPointY;
            child.rotationPointZ -= parent.rotationPointZ;

            child.rotateAngleX -= parent.rotateAngleX;
            child.rotateAngleY -= parent.rotateAngleY;
            child.rotateAngleZ -= parent.rotateAngleZ;

            parent.addChild(child);
        }

        protected void addChildToWithoutPoint(ModelRenderer child, ModelRenderer parent)
        {
            child.rotateAngleX -= parent.rotateAngleX;
            child.rotateAngleY -= parent.rotateAngleY;
            child.rotateAngleZ -= parent.rotateAngleZ;
            parent.addChild(child);
        }

        protected void setRotation(ModelRenderer model, float x, float y, float z)
        {
            model.rotateAngleX = x;
            model.rotateAngleY = y;
            model.rotateAngleZ = z;
        }

        protected void setPos(ModelRenderer model, float x, float y, float z)
        {
            model.rotationPointX = x;
            model.rotationPointY = y;
            model.rotationPointZ = z;
        }

        protected void applyDefaultHittingAnimation(ModelRenderer upperArmR, ModelRenderer upperArmL, ModelRenderer head, ModelRenderer chest, ModelRenderer lowerArmR, ModelRenderer lowerArmL)
        {
            if (onGround > -9990.0F)
            {
                float hitAnimation = onGround;

                float change = MathHelper.sin(MathHelper.sqrt_float(hitAnimation) * (float) Math.PI * 2.0F) * 0.2F;
                chest.rotateAngleY += change;
                head.rotateAngleY -= change;

                upperArmR.rotateAngleY += change * 0.5;
                upperArmL.rotateAngleY += change * 0.5;
                upperArmL.rotateAngleX += change * 0.5;

                lowerArmR.rotateAngleY += change * 0.5;
                lowerArmL.rotateAngleY += change * 0.5;
                lowerArmL.rotateAngleX += change * 0.5;

                hitAnimation = 1.0F - onGround;
                hitAnimation *= hitAnimation;
                hitAnimation *= hitAnimation;
                hitAnimation = 1.0F - hitAnimation;
                float f7 = MathHelper.sin(hitAnimation * (float) Math.PI);
                float f8 = MathHelper.sin(onGround * (float) Math.PI) * -(head.rotateAngleX - 0.7F) * 0.75F;

                float armRXChange = (float) (upperArmR.rotateAngleX - (f7 * 1.2D + f8)) * 0.5F;
                float armRZChange = MathHelper.sin(onGround * (float) Math.PI) * -0.4F * 0.5F;

                upperArmR.rotateAngleX += armRXChange;
                upperArmR.rotateAngleY += change * 2.0F * 0.5;
                upperArmR.rotateAngleZ += armRZChange;

                lowerArmR.rotateAngleX += armRXChange;
                lowerArmR.rotateAngleY += change * 2.0F * 0.5;
                lowerArmR.rotateAngleZ += armRZChange;
            }

        }

        protected void applyDefaultHoldingAnimation(ModelRenderer upperArmR, ModelRenderer upperArmL, ModelRenderer lowerArmR, ModelRenderer lowerArmL)
        {
            upperArmL.rotateAngleX -= heldItemLeft * 0.125F;
            upperArmR.rotateAngleX -= heldItemRight * 0.125F;

            lowerArmL.rotateAngleX -= heldItemLeft * 0.0625F;
            lowerArmR.rotateAngleX -= heldItemRight * 0.0625F;
        }

        @Override
        public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity)
        {
            super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);

            if (entity instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) entity;
                ItemStack itemstack = player.getHeldItem();

                if (TFDataManager.getTransformationTimer(player, ClientEventHandler.renderTick) == 20)
                {
                    if (itemstack != null && itemstack.getItem() == TFItems.vurpsSniper)
                    {
                        setRotation(bipedRightArm, -1.3F, bipedHead.rotateAngleY - 0.45F, 0.0F);
                        setRotation(bipedLeftArm, -1.2F, bipedHead.rotateAngleY + 0.4F, 0.0F);
                        bipedLeftArm.setRotationPoint(3.0F, 3.0F, -2.5F);
                    }
                    else
                    {
                        bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
                        bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
                    }
                }
            }
        }
    }
}