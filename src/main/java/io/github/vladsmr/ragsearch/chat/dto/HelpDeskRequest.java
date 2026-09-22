package io.github.vladsmr.ragsearch.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelpDeskRequest {

    @JsonProperty("history_id")
    private String historyId;
    @JsonProperty("prompt_message")
    private String promptMessage;

}
