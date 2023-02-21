package me.neznamy.tab.platforms.paper.nms.storage.packet;

import me.neznamy.tab.api.ProtocolVersion;
import me.neznamy.tab.api.protocol.PacketPlayOutScoreboardDisplayObjective;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class PacketPlayOutScoreboardDisplayObjectiveStorage {

    public static Class<?> CLASS;
    public static Constructor<?> CONSTRUCTOR;
    public static Field POSITION;
    public static Field OBJECTIVE_NAME;

    public static void load(NMSStorage nms) throws NoSuchMethodException {
        CONSTRUCTOR = CLASS.getConstructor(int.class, PacketPlayOutScoreboardObjectiveStorage.ScoreboardObjective);
        POSITION = nms.getFields(CLASS, int.class).get(0);
        OBJECTIVE_NAME = nms.getFields(CLASS, String.class).get(0);
    }

    public static Object build(PacketPlayOutScoreboardDisplayObjective packet, ProtocolVersion clientVersion) throws ReflectiveOperationException {
        return CONSTRUCTOR.newInstance(packet.getSlot(), NMSStorage.getInstance().newScoreboardObjective(packet.getObjectiveName()));
    }

    public static PacketPlayOutScoreboardDisplayObjective read(Object nmsPacket) throws ReflectiveOperationException {
        return new PacketPlayOutScoreboardDisplayObjective(
                POSITION.getInt(nmsPacket),
                (String) OBJECTIVE_NAME.get(nmsPacket)
        );
    }
}
