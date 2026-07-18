package toni.immersivelanterns.foundation;

import net.irisshaders.iris.shadows.ShadowRenderer;

public class IrisBridge {
    public static boolean isIrisRenderingShadows() {
        return ShadowRenderer.ACTIVE;
    }
}
