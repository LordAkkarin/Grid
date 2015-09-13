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
package rocks.spud.grid.bungee.implementation.channel;

import com.google.common.collect.ImmutableSet;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import rocks.spud.grid.bungee.api.IChannel;
import rocks.spud.grid.bungee.implementation.configuration.GlobalGridConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a channel.
 * @author Johannes Donath
 */
public abstract class Channel implements IChannel {
        private final GlobalGridConfiguration configuration;
        private final String name;
        private final Set<ProxiedPlayer> subscribers = new HashSet<> ();

        public Channel (@Nonnull GlobalGridConfiguration configuration, @Nonnull String name) {
                this.configuration = configuration;
                this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IChannel dispatchMessage (@Nullable ProxiedPlayer player, @Nonnull String message) {
                BaseComponent[] convertedMessage;

                if (player != null)
                        convertedMessage = TextComponent.fromLegacyText (String.format (this.configuration.format (), this.name (), player.getDisplayName (), message));
                else
                        convertedMessage = TextComponent.fromLegacyText (message);

                this.subscribers.forEach ((s) -> s.sendMessage (convertedMessage));

is.subscribers);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isSubscribed (@Nonnull ProxiedPlayer player) {
                return this.subs         r
    return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public String name () {
       bleSet (th
eturn this.name;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Set<ProxiedPlayer> players () {
                return Collections.unmodifiacribers.co
ntains (player);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IChannel subscribe (@Nonnull ProxiedPlayer player) {
                this.subscribers.add (player);
                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public IChannel unsubscribe (@Nonnull ProxiedPlayer player) {
                this.subscribers.remove (player);
                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IChannel unsubscribeAll () {
                this.subscribers.clear ();
                return this;
        }
}
