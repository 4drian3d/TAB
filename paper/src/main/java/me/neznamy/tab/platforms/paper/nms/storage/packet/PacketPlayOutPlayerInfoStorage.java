package me.neznamy.tab.platforms.paper.nms.storage.packet;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.neznamy.tab.api.ProtocolVersion;
import me.neznamy.tab.api.chat.WrappedChatComponent;
import me.neznamy.tab.api.protocol.PacketPlayOutPlayerInfo;
import me.neznamy.tab.api.protocol.Skin;
import me.neznamy.tab.platforms.paper.nms.storage.nms.NMSStorage;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PacketPlayOutPlayerInfoStorage {

    public static Class<?> CLASS;
    public static Constructor<?> CONSTRUCTOR;
    public static Field ACTION;
    public static Field PLAYERS;
    public static Class<Enum> EnumPlayerInfoActionClass;
    public static Class<Enum> EnumGamemodeClass;
    public static Class<?> ProfilePublicKey;
    public static Class<?> ProfilePublicKey$a;

    //1.19.3+
    public static Class<?> ClientboundPlayerInfoRemovePacket;
    public static Class<?> RemoteChatSession;
    public static Class<?> RemoteChatSession$Data;
    public static Constructor<?> newClientboundPlayerInfoRemovePacket;
    public static Constructor<?> newRemoteChatSession$Data;
    public static Method ClientboundPlayerInfoRemovePacket_getEntries;
    public static Method RemoteChatSession$Data_getSessionId;
    public static Method RemoteChatSession$Data_getProfilePublicKey;

    public static void load(NMSStorage nms) throws NoSuchMethodException {
        newClientboundPlayerInfoRemovePacket = ClientboundPlayerInfoRemovePacket.getConstructor(List.class);
        CONSTRUCTOR = CLASS.getConstructor(EnumSet.class, Collection.class);
        ClientboundPlayerInfoRemovePacket_getEntries = nms.getMethods(ClientboundPlayerInfoRemovePacket, List.class).get(0);
        ACTION = nms.getFields(CLASS, EnumSet.class).get(0);
        newRemoteChatSession$Data = RemoteChatSession$Data.getConstructor(UUID.class, ProfilePublicKey$a);
        RemoteChatSession$Data_getSessionId = nms.getMethods(RemoteChatSession$Data, UUID.class).get(0);
        RemoteChatSession$Data_getProfilePublicKey = nms.getMethods(RemoteChatSession$Data, ProfilePublicKey$a).get(0);

        PLAYERS = nms.getFields(CLASS, List.class).get(0);
        PlayerInfoDataStorage.load(nms);
    }

    public static Object build(PacketPlayOutPlayerInfo packet, ProtocolVersion clientVersion) throws ReflectiveOperationException {
        Object nmsPacket;
        if (packet.getActions().contains(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER)) {
            return newClientboundPlayerInfoRemovePacket.newInstance(
                    packet.getEntries().stream().map(PacketPlayOutPlayerInfo.PlayerInfoData::getUniqueId).collect(Collectors.toList())
            );
        }
        Enum[] array = packet.getActions().stream().map(action -> Enum.valueOf(EnumPlayerInfoActionClass, action.toString())).toArray(Enum[]::new);
        nmsPacket = CONSTRUCTOR.newInstance(EnumSet.of(array[0], array), Collections.emptyList());
        List<Object> items = new ArrayList<>();
        for (PacketPlayOutPlayerInfo.PlayerInfoData data : packet.getEntries()) {
            items.add(PlayerInfoDataStorage.build(nmsPacket, data, clientVersion));
        }
        PLAYERS.set(nmsPacket, items);
        return nmsPacket;
    }

    public static PacketPlayOutPlayerInfo read(Object nmsPacket) throws ReflectiveOperationException {
        if (ClientboundPlayerInfoRemovePacket != null && ClientboundPlayerInfoRemovePacket.isInstance(nmsPacket)) {
            List<UUID> entries = (List<UUID>) ClientboundPlayerInfoRemovePacket_getEntries.invoke(nmsPacket);
            return new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                    entries.stream().map(PacketPlayOutPlayerInfo.PlayerInfoData::new).collect(Collectors.toList())
            );
        }
        List<PacketPlayOutPlayerInfo.PlayerInfoData> listData = new ArrayList<>();
        for (Object nmsData : (List<?>) PLAYERS.get(nmsPacket)) {
            listData.add(PlayerInfoDataStorage.read(nmsData));
        }
        PacketPlayOutPlayerInfo.EnumPlayerInfoAction[] array = ((EnumSet<?>) ACTION.get(nmsPacket)).stream().map(action ->
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.valueOf(action.toString())).toArray(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction[]::new);
        return new PacketPlayOutPlayerInfo(EnumSet.of(array[0], array), listData);
    }

    public static class PlayerInfoDataStorage {

        public static Class<?> CLASS;
        public static Constructor<?> newPlayerInfoData;
        public static Method PlayerInfoData_getProfile;
        public static Method PlayerInfoData_isListed;
        public static Method PlayerInfoData_getLatency;
        public static Method PlayerInfoData_getGamemode;
        public static Method PlayerInfoData_getDisplayName;
        public static Method PlayerInfoData_getProfilePublicKeyRecord;

        public static void load(NMSStorage nms) {
            newPlayerInfoData = CLASS.getConstructors()[0];
            PlayerInfoData_getProfile = nms.getMethods(CLASS, GameProfile.class).get(0);
            PlayerInfoData_getGamemode = nms.getMethods(CLASS, EnumGamemodeClass).get(0);
            PlayerInfoData_getDisplayName = nms.getMethods(CLASS, nms.IChatBaseComponent).get(0);
            for (Method m: nms.getMethods(CLASS, int.class)) {
                // do not take .hashCode(), which is final
                if (!Modifier.isFinal(m.getModifiers())) PlayerInfoData_getLatency = m;
            }
            PlayerInfoData_getProfilePublicKeyRecord = nms.getMethods(CLASS, RemoteChatSession$Data).get(0);
            PlayerInfoData_isListed = nms.getMethods(CLASS, boolean.class).get(0);
        }

        public static Object build(Object nmsPacket, PacketPlayOutPlayerInfo.PlayerInfoData data, ProtocolVersion clientVersion) throws ReflectiveOperationException {
            NMSStorage nms = NMSStorage.getInstance();
            GameProfile profile = new GameProfile(data.getUniqueId(), data.getName());
            if (data.getSkin() != null) profile.getProperties().put("textures",
                    new Property("textures", data.getSkin().getValue(), data.getSkin().getSignature()));
            return newPlayerInfoData.newInstance(
                    data.getUniqueId(),
                    profile,
                    data.isListed(),
                    data.getLatency(),
                    data.getGameMode() == null ? null : Enum.valueOf(EnumGamemodeClass, data.getGameMode().toString()),
                    data.getDisplayName() == null ? null : nms.toNMSComponent(data.getDisplayName(), clientVersion),
                    data.getProfilePublicKey() == null ? null : newRemoteChatSession$Data.newInstance(data.getChatSessionId(), data.getProfilePublicKey()));

        }

        public static PacketPlayOutPlayerInfo.PlayerInfoData read(Object nmsData) throws ReflectiveOperationException {
            Object nmsGameMode = PlayerInfoData_getGamemode.invoke(nmsData);
            PacketPlayOutPlayerInfo.EnumGamemode gameMode = (nmsGameMode == null) ? null : PacketPlayOutPlayerInfo.EnumGamemode.valueOf(nmsGameMode.toString());
            GameProfile profile = (GameProfile) PlayerInfoData_getProfile.invoke(nmsData);
            Object nmsComponent = PlayerInfoData_getDisplayName.invoke(nmsData);
            Skin skin = null;
            if (!profile.getProperties().get("textures").isEmpty()) {
                Property pr = profile.getProperties().get("textures").iterator().next();
                skin = new Skin(pr.getValue(), pr.getSignature());
            }
            boolean listed = true;
            UUID chatSessionId = null;
            Object profilePublicKey;
            listed = (boolean) PlayerInfoData_isListed.invoke(nmsData);
            Object remoteChatSession = PlayerInfoData_getProfilePublicKeyRecord.invoke(nmsData);
            chatSessionId = remoteChatSession == null ? null : (UUID) RemoteChatSession$Data_getSessionId.invoke(remoteChatSession);
            profilePublicKey = remoteChatSession == null ? null : RemoteChatSession$Data_getProfilePublicKey.invoke(remoteChatSession);
            return new PacketPlayOutPlayerInfo.PlayerInfoData(
                    profile.getName(),
                    profile.getId(),
                    skin,
                    listed,
                    (int) PlayerInfoData_getLatency.invoke(nmsData),
                    gameMode,
                    nmsComponent == null ? null : new WrappedChatComponent(nmsComponent),
                    chatSessionId,
                    profilePublicKey);
        }
    }
}
