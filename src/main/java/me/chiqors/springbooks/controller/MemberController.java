package me.chiqors.springbooks.controller;

import me.chiqors.springbooks.config.ApplicationProperties;
import me.chiqors.springbooks.dto.MemberDTO;
import me.chiqors.springbooks.service.LogService;
import me.chiqors.springbooks.service.MemberService;
import me.chiqors.springbooks.util.FormValidation;
import me.chiqors.springbooks.util.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("${api.prefix}") // cant use ApplicationProperties.API_PREFIX since it is static final
public class MemberController {
    @Autowired
    private FormValidation formValidation;
    @Autowired
    private MemberService memberService;
    @Autowired
    private LogService logService;

    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * Retrieves all members based on optional filtering, sorting, and pagination parameters.
     *
     * @param name    Optional parameter to filter members by name.
     * @param page    Optional parameter to specify the page number to retrieve.
     * @param size    Optional parameter to specify the number of members to retrieve per page.
     * @return ResponseEntity containing a list of MemberDTOs and an HTTP status code.
     */
    @GetMapping("/members")
    public ResponseEntity<JSONResponse> getAllMembers(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false, defaultValue = "1") int page,
                                                      @RequestParam(required = false, defaultValue = "3") int size) {
        try {
            Page<MemberDTO> memberDTOList = memberService.getAllMembers(name, page, size);
            if (memberDTOList != null) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Members retrieved", memberDTOList, null);
                return ResponseEntity.ok(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.NOT_FOUND.value(), "Members not found", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve members", null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
        }
    }

    /**
     * Retrieves a member by Code.
     *
     * @param memberCode    Code of the member to retrieve.
     * @return ResponseEntity containing a MemberDTO and an HTTP status code.
     */
    @GetMapping("/member/{code}")
    public ResponseEntity<JSONResponse> getMemberByCode(@PathVariable("code") String memberCode) {
        try {
            MemberDTO memberDTO = memberService.getMemberByCode(memberCode);
            if (memberDTO != null) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Member retrieved", memberDTO, null);
                return ResponseEntity.ok(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.NOT_FOUND.value(), "Member not found", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve member", null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
        }
    }

    /**
     * Creates a new member.
     *
     * @param memberDTO    MemberDTO containing the member information to create.
     * @return ResponseEntity containing a MemberDTO and an HTTP status code.
     */
    @PostMapping("/members")
    public ResponseEntity<JSONResponse> createMember(@RequestBody MemberDTO memberDTO) {
        List<String> errors = formValidation.createMemberValidation(memberDTO);
        if (errors.isEmpty()) {
            MemberDTO createdMemberDTO = memberService.addMember(memberDTO);
            if (createdMemberDTO != null) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.CREATED.value(), "Member created", createdMemberDTO, null);
                logService.saveLog(applicationProperties.getApiPrefix() + "/members", applicationProperties.getHost(), "POST", HttpStatus.CREATED.value(), "Created member with code: " + createdMemberDTO.getMemberCode());
                return ResponseEntity.status(HttpStatus.CREATED).body(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create member", null, null);
                logService.saveLog(applicationProperties.getApiPrefix() + "/members", applicationProperties.getHost(), "POST", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create member");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
            }
        } else {
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Failed to create member", null, errors);
            logService.saveLog(applicationProperties.getApiPrefix() + "/members", applicationProperties.getHost(), "POST", HttpStatus.BAD_REQUEST.value(), "Failed to create member");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }

    /**
     * Updates a member by Code.
     *
     * @param memberDTO     MemberDTO containing the updated member information.
     * @return ResponseEntity containing a MemberDTO and an HTTP status code.
     */
    @PutMapping("/members")
    public ResponseEntity<JSONResponse> updateMember(@RequestBody MemberDTO memberDTO) {
        List<String> errors = formValidation.updateMemberValidation(memberDTO);
        if (errors.isEmpty()) {
            MemberDTO updatedMemberDTO = memberService.updateMember(memberDTO);
            if (updatedMemberDTO != null) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Member updated", updatedMemberDTO, null);
                logService.saveLog(applicationProperties.getApiPrefix() + "/members", applicationProperties.getHost(), "PUT", HttpStatus.OK.value(), "Updated member with code: " + updatedMemberDTO.getMemberCode());
                return ResponseEntity.ok(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update member", null, null);
                logService.saveLog(applicationProperties.getApiPrefix() + "/members", applicationProperties.getHost(), "PUT", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update member");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
            }
        } else {
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Failed to update member", null, errors);
            logService.saveLog(applicationProperties.getApiPrefix() + "/members", applicationProperties.getHost(), "PUT", HttpStatus.BAD_REQUEST.value(), "Failed to update member");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }

    /**
     * Deletes a member by Code.
     *
     * @param memberCode    Code of the member to delete.
     * @return ResponseEntity containing a String message and an HTTP status code.
     */
    @DeleteMapping("/members/{code}")
    public ResponseEntity<?> deleteMember(@PathVariable("code") String memberCode) {
        List<String> errors = formValidation.destroyMemberValidation(memberCode);
        if (errors.isEmpty()) {
            try {
                memberService.deleteMember(memberCode);
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Member deleted", null, null);
                logService.saveLog(applicationProperties.getApiPrefix() + "/members/" + memberCode, applicationProperties.getHost(), "DELETE", HttpStatus.OK.value(), "Deleted member with code: " + memberCode);
                return ResponseEntity.ok(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete member", null, null);
                logService.saveLog(applicationProperties.getApiPrefix() + "/members/" + memberCode, applicationProperties.getHost(), "DELETE", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete member");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
            }
        } else {
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Failed to delete member", null, errors);
            logService.saveLog(applicationProperties.getApiPrefix() + "/members/" + memberCode, applicationProperties.getHost(), "DELETE", HttpStatus.BAD_REQUEST.value(), "Failed to delete member");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }
}
