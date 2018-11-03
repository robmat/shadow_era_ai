package edu.bator.ui.cards;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T, U, G> {
    void accept(T t, U u, G g);

    default TriConsumer<T, U, G> andThen(TriConsumer<? super T, ? super U, ? super G> after) {
        Objects.requireNonNull(after);

        return (l, r, g) -> {
            accept(l, r, g);
            after.accept(l, r, g);
        };
    }
}
