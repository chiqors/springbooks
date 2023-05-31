package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents a log entity.
 */
@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "logs")
public class Log {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "timestamp")
    private LocalDate timestamp;
    @Column(name = "url_path")
    private String urlPath;
    @Column(name = "host_name")
    private String hostName;
    @Column(name = "http_method")
    private String httpMethod;
    @Column(name = "http_code")
    private int httpCode;
    @Column(name = "message")
    private String message;

    public Log(String urlPath, String hostName, String httpMethod, int httpCode, String message) {
        this.timestamp = LocalDate.now();
        this.urlPath = urlPath;
        this.hostName = hostName;
        this.httpMethod = httpMethod;
        this.httpCode = httpCode;
        this.message = message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}