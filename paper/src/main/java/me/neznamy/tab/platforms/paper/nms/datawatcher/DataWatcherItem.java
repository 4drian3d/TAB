package me.neznamy.tab.platforms.paper.nms.datawatcher;

import lombok.Data;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;

import java.lang.reflect.Field;

/**
 * Class representing NMS Data Watcher Item
 */
@Data
public class DataWatcherItem {

    /** NMS Fields */
    public static Class<?> CLASS;
    public static Field TYPE;
    public static Field VALUE;

    /** Instance fields */
    private final DataWatcherObject type;
    private final Object value;

    /**
     * Loads all required Fields
     *
     * @param   nms
     *          NMS storage reference
     */
    public static void load(NMSStorage nms) {
        VALUE = nms.getFields(CLASS, Object.class).get(0);
        TYPE = nms.getFields(CLASS, DataWatcherObject.CLASS).get(0);
    }

    /**
     * Returns and instance of this class from given NMS item
     *
     * @param   nmsItem
     *          NMS item
     * @return  instance of this class with same data
     * @throws  ReflectiveOperationException
     *          if thrown by reflective operation
     */
    public static DataWatcherItem fromNMS(Object nmsItem) throws ReflectiveOperationException {
        NMSStorage nms = NMSStorage.getInstance();
        Object value = VALUE.get(nmsItem);
        Object nmsObject = TYPE.get(nmsItem);
        DataWatcherObject object = new DataWatcherObject(DataWatcherObject.SLOT.getInt(nmsObject), DataWatcherObject.SERIALIZER.get(nmsObject));
        return new DataWatcherItem(object, value);
    }
}