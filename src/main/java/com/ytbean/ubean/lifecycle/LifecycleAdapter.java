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
 * The life cycle adapter can be used to implement only the concerned life cycle stages by inheriting this class
 *
 * @author ytbean
 * @date 2022/2/25 22:57
 */
public class LifecycleAdapter extends AbstractLifecycle {


    @Override
    protected void init0() throws LifecycleException {
        //implemented by subclass
    }

    @Override
    protected void start0() throws LifecycleException {
        //implemented by subclass
    }

    @Override
    protected void suspend0() throws LifecycleException {
        //implemented by subclass
    }

    @Override
    protected void resume0() throws LifecycleException {
        //implemented by subclass
    }

    @Override
    protected void destroy0() throws LifecycleException {
        //implemented by subclass
    }
}
