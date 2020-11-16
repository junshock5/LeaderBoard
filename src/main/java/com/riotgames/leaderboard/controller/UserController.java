package com.riotgames.leaderboard.controller;

import com.riotgames.leaderboard.dto.UserDTO;
import com.riotgames.leaderboard.service.UserService;
import io.swagger.annotations.Api;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"users"})
@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원 추가 메서드.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus signUp(@RequestBody UserDTO userDTO) {
        if (UserDTO.hasNullDataBeforeSignup(userDTO)) {
            throw new NullPointerException("회원 추가시 필수 데이터를 모두 입력해야 합니다.");
        }
        userService.register(userDTO);
        return HttpStatus.OK;
    }

    /**
     * 회원 수정 메서드.
     */
    @PatchMapping("{userId}")
    public HttpStatus updateAddress(@RequestBody UserUpdateRequest userUpdateRequest) {
        long Id = 0;
        UserDTO userDTO  = userUpdateRequest.getUserDTO();

        try {
            userService.updateUser(userDTO);
        } catch (RuntimeException e) {
            log.info("updateUser 실패", e);
        }
        return HttpStatus.OK;
    }

    /**
     * 회원 ID 삭제 메서드.
     */
    @DeleteMapping("{userId}")
    public HttpStatus deleteId(@RequestBody long id) {
        try {
            userService.deleteId(id);
        } catch (RuntimeException e) {
            log.info("deleteID 실패", e);
        }
        return HttpStatus.OK;
    }

    /**
     * 회원 전체 수 반환 메서드.
     */
    @GetMapping("totalCount")
    public long totalCount() {
        try {
            return userService.totalCount();
        } catch (RuntimeException e) {
            log.info("totalCount 실패", e);
        }
        return 0;
    }

    /**
     * 회원 계층 반환
     */
    @GetMapping("userTier")
    public String getUserTier(long id) {
        try {
            return userService.getUserTier(id);
        } catch (RuntimeException e) {
            log.info("getUserTier 실패", e);
        }
        return null;
    }

    /**
     * 회원 상위 10명 반환
     */
    @GetMapping("top10")
    public List<UserDTO> top10() {
        try {
            return userService.top10();
        } catch (RuntimeException e) {
            log.info("top10 실패", e);
        }
        return null;
    }

    /**
     * 특정 회원 특정 범위 interval 반환
     */
    @GetMapping("rangeByRankAndInterval")
    public List<UserDTO> rangeByRankAndInterval(long id,int interval) {
        try {
            return userService.getUsersRangeByIdAndInterval(id,interval);
        } catch (RuntimeException e) {
            log.info("rangeByRankAndInterval 실패", e);
        }
        return null;
    }


    // -------------- request 객체 --------------

    @Setter
    @Getter
    private static class UserUpdateRequest {
        @NonNull
        private UserDTO userDTO;
    }

}
