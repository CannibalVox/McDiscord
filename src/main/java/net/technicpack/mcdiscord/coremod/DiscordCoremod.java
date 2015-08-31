package net.technicpack.mcdiscord.coremod;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.technicpack.mcdiscord.coremod.asm.ClickableHudEditor;
import net.technicpack.mcdiscord.coremod.asm.IAsmEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.SortingIndex(value=1500)
public class DiscordCoremod implements IFMLLoadingPlugin {
    public static List<IAsmEditor> editors = new ArrayList<IAsmEditor>(1);

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "net.technicpack.mcdiscord.coremod.DiscordClassTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return "net.technicpack.mcdiscord.McDiscord";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        boolean isObfuscated = (Boolean) data.get("runtimeDeobfuscationEnabled");

        editors.clear();
        editors.add(new ClickableHudEditor(isObfuscated));
    }

    @Override
    public String getAccessTransformerClass() {
        return "net.technicpack.mcdiscord.coremod.DiscordAccessTransformer";
    }
}

