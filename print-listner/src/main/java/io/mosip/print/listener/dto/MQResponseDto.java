package io.mosip.print.listener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MQResponseDto {
    private String id;
    private PrintStatusRequestDto data;
}
