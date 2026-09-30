package com.sandboxpoc.hostruntime.util

import com.sandboxpoc.hostruntime.storage.StorageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Timestamped transition log. Every identity/runtime state transition goes
 * through here; entries persist in sandbox/lifecycle.log and are viewable in
 * Settings → Advanced.
 */
class EventLog(private val storage: StorageManager) {

    private val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    private fun write(level: String, tag: String, msg: String) {
        storage.appendLog("[${fmt.format(Date())}] $level/$tag: $msg")
    }

    fun i(tag: String, msg: String) = write("I", tag, msg)
    fun w(tag: String, msg: String) = write("W", tag, msg)
    fun e(tag: String, msg: String) = write("E", tag, msg)

    fun recent(): List<String> = storage.readLog()
}
