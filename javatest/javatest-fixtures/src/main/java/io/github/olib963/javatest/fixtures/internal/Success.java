package io.github.olib963.javatest.fixtures.internal;

import io.github.olib963.javatest.fixtures.Try;

import java.util.Objects;
import java.util.function.Function;

public class Success<A> implements Try<A> {
    private final A value;
    public Success(A value) {
        this.value = value;
    }

    @Override
    public Try<A> mapError(Function<Exception, Exception> f) {
        return this;
    }

    @Override
    public <B> Try<B> map(Function<A, B> f) {
        return new Success<>(f.apply(value));
    }

    @Override
    public <B> Try<B> flatMap(Function<A, Try<B>> f) {
        return f.apply(value);
    }

    @Override
    public A recoverWith(Function<Exception, A> f) {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Success<?> success = (Success<?>) o;
        return Objects.equals(value, success.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Success.class, value);
    }
}
