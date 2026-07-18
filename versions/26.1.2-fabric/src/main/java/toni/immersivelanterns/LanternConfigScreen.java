package toni.immersivelanterns;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

final class LanternConfigScreen extends Screen {
    private final Screen parent;

    LanternConfigScreen(Screen parent) {
        super(Component.translatable("immersivelanterns.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        var config = LanternConfig.get();
        var left = width / 2 - 100;
        var top = height / 2 - 80;

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

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(left, top + 116, 200, 20).build());
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
        LanternConfig.get().save();
        minecraft.setScreen(parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(font, title, width / 2, height / 2 - 108, 0xFFFFFFFF);
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
                    String.format(java.util.Locale.ROOT, "%.2f", getter.getAsDouble())));
        }

        @Override
        protected void applyValue() {
            setter.accept(value);
            updateMessage();
        }
    }
}
