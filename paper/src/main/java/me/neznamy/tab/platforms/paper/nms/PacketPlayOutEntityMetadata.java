package me.neznamy.tab.platforms.paper.nms;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import me.neznamy.tab.api.protocol.TabPacket;
import me.neznamy.tab.platforms.paper.nms.datawatcher.DataWatcher;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Custom class for holding data used in PacketPlayOutEntityMetadata minecraft packet.
 */
@AllArgsConstructor @ToString
public class PacketPlayOutEntityMetadata implements TabPacket {

    /** NMS Fields */
    public static Class<?> CLASS;
    public static Constructor<?> CONSTRUCTOR;
    public static Field LIST;

    /** Packet's instance fields */
    private final int entityId;
    @NonNull private final DataWatcher dataWatcher;

    /**
     * Loads all required Fields and throws Exception if something went wrong
     *
     * @param   nms
     *          NMS storage reference
     * @throws  NoSuchMethodException
     *          If something fails
     */
    public static void load(NMSStorage nms) throws NoSuchMethodException {
        CONSTRUCTOR = CLASS.getConstructor(int.class, List.class);
        LIST = nms.getFields(CLASS, List.class).get(0);
    }

    /**
     * Converts this class into NMS packet
     *
     * @return  NMS packet
     * @throws  ReflectiveOperationException
     *          If something went wrong
     */
    public Object build() throws ReflectiveOperationException {
        return CONSTRUCTOR.newInstance(entityId, DataWatcher.packDirty.invoke(dataWatcher.build()));
    }
}