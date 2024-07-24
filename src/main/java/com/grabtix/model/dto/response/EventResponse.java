package com.grabtix.model.dto.response;

import com.grabtix.model.entity.Creator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private String id;
    private String name;
    private String category;
    private String description;
    private String location;
    private Date date;
    private Integer vipQuota;
    private Integer regularQuota;
    private double vipTicketPrice;
    private double regularTicketPrice;
    private String creatorName;
}
