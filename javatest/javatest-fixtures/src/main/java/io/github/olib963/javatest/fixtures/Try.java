package io.github.olib963.javatest.fixtures;

import io.github.olib963.javatest.CheckedSupplier;
import io.github.olib963.javatest.fixtures.internal.Failure;
import io.github.olib963.javatest.fixtures.internal.Success;
import io.github.olib963.javatest.javafire.fixtures.internal.Failure;
import io.github.olib963.javatest.javafire.fixtures.internal.Success;

import java.util.function.Function;

public interface Try<A> {

    Try<A> mapError(Function<Exception, Exception> f);

    <B> Try<B> map(Function<A, B> f);

    <B> Try<B> flatMap(Function<A, Try<B>> f);

    A recoverWith(Function<Exception, A> f);

    static <T> Try<T> Try(CheckedSupplier<T> function) {
        try {
            return new Success<>(function.get());
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    static <T> Try<Void> Try(CheckedConsumer<T> function, T value) {
        try {
            function.accept(value);
            return Success();
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    static Try<Void> Success() {
        return new Success<>(null);
    }

    static <T> Try<T> Success(T value) {
        return new Success<>(value);
    }

    static <T> Try<T> Failure(String reason) {
        return new Failure<>(new Exception(reason));
    }

}
