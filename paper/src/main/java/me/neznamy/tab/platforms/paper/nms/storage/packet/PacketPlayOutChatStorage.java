package me.neznamy.tab.platforms.paper.nms.storage.packet;

import me.neznamy.tab.api.ProtocolVersion;
import me.neznamy.tab.api.protocol.PacketPlayOutChat;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;

import java.lang.reflect.Constructor;

@SuppressWarnings("rawtypes")
public class PacketPlayOutChatStorage {

    public static Class<?> CLASS;
    public static Class<Enum> ChatMessageTypeClass;
    public static Constructor<?> CONSTRUCTOR;

    public static void load(NMSStorage nms) throws NoSuchMethodException {
        try {
            CONSTRUCTOR = CLASS.getConstructor(nms.IChatBaseComponent, boolean.class);
        } catch (NoSuchMethodException e) {
            //1.19.0
            CONSTRUCTOR = CLASS.getConstructor(nms.IChatBaseComponent, int.class);
        }
    }

    public static Object build(PacketPlayOutChat packet, ProtocolVersion clientVersion) throws ReflectiveOperationException {
        NMSStorage nms = NMSStorage.getInstance();
        Object component = nms.toNMSComponent(packet.getMessage(), clientVersion);
        return CONSTRUCTOR.newInstance(component, packet.getType() == PacketPlayOutChat.ChatMessageType.GAME_INFO);
    }
}
