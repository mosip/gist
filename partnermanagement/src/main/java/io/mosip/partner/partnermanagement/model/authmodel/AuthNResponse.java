/**
 * 
 */
package io.mosip.partner.partnermanagement.model.authmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ramadurai Pandian
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthNResponse {

	private String status;

	private String message;

}
