package com.tarun.podcasts.schedulers

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {
    /**
     * Getter for io scheduler thread
     *
     * @return The io Scheduler
     */
    fun io(): Scheduler?

    /**
     * Getter for ui scheduler thread
     *
     * @return The ui scheduler
     */
    fun ui(): Scheduler?
}