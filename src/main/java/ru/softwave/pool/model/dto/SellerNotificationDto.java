package ru.softwave.pool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerNotificationDto {

    private String sellerInn;
    private String sellerKpp;
    private String sellerName;
    private String cteId;
    private String cteName;
}
