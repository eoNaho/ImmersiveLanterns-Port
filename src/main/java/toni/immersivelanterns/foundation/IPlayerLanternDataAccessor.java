package toni.immersivelanterns.foundation;

import net.minecraft.world.phys.Vec3;

public interface IPlayerLanternDataAccessor {
    public void immersiveLanterns$setLastHipPosition(Vec3 val);
    public Vec3 immersiveLanterns$getLastHipPosition();

    public void immersiveLanterns$setWasCrouching(boolean val);
    public boolean immersiveLanterns$getWasCrouching();

    public void immersiveLanterns$setZAngle(float val);
    public float immersiveLanterns$getZAngle();

    public void immersiveLanterns$setZVel(float val);
    public float immersiveLanterns$getZVel();

    public void immersiveLanterns$setXAngle(float val);
    public float immersiveLanterns$getXAngle();

    public void immersiveLanterns$setXVel(float val);
    public float immersiveLanterns$getXVel();
}
