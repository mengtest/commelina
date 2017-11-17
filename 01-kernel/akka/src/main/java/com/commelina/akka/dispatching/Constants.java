package com.commelina.akka.dispatching;

/**
 * @author panyao
 * @date 2017/10/17
 */
public interface Constants {

    String CLUSTER_FRONTEND = "frontend";
    String CLUSTER_BACKEND = "backend";
    String CLUSTER_FRONTEND_PATH = "/user/" + CLUSTER_FRONTEND;
    String CLUSTER_BACKEND_PATH = "/user/" + CLUSTER_BACKEND;

}
