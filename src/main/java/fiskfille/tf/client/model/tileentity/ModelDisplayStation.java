package fiskfille.tf.client.model.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelDisplayStation extends ModelBase
{
    public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer shape28;
    public ModelRenderer shape28_1;
    public ModelRenderer shape28_2;
    public ModelRenderer shape28_3;
    public ModelRenderer shape3;
    public ModelRenderer tool2_1;
    public ModelRenderer shape10;
    public ModelRenderer shape13;
    public ModelRenderer shape14;
    public ModelRenderer shape13_1;
    public ModelRenderer shape3_1;
    public ModelRenderer shape22;
    public ModelRenderer shape4;
    public ModelRenderer shape6;
    public ModelRenderer shape6_1;
    public ModelRenderer tool2_2;
    public ModelRenderer tool2_3;
    public ModelRenderer shape4_1;
    public ModelRenderer shape6_2;
    public ModelRenderer shape6_3;
    public ModelRenderer tool1_1;
    public ModelRenderer tool1_3;
    public ModelRenderer tool1_2;
    public ModelRenderer tool1_4;
    public ModelRenderer tool1_5;
    public ModelRenderer shape30;
    public ModelRenderer shape30_1;
    public ModelRenderer shape30_2;
    public ModelRenderer shape30_3;

    public ModelDisplayStation()
    {
        textureWidth = 64;
        textureHeight = 64;
        shape6 = new ModelRenderer(this, 6, 20);
        shape6.setRotationPoint(1.0F, -0.5F, 0.0F);
        shape6.addBox(0.0F, 0.0F, -1.0F, 3, 4, 1, 0.0F);
        setRotateAngle(shape6, 0.0F, 0.3665191429188092F, 0.0F);
        tool1_4 = new ModelRenderer(this, 15, 37);
        tool1_4.setRotationPoint(-5.1F, -0.3F, 0.2F);
        tool1_4.addBox(-1.0F, -2.0F, -1.0F, 2, 5, 2, 0.0F);
        setRotateAngle(tool1_4, -0.31869712141416456F, -0.36425021489121656F, -0.40980330836826856F);
        shape10 = new ModelRenderer(this, 15, 3);
        shape10.setRotationPoint(-2.5F, -30.0F, -2.0F);
        shape10.addBox(0.0F, 0.0F, -2.0F, 5, 3, 2, 0.0F);
        setRotateAngle(shape10, 0.5759586531581287F, 0.0F, 0.0F);
        shape22 = new ModelRenderer(this, 0, 15);
        shape22.setRotationPoint(-3.6F, -20.0F, -0.6F);
        shape22.addBox(0.0F, -1.0F, -0.6F, 3, 2, 2, 0.0F);
        setRotateAngle(shape22, 0.0F, 0.24434609527920614F, 0.0F);
        tool1_3 = new ModelRenderer(this, 15, 45);
        tool1_3.setRotationPoint(-4.0F, 0.3F, 0.0F);
        tool1_3.addBox(-5.0F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
        setRotateAngle(tool1_3, -0.27314402793711257F, -0.5009094953223726F, 1.2747884856566583F);
        shape30 = new ModelRenderer(this, 0, 0);
        shape30.setRotationPoint(0.5F, -1.0F, 0.5F);
        shape30.addBox(0.0F, 0.0F, 0.0F, 15, 1, 1, 0.0F);
        shape3 = new ModelRenderer(this, 0, 25);
        shape3.setRotationPoint(2.5F, -15.5F, 0.0F);
        shape3.addBox(0.0F, 0.0F, -2.0F, 3, 3, 2, 0.0F);
        setRotateAngle(shape3, 0.0F, 0.5235987755982988F, 0.0F);
        tool1_5 = new ModelRenderer(this, 24, 40);
        tool1_5.setRotationPoint(0.0F, 3.0F, 0.0F);
        tool1_5.addBox(-0.5F, -0.5F, -0.5F, 1, 3, 1, 0.0F);
        shape2 = new ModelRenderer(this, 0, 30);
        shape2.setRotationPoint(7.0F, 0.0F, 14.0F);
        shape2.addBox(-2.5F, -30.0F, -2.0F, 5, 30, 2, 0.0F);
        shape1 = new ModelRenderer(this, 0, 48);
        shape1.setRotationPoint(-7.0F, 22.5F, -7.0F);
        shape1.addBox(0.0F, 0.0F, 0.0F, 14, 2, 14, 0.0F);
        shape3_1 = new ModelRenderer(this, 0, 25);
        shape3_1.mirror = true;
        shape3_1.setRotationPoint(-2.5F, -15.5F, 0.0F);
        shape3_1.addBox(-3.0F, 0.0F, -2.0F, 3, 3, 2, 0.0F);
        setRotateAngle(shape3_1, 0.0F, -0.5235987755982988F, 0.0F);
        shape28 = new ModelRenderer(this, 0, 8);
        shape28.setRotationPoint(-1.0F, -2.0F, -1.0F);
        shape28.addBox(0.0F, -0.5F, 0.0F, 2, 4, 2, 0.0F);
        shape30_1 = new ModelRenderer(this, 0, 0);
        shape30_1.setRotationPoint(0.5F, -1.0F, 0.5F);
        shape30_1.addBox(0.0F, 0.0F, 0.0F, 13, 1, 1, 0.0F);
        setRotateAngle(shape30_1, 0.0F, 1.5707963267948966F, 0.0F);
        shape4 = new ModelRenderer(this, 0, 19);
        shape4.setRotationPoint(3.0F, 0.0F, 0.0F);
        shape4.addBox(0.0F, -0.5F, -2.0F, 1, 4, 2, 0.0F);
        setRotateAngle(shape4, 0.0F, 0.5061454830783556F, 0.0F);
        shape6_3 = new ModelRenderer(this, 10, 25);
        shape6_3.setRotationPoint(-1.0F, -0.5F, -2.0F);
        shape6_3.addBox(-2.0F, 0.0F, -1.0F, 2, 4, 1, 0.0F);
        setRotateAngle(shape6_3, 0.0F, -2.0245819323134224F, 0.0F);
        shape30_3 = new ModelRenderer(this, 0, 0);
        shape30_3.setRotationPoint(0.5F, -1.0F, 0.5F);
        shape30_3.addBox(0.0F, 0.0F, 0.0F, 13, 1, 1, 0.0F);
        setRotateAngle(shape30_3, 0.0F, 1.5707963267948966F, 0.0F);
        shape28_2 = new ModelRenderer(this, 0, 8);
        shape28_2.mirror = true;
        shape28_2.setRotationPoint(13.0F, -2.0F, 13.0F);
        shape28_2.addBox(0.0F, -0.5F, 0.0F, 2, 4, 2, 0.0F);
        shape30_2 = new ModelRenderer(this, 0, 0);
        shape30_2.setRotationPoint(0.5F, -1.0F, 0.5F);
        shape30_2.addBox(0.0F, 0.0F, 0.0F, 15, 1, 1, 0.0F);
        shape4_1 = new ModelRenderer(this, 0, 19);
        shape4_1.setRotationPoint(-3.0F, 0.0F, 0.0F);
        shape4_1.addBox(-1.0F, -0.5F, -2.0F, 1, 4, 2, 0.0F);
        setRotateAngle(shape4_1, 0.0F, -0.5061454830783556F, 0.0F);
        tool1_2 = new ModelRenderer(this, 29, 40);
        tool1_2.setRotationPoint(-5.0F, 0.0F, 0.0F);
        tool1_2.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        setRotateAngle(tool1_2, 0.593411945678072F, 0.0F, 0.0F);
        shape6_1 = new ModelRenderer(this, 10, 25);
        shape6_1.setRotationPoint(1.0F, -0.5F, -2.0F);
        shape6_1.addBox(0.0F, 0.0F, -1.0F, 2, 4, 1, 0.0F);
        setRotateAngle(shape6_1, 0.0F, 2.0245819323134224F, 0.0F);
        shape28_1 = new ModelRenderer(this, 0, 8);
        shape28_1.mirror = true;
        shape28_1.setRotationPoint(13.0F, -2.0F, -1.0F);
        shape28_1.addBox(0.0F, -0.5F, 0.0F, 2, 4, 2, 0.0F);
        shape13_1 = new ModelRenderer(this, 17, 21);
        shape13_1.setRotationPoint(-2.5F, -12.0F, -1.0F);
        shape13_1.addBox(0.0F, 0.0F, -1.0F, 1, 13, 2, 0.0F);
        setRotateAngle(shape13_1, 0.0F, 0.0F, 0.3490658503988659F);
        tool2_1 = new ModelRenderer(this, 15, 45);
        tool2_1.setRotationPoint(2.5F, -29.1F, 0.0F);
        tool2_1.addBox(0.0F, 0.0F, -1.0F, 5, 1, 1, 0.0F);
        setRotateAngle(tool2_1, -0.05235987755982988F, 0.10471975511965977F, -0.08726646259971647F);
        tool2_2 = new ModelRenderer(this, 15, 37);
        tool2_2.setRotationPoint(5.0F, -0.4F, -0.5F);
        tool2_2.addBox(-1.0F, -2.0F, -1.0F, 2, 5, 2, 0.0F);
        setRotateAngle(tool2_2, -0.06981317007977318F, -0.10471975511965977F, -0.15707963267948966F);
        tool2_3 = new ModelRenderer(this, 24, 40);
        tool2_3.setRotationPoint(0.0F, 3.0F, 0.0F);
        tool2_3.addBox(-0.5F, -0.5F, -0.5F, 1, 3, 1, 0.0F);
        shape28_3 = new ModelRenderer(this, 0, 8);
        shape28_3.setRotationPoint(-1.0F, -2.0F, 13.0F);
        shape28_3.addBox(0.0F, -0.5F, 0.0F, 2, 4, 2, 0.0F);
        shape14 = new ModelRenderer(this, 24, 24);
        shape14.setRotationPoint(-2.5F, -12.0F, -2.0F);
        shape14.addBox(0.0F, 0.0F, 0.0F, 5, 13, 2, 0.0F);
        setRotateAngle(shape14, -0.15707963267948966F, 0.0F, 0.0F);
        shape13 = new ModelRenderer(this, 17, 21);
        shape13.mirror = true;
        shape13.setRotationPoint(2.5F, -12.0F, -1.0F);
        shape13.addBox(-1.0F, 0.0F, -1.0F, 1, 13, 2, 0.0F);
        setRotateAngle(shape13, 0.0F, 0.0F, -0.3490658503988659F);
        tool1_1 = new ModelRenderer(this, 15, 45);
        tool1_1.setRotationPoint(0.0F, 0.0F, 0.4F);
        tool1_1.addBox(-4.5F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
        setRotateAngle(tool1_1, 0.091106186954104F, -0.136659280431156F, -0.27314402793711257F);
        shape6_2 = new ModelRenderer(this, 6, 20);
        shape6_2.setRotationPoint(-1.0F, -0.5F, 0.0F);
        shape6_2.addBox(-3.0F, 0.0F, -1.0F, 3, 4, 1, 0.0F);
        setRotateAngle(shape6_2, 0.0F, -0.3665191429188092F, 0.0F);
        shape4.addChild(shape6);
        tool1_3.addChild(tool1_4);
        shape2.addChild(shape10);
        shape2.addChild(shape22);
        tool1_1.addChild(tool1_3);
        shape28.addChild(shape30);
        shape2.addChild(shape3);
        tool1_4.addChild(tool1_5);
        shape1.addChild(shape2);
        shape2.addChild(shape3_1);
        shape1.addChild(shape28);
        shape28_2.addChild(shape30_1);
        shape3.addChild(shape4);
        shape4_1.addChild(shape6_3);
        shape28_3.addChild(shape30_3);
        shape1.addChild(shape28_2);
        shape28_3.addChild(shape30_2);
        shape3_1.addChild(shape4_1);
        tool1_1.addChild(tool1_2);
        shape4.addChild(shape6_1);
        shape1.addChild(shape28_1);
        shape2.addChild(shape13_1);
        shape2.addChild(tool2_1);
        tool2_1.addChild(tool2_2);
        tool2_2.addChild(tool2_3);
        shape1.addChild(shape28_3);
        shape2.addChild(shape14);
        shape2.addChild(shape13);
        shape22.addChild(tool1_1);
        shape4_1.addChild(shape6_2);
    }

    public void render()
    {
        shape1.render(0.0625F);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
