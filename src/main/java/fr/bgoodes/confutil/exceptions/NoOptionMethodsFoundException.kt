package fr.bgoodes.confutil.exceptions

class NoOptionMethodsFoundException(className: String) :
    Exception("No option methods annotated with @Option found in class $className")