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
package rocks.spud.grid.bungee.command;

import net.md_5.bungee.api.plugin.Command;
import rocks.spud.grid.bungee.GridPlugin;
import rocks.spud.grid.bungee.implementation.Grid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides an abstract implementation of {@link net.md_5.bungee.api.plugin.Command}.
 *
 * @author Johannes Donath
 */
public abstract class AbstractCommand extends Command {
        private final GridPlugin plugin;

        public AbstractCommand (@Nonnull GridPlugin plugin, @Nonnull String name) {
                super (name);
                this.plugin = plugin;
        }

        public AbstractCommand (@Nonnull GridPlugin plugin, @Nonnull String name, @Nullable String permission, @Nullable String... aliases) {
                super (name, permission, aliases);
                this.plugin = plugin;
        }

        /**
         * Retrieves the active {@link rocks.spud.grid.bungee.implementation.Grid} instance.
         *
         * @return The instance.
         */
        @Nonnull
        public Grid grid () {
                // noinspection ConstantConditions
                return this.plugin ().api ();
        }

        /**
         * Retrieves the parent {@link rocks.spud.grid.bungee.GridPlugin} instance.
         *
         * @return The instance.
         */
        @Nonnull
        public GridPlugin plugin () {
                return this.plugin;
        }
}
