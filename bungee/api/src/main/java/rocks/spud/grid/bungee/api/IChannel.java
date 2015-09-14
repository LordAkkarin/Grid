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

import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Represents a single chat channel.
 *
 * @author Johannes Donath
 */
public interface IChannel {

        /**
         * Dispatches a message in a channel.
         *
         * @param player  The {@link net.md_5.bungee.api.connection.ProxiedPlayer}.
         * @param message The message {@link java.lang.String}.
         * @return The {@link rocks.spud.grid.bungee.api.IChannel} instance.
         */
        @Nonnull
        IChannel dispatchMessage (@Nullable ProxiedPlayer player, @Nonnull String message);
        /**
         * Checks whether a player is already subscribed.
         *
         * @param player The {@link net.md_5.bungee.api.connection.ProxiedPlayer} to check for.
         * @return {@code true} if subscribed, {@code false} otherwise.
         */
        boolean isSubscribed (@Nonnull ProxiedPlayer player);
        /**
         * Checks whether the channel is temporary (non-permanent).
         *
         * @return {@code true} if the channel is temporary, {@code false} otherwise.
         */
        boolean isTemporary ();
        /**
         * Retrieves the channel name.
         *
         * @return The name.
         */
        @Nonnull
        String name ();
        /**
         * Retrieves a list of subscribed players.
         *
         * @return The {@link net.md_5.bungee.api.connection.ProxiedPlayer} list.
         */
        @Nonnull
        Set<ProxiedPlayer> players ();
        /**
         * Subscribes a player to the channel.
         *
         * @param player The {@link net.md_5.bungee.api.connection.ProxiedPlayer} to subscribe.
         * @return The {@link rocks.spud.grid.bungee.api.IChannel} instance.
         */
        @Nonnull
        IChannel subscribe (@Nonnull ProxiedPlayer player);

        /**
         * Un-Subscribes a player from the channel.
         *
         * @param player The {@link net.md_5.bungee.api.connection.ProxiedPlayer} to un-subscribe.
         * @return The {@link rocks.spud.grid.bungee.api.IChannel} instance.
         */
        @Nonnull
        IChannel unsubscribe (@Nonnull ProxiedPlayer player);

        /**
         * Un-Subscribes all players from the channel.
         *
         * @return The {@link rocks.spud.grid.bungee.api.IChannel} instance.
         */
        IChannel unsubscribeAll ();
}
