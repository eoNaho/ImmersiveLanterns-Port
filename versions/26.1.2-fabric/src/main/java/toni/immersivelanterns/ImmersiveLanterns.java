package toni.immersivelanterns;

import eu.pb4.trinkets.api.TrinketsApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LanternBlock;

public final class ImmersiveLanterns implements ModInitializer, ClientModInitializer {
    public static final String ID = "immersivelanterns";

    @Override
    public void onInitialize() {
        TrinketsApi.registerTrinketPredicate(
                Identifier.fromNamespaceAndPath(ID, "lantern"),
                (stack, slot, entity) -> isLantern(stack));
    }

    @Override
    public void onInitializeClient() {
        LanternConfig.load();
        var renderer = new LanternTrinketRenderer();
        for (var item : BuiltInRegistries.ITEM) {
            if (isLantern(new ItemStack(item))) {
                LanternTrinketRenderer.register(item, renderer);
            }
        }
    }

    public static boolean isEquipped(Player player) {
        return !getEquipped(player).isEmpty();
    }

    public static ItemStack getEquipped(Player player) {
        var attachment = TrinketsApi.getAttachment(player);
        if (attachment == null) {
            return ItemStack.EMPTY;
        }

        return attachment.findFirst(ImmersiveLanterns::isLantern)
                .map(slot -> slot.get().copy())
                .orElse(ItemStack.EMPTY);
    }

    private static boolean isLantern(ItemStack stack) {
        return stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof LanternBlock;
    }
}
