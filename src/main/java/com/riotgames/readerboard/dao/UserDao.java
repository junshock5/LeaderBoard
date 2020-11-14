package com.riotgames.readerboard.dao;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;

@Repository
public class UserDao {

    private static final String FILE_DIRECTORY_NAME = System.getProperty("user.dir") + File.separator;

    // 상위 25000개 User 정보 mysql에 insert 만약 id값이 있다면 수정
    @PostConstruct
    public void init() throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(FILE_DIRECTORY_NAME + "InitialData.txt"))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] pair = line.split(",");
                String a = pair[0];
                String b = pair[1];
            }
        }
    }

    @PreDestroy
    public void destory() {
    }


}
