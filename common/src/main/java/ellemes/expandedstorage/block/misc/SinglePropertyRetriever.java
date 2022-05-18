/*
 * Copyright 2021-2022 Ellemes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ellemes.expandedstorage.block.misc;

import java.util.Optional;

public final class SinglePropertyRetriever<A> implements PropertyRetriever<A> {
    private final A single;

    public SinglePropertyRetriever(A single) {
        this.single = single;
    }

    @Override
    public <B> Optional<B> get(Property<A, B> property) {
        return Optional.ofNullable(property.get(single));
    }
}
