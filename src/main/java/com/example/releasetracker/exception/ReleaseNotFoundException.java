package com.example.releasetracker.exception;

public class ReleaseNotFoundException extends RuntimeException{
    public ReleaseNotFoundException(Long id) {
        super("Release with id " + id + " not found");
    }
}
