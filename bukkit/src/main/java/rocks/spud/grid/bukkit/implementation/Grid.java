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
package rocks.spud.grid.bukkit.implementation;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import rocks.spud.grid.api.ErrorCode;
import rocks.spud.grid.bukkit.GridPlugin;
import rocks.spud.grid.bukkit.api.IGrid;
import rocks.spud.grid.util.Packet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Provides an implementation of {@link rocks.spud.grid.bukkit.api.IGrid}.
 *
 * @author Johannes Donath
 */
public class Grid implements IGrid, PluginMessageListener {
        public static final String CHANNEL_NAME = "BungeeCord|Grid";

        private final Map<UUID, SuccessCallback> successCallbackMap = new HashMap<> ();
        private final Map<UUID, FailureCallback> failureCallbackMap = new HashMap<> ();

        private final GridPlugin plugin;

        public Grid (@Nonnull GridPlugin plugin) {
                this.plugin = plugin;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid createChannel (@Nonnull String name, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                // @formatter:off
                this.sendPacket (
                        Packet.empty ()
                                .write (this.storeCallback (callback, failureCallback))
                                .write ("Channel")
                                .write ("Create")
                                .write (name)
                );
                // @formatter:on

                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid createChannel (@Nonnull String name, @Nullable SuccessCallback callback) throws IllegalStateException {
                return this.createChannel (name, callback, null);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid createChannel (@Nonnull String name, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                return this.createChannel (name, null, failureCallback);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid createChannel (@Nonnull String name) {
                return this.createChannel (name, null, null);
        }

        /**
         * Generates a unique request identifier.
         * @return The identifier.
         */
        @Nonnull
        private UUID generateRequestIdentifier () {
                UUID identifier;

                do {
                        identifier = UUID.randomUUID ();
                }
                while (this.successCallbackMap.containsKey (identifier) || this.failureCallbackMap.containsKey (identifier));

                return identifier;
        }

        /**
         * Retrieves the first available player.
         * @return The player.
         * @throws java.lang.IllegalStateException when no player is available.
         */
        @Nonnull
        private Player getFirstAvailablePlayer () throws IllegalStateException {
                Collection<? extends Player> players = this.plugin.getServer ().getOnlinePlayers ();
                if (players.size () == 0) {
                        throw new IllegalStateException ("No player available for command broadcast");
                }
                return players.iterator ().next ();
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid message (@Nonnull String channelName, @Nonnull String message, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                // @formatter:off
                this.sendPacket (
                        Packet.empty ()
                              .write (this.storeCallback (callback, failureCallback))
                              .write ("Channel")
                              .write ("Message")
                              .write (channelName)
                              .write (message)
                );
                // @formatter:on

                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid message (@Nonnull String channelName, @Nonnull String message, @Nullable SuccessCallback callback) throws IllegalStateException {
                return this.message (channelName, message, callback, null);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid message (@Nonnull String channelName, @Nonnull String message, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                return this.message (channelName, message, null, failureCallback);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid message (@Nonnull String channelName, @Nonnull String message) throws IllegalStateException {
                return this.message (channelName, message, null, null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPluginMessageReceived (@Nonnull String channel, @Nonnull Player player, @Nonnull byte[] message) {
                Packet packet = Packet.wrap (message);
                UUID requestID = packet.readUUID ();
                Set<ErrorCode> errorCodes = packet.readErrorCode ();

                if (ErrorCode.hasError (errorCodes)) {
                        if (this.failureCallbackMap.containsKey (requestID)) {
                                this.failureCallbackMap.get (requestID).accept (errorCodes);
                        }
                } else if (this.successCallbackMap.containsKey (requestID)) {
                        this.successCallbackMap.get (requestID).accept ();
                }
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid removeChannel (@Nonnull String name, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                // @formatter:off
                this.sendPacket (
                        Packet.empty ()
                                .write (this.storeCallback (callback, failureCallback))
                                .write ("Channel")
                                .write ("Remove")
                                .write (name)
                );
                // @formatter:on

                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid removeChannel (@Nonnull String name, @Nullable SuccessCallback callback) throws IllegalStateException {
                return this.createChannel (name, callback, null);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid removeChannel (@Nonnull String name, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                return this.createChannel (name, null, failureCallback);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid removeChannel (@Nonnull String name) throws IllegalStateException {
                return this.createChannel (name, null, null);
        }

        /**
         * Broadcasts a packet.
         * @param packet The packet.
         * @throws java.lang.IllegalStateException when no player is available to broadcast the command.
         */
        private void sendPacket (@Nonnull Packet packet) throws IllegalStateException {
                this.getFirstAvailablePlayer ().sendPluginMessage (this.plugin, CHANNEL_NAME, packet.array ());
        }

        /**
         * Generates a unique request identifier and stores the callbacks (if any).
         * @param callback The success callback.
         * @param failureCallback The failure callback.
         * @return The identifier.
         */
        @Nonnull
        private UUID storeCallback (@Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                UUID identifier = this.generateRequestIdentifier ();
                if (callback != null) { this.successCallbackMap.put (identifier, callback); }
                if (failureCallback != null) { this.failureCallbackMap.put (identifier, failureCallback); }
                return identifier;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid subscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                // @formatter:off
                this.sendPacket (
                        Packet.empty ()
                                .write (this.storeCallback (callback, failureCallback))
                                .write ("Channel")
                                .write ("Subscribe")
                                .write (playerName)
                                .write (channelName)
                );
                // @formatter:on

                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid subscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable SuccessCallback callback) throws IllegalStateException {
                return this.subscribe (channelName, playerName, callback, null);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid subscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                return this.subscribe (channelName, playerName, null, failureCallback);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid subscribe (@Nonnull String channelName, @Nonnull String playerName) throws IllegalStateException {
                return this.subscribe (channelName, playerName, null, null);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid subscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                return this.subscribe (channelName, player.getName (), callback, failureCallback);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid subscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable SuccessCallback callback) throws IllegalStateException {
                return this.subscribe (player, channelName, callback, null);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid subscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                return this.subscribe (player, channelName, null, failureCallback);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid subscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName) throws IllegalStateException {
                return this.subscribe (player, channelName, null, null);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid unsubscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                // @formatter:off
                this.sendPacket (
                        Packet.empty ()
                                .write (this.storeCallback (callback, failureCallback))
                                .write ("Channel")
                                .write ("Unsubscribe")
                                .write (playerName)
                                .write (channelName)
                );
                // @formatter:on

                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid unsubscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable SuccessCallback callback) throws IllegalStateException {
                return this.unsubscribe (channelName, playerName, callback, null);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid unsubscribe (@Nonnull String playerName, @Nonnull String channelName, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                return this.unsubscribe (channelName, playerName, null, failureCallback);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid unsubscribe (@Nonnull String channelName, @Nonnull String playerName) throws IllegalStateException {
                return this.unsubscribe (channelName, playerName, null, null);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid unsubscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable SuccessCallback callback, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                return this.unsubscribe (channelName, player.getName (), callback, failureCallback);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid unsubscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable SuccessCallback callback) throws IllegalStateException {
                return this.unsubscribe (player, channelName, callback, null);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid unsubscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName, @Nullable FailureCallback failureCallback) throws IllegalStateException {
                return this.unsubscribe (player, channelName, null, failureCallback);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IGrid unsubscribe (@Nonnull OfflinePlayer player, @Nonnull String channelName) throws IllegalStateException {
                return this.unsubscribe (player, channelName, null, null);
        }
}
