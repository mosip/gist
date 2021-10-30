
package io.mosip.print.listener.exception;

// TODO: Auto-generated Javadoc
/**
 * The Enum PRTPlatformErrorMessages.
 *
 * @author M1047487
 */
public enum PlatformErrorMessages {


	/** The PRT prt applicant photo not set. */
	PRT_PRT_APPLICANT_PHOTO_NOT_SET(PlatformConstants.PRT_PRINT_PREFIX + "006", "Error while setting applicant photo"),
	/** The PRT prt qrcode not set. */
	PRT_PRT_QRCODE_NOT_SET(PlatformConstants.PRT_PRINT_PREFIX + "007", "Error while setting qrCode for uin card"),
	/** The PRT prt pdf generation failed. */
	PRT_PRT_PDF_GENERATION_FAILED(PlatformConstants.PRT_PRINT_PREFIX + "003", "PDF Generation Failed"),
	/** The PRT prt qrcode not generated. */
	PRT_PRT_QRCODE_NOT_GENERATED(PlatformConstants.PRT_PRINT_PREFIX + "005", "Error while generating QR Code"),
	/** The PRT prt qr code generation error. */
	PRT_PRT_QR_CODE_GENERATION_ERROR(PlatformConstants.PRT_PRINT_PREFIX + "022", "Error while QR Code Generation"),

	/** The PRT prt vid creation error. */
	PRT_PRT_VID_CREATION_ERROR(PlatformConstants.PRT_PRINT_PREFIX + "023", "Error while creating VID"),

	/** The PRT prt vid exception. */
	PRT_PRT_VID_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "018",
			"Could not generate/regenerate VID as per policy,Please use existing VID"),
	// Printing stage exceptions
	PRT_PRT_PDF_NOT_GENERATED(PlatformConstants.PRT_PRINT_PREFIX + "001", "Error while generating PDF for UIN Card"),
	/** The PRT rgs json parsing exception. */
	PRT_RGS_JSON_PARSING_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "017", "JSON Parsing Failed"),
	/** The invalid input parameter. */
	PRT_PGS_INVALID_INPUT_PARAMETER(PlatformConstants.PRT_PRINT_PREFIX + "011", "Invalid Input Parameter - %s"),
	/** The PRT rgs json mapping exception. */
	PRT_RGS_JSON_MAPPING_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "016", "JSON Mapping Failed"),
	PRT_PRT_PDF_SIGNATURE_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "024", "PDF Signature error"),
	/** The PRT pvm invalid uin. */
	PRT_PVM_INVALID_UIN(PlatformConstants.PRT_PRINT_PREFIX + "012", "Invalid UIN"),
	/** The PRT rct unknown resource exception. */
	PRT_RCT_UNKNOWN_RESOURCE_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "001", "Unknown resource provided"),
	/** The PRT utl digital sign exception. */
	PRT_UTL_DIGITAL_SIGN_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "003", "Failed to generate digital signature"),
	/** The PRT utl biometric tag match. */
	PRT_UTL_BIOMETRIC_TAG_MATCH(PlatformConstants.PRT_PRINT_PREFIX + "001", "Both Files have same biometrics"),
	/** The PRT prt uin not found in database. */
	PRT_PRT_UIN_NOT_FOUND_IN_DATABASE(PlatformConstants.PRT_PRINT_PREFIX + "002", "UIN not found in database"),
	/** The PRT bdd abis abort. */
	PRT_BDD_ABIS_ABORT(PlatformConstants.PRT_PRINT_PREFIX + "002",
			"ABIS for the Reference ID and Request ID was Abort"),
	/** The PRT tem processing failure. */
	PRT_TEM_PROCESSING_FAILURE(PlatformConstants.PRT_PRINT_PREFIX + "002", "The Processing of Template Failed "),
	PRT_SYS_JSON_PARSING_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "009", "Error while parsing Json"),
	PRT_AUT_INVALID_TOKEN(PlatformConstants.PRT_PRINT_PREFIX + "01", "Invalid Token Present"),
	/** The PRT cmb unsupported encoding. */
	PRT_CMB_UNSUPPORTED_ENCODING(PlatformConstants.PRT_PRINT_PREFIX + "002", "Unsupported Failure"),
	/** The PRT sys no such field exception. */
	PRT_SYS_NO_SUCH_FIELD_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "008", "Could not find the field"),

	/** The PRT sys instantiation exception. */
	PRT_SYS_INSTANTIATION_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "007",
			"Error while creating object of JsonValue class"),

	PRT_PIS_IDENTITY_NOT_FOUND(PlatformConstants.PRT_PRINT_PREFIX + "002",
			"Unable to Find Identity Field in ID JSON"),
	/** Access denied for the token present. */
	PRT_AUT_ACCESS_DENIED(PlatformConstants.PRT_PRINT_PREFIX + "02", "Access Denied For Role - %s"),
	DATASHARE_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "025", "Data share api failure"),
	API_NOT_ACCESSIBLE_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "026", "Api not accessible failure"),
	CERTIFICATE_THUMBPRINT_ERROR(PlatformConstants.PRT_PRINT_PREFIX + "026", "certificate thumbprint failure"),
	PRT_INVALID_KEY_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "027", "invalid key"),
	PRT_PDF_SIGN_EXCEPTION(PlatformConstants.PRT_PRINT_PREFIX + "028", "error occured while signing pdf"),
	PRT_FAILED(PlatformConstants.PRT_PRINT_PREFIX + "029","Printing Process Failed. Conatct Administrator"),
	PRT_ERROR(PlatformConstants.PRT_PRINT_PREFIX + "030", ""),
	PRT_NOT_CONFIG(PlatformConstants.PRT_PRINT_PREFIX + "031", "Printer Name not configured Property file"),
	PRT_OFFLINE(PlatformConstants.PRT_PRINT_PREFIX + "032", "Printer is Offline"),
	PRT_NOT_FOUND(PlatformConstants.PRT_PRINT_PREFIX + "033", "Printer Name not found in Printer List");




	/** The error message. */
	private final String errorMessage; 

	/** The error code. */
	private final String errorCode;

	/**
	 * Instantiates a new platform error messages.
	 *
	 * @param errorCode
	 *            the error code
	 * @param errorMsg
	 *            the error msg
	 */
	private PlatformErrorMessages(String errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMessage = errorMsg;
	}

	/**
	 * Gets the error message.
	 *
	 * @return the error message
	 */
	public String getMessage() {
		return this.errorMessage;
	}

	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public String getCode() {
		return this.errorCode;
	}

}