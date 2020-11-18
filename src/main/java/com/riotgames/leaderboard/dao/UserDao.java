package com.riotgames.leaderboard.dao;

import com.riotgames.leaderboard.dto.UserDTO;
import com.riotgames.leaderboard.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@Repository
@Log4j2
public class UserDao {

    @Autowired
    private final UserMapper userMapper;

    private static final String FILE_DIRECTORY_NAME = System.getProperty("user.dir") + File.separator;

    public UserDao(UserMapper userProfileMapper) {
        this.userMapper = userProfileMapper;
    }

    @PostConstruct
    public void init() throws IOException {
        loadInitialData();
    }

    private void loadInitialData() throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(FILE_DIRECTORY_NAME + "InitialData.txt"))) {
            Map<Long, Integer> userMap = new HashMap<Long, Integer>();
            String line;
            while ((line = in.readLine()) != null) {
                String[] pair = line.split(",");
                long id = Long.parseLong(pair[0]);
                int mmr = Integer.parseInt(pair[1]);
                userMap.put(id, mmr);
            }
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
    }


}
