package io.mosip.partner.partnermanagement.model.partner;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.Data;

/**
 * @author sanjeev.shrivastava
 *
 */

@Data
public class PartnerRequest{
		
	@NotEmpty(message="Please provide partnerId")
	public String partnerId;

	@ApiModelProperty(required = false, hidden = true)
	public String policyGroup;
	
	@NotEmpty(message = "Please provide organizationName")
	public String organizationName;
	
	@NotEmpty(message = "Please provide address")
	public String address;
	
	@NotEmpty(message = "Please provide contactNumber")
	public String contactNumber;
	
	@NotEmpty(message = "Please provide emailId")
	public String emailId;
	
	@NotEmpty(message="Please provide partner Type")
	public String partnerType;

//	@NotEmpty(message="Please provide Policy Name")
//	public String policyName;
}