package fiskfille.tf.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.data.TFEntityData;
import fiskfille.tf.common.network.MessageGroundBridgeTeleport;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.tileentity.TileEntityControlPanel;
import fiskfille.tf.common.tileentity.TileEntityGroundBridgeTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class BlockGroundBridgeTeleporter extends BlockBreakable implements ITileEntityProvider
{
    public BlockGroundBridgeTeleporter()
    {
        super(TransformersMod.modid + ":ground_bridge_teleporter", Material.portal, false);
        setTickRandomly(true);
//        setLightLevel(1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }
    
    public boolean canPaneConnectTo(IBlockAccess world, int x, int y, int z, ForgeDirection dir)
    {
        return world.getBlock(x, y, z) == TFBlocks.groundBridgeTeleporter;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
    	float thickness = 0.125F;
        float f = 0.5F - thickness;
        float f1 = 0.5F + thickness;
        float f2 = 0.5F - thickness;
        float f3 = 0.5F + thickness;
        boolean flag  = canPaneConnectTo(world, x, y, z - 1, NORTH) || canPaneConnectTo(world, x, y, z + 1, SOUTH);
        boolean flag1 = canPaneConnectTo(world, x - 1, y, z, WEST ) || canPaneConnectTo(world, x + 1, y, z, EAST );

        if (!flag && flag1)
        {
            f = 0.0F;
            f1 = 1.0F;
        }

        if (flag && !flag1)
        {
            f2 = 0.0F;
            f3 = 1.0F;
        }

        setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public static boolean spawnTeleporter(World world, int x, int y, int z, TileEntityControlPanel tile)
    {
        if (!tile.errors.isEmpty())
        {
            return false;
        }
        else if (isNorthSouthFacingFramePresent(world, x, y, z))
        {
            fillNorthFacingFrame(world, x, y, z, TFBlocks.groundBridgeTeleporter, tile, false);
            return true;
        }
        else if (isEastWestFacingFramePresent(world, x, y, z))
        {
            fillEastFacingFrame(world, x, y, z, TFBlocks.groundBridgeTeleporter, tile, false);
            return true;
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        int i1 = 0;

        if (world.getBlock(x, y, z) == this)
        {
            i1 = func_149999_b(world.getBlockMetadata(x, y, z));

            if (i1 == 0)
            {
                return false;
            }

            if (i1 == 2 && side != 5 && side != 4)
            {
                return false;
            }

            if (i1 == 1 && side != 3 && side != 2)
            {
                return false;
            }
        }

        boolean flag = world.getBlock(x - 1, y, z) == this && world.getBlock(x - 2, y, z) != this;
        boolean flag1 = world.getBlock(x + 1, y, z) == this && world.getBlock(x + 2, y, z) != this;
        boolean flag2 = world.getBlock(x, y, z - 1) == this && world.getBlock(x, y, z - 2) != this;
        boolean flag3 = world.getBlock(x, y, z + 1) == this && world.getBlock(x, y, z + 2) != this;
        boolean flag4 = flag || flag1 || i1 == 1;
        boolean flag5 = flag2 || flag3 || i1 == 2;
        return flag4 && side == 4 || (flag4 && side == 5 || (flag5 && side == 2 || flag5 && side == 3));
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (!world.isRemote)
        {
            if (entity.ridingEntity == null && entity.riddenByEntity == null && entity instanceof EntityLivingBase)
            {
                TileEntityGroundBridgeTeleporter teleporter = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x, y, z);

                if (teleporter != null && teleporter.controlPanel != null)
                {
                    TileEntityControlPanel controlPanel = teleporter.controlPanel;

                    if (TFEntityData.getData(entity).groundBridgeCooldown == 0 && controlPanel.groundBridgeFramePos != null)
                    {
                        if (teleporter.returnPortal)
                        {
                            int posX = controlPanel.groundBridgeFramePos.posX;
                            int posY = controlPanel.groundBridgeFramePos.posY;
                            int posZ = controlPanel.groundBridgeFramePos.posZ;
                            float yawDiff = entity.rotationYaw - (controlPanel.portalDirection * 90 + 180);
                            float yaw = (controlPanel.getSrcPortalDirection() * 90 + 180) + yawDiff;

                            int motionXMultiplier = BlockBed.field_149981_a[controlPanel.portalDirection][0];
                            int motionZMultiplier = BlockBed.field_149981_a[controlPanel.portalDirection][1];

                            TFNetworkManager.networkWrapper.sendToDimension(new MessageGroundBridgeTeleport(entity, posX, posY, posZ, yaw, motionXMultiplier, motionZMultiplier), entity.dimension);

                            entity.setLocationAndAngles(posX + 0.5, posY + 1.0, posZ + 0.5, yaw, entity.rotationPitch);

                            entity.motionX *= motionXMultiplier == 0 ? -1 : 1;
                            entity.motionZ *= motionZMultiplier == 0 ? -1 : 1;
                        }
                        else
                        {
                            int posX = controlPanel.destX;
                            int posY = controlPanel.destY - 3;
                            int posZ = controlPanel.destZ;
                            float yawDiff = entity.rotationYaw - (controlPanel.getSrcPortalDirection() * 90 + 180);
                            float yaw = (controlPanel.portalDirection * 90 + 180 * 90 + 180) + yawDiff;

                            int motionXMultiplier = BlockBed.field_149981_a[controlPanel.portalDirection][0];
                            int motionZMultiplier = BlockBed.field_149981_a[controlPanel.portalDirection][1];

                            TFNetworkManager.networkWrapper.sendToDimension(new MessageGroundBridgeTeleport(entity, posX, posY, posZ, yaw, motionXMultiplier, motionZMultiplier), entity.dimension);

                            entity.setLocationAndAngles(posX + 0.5, posY + 1.0, posZ + 0.5, yaw, entity.rotationPitch);

                            entity.motionX *= motionXMultiplier == 0 ? -1 : 1;
                            entity.motionZ *= motionZMultiplier == 0 ? -1 : 1;
                        }
                    }

                    TFEntityData.getData(entity).groundBridgeCooldown = 20;
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        for (int l = 0; l < 4; ++l)
        {
            double particleX = (double) ((float) x + rand.nextFloat());
            double particleY = (double) ((float) y + rand.nextFloat());
            double particleZ = (double) ((float) z + rand.nextFloat());
            int i1 = rand.nextInt(2) * 2 - 1;
            double motionX = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            double motionY = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            double motionZ = ((double) rand.nextFloat() - 0.5D) * 0.5D;

            if (world.getBlock(x - 1, y, z) != this && world.getBlock(x + 1, y, z) != this)
            {
                particleX = (double) x + 0.5D + 0.25D * (double) i1;
                motionX = (double) (rand.nextFloat() * 2.0F * (float) i1);
            }
            else
            {
                particleZ = (double) z + 0.5D + 0.25D * (double) i1;
                motionZ = (double) (rand.nextFloat() * 2.0F * (float) i1);
            }

//            world.spawnParticle("smoke", particleX, particleY, particleZ, motionX / 10, motionY, motionZ / 10);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z)
    {
        return Item.getItemById(0);
    }

    public static int func_149999_b(int meta)
    {
        return meta & 3;
    }

    public static boolean isNorthSouthFacingFramePresent(IBlockAccess world, int x, int y, int z)
    {
        Block b = TFBlocks.groundBridgeFrame;

        if (world.getBlock(x, y, z) == b && world.getBlock(x - 1, y, z) == b && world.getBlock(x + 1, y, z) == b)
        {
            if (world.getBlock(x - 2, y + 1, z) == b && world.getBlock(x + 2, y + 1, z) == b)
            {
                int j = 0;

                for (int i = 0; i < 3; ++i)
                {
                    if (world.getBlock(x - 3, y + 2 + i, z) == b && world.getBlock(x + 3, y + 2 + i, z) == b)
                    {
                        ++j;
                    }
                }

                if (j == 3)
                {
                    if (world.getBlock(x - 2, y + 5, z) == b && world.getBlock(x + 2, y + 5, z) == b)
                    {
                        if (world.getBlock(x, y + 6, z) == b && world.getBlock(x - 1, y + 6, z) == b && world.getBlock(x + 1, y + 6, z) == b)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean isEastWestFacingFramePresent(IBlockAccess world, int x, int y, int z)
    {
        Block b = TFBlocks.groundBridgeFrame;

        if (world.getBlock(x, y, z) == b && world.getBlock(x, y, z - 1) == b && world.getBlock(x, y, z + 1) == b)
        {
            if (world.getBlock(x, y + 1, z - 2) == b && world.getBlock(x, y + 1, z + 2) == b)
            {
                int j = 0;

                for (int i = 0; i < 3; ++i)
                {
                    if (world.getBlock(x, y + 2 + i, z - 3) == b && world.getBlock(x, y + 2 + i, z + 3) == b)
                    {
                        ++j;
                    }
                }

                if (j == 3)
                {
                    if (world.getBlock(x, y + 5, z - 2) == b && world.getBlock(x, y + 5, z + 2) == b)
                    {
                        if (world.getBlock(x, y + 6, z) == b && world.getBlock(x, y + 6, z - 1) == b && world.getBlock(x, y + 6, z + 1) == b)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static void fillNorthFacingFrame(World world, int x, int y, int z, Block block, TileEntityControlPanel tile, boolean returnPortal)
    {
        for (int i = 0; i < 5; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
            	int k = 0;
            	int l = 0;
            	
            	if (world.getTileEntity(x - 1 + j, y + 1 + i, z) instanceof TileEntityGroundBridgeTeleporter)
                {
                    TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x - 1 + j, y + 1 + i, z);
                    k = tileentity.ticks;
                }
                if (world.getTileEntity(x - 2 + i, y + 2 + j, z) instanceof TileEntityGroundBridgeTeleporter)
                {
                    TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x - 2 + i, y + 2 + j, z);
                    l = tileentity.ticks;
                }
            	
                world.setBlock(x - 1 + j, y + 1 + i, z, block);
                world.setBlock(x - 2 + i, y + 2 + j, z, block);

                if (world.getTileEntity(x - 1 + j, y + 1 + i, z) instanceof TileEntityGroundBridgeTeleporter)
                {
                    TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x - 1 + j, y + 1 + i, z);
                    tileentity.controlPanel = tile;
                    tileentity.returnPortal = returnPortal;
                    tileentity.lastUpdate = 0;
                    tileentity.ticks = ++k;
                }
                if (world.getTileEntity(x - 2 + i, y + 2 + j, z) instanceof TileEntityGroundBridgeTeleporter)
                {
                    TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x - 2 + i, y + 2 + j, z);
                    tileentity.controlPanel = tile;
                    tileentity.returnPortal = returnPortal;
                    tileentity.lastUpdate = 0;
                    tileentity.ticks = l;
                }
            }
        }

        world.setBlockMetadataWithNotify(x, y + 2, z, 1, 2);
    }

    public static void fillEastFacingFrame(World world, int x, int y, int z, Block block, TileEntityControlPanel tile, boolean returnPortal)
    {
        for (int i = 0; i < 5; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
            	int k = 0;
            	int l = 0;
            	
            	if (world.getTileEntity(x, y + 1 + i, z - 1 + j) instanceof TileEntityGroundBridgeTeleporter)
                {
                    TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x, y + 1 + i, z - 1 + j);
                    k = tileentity.ticks;
                }
                if (world.getTileEntity(x, y + 2 + j, z - 2 + i) instanceof TileEntityGroundBridgeTeleporter)
                {
                    TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x, y + 2 + j, z - 2 + i);
                    l = tileentity.ticks;
                }
            	
                world.setBlock(x, y + 1 + i, z - 1 + j, block);
                world.setBlock(x, y + 2 + j, z - 2 + i, block);

                if (world.getTileEntity(x, y + 1 + i, z - 1 + j) instanceof TileEntityGroundBridgeTeleporter)
                {
                    TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x, y + 1 + i, z - 1 + j);
                    tileentity.controlPanel = tile;
                    tileentity.returnPortal = returnPortal;
                    tileentity.lastUpdate = 0;
                    tileentity.ticks = ++k;
                }
                if (world.getTileEntity(x, y + 2 + j, z - 2 + i) instanceof TileEntityGroundBridgeTeleporter)
                {
                    TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x, y + 2 + j, z - 2 + i);
                    tileentity.controlPanel = tile;
                    tileentity.returnPortal = returnPortal;
                    tileentity.lastUpdate = 0;
                    tileentity.ticks = l;
                }
            }
        }

        world.setBlockMetadataWithNotify(x, y + 2, z, 1, 2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TileEntityGroundBridgeTeleporter();
    }
}
