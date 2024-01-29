package fr.bgoodes.confutil

@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Option(
    val key: String = "",
    val defaultValue: String = ""
)
