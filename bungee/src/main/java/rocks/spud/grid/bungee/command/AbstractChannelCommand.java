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

import com.google.common.base.Joiner;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import rocks.spud.grid.bungee.GridPlugin;
import rocks.spud.grid.bungee.implementation.channel.Channel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Johannes Donath
 */
public abstract class AbstractChannelCommand extends AbstractCommand {

        public AbstractChannelCommand (@Nonnull GridPlugin plugin, @Nonnull String name) {
                super (plugin, name);
        }

        public AbstractChannelCommand (@Nonnull GridPlugin plugin, @Nonnull String name, @Nullable String permission, @Nullable String... aliases) {
                super (plugin, name, permission, aliases);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void execute (@Nonnull CommandSender sender, @Nonnull String[] args) {
                Channel channel = this.getChannel (sender, args);

                if (!sender.hasPermission ("grid.command.message." + channel.name ())) {
                        // TODO: Localization
                        TextComponent component = new TextComponent ("You do not have permission to message this channel.");
                        component.setColor (ChatColor.DARK_RED);

                        sender.sendMessage (component);
                        return;
                }

                String message = Joiner.on (' ').join (args);
                channel.dispatchMessage ((sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender) : null), message);

                if (sender instanceof ProxiedPlayer && !channel.isSubscribed (((ProxiedPlayer) sender))) {
                        sender.sendMessage (TextComponent.fromLegacyText (String.format (this.grid ().configuration ().format (), channel.name (), ((ProxiedPlayer) sender).getDisplayName (), message)));
                }
        }

        /**
         * Retrieves the channel.
         *
         * @param sender The command sender.
         * @param args   The command arguments.
         * @return The channel.
         */
        @Nonnull
        public abstract Channel getChannel (@Nonnull CommandSender sender, @Nonnull String[] args);
}
