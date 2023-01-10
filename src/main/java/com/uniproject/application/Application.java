package com.uniproject.application;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
//@Theme(variant = Lumo.DARK)
@Theme("flowcrmtutorial")
@NpmPackage(value = "line-awesome", version = "1.3.0")
@PWA(name = "VaadinCRM", shortName = "CRM", offlinePath="offline.html", offlineResources = { "./images/offline.png"})
//@ConfigurationProperties(prefix = "spring.jpa.defer-datasource-initialization")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

}
