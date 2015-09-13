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
package rocks.spud.grid.bungee.api;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import rocks.spud.grid.bungee.api.configuration.IGlobalGridConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Johannes Donath
 */
public interface IGrid {

        /**
         * Retrieves the {@link rocks.spud.grid.bungee.api.IGrid} configuration.
         *
         * @return The configuration.
         */
        @Nonnull
        IGlobalGridConfiguration configuration ();

        /**
         * Creates a temporary (non-permanent) channel.
         *
         * @param name The channel name {@link java.lang.String}.
         * @return The {@link rocks.spud.grid.bungee.api.IGrid} instance.
         *
         * @throws java.lang.IllegalArgumentException if a channel with the same name already exists.
         */
        @Nonnull
        IChannel createChannel (@Nonnull String name) throws IllegalArgumentException;
        /**
         * Retrieves a channel based on it's name.
         *
         * @param name The name {@link java.lang.String}.
         * @return The {@link rocks.spud.grid.bungee.api.IChannel} (if any).
         */
        @Nonnull
        Optional<? extends IChannel> getChannel (@Nonnull String name);
        /**
         * Retrieves a {@link net.md_5.bungee.api.connection.Server}'s default channel.
         *
         * @param server The {@link net.md_5.bungee.api.connection.Server}.
         * @return The {@link rocks.spud.grid.bungee.api.IChannel} (if any).
         */
        @Nonnull
        Optional<? extends IChannel> getChannel (@Nonnull Server server);
        /**
         * Retrieves a {@link net.md_5.bungee.api.connection.Server}'s default channel.
         *
         * @param serverInfo The {@link net.md_5.bungee.api.config.ServerInfo} that describes the {@link net.md_5.bungee.api.connection.Server}.
         * @return The {@link rocks.spud.grid.bungee.api.IChannel} (if any).
         */
        @Nonnull
        Optional<? extends IChannel> getChannel (@Nonnull ServerInfo serverInfo);
        /**
         * Retrieves the global default channel.
         *
         * @return The {@link rocks.spud.grid.bungee.api.IChannel}.
         */
        @Nonnull
        IChannel getDefaultChannel ();
        /**
         * Handles an incoming message.
         *
         * @param server  The target {@link net.md_5.bungee.api.connection.Server}.
         * @param player  The sending {@link net.md_5.bungee.api.connection.ProxiedPlayer} (if any).
         * @param message The message {@link java.lang.String}.
         * @return The {@link rocks.spud.grid.bungee.api.IGrid instance}.
         */
        @Nonnull
        IGrid handleMessage (@Nonnull Server server, @Nullable ProxiedPlayer player, @Nonnull String message);
        /**
         * Removes a temporary (non-permanent) channel.
         *
         * @param name The channel name {@link java.lang.String}.
         * @return The {@link rocks.spud.grid.bungee.api.IGrid} instance.
         *
         * @throws java.lang.IllegalArgumentException if the channel is permanent.
         * @throws java.util.NoSuchElementException   if no channel with that name exists.
         */
        @Nonnull
        IGrid removeChannel (@Nonnull String name) throws IllegalArgumentException, NoSuchElementException;

        /**
         * Removes a temporary (non-permanent) channel.
         *
         * @param channel The channel name {@link java.lang.String}.
         * @return The {@link rocks.spud.grid.bungee.api.IGrid} instance.
         *
         * @throws java.lang.IllegalArgumentException if the channel is permanent.
         */
        @Nonnull
        IGrid removeChannel (@Nonnull IChannel channel) throws IllegalArgumentException;
}
