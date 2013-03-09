package com.thevoxelbox.voxelguest.modules.helper;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author TheCryoknight
 */
public final class HelperListener implements Listener
{
    private final HelperManager manager;

    /**
     * Creates a new helper listener instance.
     *
     * @param module The owning module.
     */
    public HelperListener(final HelperModule module)
    {
        manager = module.getManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if (player != null)
        {
            if (manager.isHelper(player))
            {
                final String msg = manager.getActiveRequests();
                if (msg != null)
                {
                    player.sendMessage(msg);
                }
            }
            if (manager.isNonAdminHelper(player))
            {
                player.setMetadata("isHelper", manager.getHelper(player));
            }
        }
    }
}
