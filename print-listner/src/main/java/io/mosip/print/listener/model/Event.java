package io.mosip.print.listener.model;

import lombok.Data;
@Data
public class Event {
    private String id; //uuid
    private String dataShareUri; //URL
   

    // Getter Methods 
   
    public String getId() {
     return id;
    }
   
     public String getDataShareUri() {
     return dataShareUri;
    }

    // Setter Methods 
   
    public void setId(String id) {
     this.id = id;
    }
   
     public void setDataShareUri(String dataShareUri) {
     this.dataShareUri = dataShareUri;
    }   
}