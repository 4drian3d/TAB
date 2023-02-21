package me.neznamy.tab.platforms.paper;

<<<<<<< HEAD
import com.destroystokyo.paper.profile.ProfileProperty;
=======
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
>>>>>>> 0a6a4f92 (Initial Paper plugin implementation)
import io.netty.channel.Channel;
import lombok.NonNull;
import me.neznamy.tab.api.chat.rgb.RGBUtils;
import me.neznamy.tab.api.protocol.PacketPlayOutBoss;
import me.neznamy.tab.api.protocol.PacketPlayOutChat;
import me.neznamy.tab.api.protocol.Skin;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;
import me.neznamy.tab.shared.ITabPlayer;
import me.neznamy.tab.shared.TAB;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TabPlayer implementation for Bukkit platform
 */
public class PaperTabPlayer extends ITabPlayer {

    /** Player's NMS handle (EntityPlayer), preloading for speed */
    private Object handle;

    /** Player's connection for sending packets, preloading for speed */
    private Object playerConnection;
    
    /** Bukkit BossBars the player can currently see */
    private final Map<UUID, BossBar> bossBars = new HashMap<>();

    /**
     * Constructs new instance with given bukkit player and protocol version
     *
     * @param   p
     *          bukkit player
     * @param   protocolVersion
     *          Player's protocol network id
     */
    public PaperTabPlayer(Player p, int protocolVersion) {
        super(p, p.getUniqueId(), p.getName(), TAB.getInstance().getConfiguration().getServerName(), p.getWorld().getName(), protocolVersion, true);
        try {
            handle = NMSStorage.getInstance().getHandle.invoke(player);
            playerConnection = NMSStorage.getInstance().PLAYER_CONNECTION.get(handle);
        } catch (ReflectiveOperationException e) {
            TAB.getInstance().getErrorManager().printError("Failed to get playerConnection of " + p.getName(), e);
        }
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        long time = System.nanoTime();
        boolean value = getPlayer().hasPermission(permission);
        TAB.getInstance().getCPUManager().addMethodTime("hasPermission", System.nanoTime()-time);
        return value;
    }

    @Override
    public int getPing() {
<<<<<<< HEAD
        return getPlayer().getPing();
=======
        try {
            int ping = NMSStorage.getInstance().PING.getInt(handle);
            if (ping > 10000 || ping < 0) ping = -1;
            return ping;
        } catch (IllegalAccessException e) {
            return -1;
        }
>>>>>>> 0a6a4f92 (Initial Paper plugin implementation)
    }

    @Override
    public void sendPacket(Object nmsPacket) {
        if (nmsPacket == null || !getPlayer().isOnline()) return;
        long time = System.nanoTime();
        try {
            if (nmsPacket instanceof PacketPlayOutBoss) {
                handle((PacketPlayOutBoss) nmsPacket);
            } else if (nmsPacket instanceof PacketPlayOutChat) {
                getPlayer().sendMessage(((PacketPlayOutChat) nmsPacket).getMessage().toLegacyText());
            } else {
                NMSStorage.getInstance().sendPacket.invoke(playerConnection, nmsPacket);
            }
        } catch (ReflectiveOperationException e) {
            TAB.getInstance().getErrorManager().printError("An error occurred when sending " + nmsPacket.getClass().getSimpleName(), e);
        }
        TAB.getInstance().getCPUManager().addMethodTime("sendPacket", System.nanoTime()-time);
    }

