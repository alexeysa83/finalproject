package com.github.alexeysa83.finalproject.dao.config;

import org.springframework.beans.factory.annotation.Value;

public class DatasourceSettings {
    @Value("${url}")
    private String url;

    @Value("${usernameDB}")
    private String usernameDB;

    @Value("${password}")
    private String password;

    @Value("${driver}")
    private String driver;

    public String getUrl() {
        return url;
    }

    public String getUsernameDB() {
        return usernameDB;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}
