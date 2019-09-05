package io.github.pheric.netcheck

import org.bukkit.plugin.java.JavaPlugin

class NetCheck : JavaPlugin() {
    private val cn = CheckNetwork(this)
    private val ah = AllowanceHelper(this)

    override fun onEnable() {
        JoinListener(this, ah, cn)
        getCommand("netcheck")?.setExecutor(Commands(cn, ah))
    }

    override fun onDisable() {
        ah.saveExceptions()
    }
}
