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
package rocks.spud.grid.bungee.implementation.configuration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.torchmind.candle.Candle;
import com.torchmind.candle.CandleWriter;
import com.torchmind.candle.api.ICommentNode;
import com.torchmind.candle.api.IObjectNode;
import com.torchmind.candle.api.error.CandleException;
import com.torchmind.candle.api.property.array.IStringArrayPropertyNode;
import com.torchmind.candle.node.CommentNode;
import com.torchmind.candle.node.ObjectNode;
import com.torchmind.candle.node.property.BooleanPropertyNode;
import com.torchmind.candle.node.property.StringPropertyNode;
import com.torchmind.candle.node.property.array.StringArrayPropertyNode;
import rocks.spud.grid.bungee.api.configuration.IGlobalGridConfiguration;
import rocks.spud.grid.bungee.api.configuration.IServerGridConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Provides a global (proxy-wide) configuration for {@link rocks.spud.grid.bungee.api.IGrid}.
 * @author Johannes Donath
 */
public class GlobalGridConfiguration implements IGlobalGridConfiguration {
        public static final String FILE_HEADER =
                "*\n" +
                " * GRID PLUGIN CONFIGURATION\n" +
                " *\n" +
                " * For more information on the options available to\n" +
                " * to you within this configuration file, please\n" +
                " * refer to https://github.com/PlaySpud/Grid/wiki/Configuration\n" +
                " *\n" +
                " * Note: Make sure this file is in the correct format before\n" +
                " * attempting to reload as your configuration file will be\n" +
                " * moved and thus reset if the format is invalid.\n ";

        public static final String FILE_FOOTER =
                "*\n" +
                " * To customize your user experience even further you\n" +
                " * may add per-server configurations here.\n" +
                " * Every server configuration block is named the same\n" +
                " * way it is within your config.yml.\n" +
                " *\n" +
                " * For more information on which options can be\n" +
                " * overridden on a per-server basis at the moment\n" +
                " * please refer to the plugin documentation which\n" +
                " * is available at\n" +
                " * https://github.com/PlaySpud/Grid/wiki/Configuration\n ";

        public static final String DEFAULT_FORMAT = "[%1$s] <%2$s> %3$s";
        public static final String DEFAULT_CHANNEL = "Global";
        public static final String[] DEFAULT_CHANNELS = new String[] { "Global", "Admins" };
        public static final boolean DEFAULT_REGISTER_SHORTHAND_COMMANDS = true;

        private final Map<String, ServerGridConfiguration> configurationMap;

        private final Map<String, Set<String>> autosubscribeGroups;
        private final String format;
        private final String defaultChannel;
        private final Set<String> channels;
        private final boolean registerShorthandCommands;

        public GlobalGridConfiguration (@Nonnull Candle document) {
                createDefaults (document);

                this.format = document.getString ("global.format", DEFAULT_FORMAT);
                this.defaultChannel = document.getString ("global.defaultChannel", DEFAULT_CHANNEL);
                // noinspection ConstantConditions
                this.channels = ImmutableSet.copyOf (document.getStringArray ("global.channels", DEFAULT_CHANNELS));
                this.registerShorthandCommands = document.getBoolean ("global.registerShorthandCommands", DEFAULT_REGISTER_SHORTHAND_COMMANDS);

                {
                        ImmutableMap.Builder<String, Set<String>> autosubscribeBuilder = ImmutableMap.builder ();

                        document.get ("autosubscribe", IObjectNode.class).forEach ((p) -> {
                                if (p instanceof ICommentNode) return;
                                if (!(p instanceof IStringArrayPropertyNode))
                                        throw new RuntimeException ("Auto-Subscribe groups may only be configured as string arrays");
                                IStringArrayPropertyNode node = ((IStringArrayPropertyNode) p);

                                autosubscribeBuilder.put (node.name (), ImmutableSet.copyOf (node.array ()));
                        });

                        this.autosubscribeGroups = autosubscribeBuilder.build ();
                }

                {
                        ImmutableMap.Builder<String, ServerGridConfiguration> configurationMapBuilder = ImmutableMap.builder ();

                        document.forEach (IObjectNode.class, (o) -> {
                                if (o.name ().equalsIgnoreCase ("global") || o.name ().equalsIgnoreCase ("autosubscribe"))
                                        return;

                                configurationMapBuilder.put (o.name (), new ServerGridConfiguration (o));
                        });

                        this.configurationMap = configurationMapBuilder.build ();
                }
        }

        public GlobalGridConfiguration (@Nonnull File configurationFile) throws CandleException, IOException {
                this (Candle.readFile (configurationFile));
        }

