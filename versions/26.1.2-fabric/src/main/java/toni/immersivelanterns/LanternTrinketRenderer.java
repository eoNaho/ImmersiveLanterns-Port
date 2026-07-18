package toni.immersivelanterns;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import eu.pb4.trinkets.api.client.TrinketRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.WeakHashMap;

final class LanternTrinketRenderer implements TrinketRenderer {
    private static final Map<Object, Pendulum> PENDULUMS = new WeakHashMap<>();

    static void register(Item item, LanternTrinketRenderer renderer) {
        TrinketRendererRegistry.registerRenderer(item, renderer);
    }

    @Override
    public void submit(ItemStack stack, TrinketSlotAccess slot,
                       EntityModel<? extends LivingEntityRenderState> model,
                       PoseStack poseStack, SubmitNodeCollector collector, int light,
                       LivingEntityRenderState renderState, float limbAngle, float limbDistance) {
        if (!(model instanceof HumanoidModel<?> humanoidModel)
                || !(renderState instanceof HumanoidRenderState humanoidState)) {
            return;
        }

        poseStack.pushPose();
        TrinketRenderer.translateToChest(poseStack, humanoidModel, humanoidState);
        // Port of Useful Lanterns' proven body-relative transform. The
        // translateToChest helper already contributes (0, .4, -.16), so these
        // are the remaining offsets from its (.29, .73, -.19) anchor.
        var config = LanternConfig.get();
        var side = config.rightSide ? -1.0F : 1.0F;
        var wearingTorsoArmor = !humanoidState.chestEquipment.isEmpty() || !humanoidState.legsEquipment.isEmpty();
        var armorOffset = wearingTorsoArmor ? -0.035F : 0.0F;
        var crouchY = humanoidState.isCrouching ? 0.04F : 0.0F;
        var crouchZ = humanoidState.isCrouching ? 0.05F : 0.0F;
        poseStack.translate(0.29F * side, 0.33F + crouchY, -0.03F + armorOffset + crouchZ);
        var entity = slot.inventory().getAttachment().getEntity();
        var swing = PENDULUMS.computeIfAbsent(entity, ignored -> new Pendulum(entity));
        if (config.physics) {
            swing.update(entity, config.physicsStrength);
        } else {
            swing.reset(entity);
        }
        var partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
        if (config.physics) {
            poseStack.mulPose(Axis.ZP.rotation((float) Math.toRadians(swing.roll(partialTick))));
            poseStack.mulPose(Axis.XP.rotation((float) Math.toRadians(swing.pitch(partialTick))));
        }
        poseStack.translate(0.0F, 0.30F, 0.0F);
        poseStack.scale(config.scale, config.scale, config.scale);
        poseStack.mulPose(Axis.XP.rotation((float) Math.PI));
        poseStack.mulPose(Axis.XP.rotation((float) Math.toRadians(-17.0)));
        poseStack.mulPose(Axis.YP.rotation((float) Math.toRadians(16.0 * side)));
        poseStack.mulPose(Axis.ZP.rotation((float) Math.toRadians(10.0 * side)));
        poseStack.translate(-0.5F, 0.0F, -0.5F);

        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            poseStack.popPose();
            return;
        }
        var blockState = blockItem.getBlock().defaultBlockState();
        var renderState3d = new BlockModelRenderState();
        new BlockModelResolver(Minecraft.getInstance().getModelManager()).update(
                renderState3d, blockState, BlockDisplayContext.create());
        renderState3d.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    private static final class Pendulum {
        private static final float GRAVITY = 0.07F;
        private static final float DAMPING = 0.72F;
        private static final float YAW_FORCE = 0.14F;
        private static final float SWING_MAX = 28.0F;
        private static final float BOUNCE_FORCE = 8.0F;
        private static final float BOUNCE_MIN = 0.3F;
        private static final float BOUNCE_CAP = 7.0F;
        private static final float BOUNCE_MAX_ANGLE = 28.0F;

        private int lastTick;
        private double previousY;
        private float previousBodyYaw;
        private boolean wasOnGround;
        private float pitch;
        private float previousPitch;
        private float pitchVelocity;
        private float roll;
        private float previousRoll;
        private float rollVelocity;
        private float peakFallSpeed;
        private float fallDistance;
        private float previousLocalForward;
        private float previousLocalSide;
        private boolean wasCrouching;

        private Pendulum(LivingEntity entity) {
            lastTick = entity.tickCount - 1;
            previousY = entity.getY();
            previousBodyYaw = entity.yBodyRot;
            wasOnGround = entity.onGround();
        }

        private void update(LivingEntity entity, float strength) {
            if (lastTick == entity.tickCount) {
                return;
            }
            lastTick = entity.tickCount;
            previousPitch = pitch;
            previousRoll = roll;

            var verticalSpeed = (float) (entity.getY() - previousY);
            previousY = entity.getY();
            var onGround = entity.onGround();
            if (!onGround && verticalSpeed < 0.0F) {
                peakFallSpeed = Math.max(peakFallSpeed, -verticalSpeed);
                fallDistance += -verticalSpeed;
            }

            var yawDelta = Mth.wrapDegrees(entity.yBodyRot - previousBodyYaw);
            previousBodyYaw = entity.yBodyRot;
            var movement = entity.getDeltaMovement();
            var yawRadians = Math.toRadians(entity.yBodyRot);
            var sinYaw = (float) Math.sin(yawRadians);
            var cosYaw = (float) Math.cos(yawRadians);
            var localForward = (float) (movement.z * cosYaw + movement.x * sinYaw);
            var localSide = (float) (movement.x * cosYaw - movement.z * sinYaw);
            var movementMultiplier = entity.isSprinting() ? 1.45F : entity.isCrouching() ? 0.65F : 1.0F;
            pitchVelocity += (localForward - previousLocalForward) * -18.0F * movementMultiplier * strength;
            rollVelocity += (localSide - previousLocalSide) * 16.0F * movementMultiplier * strength;
            previousLocalForward = localForward;
            previousLocalSide = localSide;

            if (entity.isCrouching() && !wasCrouching) {
                pitchVelocity += 3.5F * strength;
                rollVelocity += 1.8F * strength;
            }
            wasCrouching = entity.isCrouching();

            rollVelocity += yawDelta * YAW_FORCE * strength;
            rollVelocity -= roll * GRAVITY;
            rollVelocity *= DAMPING;
            roll = Mth.clamp(roll + rollVelocity, -SWING_MAX, SWING_MAX);

            if (!onGround && verticalSpeed < -0.05F) {
                var targetPitch = -(float) Math.min(Math.sqrt(fallDistance * 2.5F) * 26.0F, 90.0F);
                pitchVelocity += (targetPitch - pitch) * 0.08F * strength;
            }
            pitchVelocity -= pitch * GRAVITY;
            pitchVelocity *= DAMPING;
            pitch = Mth.clamp(pitch + pitchVelocity, -90.0F, BOUNCE_MAX_ANGLE);

            if (onGround && !wasOnGround) {
                var landingKick = Mth.clamp(peakFallSpeed * BOUNCE_FORCE, BOUNCE_MIN, BOUNCE_CAP);
                var returnFromRaisedPosition = -pitch * 0.35F;
                pitchVelocity += (landingKick + returnFromRaisedPosition) * strength;
                rollVelocity += landingKick * 0.2F * strength * ((entity.getId() & 1) == 0 ? 1.0F : -1.0F);
                peakFallSpeed = 0.0F;
                fallDistance = 0.0F;
            }
            if (onGround) {
                peakFallSpeed = 0.0F;
                fallDistance = 0.0F;
            }
            wasOnGround = onGround;
        }

        private float pitch(float partialTick) {
            return Mth.lerp(partialTick, previousPitch, pitch);
        }

        private float roll(float partialTick) {
            return Mth.lerp(partialTick, previousRoll, roll);
        }

        private void reset(LivingEntity entity) {
            lastTick = entity.tickCount;
            previousY = entity.getY();
            previousBodyYaw = entity.yBodyRot;
            wasOnGround = entity.onGround();
            previousLocalForward = 0.0F;
            previousLocalSide = 0.0F;
            pitch = previousPitch = pitchVelocity = 0.0F;
            roll = previousRoll = rollVelocity = 0.0F;
            peakFallSpeed = fallDistance = 0.0F;
            wasCrouching = entity.isCrouching();
        }
    }
}
