package com.thoughtworks.rslist.fonteddata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventForModify {
    private String keyWord;
    private String eventName;
    private int userId;
}
