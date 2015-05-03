package fiskfille.tf.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import fiskfille.tf.common.entity.EntityLaser;
import fiskfille.tf.common.network.MessageSniperShoot;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.playerdata.TFDataManager;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFShootManager;

public class ItemVurpsSniper extends ItemSword
{
    public ItemVurpsSniper(ToolMaterial material)
    {
        super(material);
        this.setMaxDamage(1500);
    }
    
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        boolean isCreativeMode = player.capabilities.isCreativeMode;
        
        if (TFHelper.isPlayerVurp(player) && !TFDataManager.isInVehicleMode(player))
        {
            stack.damageItem(1, player);
            
            if (world.isRemote)
            {
                if (!TFShootManager.sniperFilling && TFShootManager.sniperCharge > 0)
                {
                    TFShootManager.sniperCharge -= 5;
                    boolean consume = TFShootManager.sniperCharge <= 0;
                    TFNetworkManager.networkWrapper.sendToServer(new MessageSniperShoot(player, consume));
                }
                else
                {
                    if (player.inventory.hasItem(TFItems.energonCrystalPiece))
                    {
                        TFShootManager.sniperFilling = true;
                    }
                }
            }
        }
        
        return stack;
    }
}