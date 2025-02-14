package fiskfille.tf.common.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.energon.IEnergon;
import fiskfille.tf.common.tileentity.TileEntityCrystal;

public class BlockEnergonCrystal extends BlockBasic implements ITileEntityProvider, IEnergon
{
    private Random rand = new Random();
    private Energon energonType;

    public BlockEnergonCrystal(Energon type)
    {
        super(Material.glass);
        energonType = type;
        
        setHarvestLvl("pickaxe", 1);
        setStepSound(Block.soundTypeGlass);
        setHardness(6.0F);
        setResistance(10.0F);
        setLightLevel(0.75F);
    }

    @Override
    public Energon getEnergonType()
    {
        return energonType;
    }

    @Override
    public int getMass()
    {
        return 576;
    }

    @Override
    protected boolean canSilkHarvest()
    {
        return true;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return random.nextInt(3) + 2;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_)
    {
        return energonType.getCrystalPiece();
    }

    @Override
    public int getExpDrop(IBlockAccess world, int p_149690_5_, int p_149690_7_)
    {
        return MathHelper.getRandomIntegerInRange(rand, 0, 2);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean hasTileEntity()
    {
        return true;
    }

    private boolean isSolid(World world, int x, int y, int z)
    {
        if (World.doesBlockHaveSolidTopSurface(world, x, y, z))
        {
            return true;
        }
        else
        {
            Block block = world.getBlock(x, y, z);
            return block.canPlaceTorchOnTop(world, x, y, z);
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return world.isSideSolid(x - 1, y, z, EAST, true) || world.isSideSolid(x + 1, y, z, WEST, true) || world.isSideSolid(x, y, z - 1, SOUTH, true) || world.isSideSolid(x, y, z + 1, NORTH, true) || isSolid(world, x, y - 1, z) || isSolid(world, x, y + 1, z);
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        int rotation = metadata;

        if (side == 0 && isSolid(world, x, y + 1, z))
        {
            rotation = 6;
        }

        if (side == 1 && isSolid(world, x, y - 1, z))
        {
            rotation = 5;
        }

        if (side == 2 && world.isSideSolid(x, y, z + 1, NORTH, true))
        {
            rotation = 4;
        }

        if (side == 3 && world.isSideSolid(x, y, z - 1, SOUTH, true))
        {
            rotation = 3;
        }

        if (side == 4 && world.isSideSolid(x + 1, y, z, WEST, true))
        {
            rotation = 2;
        }

        if (side == 5 && world.isSideSolid(x - 1, y, z, EAST, true))
        {
            rotation = 1;
        }

        return rotation;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        super.updateTick(world, x, y, z, rand);

        if (world.getBlockMetadata(x, y, z) == 0)
        {
            onBlockAdded(world, x, y, z);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        if (world.getBlockMetadata(x, y, z) == 0)
        {
            if (world.isSideSolid(x - 1, y, z, EAST, true))
            {
                world.setBlockMetadataWithNotify(x, y, z, 1, 2);
            }
            else if (world.isSideSolid(x + 1, y, z, WEST, true))
            {
                world.setBlockMetadataWithNotify(x, y, z, 2, 2);
            }
            else if (world.isSideSolid(x, y, z - 1, SOUTH, true))
            {
                world.setBlockMetadataWithNotify(x, y, z, 3, 2);
            }
            else if (world.isSideSolid(x, y, z + 1, NORTH, true))
            {
                world.setBlockMetadataWithNotify(x, y, z, 4, 2);
            }
            else if (isSolid(world, x, y - 1, z))
            {
                world.setBlockMetadataWithNotify(x, y, z, 5, 2);
            }
            else if (isSolid(world, x, y + 1, z))
            {
                world.setBlockMetadataWithNotify(x, y, z, 6, 2);
            }
        }

        canPlaceAt(world, x, y, z);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        neighbourChanged(world, x, y, z, block);
    }

    protected boolean neighbourChanged(World world, int x, int y, int z, Block block)
    {
        if (canPlaceAt(world, x, y, z))
        {
            int metadata = world.getBlockMetadata(x, y, z);
            boolean canSupport = true;

            if (!world.isSideSolid(x - 1, y, z, EAST, true) && metadata == 1)
            {
                canSupport = false;
            }
            else if (!world.isSideSolid(x + 1, y, z, WEST, true) && metadata == 2)
            {
                canSupport = false;
            }
            else if (!world.isSideSolid(x, y, z - 1, SOUTH, true) && metadata == 3)
            {
                canSupport = false;
            }
            else if (!world.isSideSolid(x, y, z + 1, NORTH, true) && metadata == 4)
            {
                canSupport = false;
            }
            else if (!isSolid(world, x, y - 1, z) && metadata == 5)
            {
                canSupport = false;
            }
            else if (!isSolid(world, x, y + 1, z) && metadata == 6)
            {
                canSupport = false;
            }

            if (!canSupport)
            {
                if (new Random().nextInt(9) == 0)
                {
                    if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
                    {
                        float f = 0.7F;
                        double motionX = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
                        double motionY = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
                        double motionZ = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
                        EntityItem entityitem = new EntityItem(world, (double) x + motionX, (double) y + motionY, (double) z + motionZ, new ItemStack(energonType.getCrystal()));
                        entityitem.delayBeforeCanPickup = 10;
                        world.spawnEntityInWorld(entityitem);
                    }
                }
                else
                {
                    dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                }

                world.setBlockToAir(x, y, z);

                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    protected boolean canPlaceAt(World world, int x, int y, int z)
    {
        if (!canPlaceBlockAt(world, x, y, z))
        {
            if (world.getBlock(x, y, z) == this)
            {
                dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    @Override
    public MovingObjectPosition collisionRayTrace(World p_149731_1_, int p_149731_2_, int p_149731_3_, int p_149731_4_, Vec3 p_149731_5_, Vec3 p_149731_6_)
    {
        int l = p_149731_1_.getBlockMetadata(p_149731_2_, p_149731_3_, p_149731_4_) & 7;
        float f = 0.21F;

        if (l == 1)
        {
            setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        }
        else if (l == 2)
        {
            setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        }
        else if (l == 3)
        {
            setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        }
        else if (l == 4)
        {
            setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
        else if (l == 6)
        {
            f = 0.2F;
            setBlockBounds(0.5F - f, 0.0F + f * 2, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        }
        else
        {
            f = 0.2F;
            setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
        }

        return super.collisionRayTrace(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_, p_149731_5_, p_149731_6_);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TileEntityCrystal();
    }
}
