package com.example.demo.exception;

public class DuplicateBookException extends RuntimeException
{
    public DuplicateBookException(String msg)
    {
        super(msg);
    }
}
