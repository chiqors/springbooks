package me.chiqors.springbooks.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {
    @Value("${host}")
    private String host;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Value("${allowed.borrow.same.book}")
    private Integer allowedBorrowSameBook;
    @Value("${allowed.total.borrow.book}")
    private Integer allowedTotalBorrowBook;
    @Value("${logs.directory}")
    private String logsDirectory;
    @Value("${log.file.prefix}")
    private String logFilePrefix;
    @Value("${log.file.extension}")
    private String logFileExtension;
    @Value("${max.log.file.size}")
    private Integer maxLogFileSize;
}