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

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import rocks.spud.grid.bungee.GridPlugin;
import rocks.spud.grid.bungee.implementation.channel.Channel;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Handles chat related event handling.
 *
 * @author Johannes Donath
 */
public class PlayerListener implements Listener {
        private final GridPlugin plugin;

        public PlayerListener (@Nonnull GridPlugin plugin) {
                this.plugin = plugin;
        }

        /**
         * Handles chat messages passing through the server.
         *
         * @param event The event.
         */
        @EventHandler
        public void onChat (@Nonnull ChatEvent event) {
                if (event.isCommand ()) { return; }
                if (!(event.getSender () instanceof ProxiedPlayer)) { return; }

                ProxiedPlayer player = ((ProxiedPlayer) event.getSender ());

                // noinspection ConstantConditions
                this.plugin.api ().handleMessage (player.getServer (), player, event.getMessage ());
                event.setCancelled (true);
        }

        /**
         * Handles disconnecting players.
         *
         * @param event The event.
         */
        @EventHandler
        public void onPlayerDisconnect (@Nonnull PlayerDisconnectEvent event) {
                // noinspection ConstantConditions
                this.plugin.api ().unsubscribePlayer (event.getPlayer ());
                this.plugin.getLogger ().info ("Un-Subscribed " + event.getPlayer ().getDisplayName () + " from all channels.");
        }

        /**
         * Handles player logins.
         *
         * @param event The event.
         */
        @EventHandler
        public void onPostLogin (@Nonnull PostLoginEvent event) {
                // noinspection ConstantConditions
                this.plugin.api ().getDefaultChannel ().subscribe (event.getPlayer ());
                this.plugin.getLogger ().info ("Subscribed " + event.getPlayer ().getDisplayName () + " to global channel.");

                // noinspection ConstantConditions
                for (String group : this.plugin.api ().configuration ().autosubscribeGroups ()) {
                        if (!event.getPlayer ().hasPermission ("grid.autosubscribe." + group)) continue;
                        // noinspection ConstantConditions
                        this.plugin.api ().configuration ().autosubscribeGroup (group)
                                .ifPresent ((g) -> g.forEach ((c) -> {
                                        // noinspection ConstantConditions
                                        Optional<Channel> channel = this.plugin.api ().getChannel (c);

                                        if (!channel.isPresent ())
                                                this.plugin.getLogger ().warning ("Channel \"" + c + "\" in autosubscribe group \"" + group + "\" does not exist.");
                                        else {
                                                channel.get ().subscribe (event.getPlayer ());
                                                this.plugin.getLogger ().info ("Subscribed " + event.getPlayer ().getDisplayName () + " to channel \"" + c + "\" via autosubscribe group \"" + group + "\"");
                                        }
                                }));
                }
        }

        /**
         * Handles server connections.
         *
         * @param event The event.
         */
        @EventHandler
        public void onServerConnected (@Nonnull ServerConnectedEvent event) {
                // noinspection ConstantConditions
                this.plugin.api ().getChannel (event.getServer ())
                           .ifPresent ((s) -> {
                                   s.subscribe (event.getPlayer ());
                                   this.plugin.getLogger ().info ("Subscribed " + event.getPlayer ().getDisplayName () + " to server channel \"" + s.name () + "\".");
                           });
        }

        /**
         * Handles disconnecting players.
         *
         * @param event The event.
         */
        @EventHandler
        public void onServerDisconnect (@Nonnull ServerDisconnectEvent event) {
                // noinspection ConstantConditions
                this.plugin.api ().getChannel (event.getTarget ())
                           .ifPresent ((s) -> {
                                   s.unsubscribe (event.getPlayer ());
                                   this.plugin.getLogger ().info ("Un-Subscribed " + event.getPlayer ().getDisplayName () + " from server channel \"" + s.name () + "\".");
                           });
        }
}
