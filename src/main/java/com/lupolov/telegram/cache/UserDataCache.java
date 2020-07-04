package com.lupolov.telegram.cache;

import com.lupolov.telegram.bot.state.BotState;
import com.lupolov.telegram.model.TrainSearchRequestData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserDataCache {

    private final Map<Integer, BotState> userToBotState = new HashMap<>();
    private final Map<Integer, TrainSearchRequestData> userToRequestData = new HashMap<>();

    public void setBotStateForUser(int userId, BotState botState) {
        userToBotState.put(userId,botState);
    }

    public BotState getCurrentBotStateByUserId(int userId) {
        var botState = userToBotState.get(userId);

        if (botState == null) {
            botState = BotState.SHOW_MAIN_MENU;
        }

        return botState;
    }

    public void saveTrainSearchData(int userId, TrainSearchRequestData trainSearchData) {
        userToRequestData.put(userId, trainSearchData);
    }

    public TrainSearchRequestData getUserTrainSearchData(int userId) {
        TrainSearchRequestData trainSearchData = userToRequestData.get(userId);
        if (trainSearchData == null) {
            trainSearchData = new TrainSearchRequestData();
        }

        return trainSearchData;
    }
}
