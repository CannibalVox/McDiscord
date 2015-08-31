package net.technicpack.mcdiscord.coremod;

import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class DiscordResourcePack implements IResourcePack {

    protected ModContainer ownerContainer;

    public DiscordResourcePack(ModContainer container) {
        this.ownerContainer = container;
    }

    @Override
    public InputStream getInputStream(ResourceLocation p_110590_1_) throws IOException {
        InputStream inputstream = getResourceStream(p_110590_1_);

        if (inputstream != null) {
            return inputstream;
        } else {
            throw new FileNotFoundException(p_110590_1_.getResourcePath());
        }
    }

    private String getResourcePath(ResourceLocation location) {
        return "/assets/mcdiscord/" + location.getResourcePath();
    }

    private InputStream getResourceStream(ResourceLocation resourceLocation) {
        InputStream lis = DiscordResourcePack.class.getResourceAsStream(getResourcePath(resourceLocation));
        return lis;
    }

    @Override
    public boolean resourceExists(ResourceLocation p_110589_1_) {
        return getResourceStream(p_110589_1_) != null;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Set getResourceDomains() {
        return ImmutableSet.of("mcdiscord");
    }

    @Override
    public IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException {
        return null;
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        return null;
    }

    @Override
    public String getPackName() {
        return "Default";
    }
}
