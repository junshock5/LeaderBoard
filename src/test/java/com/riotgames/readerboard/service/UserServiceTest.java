package com.riotgames.readerboard.service;

import com.riotgames.readerboard.dto.UserDTO;
import com.riotgames.readerboard.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@AutoConfigureMockMvc
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserMapper userMapper;

    // 새로운 유저 객체를 생성 하여 반환 한다.
    public UserDTO generateUser() {
        MockitoAnnotations.initMocks(this); // mock all the field having @Mock annotation
        UserDTO userDTO = UserDTO.builder()
                .id(1)
                .matchMakerRank(3500)
                .rank(1)
                .tier(UserDTO.Tier.CHALLENGER)
                .build();
        return userDTO;
    }

    // 새로운 유저 객체 리스트를 생성 하여 반환 한다.
    public List<UserDTO> generateUserList() {
        MockitoAnnotations.initMocks(this); // mock all the field having @Mock annotation
        List<UserDTO> userDTOList = new ArrayList<UserDTO>();
        for (int i = 1; i <= 20; i++) {
            UserDTO userDTO = UserDTO.builder()
                    .id(i)
                    .matchMakerRank(3500 + i)
                    .rank(i)
                    .tier(UserDTO.Tier.CHALLENGER)
                    .build();
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Test
    @DisplayName("생성된 유저의 id 값을 통해 null 인지 확인, 티어가 동일 한지 확인")
    public void testGetUserInfo() {
        UserDTO userDTO = generateUser();
        given(userMapper.getUserProfile(1)).willReturn(userDTO);
        assertTrue(!userMapper.getUserProfile(1).equals(null));
        assertTrue(UserDTO.Tier.CHALLENGER == userDTO.getTier());
    }

    @Test
    @DisplayName("가상 유저 생성 후 mapper 이용해 정상 삽입 되는지 test")
    public void testRegister() {
        UserDTO userDTO = generateUser();
        try {
            userMapper.register(userDTO);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    @DisplayName("가상 유저 생성 후 mapper 이용해 정상 수정 되는지 test")
    public void testUpdateUser() {
        UserDTO userDTO = generateUser();
        userDTO.setMatchMakerRank(333);
        try {
            userService.updateUser(userDTO);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
        assertTrue(333 == userDTO.getMatchMakerRank());
    }

    @Test
    @DisplayName("가상 유저 생성 후 mapper 이용해 정상 삭제 되는지 test")
    public void testDeleteId() {
        UserDTO userDTO = generateUser();
        try {
            userService.deleteId(userDTO.getId());
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
        assertNull(userMapper.getUserProfile(1));
    }

    @Test
    @DisplayName("가상 유저 생성 후 ID 값이 중복 되는지 test")
    public void testIsDuplicatedId() {
        UserDTO userDTO = generateUser();
        assertFalse(userService.isDuplicatedId(userDTO.getId()) == true);
    }

    @Test
    @DisplayName("가상 유저 리스트 생성 후 변수 대입 됬는지 test")
    public void testTotalUsers() {
        List<UserDTO> userDTOList = generateUserList();
        for (UserDTO userDTO : userDTOList) {
            userMapper.register(userDTO);
        }
        assertNotEquals(null, userService.totalUsers());
    }

    @Test
    @DisplayName("가상 유저 생성 후 순위 변경 후 값이 같은지 test")
    public void testUpdateRank() {
        UserDTO userDTO = generateUser();
        userDTO.setRank(100);
        userMapper.updateUser(userDTO);
        assertEquals(100, userDTO.getRank());
    }

    @Test
    @DisplayName("가상 유저 생성 후 티어 변경 후 값이 같은지 test")
    public void testGetUserTier() {
        UserDTO userDTO = generateUser();
        userDTO.setTier(UserDTO.Tier.DIAMOND);
        userMapper.updateUser(userDTO);
        assertEquals(UserDTO.Tier.DIAMOND, userDTO.getTier());
    }

    @Test
    @DisplayName("가상 유저 리스트 생성 후 메서드 호출 시 list 개수와 같은지 test")
    public void testGetUsersRangeByIdAndInterval() {
        List<UserDTO> userDTOList = generateUserList();
        given(userMapper.getUsersRangeByIdAndInterval(5, 3)).willReturn(userDTOList);
        try {
            userMapper.getUsersRangeByIdAndInterval(5, 3);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }
}