package net.happykoo.vcs.exception;

public class DomainNotFoundException extends RuntimeException {
    public DomainNotFoundException() {
        super();
    }

    public DomainNotFoundException(String message) {
        super(message);
    }
}
