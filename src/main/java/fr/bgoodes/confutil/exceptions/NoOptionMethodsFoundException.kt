package fr.bgoodes.confutil.exceptions;

public class NoOptionMethodsFoundException extends Exception {
    public NoOptionMethodsFoundException(String className) {
        super("No option methods annotated with @Option found in class " + className);
    }
}