package me.chiqors.springbooks.service;

import static me.chiqors.springbooks.config.Constant.LOGS_DIRECTORY;
import static me.chiqors.springbooks.config.Constant.LOG_FILE_EXTENSION;
import static me.chiqors.springbooks.config.Constant.LOG_FILE_PREFIX;
import static me.chiqors.springbooks.config.Constant.MAX_LOG_FILE_SIZE;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class LogService {
    private final Gson gson;

    public LogService() {
        createLogsDirectory();
        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    private void createLogsDirectory() {
        File logsDirectory = new File(LOGS_DIRECTORY);
        if (!logsDirectory.exists()) {
            boolean created = logsDirectory.mkdirs();
            if (!created) {
                // Handle the case where logs directory creation fails
                System.err.println("Failed to create logs directory: " + logsDirectory.getAbsolutePath());
            }
        }
    }

    public void saveLog(String urlPath, String hostName, String httpMethod, int httpCode, String message) {
        JsonObject logEntry = generateLogEntry(urlPath, hostName, httpMethod, httpCode, message);
        String logFileName = getLogFileName();
        try {
            FileWriter fileWriter = new FileWriter(logFileName, true);
            fileWriter.write(gson.toJson(logEntry));
            fileWriter.write(System.lineSeparator()); // Add a line separator between log entries
            fileWriter.close();
        } catch (IOException e) {
            // Handle exception appropriately
        }
    }

    private JsonObject generateLogEntry(String urlPath, String hostName, String httpMethod, int httpCode, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());

        JsonObject logEntry = new JsonObject();
        logEntry.addProperty("timestamp", timestamp);
        logEntry.addProperty("urlPath", urlPath);
        logEntry.addProperty("hostName", hostName);
        logEntry.addProperty("httpMethod", httpMethod);
        logEntry.addProperty("httpCode", httpCode);
        logEntry.addProperty("message", message);

        return logEntry;
    }

    private String getLogFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());
        return LOGS_DIRECTORY + File.separator + LOG_FILE_PREFIX + currentDate + LOG_FILE_EXTENSION;
    }

    @Scheduled(cron = "0 0 0 * * *") // Runs daily at midnight (change the cron expression as per your requirement)
    public void backupLogs() {
        File logDirectory = new File(LOGS_DIRECTORY);
        if (!logDirectory.exists() || !logDirectory.isDirectory()) {
            return; // Log directory doesn't exist or is not a directory
        }

        File[] logFiles = logDirectory.listFiles();
        if (logFiles == null || logFiles.length == 0) {
            return; // No log files found
        }

        for (File logFile : logFiles) {
            if (logFile.length() > MAX_LOG_FILE_SIZE || isLogFileOlderThanWeek(logFile)) {
                backupLogFile(logFile);
            }
        }
    }

    private boolean isLogFileOlderThanWeek(File logFile) {
        long currentTimeMillis = System.currentTimeMillis();
        long fileLastModified = logFile.lastModified();
        long weekInMillis = 7 * 24 * 60 * 60 * 1000; // 7 days
        return (currentTimeMillis - fileLastModified) > weekInMillis;
    }

    private void backupLogFile(File logFile) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());
            String zipFileName = LOGS_DIRECTORY + File.separator + "backup" + File.separator + "logs_" + currentDate + ".zip";

            File backupDirectory = new File(LOGS_DIRECTORY + File.separator + "backup");
            if (!backupDirectory.exists()) {
                backupDirectory.mkdirs();
            }

            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            ZipEntry zipEntry = new ZipEntry(logFile.getName());
            zipOutputStream.putNextEntry(zipEntry);

            // Read the log file and write its contents to the zip file
            // Here you can use FileInputStream and ZipOutputStream to perform the file copy

            zipOutputStream.closeEntry();
            zipOutputStream.close();

            // Delete the original log file
            logFile.delete();
        } catch (IOException e) {
            // Handle exception appropriately
        }
    }
}
