package me.neznamy.tab.platforms.paper.nms;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import me.neznamy.tab.api.protocol.TabPacket;
import me.neznamy.tab.platforms.paper.nms.datawatcher.DataWatcher;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.UUID;

/**
 * Custom class for holding data used in PacketPlayOutSpawnEntityLiving minecraft packet.
 */
@AllArgsConstructor @ToString
public class PacketPlayOutSpawnEntityLiving implements TabPacket {

    /** NMS Fields */
    public static Class<?> CLASS;
    public static Class<?> EntityTypes;
    public static Constructor<?> CONSTRUCTOR;
    public static Field ENTITY_ID;
    public static Field ENTITY_TYPE;
    public static Field YAW;
    public static Field PITCH;
    public static Field UUID;
    public static Field X;
    public static Field Y;
    public static Field Z;
    public static Field DATA_WATCHER;
    public static Object EntityTypes_ARMOR_STAND;

    /** Entity types */
    public static final EnumMap<EntityType, Integer> entityIds = new EnumMap<>(EntityType.class);
    
    /** Packet's instance fields */
    private final int entityId;
    @NonNull private final UUID uniqueId;
    @NonNull private final EntityType entityType;
    @NonNull private final Location location;
    private final DataWatcher dataWatcher;

    /**
     * Loads all required Fields and throws Exception if something went wrong
     *
     * @param   nms
     *          NMS storage reference
     * @throws  NoSuchMethodException
     *          If something fails
     */
    public static void load(NMSStorage nms) throws NoSuchMethodException {
        entityIds.put(EntityType.ARMOR_STAND, 2);

        CONSTRUCTOR = CLASS.getConstructor(nms.Entity);
        ENTITY_ID = nms.getFields(CLASS, int.class).get(0);
        YAW = nms.getFields(CLASS, byte.class).get(0);
        PITCH = nms.getFields(CLASS, byte.class).get(1);
        UUID = nms.getFields(CLASS, UUID.class).get(0);
        X = nms.getFields(CLASS, double.class).get(2);
        Y = nms.getFields(CLASS, double.class).get(3);
        Z = nms.getFields(CLASS, double.class).get(4);
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
        Object nmsPacket = CONSTRUCTOR.newInstance(nms.dummyEntity);
        ENTITY_ID.set(nmsPacket, entityId);
        YAW.set(nmsPacket, (byte)(location.getYaw() * 256.0f / 360.0f));
        PITCH.set(nmsPacket, (byte)(location.getPitch() * 256.0f / 360.0f));
        UUID.set(nmsPacket, uniqueId);
        X.set(nmsPacket, location.getX());
        Y.set(nmsPacket, location.getY());
        Z.set(nmsPacket, location.getZ());
        int id = entityIds.get(entityType);
        ENTITY_TYPE.set(nmsPacket, EntityTypes_ARMOR_STAND); // :(
        return nmsPacket;
    }

    private int floor(double paramDouble) {
        int i = (int)paramDouble;
        return paramDouble < i ? i - 1 : i;
    }
}