package io.github.olib963.javatest.fixtures.internal;

import io.github.olib963.javatest.fixtures.Try;

import java.util.Objects;
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
    @SuppressWarnings("unchecked")
    public <B> Try<B> map(Function<A, B> f) {
        return (Try<B>) this;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <B> Try<B> flatMap(Function<A, Try<B>> f) {
        return (Try<B>) this;
    }

    @Override
    public A recoverWith(Function<Exception, A> f) {
        return f.apply(error);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Failure<?> failure = (Failure<?>) o;
        return Objects.equals(error, failure.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Failure.class, error);
    }
}
