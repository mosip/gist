package org.mosip.resident.pojo;

import com.google.gson.annotations.SerializedName;
public class IdentityWrapper {
    @SerializedName("identity")
    public Identity identity;

    public IdentityWrapper(int nLang){
        identity = new Identity(nLang);
    }

    public class Identity {
        @SerializedName("addressLine1")
        public SimpleObject[] addressLine1;

        @SerializedName("addressLine2")
        public SimpleObject[] addressLine2;
        @SerializedName("addressLine3")
        public SimpleObject[] addressLine3;

        @SerializedName("IDSchemaVersion")
        public double IDSchemaVersion;

        @SerializedName("UIN")
        public String UIN;

        public void setAddress(int lineNo, int index, String lang, String value) {
            switch (lineNo) {
                case 1:
                    addressLine1[index] = new SimpleObject(lang, value);
                    break;
                case 2:
                    addressLine2[index] = new SimpleObject(lang, value);
                    break;
                case 3:
                    addressLine3[index] = new SimpleObject(lang, value);
                    break;

            }
        }

        public Identity(int nLanguages) {
            IDSchemaVersion = 0.1;
            addressLine1 = new SimpleObject[nLanguages];
            addressLine2 = new SimpleObject[nLanguages];
            addressLine3 = new SimpleObject[nLanguages];

        }


        public class SimpleObject {
            @SerializedName("language")
            public String language;
            @SerializedName("value")
            public String value;

            public SimpleObject(String lang, String val) {
                language = lang;
                value = val;
            }
        }
    }
}