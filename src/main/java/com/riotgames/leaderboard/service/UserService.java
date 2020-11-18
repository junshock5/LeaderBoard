package com.riotgames.leaderboard.service;

import com.riotgames.leaderboard.dto.UserDTO;
import com.riotgames.leaderboard.exception.DuplicateIdException;
import com.riotgames.leaderboard.exception.user.DeleteException;
import com.riotgames.leaderboard.exception.user.InsertException;
import com.riotgames.leaderboard.exception.user.UpdateException;
import com.riotgames.leaderboard.mapper.UserMapper;
import com.sun.istack.internal.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

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
            throw new InsertException("insertUser ERROR! 유저 추가 메서드를 확인해주세요\n" + "Params : " + userDTO);
        }
        updateRank();
    }

    public void updateUser(@NotNull UserDTO userDTO) {
        try {
            userMapper.updateUser(userDTO);
        } catch (UpdateException e) {
            log.error("updateAddress ERROR! {}", userDTO);
            throw new UpdateException("updateAddress ERROR! 유저 변경 메서드를 확인해주세요\n" + "Params : " + userDTO);
        }
        updateRank();
    }

    public void deleteId(long id) {
        if (id != 0) {
            userMapper.deleteUserProfile(id);
        } else {
            log.error("deleteId ERROR! {}", id);
            throw new DeleteException("deleteId ERROR! id 삭제 메서드를 확인해주세요\n" + "Params : " + id);
        }
        updateRank();
    }

    public boolean isDuplicatedId(long id) {
        return userMapper.idCheck(id) == 1;
    }

    public long totalCount() {
        return userMapper.totalCount();
    }

    public List<UserDTO> totalUsers() {
        return userMapper.totalUsers();
    }

    public void updateRank(){
        Map<Long, Integer> userMap =
                totalUsers().stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getMatchMakerRank));

        List<Long> keySetList = new ArrayList<>(userMap.keySet());
        // 내림 차순 정렬
        Collections.sort(keySetList, (o1, o2) -> (userMap.get(o2).compareTo(userMap.get(o1))));

        int i = 1;
        for (Long key : keySetList) {
            UserDTO userDTO = UserDTO.builder()
                    .id(key)
                    .matchMakerRank(userMap.get(key))
                    .rank(i++)
                    .build();

            if (userMapper.idCheck(key) == 1) { // 이미 있는 id이기 때때문에 mmr값이 같은지 확인고 없다면 update
                userMapper.updateUser(userDTO);
            } else { // id값이 없기에 그냥 insert
                int insertCount = userMapper.register(userDTO);
                if (insertCount != 1) {
                    log.error("insertMember ERROR! {}", userDTO);
                    throw new RuntimeException(
                            "insertUser ERROR! 유저 추가 메서드를 확인해주세요\n" + "Params : " + userDTO);
                }
            }
        }
    }

    public String getUserTier(long id) {
        return checkTierbyRank(userMapper.getUserRank(id)).toString();
    }

    public List<UserDTO> top10() {
        return userMapper.top10();
    }

    public List<UserDTO> getUsersRangeByIdAndInterval(long id,int interval) {
        int rank = userMapper.getUserProfile(id).getRank();
        return userMapper.getUsersRangeByIdAndInterval(rank,interval);
    }

    private UserDTO.Tier checkTierbyRank(long rank){
        long totalUserCount = totalCount();
        UserDTO.Tier tier;
        if (rank <= 100) {
            tier = UserDTO.Tier.CHALLENGER;
        } else if ((double)rank / (double)totalUserCount * 100 <= 1) {
            tier = UserDTO.Tier.MASTER;
        } else if ((double)rank / (double)totalUserCount * 100 <= 5 && (double)rank / (double)totalUserCount * 100 > 1) {
            tier = UserDTO.Tier.DIAMOND;
        } else if ((double)rank / (double)totalUserCount * 100 <= 10 && (double)rank / (double)totalUserCount * 100 > 5) {
            tier = UserDTO.Tier.PLATINUM;
        } else if ((double)rank / (double)totalUserCount * 100 <= 25 && (double)rank / (double)totalUserCount * 100 > 10) {
            tier = UserDTO.Tier.GOLD;
        } else if ((double)rank / (double)totalUserCount * 100 <= 65 && (double)rank / (double)totalUserCount * 100 > 25) {
            tier = UserDTO.Tier.SILVER;
        } else {
            tier = UserDTO.Tier.BROZNE;
        }

        return tier;
    }

}
