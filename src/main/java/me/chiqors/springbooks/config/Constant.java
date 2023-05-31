package me.chiqors.springbooks.config;

public class Constant {
    public static final String HOST = "http://localhost:8080";
    public static final String API_PREFIX = "/api";

    public static final Integer ALLOWED_BORROW_SAME_BOOK = 2;
    public static final Integer ALLOWED_TOTAL_BORROW_BOOK = 5;

    public static final String LOGS_DIRECTORY = "logs";
    public static final String LOG_FILE_PREFIX = "app_";
    public static final String LOG_FILE_EXTENSION = ".log";
    public static final long MAX_LOG_FILE_SIZE = 10 * 1024 * 1024; // 10MB
}
