package io.github.pheric.netcheck

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*

class Commands(val cn: CheckNetwork, val ah: AllowanceHelper) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§cNetCheck: §rToo few arguments!")
            return false
        }
        when (args[0].toLowerCase()) {
            "add" -> {
                if (!sender.hasPermission("netcheck.add")){
                    sender.sendMessage("§cNetCheck: §rInsufficient permissions")
                    return false
                }

                if (args.size < 2) {
                    sender.sendMessage("§cNetCheck: §rYou must specify at least one UUID to add.")
                    return false
                }

                args.forEachIndexed { i, arg ->
                    if (i > 1) {
                        if (UUID.fromString(arg) == null ) {
                            sender.sendMessage("§cNetCheck: §rInvalid UUID: $arg")
                        } else {
                            ah.addExempt(arg)
                            sender.sendMessage("§aNetCheck: §rAdded UUID: $arg")
                        }
                    }
                }
            }
            "del" -> {
                if (!sender.hasPermission("netcheck.del")){
                    sender.sendMessage("§cNetCheck: §rInsufficient permissions")
                    return false
                }

                if (args.size < 2) {
                    sender.sendMessage("§cNetCheck: §rYou must specify at least one UUID to remove.")
                    return false
                }

                args.forEachIndexed { i, arg ->
                    if (i > 1) {
                        if (UUID.fromString(arg) == null ) {
                            sender.sendMessage("§cNetCheck: §rInvalid UUID: $arg")
                        } else {
                            ah.delExempt(arg)
                            sender.sendMessage("§aNetCheck: §rRemoved UUID: $arg")
                        }
                    }
                }
            }
            "shownets" -> {
                if (!sender.hasPermission("netcheck.shownets")) {
                    sender.sendMessage("§cNetCheck: §rInsufficient permissions")
                    return false
                }
                sender.sendMessage("§aNetCheck: §rThe following networks are registered:")
                cn.getNetworks().forEach { sender.sendMessage(it) }
                sender.sendMessage("§a--- end list ---")
            }
            else -> {
                sender.sendMessage("§a/netcheck <add|del|shownets> [uuid]...")
            }
        }

        return true
    }
}