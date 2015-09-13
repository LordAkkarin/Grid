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
import java.util.Optional;

/**
 * @author Johannes Donath
 */
public class ChannelCommand extends AbstractCommand {

        public ChannelCommand (@Nonnull GridPlugin plugin) {
                // noinspection ConstantConditions
                super (plugin, "channel", null, (plugin.api ().configuration ().registerShorthandCommands () ? new String[] { "c" } : new String[0]));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void execute (@Nonnull CommandSender sender, @Nonnull String[] args) {
                Optional<Channel> channel = this.grid ().getChannel (args[0]);

                if (!sender.hasPermission ("grid.command.message." + args[0])) {
                        // TODO: Localization
                        TextComponent component = new TextComponent ("You do not have permission to message this channel.");
                        component.setColor (ChatColor.DARK_RED);

                        sender.sendMessage (component);
                        return;
                }

                if (!channel.isPresent ()) {
                        // TODO: Localization
                        TextComponent component = new TextComponent ("No such channel.");
                        component.setColor (ChatColor.DARK_RED);

                        sender.sendMessage (component);
                        return;
                }

                args[0] = null;
                String message = Joiner.on (' ').skipNulls ().join (args);
                channel.get ().dispatchMessage (((ProxiedPlayer) sender), message);

                if (!channel.get ().isSubscribed (((ProxiedPlayer) sender))) {
                        sender.sendMessage (TextComponent.fromLegacyText (String.format (this.grid ().configuration ().format (), channel.get ().name (), ((ProxiedPlayer) sender).getDisplayName (), message)));
                }
        }
}
