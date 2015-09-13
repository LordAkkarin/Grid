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
public class SubscribeChannelCommand extends AbstractCommand {

        public SubscribeChannelCommand (@Nonnull GridPlugin plugin) {
                super (plugin, "subscribe");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void execute (@Nonnull CommandSender sender, @Nonnull String[] args) {
                if (!(sender instanceof ProxiedPlayer)) {
                        // TODO: Localization
                        TextComponent component = new TextComponent ("Cannot subscribe to channels from console.");
                        component.setColor (ChatColor.DARK_RED);

                        sender.sendMessage (component);
                        return;
                }

                Optional<Channel> channel = this.grid ().getChannel (args[0]);

                if (!sender.hasPermission ("grid.command.subscribe." + args[0])) {
                        // TODO: Localization
                        TextComponent component = new TextComponent ("You do not have permission to subscribe to this channel.");
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

                ProxiedPlayer player = ((ProxiedPlayer) sender);

                if (channel.get ().isSubscribed (player)) {
                        // TODO: Localization
                        TextComponent component = new TextComponent ("You are already subscribed to this channel.");
                        component.setColor (ChatColor.GOLD);

                        sender.sendMessage (component);
                        return;
                }

                // TODO: Localization
                TextComponent component = new TextComponent ("You subscribed to channel " + channel.get ().name ());
                component.setColor (ChatColor.DARK_GREEN);

                channel.get ().subscribe (player);
                player.sendMessage (component);
        }
}
