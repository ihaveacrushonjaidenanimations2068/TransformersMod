package fiskfille.tf.helper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fiskfille.tf.client.tutorial.TutorialHandler;
import fiskfille.tf.common.data.TFDataManager;
import fiskfille.tf.common.network.MessageLaserShoot;
import fiskfille.tf.common.network.MessageVehicleShoot;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.transformer.TransformerVurp;
import fiskfille.tf.common.transformer.base.Transformer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import org.lwjgl.input.Mouse;

public class TFShootManager
{
    public static int shootCooldown = 0;
    public static int shotsLeft = 4;

    public static boolean reloading;

    public static int laserCharge;
    public static boolean laserFilling;

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entity;

            if (event.entity.worldObj.isRemote)
            {
                int altMode = TFDataManager.getAltMode(player);
                boolean isTransfromed = altMode != -1;

                if (player == Minecraft.getMinecraft().thePlayer)
                {
                    Transformer transformer = TFHelper.getTransformer(player);

                    if (laserFilling)
                    {
                        int max = 50;

                        if (laserCharge < max)
                        {
                            laserCharge += 1;
                        }
                        else if (laserCharge >= max)
                        {
                            laserFilling = false;
                            laserCharge = max;
                        }
                    }

                    if (transformer != null)
                    {
                        if (shootCooldown > 0)
                        {
                            shootCooldown--;
                        }

                        Item ammo = transformer.getShootItem(altMode);

                        if (ammo != null)
                        {
                            int ammoCount = getShotsLeft(player, transformer, ammo, altMode);

                            if (isTransfromed)
                            {
                                if (reloading && shootCooldown <= 0)
                                {
                                    shotsLeft = ammoCount;
                                    reloading = false;
                                }
                            }
                            else
                            {
                                int shots = ammoCount;

                                if (shotsLeft > shots)
                                {
                                    shotsLeft = shots;
                                }
                            }
                        }
                    }
                }

                Transformer transformer = TFHelper.getTransformer(player);

                if (Mouse.isButtonDown(1))
                {
                    if (transformer != null && isTransfromed)
                    {
                        if (transformer.canShoot(player, altMode) && transformer.hasRapidFire(altMode) && player.ticksExisted % 2 == 0)
                        {
                            stealthForceShoot(transformer, player, altMode);
                        }
                    }
                }
            }
        }
    }

    private int getShotsLeft(EntityPlayer player, Transformer transformer, Item shootItem, int altMode)
    {
        int maxAmmo = transformer.getShots(altMode);
        int ammoCount;

        if (player.capabilities.isCreativeMode)
        {
            ammoCount = maxAmmo;
        }
        else
        {
            ammoCount = getAmountOf(shootItem, player);
        }

        if (ammoCount > maxAmmo)
        {
            ammoCount = maxAmmo;
        }

        if (shotsLeft > ammoCount)
        {
            shotsLeft = ammoCount;
        }

        return ammoCount;
    }

    private int getAmountOf(Item item, EntityPlayer player)
    {
        int amount = 0;
        InventoryPlayer inventory = player.inventory;

        for (ItemStack stack : inventory.mainInventory)
        {
            if (stack != null)
            {
                if (stack.getItem() == item)
                {
                    amount += stack.stackSize;
                }
            }
        }

        return amount;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        EntityPlayer player = event.entityPlayer;
        Transformer transformer = TFHelper.getTransformer(player);
        Action action = event.action;

        if (action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
        {
            int altMode = TFDataManager.getAltMode(player);
            boolean isTransformed = altMode != -1;

            if (transformer != null && isTransformed)
            {
                if (transformer.canShoot(player, altMode) && !transformer.hasRapidFire(altMode) && player.worldObj.isRemote)
                {
                    stealthForceShoot(transformer, player, altMode);
                    event.setCanceled(true);
                }
            }
        }
    }

    private void stealthForceShoot(Transformer transformer, EntityPlayer player, int altMode)
    {
        if (player == Minecraft.getMinecraft().thePlayer)
        {
            if (transformer instanceof TransformerVurp)
            {
                if (transformer.canShoot(player, altMode))
                {
                    if (!laserFilling && laserCharge > 0)
                    {
                        laserCharge -= 5;
                        player.playSound("random.fizz", 1, 2F);
                        TFNetworkManager.networkWrapper.sendToServer(new MessageLaserShoot(player, false));
                        TutorialHandler.shoot(player);
                    }
                    else
                    {
                        if (!laserFilling && (player.inventory.hasItem(transformer.getShootItem(altMode)) || player.capabilities.isCreativeMode))
                        {
                            TFNetworkManager.networkWrapper.sendToServer(new MessageLaserShoot(player, true));
                            TutorialHandler.shoot(player);
                            laserFilling = true;
                        }
                    }
                }
            }
            else
            {
                if (shotsLeft > 0)
                {
                    if (shootCooldown <= 0)
                    {
                        if (transformer.canShoot(player, altMode))
                        {
                            Item shootItem = transformer.getShootItem(altMode);

                            boolean isCreative = player.capabilities.isCreativeMode;
                            boolean hasAmmo = isCreative || player.inventory.hasItem(shootItem);

                            if (hasAmmo)
                            {
                                TFNetworkManager.networkWrapper.sendToServer(new MessageVehicleShoot(player));
                                TutorialHandler.shoot(player);

                                if (!isCreative)
                                {
                                    player.inventory.consumeInventoryItem(shootItem);
                                }
                            }
                        }

                        if (shotsLeft > transformer.getShots(altMode))
                        {
                            shotsLeft = transformer.getShots(altMode);
                        }

                        shotsLeft--;

                        if (shotsLeft <= 0)
                        {
                            shootCooldown = 20;
                            reloading = true;
                        }
                    }
                }
                else
                {
                    if (!reloading)
                    {
                        shootCooldown = 20;
                        reloading = true;
                    }
                }
            }
        }
    }
}
