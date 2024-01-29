package fr.bgoodes.confutil

interface OptionChangeListener {
    fun onChange(key: String, oldValue: Any?, newValue: Any?): Boolean
}