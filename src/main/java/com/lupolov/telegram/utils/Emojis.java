package com.lupolov.telegram.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emojis {
    MAG(EmojiParser.parseToUnicode(":mag:")),
    STATION(EmojiParser.parseToUnicode(":station:")),
    SCHEDULE(EmojiParser.parseToUnicode(":calendar:")),
    ROUTE(EmojiParser.parseToUnicode(":repeat:")),
    DEPART(EmojiParser.parseToUnicode(":clock8:")),
    ARRIVAL(EmojiParser.parseToUnicode(":clock3:")),
    TRAVEL_TIME(EmojiParser.parseToUnicode(":alarm_clock:")),
    DISTANCE(EmojiParser.parseToUnicode(":checkered_flag:")),
    CHECK_MARK(EmojiParser.parseToUnicode(":white_check_mark:")),
    NEUTRAL_FACE(EmojiParser.parseToUnicode(":neutral_face:")),
    ERROR(EmojiParser.parseToUnicode(":scream:")),
    WRITING_HAND(EmojiParser.parseToUnicode(":writing_hand:")),
    UA_FLAG(EmojiParser.parseToUnicode(":ua:")),
    POINT_UP(EmojiParser.parseToUnicode(":point_up:")),
    WARNING(EmojiParser.parseToUnicode(":warning:")),
    NERD_FACE(EmojiParser.parseToUnicode(":nerd_face:")),
    MAN_SHRUGGING(EmojiParser.parseToUnicode(":man_shrugging:"));

    private String emojiName;


    @Override
    public String toString() {
        return emojiName;
    }
}
