package me.chiqors.springbooks.repository;

import me.chiqors.springbooks.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    @Query(value = "SELECT * FROM logs WHERE timestamp BETWEEN ?1 AND ?2", nativeQuery = true)
    List<Log> getLogByTimestampBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
