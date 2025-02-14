package fiskfille.tf.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersAPI;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.event.ClientEventHandler;
import fiskfille.tf.client.tutorial.TutorialHandler;
import fiskfille.tf.common.data.TFDataManager;
import fiskfille.tf.common.item.ItemVurpsSniper;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.motion.TFMotionManager;
import fiskfille.tf.common.motion.VehicleMotion;
import fiskfille.tf.common.transformer.TransformerVurp;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.config.TFConfig;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFShootManager;

public class GuiOverlay extends Gui
{
    private Minecraft mc;
    private RenderItem itemRenderer;
    public static final ResourceLocation texture = new ResourceLocation(TransformersMod.modid, "textures/gui/mod_icons.png");

    public static double prevSpeed;
    public static double speed;

    public GuiOverlay(Minecraft mc)
    {
        super();
        this.mc = mc;
        itemRenderer = new RenderItem();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRender(RenderGameOverlayEvent.Pre event)
    {
        if (!event.isCanceled())
        {
            int width = event.resolution.getScaledWidth();
            int height = event.resolution.getScaledHeight();
            EntityPlayer player = mc.thePlayer;

            if (event.type == ElementType.HOTBAR)
            {
                Transformer transformer = TFHelper.getTransformer(player);
                boolean flag = transformer == null || transformer.renderSpeedAndNitro(player, TFDataManager.getAltMode(player));

                if (flag)
                {
                    renderNitroAndSpeed(event, width, height, player);
                }

                renderKatanaDash(event, width, height, player);
                renderShotsLeft(event, width, height, player);
                renderLaserCharge(event, width, height, player);
                renderTutorial(event, width, height, player);
            }
        }
    }

    public void renderLaserCharge(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player)
    {
        int altMode = TFDataManager.getAltMode(player);

        ItemStack heldItem = player.getHeldItem();
        Transformer transformer = TFHelper.getTransformer(player);
        boolean hasSniper = heldItem != null && heldItem.getItem() instanceof ItemVurpsSniper && TFDataManager.getTransformationTimer(player, ClientEventHandler.renderTick) == 20;

        if (transformer instanceof TransformerVurp && (hasSniper || transformer.canShoot(player, altMode)))
        {
            float stealthModeTimer = TFDataManager.getStealthModeTimer(player, ClientEventHandler.renderTick);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(0F, 0F, 0F, 0.3F);

            int x = (int) ((5 - stealthModeTimer) * 20 - 94);

            if (hasSniper)
            {
                x = 6;
            }

            int y = 3;

            if (!hasSniper)
            {
                y = 30;
            }

            //Charge Outline
            drawTexturedModalRect(x + 17, y, 0, 0, 102, 12);

            if (hasSniper)
            {
                GL11.glColor4f(0.0F, 1.0F, 1.0F, 0.5F);
            }
            else
            {
                GL11.glColor4f(1F, 0F, 0F, 0.5F);
            }

            //Charge Bar
            drawTexturedModalRect(x + 18, y + 1, 0, 0, TFShootManager.laserCharge * 2, 10);


            GL11.glColor4f(0F, 0F, 0F, 0.2F);
            drawTexturedModalRect(x - 1, y, 0, 0, 16, 16);
            drawTexturedModalRect(x, y + 1, 0, 0, 14, 14);
            GL11.glEnable(GL11.GL_TEXTURE_2D);

            drawCenteredString(mc.fontRenderer, StatCollector.translateToLocal("stats.laser_charge.name"), x + 50 + 18, y + 2, 0xffffff);


            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), new ItemStack(transformer.getShootItem(altMode)), x - 1, y);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            float scale = 0.5F;
            GL11.glPushMatrix();
            GL11.glScalef(scale, scale, scale);
            drawString(mc.fontRenderer, StatCollector.translateToLocalFormatted("stats.ammo.name", StatCollector.translateToLocal(transformer.getShootItem(altMode).getUnlocalizedName() + ".name")), (int) ((x - 1) / scale), (int) ((y + 17) / scale), 0xffffff);
            GL11.glPopMatrix();
        }
    }

    public void renderNitroAndSpeed(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player)
    {
        VehicleMotion transformedPlayer = TFMotionManager.getTransformerPlayer(player);

        float transformationTimer = TFDataManager.getTransformationTimer(player, event.partialTicks);

        if (transformedPlayer != null && transformationTimer <= 20)
        {
            float nitro = transformedPlayer.getPrevNitro() + (transformedPlayer.getNitro() - transformedPlayer.getPrevNitro()) * event.partialTicks;
            int i = (int)(transformationTimer * 10);
            
            if (transformationTimer <= 19)
            {
                double speed = GuiOverlay.prevSpeed + (GuiOverlay.speed - GuiOverlay.prevSpeed) * event.partialTicks;

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(0F, 0F, 0F, 0.3F);
                //Speed Outline
                drawTexturedModalRect(5 - i, 3, 0, 0, 202, 12);

                //Nitro Outline
                drawTexturedModalRect(5 - i, 16, 0, 0, 202, 12);
                GL11.glColor4f(0.0F, 1.0F, 1.0F, 0.5F);
                //Nitro Bar
                drawTexturedModalRect(6 - i, 4, 0, 0, (int) (nitro * 1.25F), 10);
                GL11.glColor4f(1F, 0F, 0F, 0.5F);
                //Speed Bar
                drawTexturedModalRect(6 - i, 17, 0, 0, speed > 200 ? 200 : (int) speed, 10);
                GL11.glEnable(GL11.GL_TEXTURE_2D);

                drawCenteredString(mc.fontRenderer, StatCollector.translateToLocal("stats.nitro.name"), 106 - i, 5, 0xffffff);
                drawCenteredString(mc.fontRenderer, (int) (TFConfig.useMiles ? speed * 0.621371192 : speed) + (TFConfig.useMiles ? " mph" : " km/h"), 106 - i, 18, 0xffffff);
            }
        }
    }

    public void renderShotsLeft(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player)
    {
        Transformer transformer = TFHelper.getTransformer(player);

        int altMode = TFDataManager.getAltMode(player);

        if (transformer != null && !(transformer instanceof TransformerVurp))
        {
            float transformationTimer = TFDataManager.getTransformationTimer(player, ClientEventHandler.renderTick);
            float stealthModeTimer = TFDataManager.getStealthModeTimer(player, ClientEventHandler.renderTick);

            if (transformationTimer <= 20 && transformer.canShoot(player, altMode))
            {
                int transformationOffsetX = 0;

                if (transformer.hasStealthForce(player, altMode) && stealthModeTimer <= 5)
                {
                    transformationOffsetX = (int) (stealthModeTimer * 25);
                }
                else
                {
                    transformationOffsetX = (int) (transformationTimer * 8.5F);
                }

                int y = 30;
                int x = 100;
                int j = 20 - TFShootManager.shootCooldown;
                double d = j * 2.5;
                String shotsLeft = "" + TFShootManager.shotsLeft;

                if (TFShootManager.shotsLeft <= 0)
                {
                    shotsLeft = EnumChatFormatting.RED + shotsLeft;
                }

                drawString(mc.fontRenderer, StatCollector.translateToLocal("stats.shots_left.name") + ": " + shotsLeft, x - 75 - transformationOffsetX, 32, 0xffffff);

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(0F, 0F, 0F, 0.15F);
                drawTexturedModalRect(x - transformationOffsetX, y, 0, 0, 52, 12);
                GL11.glColor4f(0F, 0F, 0F, 0.2F);
                drawTexturedModalRect(x - 95 - transformationOffsetX, y - 1, 0, 0, 16, 16);
                drawTexturedModalRect(x - 94 - transformationOffsetX, y, 0, 0, 14, 14);
                GL11.glColor4f(1F, 0F, 0F, 0.25F);
                drawTexturedModalRect(x + 1 - transformationOffsetX, y + 1, 0, 0, (int) d, 10);
                GL11.glEnable(GL11.GL_TEXTURE_2D);

                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                GL11.glEnable(GL11.GL_LIGHTING);
                itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), new ItemStack(transformer.getShootItem(altMode)), x - 95 - transformationOffsetX, y - 1);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);

                float scale = 0.5F;
                GL11.glPushMatrix();
                GL11.glScalef(scale, scale, scale);
                drawString(mc.fontRenderer, StatCollector.translateToLocalFormatted("stats.ammo.name", StatCollector.translateToLocal(transformer.getShootItem(altMode).getUnlocalizedName() + ".name")), (int) ((x - 95 - transformationOffsetX) / scale), (int) ((y + 16) / scale), 0xffffff);
                GL11.glPopMatrix();
            }
        }
        else if (transformer instanceof TransformerVurp)
        {
            ItemStack heldItem = player.getHeldItem();

            if (heldItem != null)
            {
                float transformationTimer = TFDataManager.getTransformationTimer(player, ClientEventHandler.renderTick);

                if (transformationTimer == 20 && heldItem.getItem() == TFItems.vurpsSniper && TFHelper.isPlayerVurp(player))
                {
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glColor4f(0F, 0F, 0F, 0.15F);

                    if (mc.gameSettings.thirdPersonView == 0 && heldItem.getItem() == TFItems.vurpsSniper && TFDataManager.getZoomTimer(player) > 7)
                    {
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        GL11.glDepthMask(false);
                        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GL11.glDisable(GL11.GL_ALPHA_TEST);
                        mc.getTextureManager().bindTexture(new ResourceLocation(TransformersMod.modid, "textures/misc/sniper_scope.png"));
                        Tessellator tessellator = Tessellator.instance;
                        tessellator.startDrawingQuads();
                        tessellator.addVertexWithUV(0.0D, height, -90.0D, 0.0D, 1.0D);
                        tessellator.addVertexWithUV(width, height, -90.0D, 1.0D, 1.0D);
                        tessellator.addVertexWithUV(width, 0.0D, -90.0D, 1.0D, 0.0D);
                        tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
                        tessellator.draw();
                        GL11.glDepthMask(true);
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    public void renderKatanaDash(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player)
    {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() == TFItems.purgesKatana && !TFDataManager.isTransformed(player) && TFHelper.isPlayerPurge(player))
        {
            if (player.isUsingItem())
            {
                int x = width / 2 - 26;
                int j = TFItems.purgesKatana.getMaxItemUseDuration(player.getHeldItem()) - player.getItemInUseCount();
                double d = (double) j / 10;

                if (d > 2.0D)
                {
                    d = 2.0D;
                }

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(0F, 0F, 0F, 0.15F);

                int y = 0;

                if (TFConfig.purgeDashTop)
                {
                    y = 5;
                }
                else
                {
                    y = height / 2 + 9;
                }

                drawTexturedModalRect(x, y, 0, 0, 52, 12);
                GL11.glColor4f(1F, 0F, 0F, 0.25F);
                drawTexturedModalRect(x + 1, y + 1, 0, 0, (int) (d * 25), 10);

                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
        }
    }

    public void renderTutorial(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player)
    {
        if (TutorialHandler.currentTutorial != null)
        {
            TutorialHandler.currentTutorial.ticker.render(event, width, height, player);
        }

        if (TutorialHandler.completedTutorial != null)
        {
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/achievement/achievement_background.png"));
            int i = (int) (TutorialHandler.animationTimer > 90 ? (100 - TutorialHandler.animationTimer) * 3.2F : TutorialHandler.animationTimer < 10 ? TutorialHandler.animationTimer * 3.2F : 32) - 32;
            String s = TutorialHandler.completedTutorial.name();

            drawTexturedModalRect(width - 160, i, 96, 202, 160, 32);
            mc.fontRenderer.drawString("Tutorial Completed!", width - 130, i + 7, 0xffff00);
            mc.fontRenderer.drawString(s.substring(0, 1) + s.substring(1, s.length()).toLowerCase(), width - 130, i + 18, 0xffffff);

            ItemStack itemstack = null;

            for (int j = 0; j < TransformersAPI.getTransformers().size(); ++j)
            {
                Transformer transformer = TransformersAPI.getTransformers().get(j);

                int altMode = TFDataManager.getAltMode(player);

                if (transformer.getTutorialType(altMode) == TutorialHandler.completedTutorial && itemstack == null)
                {
                    itemstack = new ItemStack(TFItems.displayVehicle, 1, j);
                }
            }

            if (itemstack != null)
            {
                RenderHelper.enableGUIStandardItemLighting();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                GL11.glEnable(GL11.GL_LIGHTING);
                itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemstack, width - 152, i + 8);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }

    public void renderCrossbowAmmo(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player)
    {

    }
}