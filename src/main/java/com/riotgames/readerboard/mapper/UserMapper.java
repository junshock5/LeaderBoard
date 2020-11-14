package com.riotgames.readerboard.mapper;

import com.riotgames.readerboard.dto.UserDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    public UserDTO getUserProfile(@Param("id") long id);

    int deleteUserProfile(@Param("id") long id);

    public int register(UserDTO userDTO);

    int idCheck(long id);

    public int updateUser(UserDTO userDTO);
}
