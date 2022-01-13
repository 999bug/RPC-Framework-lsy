package com.ncst.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Lsy
 * @date 2022/1/13
 */
@Data
@NoArgsConstructor
public class People implements Serializable {
    private Integer id;
    private String msg;
    private String occupation;
    private Integer time;
    private String lovePeople;

    public People(String occupation, Integer time, String lovePeople) {
        this.occupation = occupation;
        this.time = time;
        this.lovePeople = lovePeople;
    }
}
