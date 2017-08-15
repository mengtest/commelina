package com.nexus.maven.core.message;

import java.util.List;

/**
 * Created by @panyao on 2017/8/15.
 */
public interface BroadcastResponse extends ApiResponse {

    List<Long> getUserIds();

}
