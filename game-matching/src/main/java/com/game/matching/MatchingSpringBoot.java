package com.game.matching;

import com.framework.akka_router.cluster.ClusterChildNodeSystemCreator;
import com.framework.akka_router.ForwardHandler;
import com.framework.akka_router.ServiceHandler;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

/**
 * Created by @panyao on 2017/8/29.
 */
@SpringBootApplication
public final class MatchingSpringBoot implements ApplicationContextAware {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MatchingSpringBoot.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @PostConstruct
    public void init() {
        ClusterChildNodeSystemCreator.create(MatchingRouter.class)
                .registerServiceRouter(applicationContext.getBeansOfType(ServiceHandler.class))
                .registerForwardRouter(applicationContext.getBeansOfType(ForwardHandler.class));
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
