package ru.embedding_model.ai_chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HistoryEntry {

    private String prompt;
    private String response;

    @Override
    public String toString() {
        return String.format("""
                                                 `history_entry`:
                                                     `prompt`: %s
                                     
                                                     `response`: %s
                                                 -----------------
                                                \n
                                     """, prompt, response);
    }

}
