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

import net.md_5.bungee.api.CommandSender;
import rocks.spud.grid.bungee.GridPlugin;
import rocks.spud.grid.bungee.implementation.channel.Channel;

import javax.annotation.Nonnull;

/**
 * Provides a command for utilizing the global chat channel.
 *
 * @author Johannes Donath
 */
public class GlobalChannelCommand extends AbstractChannelCommand {

        public GlobalChannelCommand (@Nonnull GridPlugin plugin) {
                // noinspection ConstantConditions
                super (plugin, "global", null, (plugin.api ().configuration ().registerShorthandCommands () ? new String[] { "g" } : new String[0]));
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Channel getChannel (@Nonnull CommandSender sender, @Nonnull String[] args) {
                return this.grid ().getDefaultChannel ();
        }
}
