/*
 * Java Lattice Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.lattices.structure;

import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

import io.jenetics.lattices.function.Int2Consumer;
import io.jenetics.lattices.function.Int2Predicate;
import io.jenetics.lattices.function.Int3Consumer;
import io.jenetics.lattices.function.Int3Predicate;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
abstract sealed class Looper implements Loop1d, Loop2d, Loop3d {
    final int[] start;
    final int[] end;
    final int[] order;

    int s0, e0, s1, e1, s2, e2;

    Looper(int[] start, int[] end, int[] order) {
        assert
            start.length == end.length &&
            start.length == order.length &&
            start.length >= 1 &&
            start.length <= 3;

        this.start = start;
        this.end = end;
        this.order = order;

        switch (start.length) {
            case 3: s2 = start[2]; e2 = end[2];
            case 2: s1 = start[1]; e1 = end[1];
            case 1: s0 = start[0]; e0 = end[0];
        }
    }


    final Int2Consumer ordered(Int2Consumer action) {
        return switch (order[0]) {
            case 0 -> action;
            case 1 -> (i, j) -> action.accept(j, i);
            default -> throw new IllegalStateException();
        };
    }

    final Int2Predicate ordered(Int2Predicate predicate) {
        return switch (order[0]) {
            case 0 -> predicate;
            case 1 -> (i, j) -> predicate.test(j, i);
            default -> throw new IllegalStateException();
        };
    }

    final Int3Consumer ordered(Int3Consumer action) {
        return switch (order[0]) {
            case 0 -> switch (order[1]) {
                case 1 -> action;
                case 2 -> (a, b, c) -> action.accept(a, c, b);
                default -> throw new IllegalStateException();
            };
            case 1 -> switch (order[1]) {
                case 0 -> (a, b, c) -> action.accept(b, a, c);
                case 2 -> (a, b, c) -> action.accept(b, c, a);
                default -> throw new IllegalStateException();
            };
            case 2 -> switch (order[1]) {
                case 0 -> (a, b, c) -> action.accept(c, a, b);
                case 1 -> (a, b, c) -> action.accept(c, b, a);
                default -> throw new IllegalStateException();
            };
            default -> throw new IllegalStateException();
        };
    }

    final Int3Predicate ordered(Int3Predicate predicate) {
        return switch (order[0]) {
            case 0 -> switch (order[1]) {
                case 1 -> predicate;
                case 2 -> (a, b, c) -> predicate.test(a, c, b);
                default -> throw new IllegalStateException();
            };
            case 1 -> switch (order[1]) {
                case 0 -> (a, b, c) -> predicate.test(b, a, c);
                case 2 -> (a, b, c) -> predicate.test(b, c, a);
                default -> throw new IllegalStateException();
            };
            case 2 -> switch (order[1]) {
                case 0 -> (a, b, c) -> predicate.test(c, a, b);
                case 1 -> (a, b, c) -> predicate.test(c, b, a);
                default -> throw new IllegalStateException();
            };
            default -> throw new IllegalStateException();
        };
    }

    /* *************************************************************************
     * Forward/Backward Looper implementations.
     * ************************************************************************/

    /**
     * Implements forward looping.
     */
    final static class Forward extends Looper {

        Forward(int[] start, int[] end, int[] order) {
            super(start, end, order);
        }

        @Override
        public void forEach(IntConsumer action) {
            for (int i0 = s0; i0 < e0; ++i0) {
                action.accept(i0);
            }
        }

        @Override
        public void forEach(Int2Consumer action) {
            final var act = ordered(action);

            for (int i1 = s1; i1 < e1; ++i1) {
                for (int i0 = s0; i0 < e0; ++i0) {
                    act.accept(i0, i1);
                }
            }
        }

        @Override
        public void forEach(Int3Consumer action) {
            final var act = ordered(action);

            for (int i2 = s2; i2 < e2; ++i2) {
                for (int i1 = s1; i1 < e1; ++i1) {
                    for (int i0 = s0; i0 < e0; ++i0) {
                        act.accept(i0, i1, i2);
                    }
                }
            }
        }

        @Override
        public boolean anyMatch(IntPredicate predicate) {
            for (int i0 = s0; i0 < e0; ++i0) {
                if (predicate.test(i0)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean anyMatch(Int2Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i1 = s1; i1 < e1; ++i1) {
                for (int i0 = s0; i0 < e0; ++i0) {
                    if (pred.test(i0, i1)) {
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        public boolean anyMatch(Int3Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i2 = s2; i2 < e2; ++i2) {
                for (int i1 = s1; i1 < e1; ++i1) {
                    for (int i0 = s0; i0 < e0; ++i0) {
                        if (pred.test(i0, i1, i2)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        @Override
        public boolean allMatch(IntPredicate predicate) {
            for (int i0 = s0; i0 < e0; ++i0) {
                if (!predicate.test(i0)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public boolean allMatch(Int2Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i1 = s1; i1 < e1; ++i1) {
                for (int i0 = s0; i0 < e0; ++i0) {
                    if (!pred.test(i0, i1)) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public boolean allMatch(Int3Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i2 = s2; i2 < e2; ++i2) {
                for (int i1 = s1; i1 < e1; ++i1) {
                    for (int i0 = s0; i0 < e0; ++i0) {
                        if (!pred.test(i0, i1, i2)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        @Override
        public boolean nonMatch(IntPredicate predicate) {
            for (int i0 = s0; i0 < e0; ++i0) {
                if (predicate.test(i0)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public boolean nonMatch(Int2Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i1 = s1; i1 < e1; ++i1) {
                for (int i0 = s0; i0 < e0; ++i0) {
                    if (pred.test(i0, i1)) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public boolean nonMatch(Int3Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i2 = s2; i2 < e2; ++i2) {
                for (int i1 = s1; i1 < e1; ++i1) {
                    for (int i0 = s0; i0 < e0; ++i0) {
                        if (pred.test(i0, i1, i2)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    /**
     * Implements backward looping.
     */
    final static class Backward extends Looper {

        Backward(int[] start, int[] end, int[] order) {
            super(start, end, order);
        }

        @Override
        public void forEach(IntConsumer action) {
            for (int i0 = e0; --i0 >= s0;) {
                action.accept(i0);
            }
        }

        @Override
        public void forEach(Int2Consumer action) {
            final var act = ordered(action);

            for (int i1 = e1; --i1 >= s1;) {
                for (int i0 = e0; --i0 >= s0;) {
                    act.accept(i0, i1);
                }
            }
        }

        @Override
        public void forEach(Int3Consumer action) {
            final var act = ordered(action);

            for (int i2 = e2; --i2 >= s2;) {
                for (int i1 = e1; --i1 >= s1;) {
                    for (int i0 = e0; --i0 >= s0;) {
                        act.accept(i0, i1, i2);
                    }
                }
            }
        }

        @Override
        public boolean anyMatch(IntPredicate predicate) {
            for (int i0 = e0; --i0 >= s0;) {
                if (predicate.test(i0)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean anyMatch(Int2Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i1 = e1; --i1 >= s1;) {
                for (int i0 = e0; --i0 >= s0;) {
                    if (pred.test(i0, i1)) {
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        public boolean anyMatch(Int3Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i2 = e2; --i2 >= s2;) {
                for (int i1 = e1; --i1 >= s1;) {
                    for (int i0 = e0; --i0 >= s0;) {
                        if (pred.test(i0, i1, i2)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        @Override
        public boolean allMatch(IntPredicate predicate) {
            for (int i0 = e0; --i0 >= s0;) {
                if (!predicate.test(i0)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public boolean allMatch(Int2Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i1 = e1; --i1 >= s1;) {
                for (int i0 = e0; --i0 >= s0;) {
                    if (!pred.test(i0, i1)) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public boolean allMatch(Int3Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i2 = e2; --i2 >= s2;) {
                for (int i1 = e1; --i1 >= s1;) {
                    for (int i0 = e0; --i0 >= s0;) {
                        if (!pred.test(i0, i1, i2)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        @Override
        public boolean nonMatch(IntPredicate predicate) {
            for (int i0 = e0; --i0 >= s0;) {
                if (predicate.test(i0)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public boolean nonMatch(Int2Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i1 = e1; --i1 >= s1;) {
                for (int i0 = e0; --i0 >= s0;) {
                    if (pred.test(i0, i1)) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public boolean nonMatch(Int3Predicate predicate) {
            final var pred = ordered(predicate);

            for (int i2 = e2; --i2 >= s2;) {
                for (int i1 = e1; --i1 >= s1;) {
                    for (int i0 = e0; --i0 >= s0;) {
                        if (pred.test(i0, i1, i2)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    static Looper forward(Range range, Precedence precedence) {
        if (range.dimensionality() != precedence.length()) {
            throw new IllegalArgumentException(
                "Dimensionality of range and precedence doesn't match: %d != %d."
                    .formatted(range.dimensionality(), precedence.length())
            );
        }

        return new Looper.Forward(
            precedence.sort(range.start().toArray()),
            precedence.sort(range.end().toArray()),
            precedence.order()
        );
    }

    static Looper backward(Range range, Precedence precedence) {
        if (range.dimensionality() != precedence.length()) {
            throw new IllegalArgumentException(
                "Dimensionality of range and precedence doesn't match: %d != %d."
                    .formatted(range.dimensionality(), precedence.length())
            );
        }

        return new Looper.Backward(
            precedence.sort(range.start().toArray()),
            precedence.sort(range.end().toArray()),
            precedence.order()
        );
    }

}
