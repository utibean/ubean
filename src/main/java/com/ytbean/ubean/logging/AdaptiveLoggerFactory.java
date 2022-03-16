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

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;

/**
 * The class AdaptiveLoggerFactory
 *
 * @author ytbean
 * @date 2022/2/25 23:39
 */
public abstract class AdaptiveLoggerFactory {

    private static volatile AdaptiveLoggerFactory defaultFactory;

    public static AdaptiveLogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static AdaptiveLogger getLogger(String name) {
        return getDefaultFactory().newInstance(name);
    }


    public static void setDefaultFactory(AdaptiveLoggerFactory defaultFactory) {
        if (defaultFactory == null) {
            throw new NullPointerException("defaultFactory");
        }
        AdaptiveLoggerFactory.defaultFactory = defaultFactory;
    }

    protected abstract AdaptiveLogger newInstance(String name);

    private static AdaptiveLoggerFactory getDefaultFactory() {
        if (defaultFactory == null) {
            synchronized (AdaptiveLoggerFactory.class) {
                if (defaultFactory == null) {
                    defaultFactory = newDefaultFactory();
                }
            }
        }
        return defaultFactory;
    }

    private static AdaptiveLoggerFactory newDefaultFactory() {
        AdaptiveLoggerFactory loggerFactory = trySLF4JLoggerFactory();
        if (loggerFactory != null) {
            loggerFactory.newInstance(AdaptiveLoggerFactory.class.getName()).debug("Using SLF4J as the default " +
                    "logging framework");
            return loggerFactory;
        }
        throw new UnsupportedOperationException("not supported except SLF4J");
    }


    private synchronized static AdaptiveLoggerFactory trySLF4JLoggerFactory() {
        try {
            ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
            if (iLoggerFactory instanceof NOPLoggerFactory) {
                return null;
            }
            return new SLF4JLoggerFactory();
        } catch (Exception e) {
            return null;
        }
    }
}
