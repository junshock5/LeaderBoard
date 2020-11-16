package com.riotgames.readerboard.service;

import com.riotgames.readerboard.dto.UserDTO;
import com.riotgames.readerboard.exception.DuplicateIdException;
import com.riotgames.readerboard.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserService {

    @Autowired
    private final UserMapper userMapper;

    public UserService(UserMapper userProfileMapper) {
        this.userMapper = userProfileMapper;
    }

    public UserDTO getUserInfo(long userId) {
        return userMapper.getUserProfile(userId);
    }

    public void register(UserDTO userDTO) {
        // id 중복체크
        boolean duplIdResult = isDuplicatedId(userDTO.getId());
        if (duplIdResult) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }
        int insertCount = userMapper.register(userDTO);

        if (insertCount != 1) {
            log.error("insertMember ERROR! {}", userDTO);
            throw new RuntimeException(
                    "insertUser ERROR! 유저 추가 메서드를 확인해주세요\n" + "Params : " + userDTO);
        }
    }

    public void updateUser(UserDTO userDTO) {
        if (userDTO != null) {
            userMapper.updateUser(userDTO);
        } else {
            log.error("updateAddress ERROR! {}", userDTO);
            throw new RuntimeException("updateAddress ERROR! 유저 변경 메서드를 확인해주세요\n" + "Params : " + userDTO);
        }
    }

    public void deleteId(long id) {
        if (id != 0) {
            userMapper.deleteUserProfile(id);
        } else {
            log.error("deleteId ERROR! {}", id);
            throw new RuntimeException("deleteId ERROR! id 삭제 메서드를 확인해주세요\n" + "Params : " + id);
        }
    }

    public boolean isDuplicatedId(long id) {
        return userMapper.idCheck(id) == 1;
    }

    public long totalCount() {
        return userMapper.totalCount();
    }

}
