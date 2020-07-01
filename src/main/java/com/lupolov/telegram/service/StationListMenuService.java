package com.lupolov.telegram.service;

import com.lupolov.telegram.model.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StationListMenuService {

    public SendMessage sendInlineKeyboardMessage(long chatId, String messageText, List<Station> stations) {

        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText(messageText);
        sendMessage.setReplyMarkup(getStationButtons(stations));

        return sendMessage;
    }

    private InlineKeyboardMarkup getStationButtons(List<Station> stations) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

            stations.forEach(station -> {
                List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                keyboardButtonsRow.add(new InlineKeyboardButton()
                        .setText(station.getLabel())
                        .setCallbackData(station.getId()));

                rowList.add(keyboardButtonsRow);
            });

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
