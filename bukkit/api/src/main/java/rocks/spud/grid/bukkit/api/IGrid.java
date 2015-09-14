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
package rocks.spud.grid.bukkit.api;

import org.bukkit.OfflinePlayer;
import rocks.spud.grid.api.ErrorCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Provides remote access to the Grid implementation.
 *
 * @author Johannes Donath
 */
public interface IGrid {

        /**
         * Creates a temporary (non-permanent) channel.
         *
         * @param name            The channel name.
         * @param callback        The success callback.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid createChannel (@Nonnull String name, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException;

        /**
         * Creates a temporary (non-permanent) channel.
         *
         * @param name     The channel name.
         * @param callback The success callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid createChannel (@Nonnull String name, @Nullable SuccessCallback callback) throws IllegalStateException;

        /**
         * Creates a temporary (non-permanent) channel.
         *
         * @param name            The channel name.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid createChannel (@Nonnull String name, @Nullable FailureCallback failureCallback) throws IllegalStateException;

        /**
         * Creates a temporary (non-permanent) channel.
         *
         * @param name The channel name.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid createChannel (@Nonnull String name) throws IllegalStateException;
        /**
         * Messages a channel.
         *
         * @param channelName     The channel name.
         * @param message         The message.
         * @param callback        The success callback.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid message (@Nonnull String channelName, @Nonnull String message, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Messages a channel.
         *
         * @param channelName The channel name.
         * @param message     The message.
         * @param callback    The success callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid message (@Nonnull String channelName, @Nonnull String message, @Nullable SuccessCallback callback) throws IllegalStateException;
        /**
         * Messages a channel.
         *
         * @param channelName     The channel name.
         * @param message         The message.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid message (@Nonnull String channelName, @Nonnull String message, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Messages a channel.
         *
         * @param channelName The channel name.
         * @param message     The message.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid message (@Nonnull String channelName, @Nonnull String message) throws IllegalStateException;
        /**
         * Removes a temporary (non-permanent) channel.
         *
         * @param name            The channel name.
         * @param callback        The success callback.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid removeChannel (@Nonnull String name, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Removes a temporary (non-permanent) channel.
         *
         * @param name     The channel name.
         * @param callback The success callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid removeChannel (@Nonnull String name, @Nullable SuccessCallback callback) throws IllegalStateException;
        /**
         * Removes a temporary (non-permanent) channel.
         *
         * @param name            The channel name.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid removeChannel (@Nonnull String name, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Removes a temporary (non-permanent) channel.
         *
         * @param name The channel name.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid removeChannel (@Nonnull String name) throws IllegalStateException;
        /**
         * Subscribes a player to a channel.
         *
         * @param playerName      The player name.
         * @param channelName     The channel name.
         * @param callback        The success callback.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid subscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Subscribes a player to a channel.
         *
         * @param playerName  The player name.
         * @param channelName The channel name.
         * @param callback    The success callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid subscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable SuccessCallback callback) throws IllegalStateException;
        /**
         * Subscribes a player to a channel.
         *
         * @param playerName      The player name.
         * @param channelName     The channel name.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid subscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Subscribes a player to a channel.
         *
         * @param playerName  The player name.
         * @param channelName The channel name.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid subscribe (@Nonnull String playerName, @Nonnull String channelName) throws IllegalStateException;
        /**
         * Subscribes a player to a channel.
         *
         * @param player          The player.
         * @param channelName     The channel name.
         * @param callback        The success callback.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid subscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Subscribes a player to a channel.
         *
         * @param player      The player.
         * @param channelName The channel name.
         * @param callback    The success callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid subscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable SuccessCallback callback) throws IllegalStateException;
        /**
         * Subscribes a player to a channel.
         *
         * @param player          The player.
         * @param channelName     The channel name.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid subscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Subscribes a player to a channel.
         *
         * @param player      The player.
         * @param channelName The channel name.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid subscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName) throws IllegalStateException;
        /**
         * Un-Subscribes a player from a channel.
         *
         * @param playerName      The player name.
         * @param channelName     The channel name.
         * @param callback        The success callback.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid unsubscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Un-Subscribes a player from a channel.
         *
         * @param playerName  The player name.
         * @param channelName The channel name.
         * @param callback    The success callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid unsubscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable SuccessCallback callback) throws IllegalStateException;
        /**
         * Un-Subscribes a player from a channel.
         *
         * @param playerName      The player name.
         * @param channelName     The channel name.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid unsubscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Un-Subscribes a player from a channel.
         *
         * @param playerName  The player name.
         * @param channelName The channel name.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid unsubscribe (@Nonnull String channelName, @Nonnull String playerName) throws IllegalStateException;
        /**
         * Un-Subscribes a player from a channel.
         *
         * @param player          The player.
         * @param channelName     The channel name.
         * @param callback        The success callback.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid unsubscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Un-Subscribes a player from a channel.
         *
         * @param player      The player.
         * @param channelName The channel name.
         * @param callback    The success callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid unsubscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable SuccessCallback callback) throws IllegalStateException;
        /**
         * Un-Subscribes a player from a channel.
         *
         * @param player          The player.
         * @param channelName     The channel name.
         * @param failureCallback The failure callback.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid unsubscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable FailureCallback failureCallback) throws IllegalStateException;
        /**
         * Un-Subscribes a player from a channel.
         *
         * @param player      The player.
         * @param channelName The channel name.
         * @return The {@link rocks.spud.grid.bukkit.api.IGrid} instance.
         *
         * @throws java.lang.IllegalStateException when no players are available to broadcast the command.
         */
        @Nonnull
        IGrid unsubscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName) throws IllegalStateException;

        /**
         * Provides a functional interface for failed queries.
         */
        @FunctionalInterface
        interface FailureCallback {
                void accept (@Nonnull Set<ErrorCode> codes);
        }

        /**
         * Provides a functional interface for successful queries that return a result.
         *
         * @param <T> The result type.
         */
        @FunctionalInterface
        interface ResultCallback<T> {
                void accept (@Nullable T result);
        }

        /**
         * Provides a functional interface for successful queries.
         */
        @FunctionalInterface
        interface SuccessCallback {
                void accept ();
        }
}
