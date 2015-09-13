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
package rocks.spud.grid.bungee.implementation;

import com.torchmind.candle.api.error.CandleException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import rocks.spud.grid.bungee.GridPlugin;
import rocks.spud.grid.bungee.api.IChannel;
import rocks.spud.grid.bungee.api.IGrid;
import rocks.spud.grid.bungee.api.configuration.IGridConfiguration;
import rocks.spud.grid.bungee.implementation.channel.Channel;
import rocks.spud.grid.bungee.implementation.channel.PermanentChannel;
import rocks.spud.grid.bungee.implementation.channel.TemporaryChannel;
import rocks.spud.grid.bungee.implementation.configuration.GlobalGridConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the plugin.
 *
 * @author Johannes Donath
 */
public class Grid implements IGrid {
        public static final String CONFIGURATION_FILE_BACKUP_NAME = "config.cndl.malformed";
        public static final String CONFIGURATION_FILE_NAME = "config.cndl";
        private final GridPlugin plugin;
        private final Map<String, Channel> channelMap = new ConcurrentHashMap<> ();
        private final GlobalGridConfiguration configuration;

        public Grid (@Nonnull GridPlugin plugin) {
                this.plugin = plugin;
                this.plugin.getDataFolder ().mkdirs ();

                try {
                        File configurationFile = new File (plugin.getDataFolder (), CONFIGURATION_FILE_NAME);
                        GlobalGridConfiguration configuration;

                        try {
                                configuration = GlobalGridConfiguration.of (configurationFile);
                        } catch (CandleException ex) {
                                if (!configurationFile.renameTo (new File (plugin.getDataFolder (), CONFIGURATION_FILE_BACKUP_NAME))) {
                                        throw new RuntimeException ("Could not recover from malformed configuration file: " + ex.getMessage (), ex);
                                }

                                configuration = GlobalGridConfiguration.of (configurationFile);
                        }

                        this.configuration = configuration;
                } catch (CandleException | IOException ex) {
                        throw new RuntimeException ("Could not read/write configuration file: " + ex.getMessage (), ex);
                }

                this.configuration.channels ().forEach ((c) -> this.channelMap.put (c, new PermanentChannel (this.configuration, c)));
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public GlobalGridConfiguration configuration () {
                return this.configuration;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public TemporaryChannel createChannel (@Nonnull String name) throws IllegalArgumentException {
                TemporaryChannel channel = new TemporaryChannel (this.configuration (), name);
                this.channelMap.put (name, channel);
                return channel;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Optional<Channel> getChannel (@Nonnull String name) {
                return Optional.ofNullable (this.channelMap.get (name));
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Optional<Channel> getChannel (@Nonnull Server server) {
                return this.getChannel (server.getInfo ());
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Optional<Channel> getChannel (@Nonnull ServerInfo serverInfo) {
                // @formatter:off
                return this.configuration ().configuration (serverInfo.getName ())
                           .map (IGridConfiguration::defaultChannel)
                                .map ((n) -> this.getChannel (n).get ());
                // @formatter:on
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Channel getDefaultChannel () {
                // @formatter:off
                return this.getChannel (this.configuration ().defaultChannel ())
                           .orElseThrow (() -> new RuntimeException ("Undefined default channel: " + this.configuration ().defaultChannel ()));
                // @formatter:on
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Grid handleMessage (@Nonnull Server server, @Nullable ProxiedPlayer player, @Nonnull String message) {
                // @formatter:off
                Channel channel = this.getChannel (server)
                                      .orElse (this.getDefaultChannel ());
                // @formatter:on

                if (player != null && !player.hasPermission ("grid.command.message." + channel.name ())) {
                        // TODO: Localization
                        TextComponent component = new TextComponent ("You do not have permission to message this channel.");
                        component.setColor (ChatColor.DARK_RED);

                        player.sendMessage (component);
                        return this;
                }

                channel.dispatchMessage (player, message);
                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Grid removeChannel (@Nonnull String name) throws IllegalArgumentException, NoSuchElementException {
                // @formatter:off
                this.removeChannel (
                        this.getChannel (name)
                            .orElseThrow (() -> new NoSuchElementException ("No channel with name \"" + name + "\" present"))
                );
                // @formatter:on

                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Grid removeChannel (@Nonnull IChannel channel) throws IllegalArgumentException {
                channel.unsubscribeAll ();
                this.channelMap.remove (channel.name ());

                return this;
        }

        /**
         * Un-Subscribes a player from all channels.
         *
         * @param player The player.
         * @return The {@link rocks.spud.grid.bungee.implementation.Grid} instance.
         */
        @Nonnull
        public Grid unsubscribePlayer (@Nonnull ProxiedPlayer player) {
                this.channelMap.forEach ((k, v) -> v.unsubscribe (player));
                return this;
        }
}
