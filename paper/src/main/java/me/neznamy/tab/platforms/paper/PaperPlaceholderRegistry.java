package me.neznamy.tab.platforms.paper;

import me.neznamy.tab.api.TabConstants;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.placeholder.PlaceholderManager;
import me.neznamy.tab.shared.TAB;
import me.neznamy.tab.shared.placeholders.UniversalPlaceholderRegistry;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Bukkit registry to register bukkit-only and universal placeholders
 */
public class PaperPlaceholderRegistry extends UniversalPlaceholderRegistry {

    /** Vault Chat hook */
    private Chat chat;
    /** Detection for presence of Purpur's AFK getter */
    private Method purpurIsAfk;

    /**
     * Constructs new instance and loads hooks
     */
    public BukkitPlaceholderRegistry() {
        if (Bukkit.getPluginManager().isPluginEnabled(TabConstants.Plugin.VAULT)) {
            RegisteredServiceProvider<Chat> rspChat = Bukkit.getServicesManager().getRegistration(Chat.class);
            if (rspChat != null) chat = rspChat.getProvider();
        }
        try { purpurIsAfk = Player.class.getMethod("isAfk"); } catch (NoSuchMethodException ignored) {}
    }

    @SuppressWarnings("deprecation")
    @Override
    public void registerPlaceholders(PlaceholderManager manager) {
        manager.registerPlayerPlaceholder(TabConstants.Placeholder.DISPLAY_NAME, 500, p -> ((Player) p.getPlayer()).getDisplayName());
        manager.registerServerPlaceholder(TabConstants.Placeholder.TPS, 1000, () -> formatTPS(Bukkit.getTPS()[0]));
        manager.registerServerPlaceholder(TabConstants.Placeholder.MSPT, 1000, () -> format(Bukkit.getAverageTickTime()));

        Plugin essentials = Bukkit.getPluginManager().getPlugin(TabConstants.Plugin.ESSENTIALS);
        manager.registerPlayerPlaceholder(TabConstants.Placeholder.AFK, 500, p -> {
            if (essentials != null) {
                try {
                    Object user = essentials.getClass().getMethod("getUser", UUID.class).invoke(essentials, p.getUniqueId());
                    if ((boolean) user.getClass().getMethod("isAfk").invoke(user)) return true;
                } catch (ReflectiveOperationException e) {
                    TAB.getInstance().getErrorManager().printError("Failed to get AFK status of " + p.getName() + " using Essentials", e);
                }
            }
            return purpurIsAfk != null && ((Player)p.getPlayer()).isAfk();
        });
        // Removed placeholder, keeping the implementation to avoid placeholder breaking for users on update
        manager.registerPlayerPlaceholder(TabConstants.Placeholder.ESSENTIALS_NICK, -1, TabPlayer::getName);

        if (chat != null) {
            manager.registerPlayerPlaceholder(TabConstants.Placeholder.VAULT_PREFIX, 1000, p -> chat.getPlayerPrefix((Player) p.getPlayer()));
            manager.registerPlayerPlaceholder(TabConstants.Placeholder.VAULT_SUFFIX, 1000, p -> chat.getPlayerSuffix((Player) p.getPlayer()));
        } else {
            manager.registerServerPlaceholder(TabConstants.Placeholder.VAULT_PREFIX, -1, () -> "");
            manager.registerServerPlaceholder(TabConstants.Placeholder.VAULT_SUFFIX, -1, () -> "");
        }
        manager.registerPlayerPlaceholder(TabConstants.Placeholder.HEALTH, 100, p -> (int) Math.ceil(((Player) p.getPlayer()).getHealth()));
        super.registerPlaceholders(manager);
    }
}
