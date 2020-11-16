package com.riotgames.readerboard.dao;

import com.riotgames.readerboard.dto.UserDTO;
import com.riotgames.readerboard.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;

@Repository
@Log4j2
public class UserDao {

    @Autowired
    private final UserMapper userMapper;

    private static final String FILE_DIRECTORY_NAME = System.getProperty("user.dir") + File.separator;

    public UserDao(UserMapper userProfileMapper) {
        this.userMapper = userProfileMapper;
    }

    // 상위 25000개 User 정보 mysql에 insert 만약 id값이 있다면 수정
    @PostConstruct
    public void init() throws IOException {
        loadInitialData();
    }

    @PreDestroy
    public void destory() {
    }

    public void loadInitialData() throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(FILE_DIRECTORY_NAME + "InitialData.txt"))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] pair = line.split(",");
                long id = Long.parseLong(pair[0]);
                int mmr = Integer.parseInt(pair[1]);


                UserDTO userDTO = UserDTO.builder()
                        .id(id)
                        .matchMakerRank(mmr)
                        .build();

                if (userMapper.idCheck(id) == 1) { // 이미 있는 id이기 떄문에 mmr값이 같은지 확인하고 없다면 update
                    userMapper.updateUser(userDTO);
                }else{ // id값이 없기에 그냥 insert
                    int insertCount = userMapper.register(userDTO);
                    if (insertCount != 1) {
                        log.error("insertMember ERROR! {}", userDTO);
                        throw new RuntimeException(
                                "insertUser ERROR! 유저 추가 메서드를 확인해주세요\n" + "Params : " + userDTO);
                    }
                }

            }
        }
    }


}
