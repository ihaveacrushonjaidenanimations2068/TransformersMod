package fiskfille.tf.common.energon.power;

public interface IEnergyContainer
{
    float receiveEnergy(float amount);

    float extractEnergy(float amount);

    float getEnergy();

    float getMaxEnergy();
}
