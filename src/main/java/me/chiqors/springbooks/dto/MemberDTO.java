package me.chiqors.springbooks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate registeredAt;
    private String deleted;
}
