package io.github.pheric.netcheck

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.plugin.java.JavaPlugin

class JoinListener(val plugin: JavaPlugin, val ah: AllowanceHelper, val nc: CheckNetwork) : Listener {
    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onJoin(e: AsyncPlayerPreLoginEvent) {
        if (!ah.isExempt(e.uniqueId.toString()) && !nc.isWhitelisted(e.address.hostAddress)) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, plugin.config.getString("whitelist_message") ?: "You must connect from a whitelisted address or create an exception!")
        } else {
            ah.addExempt(e.uniqueId.toString())
        }
    }
}