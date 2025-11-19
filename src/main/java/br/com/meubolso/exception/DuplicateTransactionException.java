package br.com.meubolso.exception;

public class DuplicateTransactionException extends RuntimeException {
    public DuplicateTransactionException(String message) {
        super(message);
    }
}