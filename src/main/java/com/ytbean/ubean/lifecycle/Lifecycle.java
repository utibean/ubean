/*
 *  Copyright 2022 ytbean.com.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ytbean.ubean.lifecycle;

/**
 * The interface Lifecycle
 *
 * @author ytbean
 * @date 2022/2/25 18:39
 */
public interface Lifecycle {


    /**
     * init
     *
     * @throws LifecycleException if fail to init
     */
    void init() throws LifecycleException;

    /**
     * start
     *
     * @throws LifecycleException if fail to start
     */
    void start() throws LifecycleException;

    /**
     * suspend
     *
     * @throws LifecycleException if fail to suspend
     */
    void suspend() throws LifecycleException;

    /**
     * resume
     *
     * @throws LifecycleException if fail to resume
     */
    void resume() throws LifecycleException;

    /**
     * destroy
     *
     * @throws LifecycleException if fail to destroy
     */
    void destroy() throws LifecycleException;

    /**
     * add life state listener
     *
     * @param listener the listener
     */
    void addLifeStateListener(LifeStateListener listener);


    /**
     * get current life state
     *
     * @return current life state
     */
    LifeState state();
}
