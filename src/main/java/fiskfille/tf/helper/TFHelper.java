package fiskfille.tf.helper;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import fiskfille.tf.common.energon.power.IEnergyContainer;
import fiskfille.tf.common.item.armor.ItemTransformerArmor;
import fiskfille.tf.common.transformer.TransformerCloudtrap;
import fiskfille.tf.common.transformer.TransformerPurge;
import fiskfille.tf.common.transformer.TransformerSkystrike;
import fiskfille.tf.common.transformer.TransformerSubwoofer;
import fiskfille.tf.common.transformer.TransformerVurp;
import fiskfille.tf.common.transformer.base.Transformer;

/**
 * @author FiskFille, gegy1000
 */
public class TFHelper
{
	/**
	 * @returns whether the player is wearing the 'Cloudtrap' set.
	 */
	public static boolean isPlayerCloudtrap(EntityPlayer player)
	{
		return getTransformer(player) instanceof TransformerCloudtrap;
	}

	/**
	 * @returns whether the player is wearing the 'Skystrike' set.
	 */
	public static boolean isPlayerSkystrike(EntityPlayer player)
	{
		return getTransformer(player) instanceof TransformerSkystrike;
	}

	/**
	 * @returns whether the player is wearing the 'Purge' set.
	 */
	public static boolean isPlayerPurge(EntityPlayer player)
	{
		return getTransformer(player) instanceof TransformerPurge;
	}

	/**
	 * @returns whether the player is wearing the 'Vurp' set.
	 */
	public static boolean isPlayerVurp(EntityPlayer player)
	{
		return getTransformer(player) instanceof TransformerVurp;
	}

	/**
	 * @returns whether the player is wearing the 'Subwoofer' set.
	 */
	public static boolean isPlayerSubwoofer(EntityPlayer player)
	{
		return getTransformer(player) instanceof TransformerSubwoofer;
	}

	/**
	 * @returns whether the player is wearing a full Transformer set.
	 */
	public static boolean isPlayerTransformer(EntityPlayer player)
	{
		Transformer helmetTransformer = getTransformerFromArmor(player, 3);
		Transformer chestTransformer = getTransformerFromArmor(player, 2);
		Transformer legsTransformer = getTransformerFromArmor(player, 1);
		Transformer feetTransformer = getTransformerFromArmor(player, 0);

		return helmetTransformer != null && helmetTransformer == chestTransformer && chestTransformer == legsTransformer && legsTransformer == feetTransformer;
	}

	/**
	 * @returns the Transformer that the player currently has fully equipped, null when not wearing a full set.
	 */
	public static Transformer getTransformer(EntityPlayer player)
	{
		if (player != null && isPlayerTransformer(player))
		{
			return getTransformerFromArmor(player, 0);
		}

		return null;
	}

	/**
	 * @returns the Transformer that the player is wearing in the specified slot.
	 */
	public static Transformer getTransformerFromArmor(EntityPlayer player, int slot)
	{
		ItemStack currentArmorStack = player.getCurrentArmor(slot);

		if (currentArmorStack != null)
		{
			Item currentArmor = currentArmorStack.getItem();

			if (currentArmor instanceof ItemTransformerArmor)
			{
				return ((ItemTransformerArmor) currentArmor).getTransformer();
			}
		}

		return null;
	}

	/**
	 * @returns the Transformer for the specific armor ItemStack.
	 */
	public static Transformer getTransformerFromArmor(ItemStack itemstack)
	{
		if (itemstack != null)
		{
			Item currentArmor = itemstack.getItem();

			if (currentArmor instanceof ItemTransformerArmor)
			{
				return ((ItemTransformerArmor) currentArmor).getTransformer();
			}
		}

		return null;
	}

	public static void replaceBlock(World world, int x, int y, int z, Block block, Block replacement)
	{
		if (world.getBlock(x, y, z) == block)
		{
			world.setBlock(x, y, z, replacement);
		}
	}
	
	public static AxisAlignedBB wrapAroundAABB(AxisAlignedBB aabb, AxisAlignedBB aabb1)
	{
		double d0 = Math.min(aabb.minX, aabb1.minX);
        double d1 = Math.min(aabb.minY, aabb1.minY);
        double d2 = Math.min(aabb.minZ, aabb1.minZ);
        double d3 = Math.max(aabb.maxX, aabb1.maxX);
        double d4 = Math.max(aabb.maxY, aabb1.maxY);
        double d5 = Math.max(aabb.maxZ, aabb1.maxZ);
        return AxisAlignedBB.getBoundingBox(d0, d1, d2, d3, d4, d5);
	}
	
	public static float transferEnergy(IEnergyContainer to, IEnergyContainer from, float amount)
	{
		float f = from.extractEnergy(amount);
		float f1 = to.receiveEnergy(f);
		
		if (f > f1)
		{
			return from.receiveEnergy(f - f1);
		}
		
		return f1;
	}
	
	public static float getPercentOf(String s, Map<String, Integer> map)
	{
		float f = 0;
		
		for (Map.Entry<String, Integer> e : map.entrySet())
        {
			f += e.getValue();
        }
		
		float percentMultiplier = 1.0F / f;

		for (Map.Entry<String, Integer> e : map.entrySet())
        {
            if (e.getKey().equals(s))
            {
            	return e.getValue() * percentMultiplier;
            }
        }
		
		return 0;
	}
}
