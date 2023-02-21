package me.neznamy.tab.platforms.paper;

import me.neznamy.tab.api.ProtocolVersion;
import me.neznamy.tab.api.protocol.*;
import me.neznamy.tab.platforms.paper.nms.PacketPlayOutEntityDestroy;
import me.neznamy.tab.platforms.paper.nms.PacketPlayOutEntityMetadata;
import me.neznamy.tab.platforms.paper.nms.PacketPlayOutEntityTeleport;
import me.neznamy.tab.platforms.paper.nms.PacketPlayOutSpawnEntityLiving;
import me.neznamy.tab.platforms.paper.nms.storage.packet.*;

public class PaperPacketBuilder extends PacketBuilder {

    {
        buildMap.put(PacketPlayOutEntityMetadata.class, (packet, version) -> ((PacketPlayOutEntityMetadata)packet).build());
        buildMap.put(PacketPlayOutEntityTeleport.class, (packet, version) -> ((PacketPlayOutEntityTeleport)packet).build());
        buildMap.put(PacketPlayOutEntityDestroy.class, (packet, version) -> ((PacketPlayOutEntityDestroy)packet).build());
        buildMap.put(PacketPlayOutSpawnEntityLiving.class, (packet, version) -> ((PacketPlayOutSpawnEntityLiving)packet).build());
        buildMap.put(PacketPlayOutPlayerListHeaderFooter.class, (packet, version) -> PacketPlayOutPlayerListHeaderFooterStorage.build((PacketPlayOutPlayerListHeaderFooter) packet, version));
        buildMap.put(PacketPlayOutChat.class, (packet, version) -> PacketPlayOutChatStorage.build((PacketPlayOutChat) packet, version));
        buildMap.put(PacketPlayOutScoreboardObjective.class, (packet, version) -> PacketPlayOutScoreboardObjectiveStorage.build((PacketPlayOutScoreboardObjective) packet, version));
        buildMap.put(PacketPlayOutScoreboardDisplayObjective.class, (packet, version) -> PacketPlayOutScoreboardDisplayObjectiveStorage.build((PacketPlayOutScoreboardDisplayObjective) packet, version));
        buildMap.put(PacketPlayOutScoreboardTeam.class, (packet, version) -> PacketPlayOutScoreboardTeamStorage.build((PacketPlayOutScoreboardTeam) packet, version));
        buildMap.put(PacketPlayOutScoreboardScore.class, (packet, version) -> PacketPlayOutScoreboardScoreStorage.build((PacketPlayOutScoreboardScore) packet, version));
    }

    @Override
    public Object build(PacketPlayOutBoss packet, ProtocolVersion clientVersion) throws ReflectiveOperationException {
        return packet;
    }

    @Override
    public Object build(PacketPlayOutPlayerInfo packet, ProtocolVersion clientVersion) throws ReflectiveOperationException {
        return PacketPlayOutPlayerInfoStorage.build(packet, clientVersion);
    }

    @Override
    public PacketPlayOutPlayerInfo readPlayerInfo(Object nmsPacket, ProtocolVersion clientVersion) throws ReflectiveOperationException {
        return PacketPlayOutPlayerInfoStorage.read(nmsPacket);
    }

    @Override
    public PacketPlayOutScoreboardObjective readObjective(Object nmsPacket) throws ReflectiveOperationException {
        return PacketPlayOutScoreboardObjectiveStorage.read(nmsPacket);
    }

    @Override
    public PacketPlayOutScoreboardDisplayObjective readDisplayObjective(Object nmsPacket) throws ReflectiveOperationException {
        return PacketPlayOutScoreboardDisplayObjectiveStorage.read(nmsPacket);
    }
}