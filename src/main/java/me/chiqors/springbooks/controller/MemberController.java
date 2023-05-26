package me.chiqors.springbooks.controller;

import me.chiqors.springbooks.dto.MemberDTO;
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

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    /**
     * Retrieves all members based on optional filtering, sorting, and pagination parameters.
     *
     * @param name    Optional parameter to filter members by name.
     * @param sort    Optional parameter to specify the sorting order (e.g., "asc" or "desc").
     * @param page    Optional parameter to specify the page number for pagination.
     * @param size    Optional parameter to specify the page size for pagination.
     * @return ResponseEntity containing a list of MemberDTOs and an HTTP status code.
     */
    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String sort,
                                           @RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
        try {
            List<MemberDTO> memberDTOs = memberService.getAllMembers(name, sort, page, size);
            return new ResponseEntity<>(memberDTOs, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve members";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable("id") Long id) {
        try {
            MemberDTO memberDTO = memberService.getMemberById(id);
            return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve member";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/members")
    public ResponseEntity<?> createMember(@RequestBody MemberDTO memberDTO) {
        try {
            MemberDTO createdMemberDTO = memberService.addMember(memberDTO);
            return new ResponseEntity<>(createdMemberDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "Failed to create member";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long id, @RequestBody MemberDTO memberDTO) {
        try {
            MemberDTO updatedMemberDTO = memberService.updateMember(id, memberDTO);
            return new ResponseEntity<>(updatedMemberDTO, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Failed to update member";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable("id") Long id) {
        try {
            boolean isDeleted = memberService.deleteMember(id);
            if (isDeleted) {
                String successMessage = "Member with id: " + id + " deleted";
                return new ResponseEntity<>(successMessage, HttpStatus.OK);
            } else {
                String errorMessage = "Failed to delete member with id: " + id;
                return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to delete member with id: " + id;
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}