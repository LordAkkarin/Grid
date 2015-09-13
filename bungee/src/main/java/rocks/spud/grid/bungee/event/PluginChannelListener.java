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
package rocks.spud.grid.bungee.event;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import rocks.spud.grid.api.ErrorCode;
import rocks.spud.grid.bungee.GridPlugin;
import rocks.spud.grid.bungee.implementation.channel.Channel;
import rocks.spud.grid.util.Packet;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Johannes Donath
 */
public class PluginChannelListener implements Listener {
        public static final String CHANNEL_NAME = "BungeeCord|Grid";
        public static final String METHOD_FORMAT = "on%sMessage";

        private final GridPlugin plugin;

        public PluginChannelListener (@Nonnull GridPlugin plugin) {
                this.plugin = plugin;
        }

        /**
         * Calls a sub-channel handler.
         *
         * @param channel The channel.
         * @param sender  The sender.
         * @param packet  The packet.
         */
        private void callHandler (@Nonnull String channel, @Nonnull Connection sender, @Nonnull Packet packet) {
                if (!(sender instanceof Server)) { return; }

                try {

                        // @formatter:off
                        MethodHandle handle = MethodHandles.lookup ()
                                                           .findVirtual (
                                                                   this.getClass (),
                                                                   String.format (METHOD_FORMAT, channel),
                                                                   MethodType.methodType (void.class, Server.class, Packet.class)
                                                           );
                        // @formatter:on

                        handle.bindTo (this);
                        handle.invokeExact (sender, packet);
                } catch (NoSuchMethodException ex) {
                        this.logger ().warning ("Could not locate handler for sub-channel: " + channel);
                } catch (Throwable ex) {
                        this.logger ().severe ("Cannot call handler for sub-channel \"" + channel + "\": " + ex.getMessage ());
                        ex.printStackTrace ();
                }
        }

        /**
         * Retrieves the plugin logger.
         *
         * @return The logger.
         */
        @Nonnull
        private Logger logger () {
                return this.plugin.getLogger ();
        }

        /**
         * Handles the "Channel" sub-channel.
         *
         * @param sender The sender.
         * @param packet The packet.
         */
        public void onChannelMessage (@Nonnull Server sender, @Nonnull Packet packet) {
                String action = packet.readString ();
                Packet response = Packet.empty ();

                // all packets include a requestID that is used to easily identify the response further down the road
                // this randomly generated ID will just be echoed back. Keep in mind that it currently uses 128 bits
                // which might be a bit excessive for smaller networks that will not make heavy use of these features
                UUID requestID = packet.readUUID ();
                response.write (requestID);

                switch (action) {
                        case "Create": {
                                String name = packet.readString ();

                                try {
                                        // noinspection ConstantConditions
                                        this.plugin.api ().createChannel (name);
                                        response.write (ErrorCode.SUCCESS);
                                } catch (IllegalArgumentException ex) {
                                        response.write (ErrorCode.DUPLICATE);
                                }
                        }
                        break;
                        case "Remove": {
                                String name = packet.readString ();

                                try {
                                        // noinspection ConstantConditions
                                        this.plugin.api ().removeChannel (name);
                                        response.write (ErrorCode.SUCCESS);
                                } catch (NoSuchElementException ex) {
                                        response.write (ErrorCode.NO_SUCH_CHANNEL);
                                } catch (IllegalArgumentException ex) {
                                        response.write (ErrorCode.NON_PERMANENT);
                                }
                        }
                        break;
                        case "Subscribe": {
                                String playerName = packet.readString ();
                                String name = packet.readString ();

                                // noinspection ConstantConditions
                                Optional<Channel> channel = this.plugin.api ().getChannel (name);
                                ProxiedPlayer player = this.plugin.getProxy ().getPlayer (playerName);

                                ErrorCode[] codes = new ErrorCode[2];

                                if (!channel.isPresent ()) { codes[0] = ErrorCode.NO_SUCH_CHANNEL; }
                                if (player == null) { codes[1] = ErrorCode.NO_SUCH_PLAYER; }

                                if (player != null && channel.isPresent ()) {
                                        if (channel.get ().isSubscribed (player)) {
                                                codes[1] = ErrorCode.SUBSCRIBED;
                                        } else { channel.get ().subscribe (player); }
                                }

                                response.write (codes);
                        }
                        break;
                        case "Unsubscribe": {
                                String playerName = packet.readString ();
                                String name = packet.readString ();

                                // noinspection ConstantConditions
                                Optional<Channel> channel = this.plugin.api ().getChannel (name);
                                ProxiedPlayer player = this.plugin.getProxy ().getPlayer (playerName);

                                ErrorCode[] codes = new ErrorCode[2];

                                if (!channel.isPresent ()) { codes[0] = ErrorCode.NO_SUCH_CHANNEL; }
                                if (player == null) { codes[1] = ErrorCode.NO_SUCH_PLAYER; }

                                if (player != null && channel.isPresent ()) {
                                        if (!channel.get ().isSubscribed (player)) {
                                                codes[1] = ErrorCode.NOT_SUBSCRIBED;
                                        } else { channel.get ().unsubscribe (player); }
                                }

                                response.write (codes);
                        }
                        break;
                        case "Message": {
                                String name = packet.readString ();
                                String message = packet.readString ();

                                // noinspection ConstantConditions
                                Optional<Channel> channel = this.plugin.api ().getChannel (name);

                                if (!channel.isPresent ()) { response.write (ErrorCode.NO_SUCH_CHANNEL); } else {
                                        channel.get ().dispatchMessage (null, message);
                                        response.write (ErrorCode.SUCCESS);
                                }
                        }
                }

                // The response generally consists of two fields:
                // requestID - The original identifier of the request
                // result - An integer value that notifies the other side about the call result
                // This is currently a very simplified solution to the communication issue and will be
                // replaced by a modern solution at some point
                sender.sendData (CHANNEL_NAME, response.array ());
        }

        /**
         * Provides an entry point for plugin messages.
         *
         * @param event The event.
         */
        @EventHandler
        public void onPluginMessage (@Nonnull PluginMessageEvent event) {
                if (!CHANNEL_NAME.equals (event.getTag ())) { return; }

                Packet packet = Packet.wrap (event.getData ());
                String subChannel = packet.readString ();

                this.callHandler (subChannel, event.getSender (), packet);
        }
}
