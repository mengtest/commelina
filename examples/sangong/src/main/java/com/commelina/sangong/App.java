package com.commelina.sangong;

import com.commelina.niosocket.BootstrapNioSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author @panyao
 * @date 2017/8/10
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Bean
    public BootstrapNioSocket createServer() {
        return new BootstrapNioSocket();
    }

}