/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rocks.spud.grid.bungee.implementation.configuration;

import com.torchmind.candle.api.IObjectNode;
import com.torchmind.candle.node.property.StringPropertyNode;
import rocks.spud.grid.bungee.api.configuration.IServerGridConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides a server-specific configuration for {@link rocks.spud.grid.bungee.api.IGrid}.
 * @author Johannes Donath
 */
public class ServerGridConfiguration implements IServerGridConfiguration {
        private final String defaultChannel;

        public ServerGridConfiguration (@Nonnull IObjectNode node) {
                this.defaultChannel = node.getString ("defaultChannel", null);
        }

        /**
         * {@inheritDoc}
         */
        @Nullable
        @Override
        public String defaultChannel () {
                return this.defaultChannel;
        }
}
