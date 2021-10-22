package io.mosip.partner.partnermanagement.model.device;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.mosip.kernel.core.exception.ServiceError;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class DeviceResponseDto {
        private String id;
        private String version;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private LocalDateTime responsetime = LocalDateTime.now(ZoneId.of("UTC"));
        private Object metadata;
        private String response;
        private List<ServiceError> errors = new ArrayList<>();
    }
