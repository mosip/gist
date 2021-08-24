package io.mosip.print.listener.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
@Setter
@Getter
public class ApplicationContext {
    private ResourceBundle partnerResourCeBundle;

    public ApplicationContext() {
        partnerResourCeBundle = ResourceBundle.getBundle("partner");
    }
}
