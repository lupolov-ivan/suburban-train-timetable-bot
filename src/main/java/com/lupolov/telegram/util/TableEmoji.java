package com.lupolov.telegram.util;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TableEmoji {
    TRAIN_NUM(EmojiParser.parseToUnicode(":station:")),
    SCHEDULE(EmojiParser.parseToUnicode(":calendar:")),
    ROUTE(EmojiParser.parseToUnicode(":repeat:")),
    DEPART(EmojiParser.parseToUnicode(":clock8:")),
    ARRIVAL(EmojiParser.parseToUnicode(":clock3:")),
    TRAVEL_TIME(EmojiParser.parseToUnicode(":alarm_clock:")),
    DISTANCE(EmojiParser.parseToUnicode(":checkered_flag:")),
    CHECK_MARK(EmojiParser.parseToUnicode(":white_check_mark:")),
    NEUTRAL_FACE(EmojiParser.parseToUnicode(":neutral_face:")),
    ERROR(EmojiParser.parseToUnicode(":scream:")),
    UA_FLAG(EmojiParser.parseToUnicode(":ua:"));

    private String emojiName;


    @Override
    public String toString() {
        return emojiName;
    }
}
