package me.neznamy.tab.platforms.paper.nms;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import me.neznamy.tab.api.protocol.TabPacket;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;
import org.bukkit.Location;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Custom class for holding data used in PacketPlayOutEntityTeleport minecraft packet.
 */
@AllArgsConstructor @ToString
public class PacketPlayOutEntityTeleport implements TabPacket {

    /** NMS Fields */
    public static Class<?> CLASS;
    public static Constructor<?> CONSTRUCTOR;
    public static Field ENTITY_ID;
    public static Field X;
    public static Field Y;
    public static Field Z;
    public static Field YAW;
    public static Field PITCH;

    /** Packet's instance fields */
    private final int entityId;
    @NonNull private final Location location;

    /**
     * Loads all required Fields and throws Exception if something went wrong
     *
     * @param   nms
     *          NMS storage reference
     * @throws  NoSuchMethodException
     *          If something fails
     */
    public static void load(NMSStorage nms) throws NoSuchMethodException {
        ENTITY_ID = nms.getFields(CLASS, int.class).get(0);
        YAW = nms.getFields(CLASS, byte.class).get(0);
        PITCH = nms.getFields(CLASS, byte.class).get(1);
        CONSTRUCTOR = CLASS.getConstructor(nms.Entity);
        X = nms.getFields(CLASS, double.class).get(0);
        Y = nms.getFields(CLASS, double.class).get(1);
        Z = nms.getFields(CLASS, double.class).get(2);
    }

    /**
     * Converts this class into NMS packet
     *
     * @return  NMS packet
     * @throws  ReflectiveOperationException
     *          If something went wrong
     */
    public Object build() throws ReflectiveOperationException {
        NMSStorage nms = NMSStorage.getInstance();
        Object nmsPacket;
        nmsPacket = CONSTRUCTOR.newInstance(nms.dummyEntity);
        ENTITY_ID.set(nmsPacket, entityId);
        X.set(nmsPacket, location.getX());
        Y.set(nmsPacket, location.getY());
        Z.set(nmsPacket, location.getZ());
        YAW.set(nmsPacket, (byte) (location.getYaw()/360*256));
        PITCH.set(nmsPacket, (byte) (location.getPitch()/360*256));
        return nmsPacket;
    }
}