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

import io.jenetics.lattices.function.Int2Consumer;
import io.jenetics.lattices.function.Int3Consumer;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
abstract sealed class ForEach {

    final int[] start;
    final int[] end;
    final int[] order;

    int s0;
    int e0;
    int s1;
    int e1;
    int s2;
    int e2;

    ForEach(int[] start, int[] end, int[] order) {
        this.start = start;
        this.end = end;
        this.order = order;

        switch (start.length) {
            case 3:
                s2 = start[2];
                e2 = end[2];
            case 2:
                s1 = start[1];
                e1 = end[1];
            case 1:
                s0 = start[0];
                e0 = end[0];
        }
    }

    final Int2Consumer ordered(Int2Consumer action) {
        return switch (order[0]) {
            case 0 -> action;
            case 1 -> (i, j) -> action.accept(j, i);
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

    abstract void apply(IntConsumer action);

    abstract void apply(Int2Consumer action);

    abstract void apply(Int3Consumer action);

    final static class Forward extends ForEach {

        Forward(int[] start, int[] end, int[] order) {
            super(start, end, order);
        }

        @Override
        void apply(IntConsumer action) {
            for (int i0 = s0; i0 < e0; ++i0) {
                action.accept(i0);
            }
        }

        @Override
        void apply(Int2Consumer action) {
            final var act = ordered(action);

            for (int i1 = s1; i1 < e1; ++i1) {
                for (int i0 = s0; i0 < e0; ++i0) {
                    act.accept(i0, i1);
                }
            }
        }

        @Override
        void apply(Int3Consumer action) {
            final var act = ordered(action);

            for (int i2 = s2; i2 < e2; ++i2) {
                for (int i1 = s1; i1 < e1; ++i1) {
                    for (int i0 = s0; i0 < e0; ++i0) {
                        act.accept(i0, i1, i2);
                    }
                }
            }
        }
    }

    final static class Backward extends ForEach {

        Backward(int[] start, int[] end, int[] order) {
            super(start, end, order);
        }

        @Override
        void apply(IntConsumer action) {
            for (int i0 = e0; --i0 >= s0;) {
                action.accept(i0);
            }
        }

        @Override
        void apply(Int2Consumer action) {
            final var act = ordered(action);

            for (int i1 = e1; --i1 >= s1;) {
                for (int i0 = e0; --i0 >= s0;) {
                    act.accept(i0, i1);
                }
            }
        }

        @Override
        void apply(Int3Consumer action) {
            final var act = ordered(action);

            for (int i2 = e2; --i2 >= s2;) {
                for (int i1 = e1; --i1 >= s1;) {
                    for (int i0 = e0; --i0 >= s0;) {
                        act.accept(i0, i1, i2);
                    }
                }
            }
        }
    }

    static ForEach forward(Range range, Precedence precedence) {
        return new ForEach.Forward(
            precedence.sort(range.start().toArray()),
            precedence.sort(range.end().toArray()),
            precedence.order()
        );
    }

    static ForEach backward(Range range, Precedence precedence) {
        return new ForEach.Backward(
            precedence.sort(range.start().toArray()),
            precedence.sort(range.end().toArray()),
            precedence.order()
        );
    }

}
