package fiskfille.tf.common.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class EntityLaserBeam extends EntityThrowable implements IEntityAdditionalSpawnData
{
    private boolean blue;

    public EntityLaserBeam(World world)
    {
        super(world);
    }

    public EntityLaserBeam(World world, EntityLivingBase entity, boolean blue)
    {
        super(world, entity);
        this.blue = blue;
    }

    public EntityLaserBeam(World world, double x, double y, double z, boolean blue)
    {
        super(world, x, y, z);
        this.blue = blue;
    }

    @Override
    protected float getGravityVelocity()
    {
        return 0.0F;
    }

    @Override
    protected float func_70182_d()
    {
        return 1.25F;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (isEntityAlive())
        {
            if (worldObj.isRemote)
            {
                for (int i = 0; i < 5; ++i)
                {
                    float f = rand.nextFloat() / 5;

                    worldObj.spawnParticle("reddust", posX + f, posY + 0.15F, posZ + f, blue ? -1.0D : 0.0D, 0.0D, blue ? 1.0D : 0.0D);
                }
            }

            if (ticksExisted > 7)
            {
                setDead();
            }
        }
    }

    @Override
    protected void onImpact(MovingObjectPosition mop)
    {
        Entity entityHit = mop.entityHit;

        if (entityHit != null)
        {
            EntityLivingBase thrower = getThrower();

            if (entityHit instanceof EntityLivingBase && thrower instanceof EntityPlayer && thrower != entityHit)
            {
                ((EntityLivingBase) entityHit).attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) thrower), 10.0F);
            }
        }

        if (mop.typeOfHit != MovingObjectType.BLOCK)
        {
            setDead();
        }
        else
        {
            if (worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ).isOpaqueCube())
            {
                setDead();
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);

        nbt.setBoolean("Blue", blue);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);

        blue = nbt.getBoolean("Blue");
    }

    @Override
    public void writeSpawnData(ByteBuf buf)
    {
        buf.writeBoolean(blue);
    }

    @Override
    public void readSpawnData(ByteBuf buf)
    {
        blue = buf.readBoolean();
    }
}