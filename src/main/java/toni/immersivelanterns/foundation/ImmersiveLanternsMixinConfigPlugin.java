package toni.immersivelanterns.foundation;



import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import toni.lib.utils.PlatformUtils;

import java.util.List;
import java.util.Set;

#if FABRIC
import net.fabricmc.loader.api.FabricLoader;
#elif FORGE
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
#else
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
#endif

public class ImmersiveLanternsMixinConfigPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("Iris") && !(isModLoaded("iris") || isModLoaded("oculus"))) {
            return false;
        }



        return true;
    }

    public static boolean isIrisRenderingShadows() {
        if (isModLoaded("iris") || isModLoaded("oculus"))
            return IrisBridge.isIrisRenderingShadows();

        return false;
    }
    public static boolean isModLoaded(String modid) {
        #if FABRIC
        return FabricLoader.getInstance().isModLoaded(modid);
        #else
        if (ModList.get() == null) {
            return LoadingModList.get().getMods().stream().map(ModInfo::getModId).anyMatch(modid::equals);
        }
        return ModList.get().isLoaded(modid);
        #endif
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
