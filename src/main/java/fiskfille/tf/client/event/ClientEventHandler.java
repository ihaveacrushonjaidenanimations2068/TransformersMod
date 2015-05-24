package fiskfille.tf.client.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.gegy1000.visualenhancements.client.render.RenderCustomPlayer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.keybinds.TFKeyBinds;
import fiskfille.tf.client.model.tools.MowzieModelRenderer;
import fiskfille.tf.client.model.transformer.definition.TFModelRegistry;
import fiskfille.tf.client.model.transformer.definition.TransformerModel;
import fiskfille.tf.client.render.entity.CustomEntityRenderer;
import fiskfille.tf.common.event.PlayerTransformEvent;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.item.armor.ItemTransformerArmor;
import fiskfille.tf.common.motion.TFMotionManager;
import fiskfille.tf.common.motion.VehicleMotion;
import fiskfille.tf.common.playerdata.TFDataManager;
import fiskfille.tf.common.proxy.ClientProxy;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.ModelOffset;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFModelHelper;

public class ClientEventHandler
{
    private final Minecraft mc = Minecraft.getMinecraft();
    private EntityRenderer renderer, prevRenderer;
    
    public static boolean prevViewBobbing;
    
    private Map<EntityPlayer, Item> prevHelm = new HashMap<EntityPlayer, Item>();
    private Map<EntityPlayer, Item> prevChest = new HashMap<EntityPlayer, Item>();
    private Map<EntityPlayer, Item> prevLegs = new HashMap<EntityPlayer, Item>();
    private Map<EntityPlayer, Item> prevBoots = new HashMap<EntityPlayer, Item>();
    
//    private RenderPlayer prevRenderPlayer;
    
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equals(TransformersMod.modid))
        {
            TransformersMod.config.load(TransformersMod.configFile);
            
            TransformersMod.configFile.save();
        }
    }
    
    @SubscribeEvent
    public void onTransform(PlayerTransformEvent event)
    {
        EntityPlayer player = event.entityPlayer;
        
        if (player == mc.thePlayer)
        {
            if (event.transformed)
            {
                GameSettings gameSettings = mc.gameSettings;
                prevViewBobbing = gameSettings.viewBobbing;
                gameSettings.viewBobbing = false;
            }
            else
            {
                mc.gameSettings.viewBobbing = prevViewBobbing;
            }
        }
    }
    
    @SubscribeEvent
    public void onPlaySound(PlaySoundAtEntityEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            if (event.name.startsWith("step.") && TFDataManager.isInVehicleMode((EntityPlayer) event.entity))
            {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderPlayerSpecialsPre(RenderPlayerEvent.Specials.Pre event)
    {
        AbstractClientPlayer player = (AbstractClientPlayer) event.entityPlayer;
        
        if (TFDataManager.getTransformationTimer(player) < 10)
        {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onRenderPlayerPost(RenderPlayerEvent.Specials.Post event)
    {
        //After rendered everything
        
//        System.out.println(mc.thePlayer.getCommandSenderName() + ":" + event.entityPlayer.getCommandSenderName());
        
        EntityPlayer player = event.entityPlayer;
        
        ModelOffset offsets = TFModelHelper.getOffsets(player);
        
        ItemStack bootsStack = player.getCurrentArmor(0);
        ItemStack legsStack = player.getCurrentArmor(1);
        ItemStack chestStack = player.getCurrentArmor(2);
        ItemStack helmStack = player.getCurrentArmor(3);
        
        Item boots = bootsStack != null ? bootsStack.getItem() : null;
        Item legs = legsStack != null ? legsStack.getItem() : null;
        Item chest = chestStack != null ? chestStack.getItem() : null;
        Item helm = helmStack != null ? helmStack.getItem() : null;
        
        boolean armorChanged = false;
        
        if (boots != prevBoots.get(player))
        {
            prevBoots.put(player, boots);
            armorChanged = true;
        }
        if (chest != prevChest.get(player))
        {
            prevChest.put(player, chest);
            armorChanged = true;
        }
        if (legs != prevLegs.get(player))
        {
            prevLegs.put(player, legs);
            armorChanged = true;
        }
        if (helm != prevHelm.get(player))
        {
            prevHelm.put(player, helm);
            armorChanged = true;
        }
        
        if (armorChanged)
        {
            offsets.headOffsetX = 0;
            offsets.headOffsetY = 0;
            offsets.headOffsetZ = 0;
        }
//        
//        if (player == mc.thePlayer)
//        {
//            if (prevRenderPlayer != null)
//            {
//                RenderManager.instance.entityRenderMap.put(player.getClass(), prevRenderPlayer);
//            }
//        }
    }
    
    @SubscribeEvent
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event)
    {
        Render entityRenderObject = RenderManager.instance.getEntityRenderObject(event.entityPlayer);
        
        ModelBiped modelBipedMain = event.renderer.modelBipedMain;
        
        EntityPlayer player = event.entityPlayer;
        Transformer transformer = TFHelper.getTransformer(player);
        boolean isClientPlayer = mc.thePlayer == player;
        float cameraYOffset = 0;
        
        if (isClientPlayer)
        {
            TFModelHelper.modelBipedMain = modelBipedMain;
        }
        
        boolean customRenderer = entityRenderObject instanceof RenderCustomPlayer;
        
        if (modelBipedMain != null)
        {
            ItemStack helm = player.getCurrentArmor(3);
            boolean wearingTransformerHelm = helm != null && helm.getItem() instanceof ItemTransformerArmor;
            ItemStack chest = player.getCurrentArmor(2);
            boolean wearingTransformerChest = chest != null && chest.getItem() instanceof ItemTransformerArmor;
            ItemStack pants = player.getCurrentArmor(1);
            boolean wearingTransformerPants = pants != null && pants.getItem() instanceof ItemTransformerArmor;
            
            modelBipedMain.bipedHead.showModel = !wearingTransformerHelm;
            modelBipedMain.bipedHeadwear.showModel = !wearingTransformerHelm;
            modelBipedMain.bipedEars.showModel = !wearingTransformerHelm;
            
            modelBipedMain.bipedBody.showModel = !wearingTransformerChest;
            modelBipedMain.bipedRightArm.showModel = !wearingTransformerChest;
            modelBipedMain.bipedLeftArm.showModel = !wearingTransformerChest;
            
            modelBipedMain.bipedLeftLeg.showModel = !wearingTransformerPants;
            modelBipedMain.bipedRightLeg.showModel = !wearingTransformerPants;
//            
//            if (!customRenderer && isClientPlayer)
//            {
//                if (wearingTransformerHelm || wearingTransformerChest || wearingTransformerPants || (player.getHeldItem() != null && player.getHeldItem().getItem() == TFItems.vurpsSniper))
//                {
//                    prevRenderPlayer = (RenderPlayer) entityRenderObject;
//                    RenderManager.instance.entityRenderMap.put(player.getClass(), ClientProxy.renderCustomPlayer);
//                }
//            }
        }
        
        if (transformer != null)
        {
            cameraYOffset = transformer.getCameraYOffset(player);
        }
        
        if (isClientPlayer && cameraYOffset != 0)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0, -CustomEntityRenderer.getOffsetY(player), 0);
        }
        
        // This prevents the player from sinking into the ground when sneaking in vehicle mode
        if (player.isSneaking() && TFDataManager.getTransformationTimer(player) < 20)
        {
            if (isClientPlayer)
            {
                GL11.glTranslatef(0, 0.08F, 0);
            }
            else
            {
                GL11.glTranslatef(0, 0.125F, 0);
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post event)
    {
        EntityPlayer player = event.entityPlayer;
        Transformer transformer = TFHelper.getTransformer(player);
        boolean isClientPlayer = mc.thePlayer == player;
        
        ModelBiped modelBipedMain = event.renderer.modelBipedMain;
        
        if (modelBipedMain != null)
        {
            modelBipedMain.bipedHead.showModel = true;
            modelBipedMain.bipedHeadwear.showModel = true;
            modelBipedMain.bipedEars.showModel = true;
            
            modelBipedMain.bipedBody.showModel = true;
            modelBipedMain.bipedRightArm.showModel = true;
            modelBipedMain.bipedLeftArm.showModel = true;
            
            modelBipedMain.bipedLeftLeg.showModel = true;
            modelBipedMain.bipedRightLeg.showModel = true;
        }
        
        if (transformer != null)
        {
            if (isClientPlayer && transformer.getCameraYOffset(player) != 0.0F)
            {
                GL11.glPopMatrix();
            }
        }
    }
    
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        
        if (mc.theWorld != null)
        {
            if (event.phase == TickEvent.Phase.START)
            {
                EntityClientPlayerMP player = mc.thePlayer;
                
                Transformer transformer = TFHelper.getTransformer(player);
                
                if (transformer != null)
                {
                    if (transformer.getCameraYOffset(player) != 0.0F)
                    {
                        if (renderer == null)
                        {
                            renderer = new CustomEntityRenderer(mc);
                        }
                        
                        if (mc.entityRenderer != renderer)
                        {
                            prevRenderer = mc.entityRenderer;
                            mc.entityRenderer = renderer;
                        }
                    }
                    else if (prevRenderer != null && mc.entityRenderer != prevRenderer)
                    {
                        mc.entityRenderer = prevRenderer;
                    }
                }
                else if (prevRenderer != null && mc.entityRenderer != prevRenderer)
                {
                    mc.entityRenderer = prevRenderer;
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event)
    {
        EntityPlayerSP player = event.entity;
        VehicleMotion transformedPlayer = TFMotionManager.getTransformerPlayer(player);
        
        int nitro = transformedPlayer == null ? 0 : transformedPlayer.getNitro();
        boolean moveForward = Minecraft.getMinecraft().gameSettings.keyBindForward.getIsKeyPressed();
        boolean nitroPressed = TFKeyBinds.keyBindingNitro.getIsKeyPressed() || Minecraft.getMinecraft().gameSettings.keyBindSprint.getIsKeyPressed();
        
        if (TFDataManager.isInVehicleMode(player))
        {
            if (nitro > 0 && moveForward && nitroPressed && !TFDataManager.isInStealthMode(player))
            {
                event.newfov = 1.3F;
            }
        }
        else
        {
            ItemStack itemstack = player.getHeldItem();
            
            if (TFDataManager.getZoomTimer(player) > 0 && TFHelper.isPlayerVurp(player) && itemstack != null && itemstack.getItem() == TFItems.vurpsSniper && this.mc.gameSettings.thirdPersonView == 0)
            {
                event.newfov = 1.0F - (float) TFDataManager.getZoomTimer(player) / 10;
            }
        }
        
        if (TFDataManager.getTransformationTimer(player) < 20 && !(nitro > 0 && moveForward && nitroPressed && !TFDataManager.isInStealthMode(player)))
        {
            event.newfov = 1.0F;
        }
    }
    
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event)
    {
        
    }
    //    TODO: Expand upon and re-implement this for 0.6.0 
    //    @SubscribeEvent
    //    public void onItemToolTip(ItemTooltipEvent event)
    //    {
    //        String s = "tooltip." + event.itemStack.getUnlocalizedName();
    //        
    //        if (!s.equals(StatCollector.translateToLocal(s)))
    //        {
    //            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
    //            {
    //                event.toolTip.add(StatCollector.translateToLocal(s));
    //            }
    //            else
    //            {
    //                event.toolTip.add(EnumChatFormatting.BLUE + "Hold SHIFT for info.");
    //            }
    //        }
    //    }
}