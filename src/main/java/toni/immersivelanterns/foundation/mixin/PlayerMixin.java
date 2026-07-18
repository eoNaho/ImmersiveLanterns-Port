package toni.immersivelanterns.foundation.mixin;

import com.mojang.math.Constants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import toni.immersivelanterns.foundation.IPlayerLanternDataAccessor;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerLanternDataAccessor {
    @Unique
    private float immersiveLanterns$zAngle;
    @Unique
    private float immersiveLanterns$zVel;
    @Unique
    private float immersiveLanterns$xAngle;
    @Unique
    private float immersiveLanterns$xVel;
    @Unique
    private boolean immersiveLanterns$wasCrouching;
    @Unique
    private Vec3 immersiveLanterns$lastHipPosition;

    @Override
    public void immersiveLanterns$setLastHipPosition(Vec3 val) {
        this.immersiveLanterns$lastHipPosition = val;
    }

    @Override
    public Vec3 immersiveLanterns$getLastHipPosition() {
        return immersiveLanterns$lastHipPosition;
    }

    @Override
    public void immersiveLanterns$setWasCrouching(boolean val) {
        immersiveLanterns$wasCrouching = val;
    }

    @Override
    public boolean immersiveLanterns$getWasCrouching() {
        return immersiveLanterns$wasCrouching;
    }

    @Override
    public void immersiveLanterns$setZAngle(float val) {
        this.immersiveLanterns$zAngle = val;
    }

    @Override
    public void immersiveLanterns$setZVel(float val) {
        this.immersiveLanterns$zVel = val;
    }

    @Override
    public float immersiveLanterns$getZAngle() {
        return immersiveLanterns$zAngle;
    }

    @Override
    public float immersiveLanterns$getZVel() {
        return immersiveLanterns$zVel;
    }

    @Override
    public void immersiveLanterns$setXAngle(float val) {
        immersiveLanterns$xAngle = val;
    }

    @Override
    public float immersiveLanterns$getXAngle() {
        return immersiveLanterns$xAngle;
    }

    @Override
    public void immersiveLanterns$setXVel(float val) {
        immersiveLanterns$xVel = val;
    }

    @Override
    public float immersiveLanterns$getXVel() {
        return immersiveLanterns$xVel;
    }
}
