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

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The skeleton implementation of lifecycle
 *
 * @author ytbean
 * @date 2022/2/25 18:42
 */
public abstract class AbstractLifecycle implements Lifecycle {

    private final CopyOnWriteArraySet<LifeStateListener> listeners = new CopyOnWriteArraySet<>();

    private volatile LifeState state = LifeState.NEW;


    @Override
    public final synchronized void init() throws LifecycleException {
        if (state != LifeState.NEW && state != LifeState.DESTROYED) {
            throw illegalStateException();
        }
        onStateChanged(LifeState.INITIALIZING);
        try {
            init0();
        } catch (Throwable cause) {
            onStateChanged(LifeState.SICK);
            throw LifecycleException.wrap(cause);
        }
        onStateChanged(LifeState.INITIALIZED);
    }


    /**
     * Internal init logic
     *
     * @throws LifecycleException if fails to init
     */
    protected abstract void init0() throws LifecycleException;


    @Override
    public final synchronized void start() throws LifecycleException {
        if (state == LifeState.NEW || state == LifeState.DESTROYED) {
            init();
        }
        if (state != LifeState.INITIALIZED) {
            throw illegalStateException();
        }
        onStateChanged(LifeState.STARTING);
        try {
            start0();
        } catch (Throwable cause) {
            onStateChanged(LifeState.SICK);
            throw LifecycleException.wrap(cause);
        }
        onStateChanged(LifeState.STARTED);
    }

    /**
     * Internal start logic
     *
     * @throws LifecycleException if fails to start
     */
    protected abstract void start0() throws LifecycleException;

    @Override
    public final synchronized void suspend() throws LifecycleException {
        if (state != LifeState.STARTED) {
            throw illegalStateException();
        }

        onStateChanged(LifeState.SUSPENDING);
        try {
            suspend0();
        } catch (Throwable cause) {
            onStateChanged(LifeState.SICK);
            throw LifecycleException.wrap(cause);
        }
        onStateChanged(LifeState.SUSPENDED);
    }

    /**
     * Internal suspend logic
     *
     * @throws LifecycleException if fails to suspend
     */
    protected abstract void suspend0() throws LifecycleException;

    @Override
    public final synchronized void resume() throws LifecycleException {

        if (state != LifeState.SUSPENDED) {
            throw illegalStateException();
        }

        onStateChanged(LifeState.RESUMING);
        try {
            resume0();
        } catch (Throwable cause) {
            onStateChanged(LifeState.SICK);
            throw LifecycleException.wrap(cause);
        }
        onStateChanged(LifeState.STARTED);
    }

    /**
     * Internal resume logic
     *
     * @throws LifecycleException if fails to resume
     */
    protected abstract void resume0() throws LifecycleException;

    @Override
    public final synchronized void destroy() throws LifecycleException {
        onStateChanged(LifeState.DESTROYING);
        try {
            destroy0();
        } catch (Throwable cause) {
            onStateChanged(LifeState.SICK);
            throw LifecycleException.wrap(cause);
        }
        onStateChanged(LifeState.DESTROYED);
    }

    /**
     * Internal destroy logic
     *
     * @throws LifecycleException if fails to destroy
     */
    protected abstract void destroy0() throws LifecycleException;

    /**
     * add a life state listener
     *
     * @param listener the listener
     */
    @Override
    public final void addLifeStateListener(LifeStateListener listener) {
        listeners.add(listener);
    }

    /**
     * get current state
     *
     * @return the state
     */
    @Override
    public final synchronized LifeState state() {
        return state;
    }

    private void onStateChanged(final LifeState newState) {
        final LifeState oldState = state;
        state = newState;
        listeners.forEach(listener -> {
            try {
                listener.onStateChanged(oldState, newState);
            } catch (Exception e) {
//                logger.error("listener[{}] of {} cause exception, {}->{}", listener.toString(), toString(), oldState,
//                        newState, e);
            }
        });
    }

    private LifecycleException illegalStateException() {
        return new LifecycleException("Illegal state " + state.name());
    }
}
