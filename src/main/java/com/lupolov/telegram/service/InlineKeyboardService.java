package com.lupolov.telegram.service;

import com.lupolov.telegram.model.Station;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InlineKeyboardService {

    public SendMessage getStationListInlineKeyboardMessage(long chatId, String messageText, List<Station> stations) {

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
                var id = station.getId();
                var info = extractInfoText(station.getInfo());
                var label = station.getLabel().concat(" ").concat(info);

                keyboardButtonsRow.add(new InlineKeyboardButton()
                                          .setText(label)
                                          .setCallbackData(id));

                rowList.add(keyboardButtonsRow);
            });

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private String extractInfoText(String text) {
        var document = Jsoup.parse(text);
        var elements = document.getElementsByTag("i");
        var infoText = elements.get(0).wholeText();

        var trimTail = infoText.substring(0,infoText.length() - 4);
        return "("+ trimTail +".)";
    }
}
