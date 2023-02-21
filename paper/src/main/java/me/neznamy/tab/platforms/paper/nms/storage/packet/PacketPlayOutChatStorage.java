package me.neznamy.tab.platforms.paper.nms.storage.packet;

import me.neznamy.tab.api.ProtocolVersion;
import me.neznamy.tab.api.protocol.PacketPlayOutChat;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;

import java.lang.reflect.Constructor;
<<<<<<< HEAD

@SuppressWarnings("rawtypes")
=======
import java.util.UUID;

@SuppressWarnings({"unchecked", "rawtypes"})
>>>>>>> 0a6a4f92 (Initial Paper plugin implementation)
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
<<<<<<< HEAD
        return CONSTRUCTOR.newInstance(component, packet.getType() == PacketPlayOutChat.ChatMessageType.GAME_INFO);
=======
        try {
            return CONSTRUCTOR.newInstance(component, packet.getType() == PacketPlayOutChat.ChatMessageType.GAME_INFO);
        } catch (Exception e) {
            //1.19.0
            return CONSTRUCTOR.newInstance(component, packet.getType().ordinal());
        }
        return packet;
>>>>>>> 0a6a4f92 (Initial Paper plugin implementation)
    }
}
