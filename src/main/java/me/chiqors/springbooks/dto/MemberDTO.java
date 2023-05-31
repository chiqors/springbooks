package me.chiqors.springbooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDTO {
    @JsonIgnore
    private long id;

    private String name;

    private String email;

    private String phone;

    @JsonProperty("registered_at")
    private String registeredAt;

    @JsonIgnore
    private boolean deleted;

    @JsonProperty("member_code")
    private String memberCode;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonIgnore
    private String deletedAt;

    public MemberDTO(String name, String email, String phone, String registeredAt, String memberCode, String updatedAt) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.registeredAt = registeredAt;
        this.memberCode = memberCode;
        this.updatedAt = updatedAt;
    }
}
