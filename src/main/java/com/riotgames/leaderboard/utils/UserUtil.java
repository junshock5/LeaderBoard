package com.riotgames.leaderboard.utils;

import com.riotgames.leaderboard.dto.UserDTO;

public class UserUtil {
    public static UserDTO.Tier calculateTierbyRank(long rank,long totalCount){
        long totalUserCount = totalCount;
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
