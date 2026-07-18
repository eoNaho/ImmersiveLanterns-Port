package toni.immersivelanterns;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

final class LanternConfigScreen extends Screen {
    private final Screen parent;
    private Component saveStatus = Component.empty();

    LanternConfigScreen(Screen parent) {
        super(Component.translatable("immersivelanterns.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        var config = LanternConfig.get();
        var left = width / 2 - 100;
        var top = height / 2 - 92;

        addRenderableWidget(Button.builder(sideLabel(), button -> {
            config.rightSide = !config.rightSide;
            button.setMessage(sideLabel());
        }).bounds(left, top, 200, 20).build());

        addRenderableWidget(Button.builder(physicsLabel(), button -> {
            config.physics = !config.physics;
            button.setMessage(physicsLabel());
        }).bounds(left, top + 26, 200, 20).build());

        addRenderableWidget(new ConfigSlider(left, top + 52,
                Component.translatable("immersivelanterns.config.scale"),
                (config.scale - 0.35F) / 0.90F,
                value -> config.scale = (float) (0.35F + value * 0.90F),
                () -> config.scale));

        addRenderableWidget(new ConfigSlider(left, top + 78,
                Component.translatable("immersivelanterns.config.physics_strength"),
                config.physicsStrength / 2.0F,
                value -> config.physicsStrength = (float) (value * 2.0F),
                () -> config.physicsStrength));

        addRenderableWidget(new ConfigSlider(left, top + 104,
                Component.translatable("immersivelanterns.config.damping"),
                (config.damping - 0.55F) / 0.37F,
                value -> config.damping = (float) (0.55F + value * 0.37F),
                () -> config.damping));

        addRenderableWidget(Button.builder(Component.translatable("immersivelanterns.config.reset"), button -> {
            config.reset();
            saveStatus = Component.translatable("immersivelanterns.config.reset_done");
            rebuildWidgets();
        }).bounds(left, top + 136, 98, 20).build());

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(left + 102, top + 136, 98, 20).build());
    }

    private Component sideLabel() {
        return Component.translatable("immersivelanterns.config.side",
                Component.translatable(LanternConfig.get().rightSide
                        ? "immersivelanterns.config.right"
                        : "immersivelanterns.config.left"));
    }

    private Component physicsLabel() {
        return Component.translatable("immersivelanterns.config.physics",
                Component.translatable(LanternConfig.get().physics ? "options.on" : "options.off"));
    }

    @Override
    public void onClose() {
        if (LanternConfig.get().save()) {
            minecraft.setScreen(parent);
        } else {
            saveStatus = Component.translatable("immersivelanterns.config.save_error");
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(font, title, width / 2, height / 2 - 108, 0xFFFFFFFF);
        var preview = minecraft.player == null ? ItemStack.EMPTY : ImmersiveLanterns.getEquipped(minecraft.player);
        if (!preview.isEmpty()) {
            var bob = LanternConfig.get().physics
                    ? (int) Math.round(Math.sin(System.currentTimeMillis() / 240.0) * 2.0)
                    : 0;
            graphics.item(preview, width / 2 - 8, height / 2 + 72 + bob);
        }
        if (!saveStatus.getString().isEmpty()) {
            graphics.centeredText(font, saveStatus, width / 2, height / 2 + 106,
                    saveStatus.getString().equals(Component.translatable("immersivelanterns.config.save_error").getString()) ? 0xFFFF5555 : 0xFF55FF55);
        }
    }

    private static final class ConfigSlider extends AbstractSliderButton {
        private final Component label;
        private final java.util.function.DoubleConsumer setter;
        private final java.util.function.DoubleSupplier getter;

        private ConfigSlider(int x, int y, Component label, double value,
                             java.util.function.DoubleConsumer setter,
                             java.util.function.DoubleSupplier getter) {
            super(x, y, 200, 20, Component.empty(), value);
            this.label = label;
            this.setter = setter;
            this.getter = getter;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.translatable("immersivelanterns.config.value", label,
                    String.format(java.util.Locale.ROOT, "%d%%", Math.round(getter.getAsDouble() * 100.0))));
        }

        @Override
        protected void applyValue() {
            setter.accept(value);
            updateMessage();
        }
    }
}
