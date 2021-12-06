package org.mosip.resident.pojo;
//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIResponse {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("version")
        @Expose
        private String version;
        @SerializedName("responsetime")
        @Expose
        private String responsetime;
        @SerializedName("response")
        @Expose
        private RestResponse response;
        @SerializedName("errors")
        @Expose
        private Object errors;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getResponsetime() {
            return responsetime;
        }

        public void setResponsetime(String responsetime) {
            this.responsetime = responsetime;
        }

        public RestResponse getResponse() {
            return response;
        }

        public void setResponse(RestResponse response) {
            this.response = response;
        }

        public Object getErrors() {
            return errors;
        }

        public void setErrors(Object errors) {
            this.errors = errors;
        }

    }
