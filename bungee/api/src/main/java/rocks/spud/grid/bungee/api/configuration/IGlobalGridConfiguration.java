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
package rocks.spud.grid.bungee.api.configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

/**
 * @author Johannes Donath
 */
public interface IGlobalGridConfiguration extends IGridConfiguration {

        /**
         * Retrieves a list of auto-subscribe groups.
         * @return The names.
         */
        @Nonnull
        Set<String> autosubscribeGroups ();

        /**
         * Retrieves a list of channels included in an auto-subscribe group (if any).
         * @param name The group name.
         * @return The channel names.
         */
        @Nonnull
        Optional<Set<String>> autosubscribeGroup (@Nonnull String name);

        /**
         * Retrieves a list of permanent channel names.
         * @return The names.
         */
        @Nonnull
        Set<String> channels ();

        /**
         * Retrieves a server specific configuration.
         * @param name The server name/alias.
         * @return The configuration (if any).
         */
        @Nonnull
        Optional<IServerGridConfiguration> configuration (@Nonnull String name);

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        String defaultChannel ();

        /**
         * Defines whether shorthand commands are enabled.
         * @return {@code true} if enabled, {@code false} otherwise.
         */
        boolean registerShorthandCommands ();

        /**
         * Retrieves the message format.
         * @return The format.
         */
        @Nonnull
        String format ();
}
