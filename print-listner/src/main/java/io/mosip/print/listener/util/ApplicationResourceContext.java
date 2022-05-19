package io.mosip.print.listener.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class ApplicationResourceContext {

    String applicatioLanguage;
    String supportedLanguage;
    ResourceBundle labelBundle;
    LinkedHashMap<String, ResourceBundle> supportedLabelBundle;
    private ResourceBundle partnerResourCeBundle;
    private ApplicationContext applicationContext;

    private static ApplicationResourceContext context;
    private ApplicationResourceContext() {
        loadResource();
    }

    private void loadResource(){
        partnerResourCeBundle = ResourceBundle.getBundle("partner");

        Locale applicationPrimaryLanguageLocale = new Locale(applicatioLanguage != null ? applicatioLanguage.substring(0, 2) : "en");

        supportedLabelBundle = new LinkedHashMap<>();
        labelBundle = ResourceBundle.getBundle("labels", applicationPrimaryLanguageLocale);
        supportedLabelBundle.put(applicatioLanguage != null ? applicatioLanguage : "eng", labelBundle);
        if(supportedLanguage != null)
            for(String supportedLang : supportedLanguage.split(",")) {
                if (supportedLang != null && !supportedLang.isEmpty()) {
                    Locale languageLocale = new Locale(supportedLang.substring(0, 2));
                    ResourceBundle labels = ResourceBundle.getBundle("labels", languageLocale);
                    supportedLabelBundle.put(supportedLang, labels);
                }
            }
        else {
            ResourceBundle labels = ResourceBundle.getBundle("labels", applicationPrimaryLanguageLocale);
            supportedLabelBundle.put(applicatioLanguage, labels);
        }
    }
    public static ApplicationResourceContext getInstance() {
        if(context == null) {
            context = new ApplicationResourceContext();
            return context;
        } else {
            return context;
        }
    }

    public ResourceBundle getLabelBundle() {
        return labelBundle;
    }

    public ResourceBundle getPartnerResourceBundle() {
        return partnerResourCeBundle;
    }

    public ResourceBundle getOtherLabelBundle(String applicatioLanguage) {
        if(supportedLabelBundle.containsKey(applicatioLanguage))
            return supportedLabelBundle.get(applicatioLanguage);
        else
            return supportedLabelBundle.get(this.applicatioLanguage);
    }

    public void setApplicationLanguage(String applicatioLanguage) {
        this.applicatioLanguage = applicatioLanguage;
        loadResource();
    }

    public void setApplicationSupportedLanguage(String supportedLanguage) {
        this.supportedLanguage = supportedLanguage;
        loadResource();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
