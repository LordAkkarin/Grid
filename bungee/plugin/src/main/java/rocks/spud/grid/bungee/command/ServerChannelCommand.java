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

/**
 * @author Johannes Donath
 */
public class ServerChannelCommand extends AbstractChannelCommand {

        public ServerChannelCommand (@Nonnull GridPlugin plugin) {
                //noinspection ConstantConditions
                super (plugin, "local", null, (plugin.api ().configuration ().registerShorthandCommands () ? new String[] { "l" } : new String[0]));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void execute (@Nonnull CommandSender sender, @Nonnull String[] args) {
                if (!(sender instanceof ProxiedPlayer)) {
                        // TODO: Localization
                        TextComponent component = new TextComponent ("Cannot send to local server from console.");
                        component.setColor (ChatColor.DARK_RED);

                        sender.sendMessage (component);
                        return;
                }

                super.execute (sender, args);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Channel getChannel (@Nonnull CommandSender sender, @Nonnull String[] args) {
                return this.grid ().getChannel (((ProxiedPlayer) sender).getServer ()).get ();
        }
}
