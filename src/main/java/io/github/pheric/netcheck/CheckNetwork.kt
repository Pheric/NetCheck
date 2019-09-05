package io.github.pheric.netcheck

import com.google.common.net.InetAddresses
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class CheckNetwork(val plugin: JavaPlugin) {
    // map: network + prefix
    private var networks = run {
        plugin.logger.info("Importing networks")
        val ret = mutableMapOf<String, Int>()
        plugin.config.getStringList("networks").filter {
            val split = it.split('/')
            split.size > 1 && InetAddresses.isInetAddress(split[0]) && !it.contains(':')
        }.forEach {
            val split = it.split('/')
            ret[split[0]] = split[1].toInt()
        }

        ret
    }

    fun isWhitelisted(clientAddr: String): Boolean {
        plugin.logger.info("isWhitelisted: $clientAddr\tLen: ${networks.size}\tSection: ${plugin.config.getStringList("networks")}")
        val clientAddr = clientAddr.split('.').map { it.toInt() }

        return networks.keys.any {
            val addr = it.split('.').map { i -> i.toInt() }
            val net = networks[it]!!

            val addrNet = getNetwork(addr, net)
            val clientNet = getNetwork(clientAddr, net)
            plugin.logger.info("WL address: $addr\tWL network: $addrNet}\nClient address: $clientAddr\tClient network: $clientNet}\nNetwork addresses match: ${addrNet == clientNet}")

            addrNet == clientNet
        }
    }

    private fun getNetwork(addr: List<Int>, prefix: Int): String {
        var prefix = prefix
        var clientNetStr = "" // 138.247.0.0/16
        for (i in 0..3) {
            var mask = 255
            while (mask > 0 && prefix > 0) {
                mask = mask shr 1
                prefix--
            }
            mask = 255 - mask

            clientNetStr += addr[i] and mask
            if (i < 3) clientNetStr += '.'
        }

        return clientNetStr
    }

    fun getNetworks(): List<String> {
        val nets = mutableListOf<String>()
        networks.forEach {(addr, prefix) -> nets.add("$addr/$prefix")}

        return nets
    }
}