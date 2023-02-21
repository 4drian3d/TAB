package me.neznamy.tab.platforms.paper.nms.storage.packet;

import me.neznamy.tab.api.ProtocolVersion;
import me.neznamy.tab.api.protocol.PacketPlayOutScoreboardScore;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PacketPlayOutScoreboardScoreStorage {

    public static Class<?> CLASS;
    public static Constructor<?> CONSTRUCTOR_1_13;
    public static Constructor<?> CONSTRUCTOR_String;
    public static Constructor<?> CONSTRUCTOR;
    public static Class<?> ScoreboardScore;
    public static Constructor<?> newScoreboardScore;
    public static Method ScoreboardScore_setScore;
    public static Class<Enum> EnumScoreboardAction;

    public static void load(NMSStorage nms) throws NoSuchMethodException {
        newScoreboardScore = ScoreboardScore.getConstructor(nms.Scoreboard, PacketPlayOutScoreboardObjectiveStorage.ScoreboardObjective, String.class);
        CONSTRUCTOR_1_13 = CLASS.getConstructor(EnumScoreboardAction, String.class, String.class, int.class);
    }

    public static Object build(PacketPlayOutScoreboardScore packet, ProtocolVersion clientVersion) throws ReflectiveOperationException {
        NMSStorage nms = NMSStorage.getInstance();
        return CONSTRUCTOR_1_13.newInstance(Enum.valueOf(EnumScoreboardAction, packet.getAction().toString()), packet.getObjectiveName(), packet.getPlayer(), packet.getScore());
    }
}
