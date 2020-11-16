package com.riotgames.readerboard.mapper;

import com.riotgames.readerboard.dto.UserDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    public UserDTO getUserProfile(@Param("id") long id);

    int deleteUserProfile(@Param("id") long id);

    public int register(UserDTO userDTO);

    int idCheck(long id);

    int getUserMatchMakerRank(long id);

    public int updateUser(UserDTO userDTO);

    long totalCount();

    List<UserDTO> totalUsers();
}
