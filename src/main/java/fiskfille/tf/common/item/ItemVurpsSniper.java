package fiskfille.tf.common.item;

import fiskfille.tf.common.block.TFBlocks;
import fiskfille.tf.common.data.TFDataManager;
import fiskfille.tf.common.network.MessageLaserShoot;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFShootManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemVurpsSniper extends Item
{
    public ItemVurpsSniper()
    {
        super();
        setMaxDamage(800);
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        boolean isCreativeMode = player.capabilities.isCreativeMode;

        if (TFHelper.isPlayerVurp(player) && !TFDataManager.isTransformed(player))
        {
            if (world.isRemote)
            {
                if (!TFShootManager.laserFilling && TFShootManager.laserCharge > 0)
                {
                    TFShootManager.laserCharge -= 5;
                    player.playSound("random.fizz", 1, 2F);
                    TFNetworkManager.networkWrapper.sendToServer(new MessageLaserShoot(player, false));
                }
                else
                {
                    if (!TFShootManager.laserFilling && (player.inventory.hasItem(Item.getItemFromBlock(TFBlocks.energonCube)) || player.capabilities.isCreativeMode))
                    {
                        stack.damageItem(1, player);
                        TFNetworkManager.networkWrapper.sendToServer(new MessageLaserShoot(player, true));
                        TFShootManager.laserFilling = true;
                    }
                }
            }
        }

        return stack;
    }
}