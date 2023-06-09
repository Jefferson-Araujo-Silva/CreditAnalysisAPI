package com.app.creditanalysis.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import java.util.Map;

public class CustomLogbackJsonLayout extends JsonLayout {

    public static final String APPLICATION = "Credit-analysis";
    public static final String REGION = EnvUtil.getEnvOrDefault("APP_REGION", "na");
    public static final String MACHINE_NAME = EnvUtil.getEnvOrDefault("USERNAME", "na");

    public CustomLogbackJsonLayout() {
        this.setIncludeMDC(false);
    }

    @Override
    protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {
        map.put("Application", APPLICATION);
        map.put("Region", REGION);
        map.put("MachineName", MACHINE_NAME);
        map.putAll(event.getMDCPropertyMap());
        super.addCustomDataToJsonMap(map, event);
    }


}
