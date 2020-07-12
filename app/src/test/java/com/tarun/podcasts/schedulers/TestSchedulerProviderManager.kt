package com.tarun.podcasts.schedulers

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.TestScheduler

/**
 * A test class corresponding to [TestSchedulerProviderManager]
 */
class TestSchedulerProviderManager(var testScheduler: TestScheduler) : SchedulerProvider {
    override fun io(): Scheduler {
        return testScheduler
    }

    override fun ui(): Scheduler {
        return testScheduler
    }
}