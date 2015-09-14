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
package rocks.spud.grid.bungee.implementation.channel;

import rocks.spud.grid.bungee.implementation.configuration.GlobalGridConfiguration;

import javax.annotation.Nonnull;

/**
 * Represents a temporary channel.
 * @author Johannes Donath
 */
public class TemporaryChannel extends Channel {

        public TemporaryChannel (@Nonnull GlobalGridConfiguration configuration, @Nonnull String name) {
                super (configuration, name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isTemporary () {
                return true;
        }
}
