/**
 * 
 */
package io.mosip.partner.partnermanagement.model.authmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ramadurai Pandian
 *
 */
@Data
@AllArgsConstructor
public class LoginUser {
	private String userName;
	private String password;
	private String appId;
	private String clientId;
	private String clientSecret;
}
