package net.technicpack.mcdiscord.coremod;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class DiscordAccessTransformer extends AccessTransformer {
    public DiscordAccessTransformer() throws IOException {
        super("mcdiscord_at.cfg");
    }
}
