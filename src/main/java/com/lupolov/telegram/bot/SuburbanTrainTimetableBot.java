package com.lupolov.telegram.bot;

import com.lupolov.telegram.model.TimetableEntry;
import com.lupolov.telegram.uz.parser.Parser;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.List;

import static com.lupolov.telegram.util.TableEmoji.*;

@Getter
@Setter
public class SuburbanTrainTimetableBot extends TelegramWebhookBot {

    private String botPath;
    private String botUsername;
    private String botToken;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return sendRawTimetable(update);
    }

    private SendMessage sendRawTimetable(Update update) {
        var parser = new Parser();

        var sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());

        var params = update.getMessage().getText().split(" > ");
        var from = params[0].trim();
        var to = params[1].trim();

        try {
            var entries = parser.getTimetableByStation(from, to);

            if (entries.isEmpty()) {
                sendMessage.setText(NEUTRAL_FACE + " Не знайдено маршрутів за вказаними параметрами");
            } else {
                sendMessage.setText(prettyPrintRows(entries));
            }
        } catch (IOException e) {
            sendMessage.setText(ERROR + " Під час пошуку розкладу відбулася помилка, спробуйте змінити параметри, або повторіть запит пізніше.");
            e.printStackTrace();
        }

        return sendMessage;
    }

    private String prettyPrintRows(List<TimetableEntry> entries) {

        var builder = new StringBuilder();

        builder.append(UA_FLAG).append(" Результати вашого запиту:\n\n");

        entries.forEach(entry -> {
            builder.append(TRAIN_NUM).append(" Потяг №").append(entry.getTrainNum()).append("\n");
            builder.append(SCHEDULE).append(" Обіг: ").append(entry.getSchedule()).append("\n");
            builder.append(ROUTE).append(" Маршрут прямування: ").append(entry.getRoute()).append("\n");
            builder.append(DEPART).append(" Відпрвлення: ").append(entry.getDeparture()).append("\n");
            builder.append(ARRIVAL).append(" Прибуття: ").append(entry.getArrival()).append("\n");
            builder.append(TRAVEL_TIME).append(" В дорозі: ").append(entry.getTravelTime()).append("\n");
            builder.append("\n");
        });

        var maybeEntry = entries.stream().findFirst();
        if (maybeEntry.isPresent()) {
            var entry = maybeEntry.get();
            builder.append(DISTANCE).append(" Відстань: ").append(entry.getDistance()).append(" км").append("\n");
            builder.append(CHECK_MARK).append(" Розклад дісний з ").append(entry.getFromRelevance()).append(" по ").append(entry.getToRelevance()).append("\n");
        }

        return builder.toString();
    }

}
