package org.javatest.fixtures.internal;

import org.javatest.fixtures.Try;

import java.util.function.Function;

public class Failure<A> implements Try<A> {
    private final Exception error;

    public Failure(Exception error) {
        this.error = error;
    }

    @Override
    public Try<A> mapError(Function<Exception, Exception> f) {
        return new Failure<>(f.apply(error));
    }

    @Override
    public <B> Try<B> map(Function<A, B> f) {
        return (Try<B>) this;
    }

    @Override
    public <B> Try<B> flatMap(Function<A, Try<B>> f) {
        return (Try<B>) this;
    }

    @Override
    public A recoverWith(Function<Exception, A> f) {
        return f.apply(error);
    }
}
