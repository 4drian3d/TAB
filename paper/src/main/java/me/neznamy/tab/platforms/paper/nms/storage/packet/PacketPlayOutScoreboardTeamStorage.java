package me.neznamy.tab.platforms.paper.nms.storage.packet;

import me.neznamy.tab.api.ProtocolVersion;
import me.neznamy.tab.api.chat.EnumChatFormat;
import me.neznamy.tab.api.chat.IChatBaseComponent;
import me.neznamy.tab.api.protocol.PacketPlayOutScoreboardTeam;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;
import me.neznamy.tab.shared.TAB;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PacketPlayOutScoreboardTeamStorage {

    public static Class<?> CLASS;
    public static Constructor<?> CONSTRUCTOR;
    public static Method Constructor_of;
    public static Method Constructor_ofBoolean;
    public static Method Constructor_ofString;
    public static Field NAME;
    public static Field ACTION;
    public static Field PLAYERS;
    public static Class<Enum> PlayerAction;

    public static Class<Enum> EnumNameTagVisibility;
    public static Class<Enum> EnumTeamPush;

    public static Class<?> ScoreboardTeam;
    public static Constructor<?> newScoreboardTeam;
    public static Method ScoreboardTeam_getPlayerNameSet;
    public static Method ScoreboardTeam_setNameTagVisibility;
    public static Method ScoreboardTeam_setCollisionRule;
    public static Method ScoreboardTeam_setPrefix;
    public static Method ScoreboardTeam_setSuffix;
    public static Method ScoreboardTeam_setColor;
    public static Method ScoreboardTeam_setAllowFriendlyFire;
    public static Method ScoreboardTeam_setCanSeeFriendlyInvisibles;

    public static void load(NMSStorage nms) throws NoSuchMethodException {
        newScoreboardTeam = ScoreboardTeam.getConstructor(nms.Scoreboard, String.class);
        NAME = nms.getFields(CLASS, String.class).get(0);
        ACTION = getInstanceFields(CLASS, int.class).get(0);
        PLAYERS = nms.getFields(CLASS, Collection.class).get(0);
        ScoreboardTeam_getPlayerNameSet = nms.getMethods(ScoreboardTeam, Collection.class).get(0);
        ScoreboardTeam_setCollisionRule = nms.getMethods(ScoreboardTeam, void.class, EnumTeamPush).get(0);
        ScoreboardTeam_setColor = nms.getMethods(ScoreboardTeam, void.class, nms.EnumChatFormat).get(0);
        Constructor_of = nms.getMethods(CLASS, CLASS, ScoreboardTeam).get(0);
        Constructor_ofBoolean = nms.getMethods(CLASS, CLASS, ScoreboardTeam, boolean.class).get(0);
        Constructor_ofString = nms.getMethods(CLASS, CLASS, ScoreboardTeam, String.class, PlayerAction).get(0);
    }

    public static Object build(PacketPlayOutScoreboardTeam packet, ProtocolVersion clientVersion) throws ReflectiveOperationException {
        NMSStorage nms = NMSStorage.getInstance();
        Object team = newScoreboardTeam.newInstance(nms.emptyScoreboard, packet.getName());
        String prefix = packet.getPlayerPrefix();
        String suffix = packet.getPlayerSuffix();
        if (clientVersion.getMinorVersion() < 13) {
            prefix = TAB.getInstance().getPlatform().getPacketBuilder().cutTo(prefix, 16);
            suffix = TAB.getInstance().getPlatform().getPacketBuilder().cutTo(suffix, 16);
        }
        ((Collection<String>)ScoreboardTeam_getPlayerNameSet.invoke(team)).addAll(packet.getPlayers());
        ScoreboardTeam_setAllowFriendlyFire.invoke(team, (packet.getOptions() & 0x1) > 0);
        ScoreboardTeam_setCanSeeFriendlyInvisibles.invoke(team, (packet.getOptions() & 0x2) > 0);
        createTeamModern(packet, clientVersion, team, prefix, suffix, nms);
        switch (packet.getAction()) {
            case 0:
                return Constructor_ofBoolean.invoke(null, team, true);
            case 1:
                return Constructor_of.invoke(null, team);
            case 2:
                return Constructor_ofBoolean.invoke(null, team, false);
            case 3:
                return Constructor_ofString.invoke(null, team, packet.getPlayers().iterator().next(), Enum.valueOf(PlayerAction, "ADD"));
            case 4:
                return Constructor_ofString.invoke(null, team, packet.getPlayers().iterator().next(), Enum.valueOf(PlayerAction, "REMOVE"));
            default:
                throw new IllegalArgumentException("Invalid action: " + packet.getAction());
        }
    }

    private static void createTeamModern(PacketPlayOutScoreboardTeam packet, ProtocolVersion clientVersion, Object team, String prefix, String suffix, NMSStorage nms) throws ReflectiveOperationException {
        if (prefix != null) ScoreboardTeam_setPrefix.invoke(team, nms.toNMSComponent(IChatBaseComponent.optimizedComponent(prefix), clientVersion));
        if (suffix != null) ScoreboardTeam_setSuffix.invoke(team, nms.toNMSComponent(IChatBaseComponent.optimizedComponent(suffix), clientVersion));
        EnumChatFormat format = packet.getColor() != null ? packet.getColor() : EnumChatFormat.lastColorsOf(prefix);
        ScoreboardTeam_setColor.invoke(team, Enum.valueOf(nms.EnumChatFormat, format.toString()));
        ScoreboardTeam_setNameTagVisibility.invoke(team, Enum.valueOf(EnumNameTagVisibility, String.valueOf(packet.getNameTagVisibility()).equals("always") ? "ALWAYS" : "NEVER"));
        ScoreboardTeam_setCollisionRule.invoke(team, Enum.valueOf(EnumTeamPush, String.valueOf(packet.getCollisionRule()).equals("always") ? "ALWAYS" : "NEVER"));
    }

<<<<<<< HEAD
=======
    private static void createTeamLegacy(PacketPlayOutScoreboardTeam packet, Object team, String prefix, String suffix, NMSStorage nms) throws ReflectiveOperationException {
        if (prefix != null) ScoreboardTeam_setPrefix.invoke(team, prefix);
        if (suffix != null) ScoreboardTeam_setSuffix.invoke(team, suffix);
        ScoreboardTeam_setNameTagVisibility.invoke(team, Enum.valueOf(EnumNameTagVisibility, String.valueOf(packet.getNameTagVisibility()).equals("always") ? "ALWAYS" : "NEVER"));
        ScoreboardTeam_setCollisionRule.invoke(team, Enum.valueOf(EnumTeamPush, String.valueOf(packet.getCollisionRule()).equals("always") ? "ALWAYS" : "NEVER"));
    }

>>>>>>> 0a6a4f92 (Initial Paper plugin implementation)
    private static List<Field> getInstanceFields(Class<?> clazz, Class<?> type) {
        List<Field> list = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == type && !Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                list.add(field);
            }
        }
        return list;
    }
}
