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
package com.ytbean.ubean.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * The class SLF4JLoggerFactory
 *
 * @author ytbean
 * @date 2022/2/26 00:00
 */
public class SLF4JLoggerFactory extends AdaptiveLoggerFactory {

    @Override
    protected AdaptiveLogger newInstance(String name) {
        Logger logger = LoggerFactory.getLogger(name);
        if (logger instanceof LocationAwareLogger) {
            return new LocationAwareSLF4JLogger(((LocationAwareLogger) logger));
        } else {
            return new SLF4JLogger(logger);
        }
    }
}
