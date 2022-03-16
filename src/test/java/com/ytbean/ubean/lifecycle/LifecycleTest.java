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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The class LifecycleTest
 *
 * @author ytbean
 * @date 2022/2/25 23:01
 */
@DisplayName("Lifecycle Tests")
public class LifecycleTest {


    private LifecycleAdapter lifecycle;

    @BeforeEach
    void activeLifecycle() {
        lifecycle = new LifecycleAdapter();
    }


    @Test
    @DisplayName("a newly created lifecycle can't be suspended")
    void newlyCreatedLifecycleCantBeSuspended() {
        Assertions.assertThrows(LifecycleException.class, () -> lifecycle.suspend());
    }

    @Test
    @DisplayName("a newly created lifecycle can't be resumed")
    void newlyCreatedLifecycleCantBeResumed() {
        Assertions.assertThrows(LifecycleException.class, () -> lifecycle.resume());
    }

    @Test
    @DisplayName("a newly created lifecycle can start without init")
    void newlyCreatedLifecycleCanStartWithoutInit() {
        Assertions.assertDoesNotThrow(() -> lifecycle.start());
        Assertions.assertEquals(LifeState.STARTED, lifecycle.state());
    }


    @Test
    @DisplayName("a newly created lifecycle should be in state NEW")
    void newlyCreatedLifecycleShouldBeInStateNew() {
        Assertions.assertEquals(LifeState.NEW, lifecycle.state(), "a newly created lifecycle should be in state NEW");
    }

    @Test
    @DisplayName("can not do any operation except destruction when state is SICK")
    void cantDoAnyOperationExceptDestructionWhenStateInSick() {
        LifecycleAdapter innerClass = new LifecycleAdapter() {
            @Override
            protected void init0() throws LifecycleException {
                throw new LifecycleException("intendly sick");
            }
        };
        Assertions.assertThrows(LifecycleException.class, innerClass::init);
        Assertions.assertThrows(LifecycleException.class, innerClass::init);
        Assertions.assertThrows(LifecycleException.class, innerClass::start);
        Assertions.assertThrows(LifecycleException.class, innerClass::suspend);
        Assertions.assertThrows(LifecycleException.class, innerClass::resume);
        Assertions.assertDoesNotThrow(innerClass::destroy);
    }

    @Test
    @DisplayName("can not init when state is not NEW or DESTROYED")
    void cantInitWhenStateIsNotNewOrDestroyed() {
        try {
            lifecycle.start();
        } catch (LifecycleException e) {
            Assertions.fail("unexpected, should not happen", e);
        }

        Assertions.assertThrows(LifecycleException.class, lifecycle::init);
    }

    @Test
    @DisplayName("state should be INITIALIZED after init")
    void stateShouldBeInitializedAfterInit() {
        try {
            lifecycle.init();
        } catch (LifecycleException e) {
            Assertions.fail("unexpected, should not happen", e);
        }
        Assertions.assertEquals(LifeState.INITIALIZED, lifecycle.state());
    }


    @Test
    @DisplayName("state should be STARTED after start")
    void stateShouldBeStartedAfterStart() {
        try {
            lifecycle.start();
        } catch (LifecycleException e) {
            Assertions.fail("unexpected, should not happen", e);
        }
        Assertions.assertEquals(LifeState.STARTED, lifecycle.state());
    }


    @Test
    @DisplayName("can not suspend when state is not STARTED")
    void cantSuspendWhenStateIsNotStarted() {
        try {
            lifecycle.init();
        } catch (LifecycleException e) {
            Assertions.fail("unexpected, should not happen", e);
        }
        Assertions.assertThrows(LifecycleException.class, lifecycle::suspend);
    }

    @Test
    @DisplayName("state should be SUSPENDED after suspend")
    void stateShouldBeSuspendedAfterSuspend() {
        try {
            lifecycle.start();
            lifecycle.suspend();
        } catch (LifecycleException e) {
            Assertions.fail("unexpected, should not happen", e);
        }
        Assertions.assertEquals(LifeState.SUSPENDED, lifecycle.state());
    }

    @Test
    @DisplayName("can not resume when state is not SUSPENDED")
    void cantResumeWhenStateIsNotSuspended() {
        try {
            lifecycle.start();
        } catch (LifecycleException e) {
            Assertions.fail("unexpected, should not happen", e);
        }
        Assertions.assertThrows(LifecycleException.class, lifecycle::resume);
    }

    @Test
    @DisplayName("state should be STARTED after resume")
    void stateShouldBeStartedAfterResume() {
        try {
            lifecycle.start();
            lifecycle.suspend();
            lifecycle.resume();
        } catch (LifecycleException e) {
            Assertions.fail("unexpected, should not happen", e);
        }
        Assertions.assertEquals(LifeState.STARTED, lifecycle.state());

    }


    @Test
    @DisplayName("state should be DESTROYED after destroy")
    void stateShouldBeDestroyedAfterDestroy() {
        try {
            lifecycle.start();
            lifecycle.destroy();
        } catch (LifecycleException e) {
            Assertions.fail("unexpected, should not happen", e);
        }
        Assertions.assertEquals(LifeState.DESTROYED, lifecycle.state());

    }

    @Test
    @DisplayName("fired only once even if a listener was added repeatly")
    void firedOncesWhenSameListenerWasAddedMultiTimes() {

        final AtomicInteger firedCounter = new AtomicInteger(0);
        LifeStateListener listener = (oldState, newState) -> firedCounter.incrementAndGet();

        lifecycle.addLifeStateListener(listener);
        lifecycle.addLifeStateListener(listener);

        try {
            lifecycle.init();
        } catch (LifecycleException e) {
            Assertions.fail("unexpected, should not happen", e);
        }

        Assertions.assertEquals(2, firedCounter.get());

        try {
            lifecycle.start();
        } catch (LifecycleException e) {
            Assertions.fail("unexpected, should not happen", e);
        }

        Assertions.assertEquals(4, firedCounter.get());
    }
}
