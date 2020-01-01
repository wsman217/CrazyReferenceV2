package me.wsman217.crazyreference.handlers;

import me.wsman217.crazyreference.CrazyReference;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void playerPreJoinEvent(PlayerLoginEvent e) {
        CrazyReference.ipHandler.insert(e.getRealAddress().toString(), e.getPlayer().getUniqueId());
    }
}
