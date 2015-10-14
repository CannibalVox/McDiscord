package net.technicpack.mcdiscord.event;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.technicpack.mcdiscord.data.ServerModel;
import org.lwjgl.opengl.GL11;

import java.net.URI;
import java.net.URISyntaxException;

public class HudHandler {
    private ResourceLocation minecraftPlayers = new ResourceLocation("mcdiscord:textures/gui/mc.png");
    private ResourceLocation discordText = new ResourceLocation("mcdiscord:textures/gui/discord.png");

    private ServerModel serverModel;

    public static HudHandler INSTANCE;

    public HudHandler(ServerModel serverModel) {
        this.serverModel = serverModel;
        this.INSTANCE = this;
    }

    @SideOnly(Side.CLIENT)
    public static boolean handleMouseClick(int xPos, int yPos, int mouseClicked) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);

        if (INSTANCE.serverModel.getInviteLink() != null && !INSTANCE.serverModel.getInviteLink().isEmpty() && xPos >= 48 && xPos < 160 && yPos >= sr.getScaledHeight() - 32 && yPos < sr.getScaledHeight() - 20) {
            try
            {
                final URI uri = new URI(INSTANCE.serverModel.getInviteLink());

                if (!uri.getScheme().toLowerCase().equals("http") && !uri.getScheme().toLowerCase().equals("https"))
                {
                    throw new URISyntaxException(INSTANCE.serverModel.getInviteLink(), "Unsupported protocol: " + uri.getScheme().toLowerCase());
                }

                visitLink(uri);
            }
            catch (URISyntaxException urisyntaxexception)
            {
                FMLLog.getLogger().error("Can\'t open url for MCDiscord", urisyntaxexception);
            }
        }
        return false;
    }

    private static void visitLink(URI link)
    {
        try
        {
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {link});
        }
        catch (Throwable throwable)
        {
            FMLLog.getLogger().error("Couldn\'t open link", throwable);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void hudRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (event.phase == TickEvent.Phase.START)
            return;

        if (!minecraft.isGuiEnabled())
            return;

        if (serverModel.getServerId() == null || serverModel.getServerId().isEmpty())
            return;

        if (minecraft.ingameGUI == null)
            return;
        if (minecraft.ingameGUI.getChatGUI() == null)
            return;

        GuiNewChat chat = minecraft.ingameGUI.getChatGUI();

        if (minecraft.gameSettings.chatVisibility == EntityPlayer.EnumChatVisibility.HIDDEN)
            return;

        float settingsChatAlpha = minecraft.gameSettings.chatOpacity * 0.9F + 0.1F;
        float timeChatAlpha = 255.0f;
        if (!chat.getChatOpen()) {
            if (chat.field_146253_i.size() < 1)
                return;
            ChatLine line = (ChatLine) chat.field_146253_i.get(0);
            if (line == null)
                return;

            int timeSinceLastMsg = minecraft.ingameGUI.getUpdateCounter() - line.getUpdatedCounter();

            if (timeSinceLastMsg >= 200)
                return;

            timeChatAlpha = getChatAlpha(timeSinceLastMsg);
        }

        int chatAlpha = (int)(timeChatAlpha * settingsChatAlpha);

        if (chatAlpha <= 3)
            return;

        EntityLivingBase entity = minecraft.renderViewEntity;
        if (entity == null || !(entity instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer)entity;

        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, sr.getScaledWidth_double(), sr.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, sr.getScaledHeight() - 32, -2000.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_LIGHTING);

        chat.drawRect(0, 4, MathHelper.ceiling_float_int(chat.func_146228_f() / chat.func_146244_h()) + 6, 18, chatAlpha / 2 << 24);
        GL11.glEnable(GL11.GL_BLEND);

        int mcPlayerCount = minecraft.thePlayer.sendQueue.playerInfoList.size();
        String playerCount = Integer.toString(mcPlayerCount);
        minecraft.fontRenderer.drawStringWithShadow(playerCount, 20, 8, 0xFFFFFF + (chatAlpha << 24));
        minecraft.getTextureManager().bindTexture(minecraftPlayers);
        renderTex(3, 5, 12, 12);

        minecraft.getTextureManager().bindTexture(discordText);
        renderTex(48, 5, 12, 12);

        int discordTextPlayerCount = serverModel.getDiscordPlayerCount();
        playerCount = Integer.toString(discordTextPlayerCount);
        minecraft.fontRenderer.drawStringWithShadow(playerCount, 65, 8, 0xFFFFFF + (chatAlpha << 24));

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glDisable(3042);

        GL11.glPopMatrix();
    }

    private void renderTex(float x, float y, float w, float h) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0.5), (double)(y + h), (double)0, 0, 1.0);
        tessellator.addVertexWithUV((double)(x + 0.5 + w), (double)(y + h), (double)0, 1.0, 1.0);
        tessellator.addVertexWithUV((double)(x + 0.5 + w), (double)(y + 0), (double)0, 1.0, 0);
        tessellator.addVertexWithUV((double)(x + 0.5), (double)(y + 0), (double)0, 0, 0);
        tessellator.draw();
    }

    private float getChatAlpha(int timeSinceLastMsg) {
        double timePercent = (double)timeSinceLastMsg / 200.0D;
        timePercent = 1.0D - timePercent;
        timePercent *= 10.0D;

        if (timePercent < 0.0D)
        {
            timePercent = 0.0D;
        }

        if (timePercent > 1.0D)
        {
            timePercent = 1.0D;
        }

        timePercent *= timePercent;
        return (int)(255.0D * timePercent);
    }
}
