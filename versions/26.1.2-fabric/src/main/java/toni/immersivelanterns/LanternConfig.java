package toni.immersivelanterns;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class LanternConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("immersivelanterns.json");
    private static LanternConfig instance = new LanternConfig();

    boolean rightSide;
    boolean physics = true;
    float scale = 0.61F;
    float physicsStrength = 1.0F;
    float damping = 0.72F;

    static LanternConfig get() {
        return instance;
    }

    static void load() {
        if (Files.exists(FILE)) {
            try (var reader = Files.newBufferedReader(FILE)) {
                var loaded = GSON.fromJson(reader, LanternConfig.class);
                if (loaded != null) {
                    instance = loaded;
                }
            } catch (IOException ignored) {
            }
        }
        instance.scale = Math.max(0.35F, Math.min(1.25F, instance.scale));
        instance.physicsStrength = Math.max(0.0F, Math.min(2.0F, instance.physicsStrength));
        instance.damping = Math.max(0.55F, Math.min(0.92F, instance.damping));
    }

    void reset() {
        rightSide = false;
        physics = true;
        scale = 0.61F;
        physicsStrength = 1.0F;
        damping = 0.72F;
    }

    boolean save() {
        try {
            Files.createDirectories(FILE.getParent());
            try (var writer = Files.newBufferedWriter(FILE)) {
                GSON.toJson(this, writer);
            }
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}
