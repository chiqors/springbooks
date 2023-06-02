package me.chiqors.springbooks.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {
    @Value("${host}")
    public static String HOST;

    @Value("${api.prefix}")
    public static String API_PREFIX;

    @Value("${allowed.borrow.same.book}")
    public static Integer ALLOWED_BORROW_SAME_BOOK;

    @Value("${allowed.total.borrow.book}")
    public static Integer ALLOWED_TOTAL_BORROW_BOOK;

    @Value("${logs.directory}")
    public static String LOGS_DIRECTORY;

    @Value("${log.file.prefix}")
    public static String LOG_FILE_PREFIX;

    @Value("${log.file.extension}")
    public static String LOG_FILE_EXTENSION;

    @Value("${max.log.file.size}")
    public static Integer MAX_LOG_FILE_SIZE;
}