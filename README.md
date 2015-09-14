[![License](https://img.shields.io/github/license/PlaySpud/Grid.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![Latest Tag](https://img.shields.io/github/tag/PlaySpud/Grid.svg?style=flat-square&label=Latest Tag)](https://github.com/PlaySpud/Grid/tags)
[![Latest Release](https://img.shields.io/github/release/PlaySpud/Grid.svg?style=flat-square&label=Latest Release)](https://github.com/PlaySpud/Grid/releases)

Grid
====
Table of Contents
-----------------
* [About](#about)
* [Contacts](#contacts)
* [Installation](INSTALLING)
* [API](#api)
* [Issues](#issues)
* [Building](#building)
* [Contributing](#contributing)

About
-----

A simple channel based chat system for BungeeCord

Contacts
--------

* [IRC #Akkarin on irc.spi.gt](http://irc.spi.gt/iris/?nick=Guest....&channels=Akkarin&prompt=1) (alternatively #Akkarin on esper.net)
* [GitHub](https://github.com/PlaySpud/Grid)

API
-----

Grid provides an API for accessing certain features. There are two APIs available at the moment which are both available
via the Spud maven repository:
* BungeeCord API
* Bukkit API

```xml
<repository>
        <id>spud</id>
        <url>https://maven.spud.rocks/snapshot/</url>
</repository>

<dependencies>
        <dependency>
                <groupId>rocks.spud.grid.bukkit</groupId>
                <artifactId>api</artifactId>
                <version>1.0-SNAPSHOT</version>
                <scope>provided</scope>
        </dependency>
        // OR
        <dependency>
                <groupId>rocks.spud.grid.bungee</groupId>
                <artifactId>plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <scope>provided</scope>
        </dependency>
</dependencies>
```

You may retrieve instances of ```rocks.spud.grid.bungee.api.IGrid``` (BungeeCord) or ```rocks.spud.grid.bukkit.api.IGrid```
(Bukkit) either via the plugin manager (BungeeCord) or via Bukkit's service manager.

**Note:** BungeeCord plugins will need to depend on the actual plugin as BungeeCord does not offer a service API.

Issues
------

You encountered problems with the mod or have a suggestion? Create an issue!

1. Make sure your issue has not been fixed in a newer version (check the list of [closed issues](https://github.com/PlaySpud/Grid/issues?q=is%3Aissue+is%3Aclosed)
1. Create [a new issue](https://github.com/LordAkkarin/bukkit-plugin-annotations/issues/new) from the [issues page](https://github.com/PlaySpud/Grid/issues)
1. Enter your issue's title (something that summarizes your issue) and create a detailed description containing:
   - What is the expected result?
   - What problem occurs?
   - How to reproduce the problem?
   - Crash Log (Please use a [Pastebin](http://www.pastebin.com) service)
1. Click "Submit" and wait for further instructions

Building
--------

1. Clone this repository via ```git clone https://github.com/PlaySpud/Grid.git``` or download a [zip](https://github.com/PlaySpud/Grid/archive/master.zip)
1. Build the modification by running ```mvn clean install``
1. The resulting jars can be found in ```universal/target``` (or their respective module target directories)

Contributing
------------

Before you add any major changes to the library you may want to discuss them with us (see [Contact](#contact)) as
we may choose to reject your changes for various reasons. All contributions are applied via [Pull-Requests](https://help.github.com/articles/creating-a-pull-request).
Patches will not be accepted. Also be aware that all of your contributions are made available under the terms of the
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt). Please read the [Contribution Guidelines](CONTRIBUTING.md)
for more information.
