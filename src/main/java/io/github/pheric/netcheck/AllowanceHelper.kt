package io.github.pheric.netcheck

import org.bukkit.plugin.java.JavaPlugin
import java.io.*

class AllowanceHelper(val plugin: JavaPlugin) {
    private val filepath = "./plugins/NetCheck/exceptions.bin"
    private val exceptions = loadExceptions()

    init {
        plugin.logger.info("Loaded ${exceptions.size} player exceptions")
    }

    fun loadExceptions(): MutableList<String> {
        try {
            return ObjectInputStream(FileInputStream(filepath)).use {
                when (val exceptions = it.readObject()) {
                    is List<*> -> exceptions as MutableList<String>
                    else -> {
                        plugin.logger.warning("Failed to deserialize Player exceptions!")
                        mutableListOf()
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            plugin.logger.info("Player exceptions file not found; one will be created on server shutdown")

            return mutableListOf()
        }
    }

    fun saveExceptions() {
        if (exceptions.isEmpty()) return
        ObjectOutputStream(FileOutputStream(filepath)).use {
            it.writeObject(exceptions)
        }
    }

    fun isExempt(uuid: String) = exceptions.contains(uuid)

    fun addExempt(uuid: String) {
        if (!exceptions.contains(uuid))
            exceptions.add(uuid)
    }

    fun delExempt(uuid: String) = exceptions.remove(uuid)
}