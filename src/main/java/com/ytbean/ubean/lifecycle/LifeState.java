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
 * The class LifeState
 *
 * @author ytbean
 * @date 2022/2/25 18:36
 */
public enum LifeState {
    NEW,

    INITIALIZING, INITIALIZED,

    STARTING, STARTED,

    SUSPENDING, SUSPENDED,

    RESUMING,  //State RESUMED is unnecessary, it means STARTED

    DESTROYING, DESTROYED,

    SICK
}
