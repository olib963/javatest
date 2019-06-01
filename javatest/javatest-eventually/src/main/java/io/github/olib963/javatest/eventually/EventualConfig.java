package io.github.olib963.javatest.eventually;

import java.time.Duration;
import java.util.Optional;

public class EventualConfig {
    public final int attempts;
    public final Duration waitInterval;
    public Optional<Duration> initialDelay;
    private EventualConfig(int attempts, Duration waitInterval, Optional<Duration> initialDelay) {
        this.attempts = attempts;
        this.waitInterval = waitInterval;
        this.initialDelay = initialDelay;
    }

    public static final EventualConfig DEFAULT_CONFIG = EventualConfig.of(13, Duration.ofSeconds(5));

    public static EventualConfig of(int attempts, Duration waitInterval) {
        return new EventualConfig(attempts, waitInterval, Optional.empty());
    }

    public static EventualConfig of(int attempts, Duration waitInterval, Duration initialDelay) {
        return new EventualConfig(attempts, waitInterval, Optional.of(initialDelay));
    }

    public EventualConfig withAttempts(int attempts) {
        return new EventualConfig(attempts, waitInterval, initialDelay);
    }

    public EventualConfig withWaitInterval(Duration waitInterval) {
        return new EventualConfig(attempts, waitInterval, initialDelay);
    }

    public EventualConfig withInitialDelay(Duration initialDelay) {
        return new EventualConfig(attempts, waitInterval, Optional.of(initialDelay));
    }

    public EventualConfig withNoInitialDelay() {
        return new EventualConfig(attempts, waitInterval, Optional.empty());
    }
}
