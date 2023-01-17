package com.test.redis.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bruce.Yang
 * @date 2022/12/21 -17:12
 */
public class Expectation<T> implements Serializable {

    private AtomicInteger working = new AtomicInteger();

    private T expected;

    private T current;

    private long updateAt;

    private int attempt;

    private long attemptAt;

    public Expectation() {
    }

    public Expectation(final T expected, final T current) {
        this.expected = expected;
        this.current = current;
    }

    public boolean isWorking() {
        return working.get() > 0;
    }

    public void markWorking() {
        working.incrementAndGet();
    }

    public void unmarkWorking() {
        working.decrementAndGet();
    }

    @JsonIgnore
    public T getValue() {
        return null != expected ? expected : current;
    }

    public T getExpected() {
        return expected;
    }

    public synchronized void setExpected(T expected) {
        this.expected = expected;
        if (updateAt + 60000 < System.currentTimeMillis()) {
            working.set(0);
        }
    }

    public T getCurrent() {
        return current;
    }

    public synchronized void setCurrent(T current) {
        this.current = current;
        updateAt = System.currentTimeMillis();
    }

    public synchronized void achieveExpected() {
        if (null != expected) {
            this.setCurrent(expected);
        }
    }

    public synchronized void set(T value) {
        this.setExpected(value);
        this.setCurrent(value);
    }

    public synchronized void set(T expected, T current) {
        this.setExpected(expected);
        this.setCurrent(current);
    }

    public synchronized void set(Expectation<T> value) {
        this.setExpected(value.expected);
        this.setCurrent(value.current);
    }

    public synchronized boolean expectButNotMatch(T value) {
        return value.equals(expected) && !value.equals(current);
    }

    public synchronized boolean expectButNotMatch() {
        return null != expected ? !expected.equals(current) : false;
    }

    public synchronized boolean bothMatch(T value) {
        return value.equals(expected) && value.equals(current);
    }

    public synchronized boolean anyMatch(T value) {
        return value.equals(expected) || value.equals(current);
    }

    public void setCurrentIfNotUnderProtected(T value, int timeout) {
        if (this.canUpdate(timeout)) {
            this.setCurrent(value);
        }
    }

    public boolean canUpdate(int timeout) {
        return updateAt + timeout < System.currentTimeMillis();
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updatedAt) {
        this.updateAt = updatedAt;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public long getAttemptAt() {
        return attemptAt;
    }

    public void setAttemptAt(long attemptAt) {
        this.attemptAt = attemptAt;
    }

    public synchronized void increaseAttempt() {
        attempt++;
        attemptAt = System.currentTimeMillis();
    }

    public synchronized void resetAttempt() {
        attempt = 0;
        attemptAt = 0;
    }
}