        /**
         * Creates a {@link rocks.spud.grid.bungee.implementation.configuration.GlobalGridConfiguration} instance based
         * on a {@link java.io.File} reference.
         * @param file The {@link java.io.File} reference.
         * @return The {@link rocks.spud.grid.bungee.implementation.configuration.GlobalGridConfiguration} instance.
         * @throws com.torchmind.candle.api.error.CandleException when saving the configuration is impossible.
         * @throws java.io.IOException when reading and/or writing the file fails.
         */
        public static GlobalGridConfiguration of (@Nonnull File file) throws CandleException, IOException {
                try {
                        return (new GlobalGridConfiguration (file));
                } catch (IOException ex) {
                        Candle document = new Candle ();

                        {
                                CommentNode node = new CommentNode (document, FILE_HEADER);
                                document.append (node);
                        }

                        GlobalGridConfiguration configuration = new GlobalGridConfiguration (document);

                        {
                                CommentNode node = new CommentNode (document, FILE_FOOTER);
                                document.append (node);
                        }

                        (new CandleWriter ()).write (file, document);
                        return configuration;
                }
        }

        /**
         * Creates all <strong>REQUIRED</strong> configuration nodes.
         * @param document The document.
         */
        private static void createDefaults (@Nonnull Candle document) {
                // global
                IObjectNode global;

                if (!document.isPresent ("global")) {
                        CommentNode comment = new CommentNode (document, " Defines a list of global (proxy-wide) settings that affect all servers within the network.");
                        document.append (comment);

                        global = new ObjectNode (document, "global");
                        document.append (global);
                } else
                        global = document.get ("global", IObjectNode.class);

                // global.format
                if (!global.isPresent ("format")) {
                        CommentNode comment0 = new CommentNode (document, " Defines the chat format used to display chat messages.");
                        CommentNode comment1 = new CommentNode (document, " The following variables may be used:");
                        CommentNode comment2 = new CommentNode (document, " * %1$s => Channel Name");
                        CommentNode comment3 = new CommentNode (document, " * %2$s => Player Name");
                        CommentNode comment4 = new CommentNode (document, " * %3$s => Message");

                        global.append (comment0);
                        global.append (comment1);
                        global.append (comment2);
                        global.append (comment3);
                        global.append (comment4);

                        StringPropertyNode node = new StringPropertyNode (document, "format", DEFAULT_FORMAT);
                        global.append (node);
                }

                // global.defaultChannel
                if (!global.isPresent ("defaultChannel")) {
                        CommentNode comment = new CommentNode (document, " Defines the standard channel to subscribe all players to.");
                        global.append (comment);

                        StringPropertyNode node = new StringPropertyNode (document, "defaultChannel", DEFAULT_CHANNEL);
                        global.append (node);
                }

                // global.registerShorthandCommands
                if (!global.isPresent ("registerShorthandCommands")) {
                        CommentNode comment = new CommentNode (document, " Defines whether shorthand channel commands like /g (global) or /s (server) will be registered.");
                        global.append (comment);

                        BooleanPropertyNode node = new BooleanPropertyNode (document, "registerShorthandCommands", DEFAULT_REGISTER_SHORTHAND_COMMANDS);
                        global.append (node);
                }

                // global.channels
                if (!global.isPresent ("channels")) {
                        CommentNode comment = new CommentNode (document, " Defines a list of existing channels throughout the network.");
                        global.append (comment);

                        StringArrayPropertyNode node = new StringArrayPropertyNode (document, "channels", DEFAULT_CHANNELS);
                        global.append (node);
                }

                // autosubscribe
                if (!document.isPresent ("autosubscribe")) {
                        CommentNode comment0 = new CommentNode (document, " Defines a set of auto-subscribe groups.");
                        CommentNode comment1 = new CommentNode (document, " Players with the right permission node (grid.autosubscribe.<groupName>) will be added to these groups automatically.");
                        document.append (comment0);
                        document.append (comment1);

                        ObjectNode autosubscribe = new ObjectNode (document, "autosubscribe");
                        document.append (autosubscribe);

                        StringArrayPropertyNode adminGroup = new StringArrayPropertyNode (document, "admin", new String[] { "Admins" });
                        autosubscribe.append (adminGroup);
                }
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Set<String> autosubscribeGroups () {
                return Collections.unmodifiableSet (this.autosubscribeGroups.keySet ());
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Optional<Set<String>> autosubscribeGroup (@Nonnull String name) {
                return Optional.ofNullable (this.autosubscribeGroups.get (name));
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Set<String> channels () {
                return this.channels;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Optional<IServerGridConfiguration> configuration (@Nonnull String name) {
                return Optional.ofNullable (this.configurationMap.get (name));
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public String defaultChannel () {
                // noinspection ConstantConditions
                return this.defaultChannel;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean registerShorthandCommands () {
                return this.registerShorthandCommands;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public String format () {
                // noinspection ConstantConditions
                return this.format;
        }
}
