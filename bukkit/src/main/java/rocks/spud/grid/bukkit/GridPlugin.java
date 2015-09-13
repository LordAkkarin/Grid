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
package rocks.spud.grid.bukkit;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import rocks.spud.grid.bukkit.api.IGrid;
import rocks.spud.grid.bukkit.implementation.Grid;

/**
 * Provides an entry point for Bukkit based servers.
 *
 * @author Johannes Donath
 */
public class GridPlugin extends JavaPlugin {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDisable () {
                super.onDisable ();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onEnable () {
                super.onEnable ();

                Grid grid = new Grid (this);

                this.getServer ().getMessenger ().registerOutgoingPluginChannel (this, Grid.CHANNEL_NAME);
                this.getServer ().getMessenger ().registerIncomingPluginChannel (this, Grid.CHANNEL_NAME, grid);
                this.getServer ().getServicesManager ().register (IGrid.class, grid, this, ServicePriority.Normal);
        }
}