    /**
     * Handles PacketPlayOutBoss packet send request using Bukkit API,
     * since the API offers everything we need and makes us not need to
     * deal with NMS code at all.
     *
     * @param   packet
     *          packet request to handle using Bukkit API
     */
    private void handle(PacketPlayOutBoss packet) {
        BossBar bar = bossBars.get(packet.getId());
        if (packet.getAction() == PacketPlayOutBoss.Action.ADD) {
            if (bossBars.containsKey(packet.getId())) return;
<<<<<<< HEAD
            bar = Bukkit.createBossBar(RGBUtils.getInstance().convertToBukkitFormat(packet.getName(), getVersion().getMinorVersion() >= 16),
=======
            bar = Bukkit.createBossBar(RGBUtils.getInstance().convertToBukkitFormat(packet.getName(), getVersion().getMinorVersion() >= 16 && TAB.getInstance().getServerVersion().getMinorVersion() >= 16),
>>>>>>> 0a6a4f92 (Initial Paper plugin implementation)
                    BarColor.valueOf(packet.getColor().name()),
                    BarStyle.valueOf(packet.getOverlay().getBukkitName()));
            if (packet.isCreateWorldFog()) bar.addFlag(BarFlag.CREATE_FOG);
            if (packet.isDarkenScreen()) bar.addFlag(BarFlag.DARKEN_SKY);
            if (packet.isPlayMusic()) bar.addFlag(BarFlag.PLAY_BOSS_MUSIC);
            bar.setProgress(packet.getPct());
            bossBars.put(packet.getId(), bar);
            bar.addPlayer(getPlayer());
            return;
        }
        if (bar == null) return; //no idea how
        switch (packet.getAction()) {
        case REMOVE:
            bar.removePlayer(getPlayer());
            bossBars.remove(packet.getId());
            break;
        case UPDATE_PCT:
            bar.setProgress(packet.getPct());
            break;
        case UPDATE_NAME:
<<<<<<< HEAD
            bar.setTitle(RGBUtils.getInstance().convertToBukkitFormat(packet.getName(), getVersion().getMinorVersion() >= 16));
=======
            bar.setTitle(RGBUtils.getInstance().convertToBukkitFormat(packet.getName(), getVersion().getMinorVersion() >= 16 && TAB.getInstance().getServerVersion().getMinorVersion() >= 16));
>>>>>>> 0a6a4f92 (Initial Paper plugin implementation)
            break;
        case UPDATE_STYLE:
            bar.setColor(BarColor.valueOf(packet.getColor().name()));
            bar.setStyle(BarStyle.valueOf(packet.getOverlay().getBukkitName()));
            break;
        case UPDATE_PROPERTIES:
            bar = bossBars.get(packet.getId());
            processFlag(bar, packet.isCreateWorldFog(), BarFlag.CREATE_FOG);
            processFlag(bar, packet.isDarkenScreen(), BarFlag.DARKEN_SKY);
            processFlag(bar, packet.isPlayMusic(), BarFlag.PLAY_BOSS_MUSIC);
            break;
        default:
            break;
        }
    }

    /**
     * Sets BossBar flag to requested target value.
     *
     * @param   bar
     *          BossBar to set flag of
     * @param   targetValue
     *          Target value of the flag
     * @param   flag
     *          Flag to set value of
     */
    private void processFlag(BossBar bar, boolean targetValue, BarFlag flag) {
        if (targetValue && !bar.hasFlag(flag)) bar.addFlag(flag);
        if (!targetValue && bar.hasFlag(flag)) bar.removeFlag(flag);
    }

    @Override
    public boolean hasInvisibilityPotion() {
        return getPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY);
    }

    @Override
    public boolean isDisguised() {
        try {
<<<<<<< HEAD
            if (!((PaperPlatform)TAB.getInstance().getPlatform()).isLibsDisguisesEnabled()) return false;
=======
            if (!((BukkitPlatform)TAB.getInstance().getPlatform()).isLibsDisguisesEnabled()) return false;
>>>>>>> 0a6a4f92 (Initial Paper plugin implementation)
            return (boolean) Class.forName("me.libraryaddict.disguise.DisguiseAPI").getMethod("isDisguised", Entity.class).invoke(null, getPlayer());
        } catch (LinkageError | ReflectiveOperationException e) {
            //java.lang.NoClassDefFoundError: Could not initialize class me.libraryaddict.disguise.DisguiseAPI
            TAB.getInstance().getErrorManager().printError("Failed to check disguise status using LibsDisguises", e);
<<<<<<< HEAD
            ((PaperPlatform)TAB.getInstance().getPlatform()).setLibsDisguisesEnabled(false);
=======
            ((BukkitPlatform)TAB.getInstance().getPlatform()).setLibsDisguisesEnabled(false);
>>>>>>> 0a6a4f92 (Initial Paper plugin implementation)
            return false;
        }
    }

    @Override
    public Skin getSkin() {
<<<<<<< HEAD
        Collection<ProfileProperty> col = getPlayer().getPlayerProfile().getProperties();
        if (col.isEmpty()) return null; //offline mode
        for (ProfileProperty property : col) {
            if (property.getName().equals("textures")) {
                return new Skin(property.getValue(), property.getSignature());
            }
        }
        return null;
=======
        try {
            Collection<Property> col = ((GameProfile)NMSStorage.getInstance().getProfile.invoke(handle)).getProperties().get("textures");
            if (col.isEmpty()) return null; //offline mode
            Property property = col.iterator().next();
            return new Skin(property.getValue(), property.getSignature());
        } catch (ReflectiveOperationException e) {
            TAB.getInstance().getErrorManager().printError("Failed to get skin of " + getName(), e);
            return null;
        }
>>>>>>> 0a6a4f92 (Initial Paper plugin implementation)
    }

    @Override
    public Player getPlayer() {
        return (Player) player;
    }

    @Override
    public boolean isOnline() {
        return getPlayer().isOnline();
    }

    @Override
    public boolean isVanished() {
        return getPlayer().getMetadata("vanished").stream().anyMatch(MetadataValue::asBoolean);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getGamemode() {
        return getPlayer().getGameMode().getValue();
    }

    @Override
    public Channel getChannel() {
        try {
            if (NMSStorage.getInstance().CHANNEL != null)
                return (Channel) NMSStorage.getInstance().CHANNEL.get(NMSStorage.getInstance().NETWORK_MANAGER.get(playerConnection));
        } catch (IllegalAccessException e) {
            TAB.getInstance().getErrorManager().printError("Failed to get channel of " + getName(), e);
        }
        return null;
    }
}
