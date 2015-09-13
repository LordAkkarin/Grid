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
package rocks.spud.grid.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import rocks.spud.grid.bungee.command.*;
import rocks.spud.grid.bungee.event.PlayerListener;
import rocks.spud.grid.bungee.event.PluginChannelListener;
import rocks.spud.grid.bungee.implementation.Grid;

import javax.annotation.Nullable;

/**
 * Provides an entry point for BungeeCord servers.
 *
 * @author Johannes Donath
 */
public class GridPlugin extends Plugin {
        private Grid grid;

        /**
         * Provides access to the {@link rocks.spud.grid.bungee.api.IGrid} API.
         *
         * @return The API.
         */
        @Nullable
        public Grid api () {
                return this.grid;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDisable () {
                super.onDisable ();

                this.getProxy ().unregisterChannel (PluginChannelListener.CHANNEL_NAME);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onEnable () {
                super.onEnable ();

                this.grid = new Grid (this);
                this.getProxy ().registerChannel (PluginChannelListener.CHANNEL_NAME);

                // Event Listeners
                {
                        this.getProxy ().getPluginManager ().registerListener (this, new PluginChannelListener (this));
                        this.getProxy ().getPluginManager ().registerListener (this, new PlayerListener (this));
                }

                // Commands
                {
                        this.getProxy ().getPluginManager ().registerCommand (this, new ChannelCommand (this));
                        this.getProxy ().getPluginManager ().registerCommand (this, new GlobalChannelCommand (this));
                        this.getProxy ().getPluginManager ().registerCommand (this, new ServerChannelCommand (this));

                        this.getProxy ().getPluginManager ().registerCommand (this, new SubscribeChannelCommand (this));
                        this.getProxy ().getPluginManager ().registerCommand (this, new UnsubscribeChannelCommand (this));
                }
        }
}
