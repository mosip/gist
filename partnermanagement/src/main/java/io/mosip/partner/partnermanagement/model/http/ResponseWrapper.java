package io.mosip.partner.partnermanagement.model.http;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.partner.partnermanagement.constant.LoginType;
import io.mosip.partner.partnermanagement.constant.PartnerTypes;
import lombok.Data;

@Data
public class ResponseWrapper<T> {
	private String id;
	private String version;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private LocalDateTime responsetime = LocalDateTime.now(ZoneId.of("UTC"));
	private Object metadata;
	@NotNull
	@Valid
	private T response;

	private List<ServiceError> errors = new ArrayList<>();

	public boolean canBeIgnored() {
		String[] ignoredList = {"KER-PCM-003", "PMS_PRT_051"};
		Boolean ignore = true;
		for(ServiceError error : errors) {
			Boolean errorPresent = false;
			for (String value : ignoredList) {
				if ((error.getErrorCode() != null && !error.getErrorCode().equals(value.toString())) || (error.getErrorCode() == null && !error.getMessage().contains(value.toString())))
					errorPresent = true;
			}

			if(!errorPresent)
				ignore = false;
		}
		return ignore;
	}
}

