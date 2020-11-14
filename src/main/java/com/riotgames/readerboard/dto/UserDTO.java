package com.riotgames.readerboard.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {

    //  - CHALLENGER // top 100 players
    //  - MASTER, // top 1% players
    //  - DIAMOND, // top 5% players
    //  - PLATINUM, // top 10% players
    //  - GOLD, // top 25% players
    //  - SILVER, // top 65% players
    //  - BROZNE, // others
    public enum Tier {
        CHALLENGER , MASTER, DIAMOND, PLATINUM, GOLD, SILVER, BROZNE
    }

    private long id;
    private int matchMakerRank;
    private int rank;
    private Tier tier;

    public UserDTO(){
    }

    public UserDTO(long id, int matchMakerRank, int rank, Tier tier) {
        this.id = id;
        this.matchMakerRank = matchMakerRank;
        this.rank = rank;
        this.tier = tier;
    }

    public static boolean hasNullDataBeforeSignup(UserDTO userDTO) {
        return userDTO.getId() == 0 || userDTO.getMatchMakerRank() == 0
                || userDTO.getRank() == 0 || userDTO.getTier() == null;
    }
}
