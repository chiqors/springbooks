package me.chiqors.springbooks.controller;

import me.chiqors.springbooks.config.Constant;
import me.chiqors.springbooks.dto.MemberDTO;
import me.chiqors.springbooks.service.LogService;
import me.chiqors.springbooks.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final LogService logService;

    @Autowired
    public MemberController(MemberService memberService, LogService logService) {
        this.memberService = memberService;
        this.logService = logService;
    }


    /**
     * Retrieves all members based on optional filtering, sorting, and pagination parameters.
     *
     * @param name    Optional parameter to filter members by name.
     * @return ResponseEntity containing a list of MemberDTOs and an HTTP status code.
     */
    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers(@RequestParam(required = false) String name) {
        try {
            List<MemberDTO> memberDTOs = memberService.getAllMembers(name);
            return new ResponseEntity<>(memberDTOs, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve members";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a member by Code.
     *
     * @param memberCode    Code of the member to retrieve.
     * @return ResponseEntity containing a MemberDTO and an HTTP status code.
     */
    @GetMapping("/member/{code}")
    public ResponseEntity<?> getMemberByCode(@PathVariable("code") String memberCode) {
        try {
            MemberDTO memberDTO = memberService.getMemberByCode(memberCode);
            if (memberDTO != null) {
                return new ResponseEntity<>(memberDTO, HttpStatus.OK);
            } else {
                String errorMessage = "Member with code: " + memberCode + " not found";
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve member";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new member.
     *
     * @param memberDTO    MemberDTO containing the member information to create.
     * @return ResponseEntity containing a MemberDTO and an HTTP status code.
     */
    @PostMapping("/members")
    public ResponseEntity<?> createMember(@RequestBody MemberDTO memberDTO) {
        try {
            MemberDTO createdMemberDTO = memberService.addMember(memberDTO);
            logService.saveLog(Constant.API_PREFIX + "/members", Constant.HOST, "POST", HttpStatus.CREATED.value(), "Created member with code: " + createdMemberDTO.getMemberCode());
            return new ResponseEntity<>(createdMemberDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "Failed to create member";
            logService.saveLog(Constant.API_PREFIX + "/members", Constant.HOST, "POST", HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates a member by Code.
     *
     * @param memberCode    Code of the member to update.
     * @param memberDTO     MemberDTO containing the updated member information.
     * @return ResponseEntity containing a MemberDTO and an HTTP status code.
     */
    @PutMapping("/members/{code}")
    public ResponseEntity<?> updateMember(@PathVariable("code") String memberCode, @RequestBody MemberDTO memberDTO) {
        try {
            MemberDTO updatedMemberDTO = memberService.updateMember(memberCode, memberDTO);
            if (updatedMemberDTO != null) {
                logService.saveLog(Constant.API_PREFIX + "/members/" + memberCode, Constant.HOST, "PUT", HttpStatus.OK.value(), "Updated member with code: " + memberCode);
                return new ResponseEntity<>(updatedMemberDTO, HttpStatus.OK);
            } else {
                String errorMessage = "Member with code: " + memberCode + " not found";
                logService.saveLog(Constant.API_PREFIX + "/members/" + memberCode, Constant.HOST, "PUT", HttpStatus.NOT_FOUND.value(), errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to update member";
            logService.saveLog(Constant.API_PREFIX + "/members/" + memberCode, Constant.HOST, "PUT", HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
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
        try {
            boolean isDeleted = memberService.deleteMember(memberCode);
            if (isDeleted) {
                String successMessage = "Member with code: " + memberCode + " deleted";
                logService.saveLog(Constant.API_PREFIX + "/members/" + memberCode, Constant.HOST, "DELETE", HttpStatus.OK.value(), successMessage);
                return new ResponseEntity<>(successMessage, HttpStatus.OK);
            } else {
                String errorMessage = "Failed to delete member with code: " + memberCode;
                logService.saveLog(Constant.API_PREFIX + "/members/" + memberCode, Constant.HOST, "DELETE", HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to delete member with code: " + memberCode;
            logService.saveLog(Constant.API_PREFIX + "/members/" + memberCode, Constant.HOST, "DELETE", HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
