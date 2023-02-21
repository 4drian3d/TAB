package me.neznamy.tab.platforms.paper.nms.datawatcher;

import lombok.AllArgsConstructor;
import me.neznamy.tab.api.ProtocolVersion;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.chat.IChatBaseComponent;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;
import me.neznamy.tab.shared.TAB;

import java.util.Optional;

/**
 * A class to help to assign DataWatcher items as positions often change per-version
 */
@AllArgsConstructor
public class DataWatcherHelper {

    /** NMS Fields */
    public static Class<?> DataWatcherRegistry;
    public static Class<?> DataWatcherSerializer;
    public static Object DataWatcherSerializer_BYTE;
    public static Object DataWatcherSerializer_FLOAT;
    public static Object DataWatcherSerializer_STRING;
    public static Object DataWatcherSerializer_OPTIONAL_COMPONENT;
    public static Object DataWatcherSerializer_BOOLEAN;

    /** Instance fields */
    private final int armorStandFlagsPosition = getArmorStandFlagsPosition();
    private final DataWatcher data;

    /**
     * Returns armor stand flags position based on server version
     *
     * @return  armor stand flags position based on server version
     */
    private int getArmorStandFlagsPosition() {
        return 15;
    }

    /**
     * Writes entity byte flags
     *
     * @param   flags
     *          flags to write
     */
    public void setEntityFlags(byte flags) {
        data.setValue(new DataWatcherObject(0, DataWatcherSerializer_BYTE), flags);
    }

    /**
     * Writes entity custom name with position based on server version and value depending on client version (RGB or not)
     *
     * @param   customName
     *          target custom name
     * @param   clientVersion
     *          client version
     */
    public void setCustomName(String customName, ProtocolVersion clientVersion) {
        data.setValue(new DataWatcherObject(2, DataWatcherSerializer_OPTIONAL_COMPONENT),
                Optional.ofNullable(NMSStorage.getInstance().toNMSComponent(IChatBaseComponent.optimizedComponent(customName), clientVersion)));
    }

    /**
     * Writes custom name visibility boolean
     *
     * @param   visible
     *          if visible or not
     */
    public void setCustomNameVisible(boolean visible) {
        data.setValue(new DataWatcherObject(3, DataWatcherSerializer_BOOLEAN), visible);
    }

    /**
     * Writes entity health
     *
     * @param   health
     *          health of entity
     */
    public void setHealth(float health) {
        data.setValue(new DataWatcherObject(6, DataWatcherSerializer_FLOAT), health);
    }

    /**
     * Writes armor stand flags
     *
     * @param   flags
     *          flags to write
     */
    public void setArmorStandFlags(byte flags) {
        data.setValue(new DataWatcherObject(armorStandFlagsPosition, DataWatcherSerializer_BYTE), flags);
    }

    /**
     * Writes wither invulnerable time
     * @param   time
     *          Time, apparently
     */
    public void setWitherInvulnerableTime(int time) {
        throw new UnsupportedOperationException("Not supported on 1.9+");
    }
}
