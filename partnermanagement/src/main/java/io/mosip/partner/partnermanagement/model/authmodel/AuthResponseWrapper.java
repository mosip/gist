package io.mosip.partner.partnermanagement.model.authmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.mosip.kernel.core.exception.ServiceError;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
public class AuthResponseWrapper<T> {
	private String id;
	private String version;
	private String responsetime;
	private Object metadata;
	@NotNull
	@Valid
	private T response;

	private List<ServiceError> errors = new ArrayList<>();

	public boolean canBeIgnored() {
		String[] ignoredList = {"KER-PCM-003", "PMS_PRT_051", "PMS_AUT_512"};
		Boolean ignore = true;
		for(ServiceError error : errors) {
			Boolean errorPresent = false;
			for (String value : ignoredList) {
				if ((error.getErrorCode() != null && error.getErrorCode().equals(value.toString())) || (error.getErrorCode() == null && error.getMessage().contains(value.toString())))
					errorPresent = true;
			}

			if(!errorPresent)
				ignore = false;
		}
		return ignore;
	}
}

