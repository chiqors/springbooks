package me.chiqors.springbooks.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.chiqors.springbooks.config.Constant;
import me.chiqors.springbooks.dto.LogDTO;
import me.chiqors.springbooks.model.Log;
import me.chiqors.springbooks.repository.LogRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.stream.Collectors;

@Service
public class LogService {
    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    // -----------------------------------------------------
    // ------------------ CONSTRUCTOR ----------------------
    // -----------------------------------------------------

    @Autowired
    public LogService(LogRepository logRepository, ObjectMapper objectMapper) {
        this.logRepository = logRepository;
        this.objectMapper = objectMapper;
    }

    // -----------------------------------------------------
    // ------------------ CONVERTER ------------------------
    // -----------------------------------------------------

    public LogDTO convertToDTO(Log log) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new LogDTO(formatter.format(log.getTimestamp()), log.getUrlPath(), log.getHostName(),
                log.getHttpMethod(), log.getHttpCode(), log.getMessage());
    }

    public Log convertToEntity(LogDTO logDTO) {
        return new Log(logDTO.getUrlPath(), logDTO.getHostName(), logDTO.getHttpMethod(),
                logDTO.getHttpCode(), logDTO.getMessage());
    }

    // -----------------------------------------------------
    // ------------------ LOG BACKUP -----------------------
    // -----------------------------------------------------

    public void saveLog(String urlPath, String hostName, String httpMethod, int httpCode, String message) {
        Log log = new Log(urlPath, hostName, httpMethod, httpCode, message);
        logRepository.save(log);
    }

    // -----------------------------------------------------
    // ------------------ SCHEDULED TASKS ------------------
    // -----------------------------------------------------

    @Scheduled(cron = "0 0 0 */7 * *") // Every 7 days at 00:00:00
    public void backupLogsFromDBtoLogsFolder() throws IOException {
        createLogBackupFolderIfNotExists();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String logFileName = Constant.LOG_FILE_PREFIX + dateFormatter.format(date) + Constant.LOG_FILE_EXTENSION;

            List<Log> logs = logRepository.getLogByTimestampBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
            String logsJson = objectMapper.writeValueAsString(logs);

            Path logFilePath = Paths.get(Constant.LOGS_DIRECTORY, logFileName);
            Files.write(logFilePath, logsJson.getBytes());

            logRepository.deleteAll(logs);
        }

        String zipFileName = Constant.LOG_FILE_PREFIX + dateFormatter.format(LocalDate.now()) + ".zip";
        zipLogFiles(zipFileName);

        deleteLogFilesExceptArchive(zipFileName);
    }

    private void createLogBackupFolderIfNotExists() throws IOException {
        File logDirectory = new File(Constant.LOGS_DIRECTORY);
        if (!logDirectory.exists()) {
            FileUtils.forceMkdir(logDirectory);
        }
    }

    private void zipLogFiles(String zipFileName) throws IOException {
        List<File> logFiles = getLogFiles();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                Files.newOutputStream(Paths.get(Constant.LOGS_DIRECTORY, zipFileName)))) {
            for (File logFile : logFiles) {
                ZipEntry zipEntry = new ZipEntry(logFile.getName());
                zipOutputStream.putNextEntry(zipEntry);
                Files.copy(logFile.toPath(), zipOutputStream);
                zipOutputStream.closeEntry();
            }
        }
    }

    private void deleteLogFilesExceptArchive(String archiveFileName) {
        List<File> logFiles = getLogFiles();
        for (File logFile : logFiles) {
            if (!logFile.getName().equals(archiveFileName)) {
                logFile.delete();
            }
        }
    }

    private List<File> getLogFiles() {
        File logDirectory = new File(Constant.LOGS_DIRECTORY);
        File[] files = logDirectory.listFiles((dir, name) -> name.endsWith(Constant.LOG_FILE_EXTENSION));
        if (files != null) {
            return new ArrayList<>(Arrays.asList(files));
        }
        return Collections.emptyList();
    }
}
