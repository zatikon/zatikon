# Zatikon

### Free and open source strategy game of sword and sorcery!

![Zatikon Screenshot 1](https://raw.githubusercontent.com/zatikon/zatikon-static/b5bd4abeeec4394592d61ae276f606cd000521a9/screenshot1.png)

Zatikon is a strategy game, originally developed by Gabe Jones and Chronic Logic, aiming to connect the timeless idea of chess
with draft-based army approach, sprinkled with fantastic medieval and magical setting. 

If you want to find out a bit more how it all came to be, visit [HISTORY](HISTORY.md) in this repository!

# Quick download

- Linux - from Flathub: https://flathub.org/apps/com.chroniclogic.Zatikon
- Windows/macOS - latest JAR: https://github.com/zatikon/zatikon/releases

# Community

- IRC: [#zatikon @ libera.chat](irc://irc.libera.chat/zatikon)
- Matrix: [#zatikon:matrix.org  ![Chat on Matrix](https://matrix.to/img/matrix-badge.svg)](https://matrix.to/#/#zatikon:matrix.org)
- Discord: https://discord.gg/6w8PfkzjV9

# Installation

## Linux

On Linux, it's recommended to use Flatpak to install the game from Flathub along with Java.
You can use your graphical Flathub manager for that by searching for Zatikon and installing it, or you can use
the following command:

```shell
$ flatpak install com.chroniclogic.Zatikon
```

Alternatively, if you already have Java and no need for automatic updates - you can simply download the latest .jar
file from the Releases page, and execute it:

```shell
$ java -jar zatikon.jar
```

### Steam Deck

Until the game lands on Steam, the easiest path to obtain it is using Flatpak through KDE's Discover app.

To do so:
1. Run the desktop mode on your Steam Deck (STEAM button -> Power -> Switch to Desktop)
2. Once ready, start Discover (shopping bag icon on bottom bar - or click the Steam Deck logo and type "discover").
3. In Discover app, if it's not yet there, add Flathub to your software repositories (Settings -> click the "Add Flathub" button in the top right corner - if it's not there, check if you have `Flathub - dl.flathub.org` on the list of sources to download apps from, marked with a tick in checkbox). Wait for any updates to finish.
4. Search for Zatikon and install it.
5. You're ready to go!
6. (Optional) Later on, you can right click on the Zatikon icon in the main menu of the desktop environment (Steam Deck logo in bottom left) and choose "Add to Steam" option.
This should allow you to run the game through "Non-Steam" section in the game mode. The game is intended
to be able to run there without having to use the desktop mode; if it doesn't, please file an issue.

## Windows/macOS

There's currently no special build or installer for Windows and macOS (although it's planned).

To run the game, install Java 17 JRE/JDK, and simply run the latest .jar file downloaded from the releases page.

# Running

You can run the game in several modes. If you are just starting out, you'll be interested in the standalone mode.

The basic way to run the game is through the shortcut found in your desktop environment, but you can also run it with
flatpak:

```shell
$ flatpak install com.chroniclogic.Zatikon
```

This way you can also add arguments, if necessary.

## Standalone/local mode

If you want to just play the game in single player mode, saving your progress data locally, you can use standalone mode.
This mode runs the server alongside the game.

To use it, in the login window tick the "Standalone mode?" checkbox, and then click Login.

## Client mode

If you want to play online with other players, you need to connect to a server. There is an official server at
zatikon.chroniclogic.com - the hostname is already prefilled in the login dialog by default.

The first time you connect to the server, you need to register your account. That's done through "Register" button, where
you'll be asked for your preferred username, credentials and email address. **Make sure you remember your password, as
recovery is not yet supported!**

After you register your account, you can log in with the account you just created.

## Cheats!

When running the game locally, you can use the cheat utility. At the moment it only increases the amount of your gold
significantly. You can run it with:

```shell
$ java -jar zatikon.jar leo.util.Cheat
```

## Running the server

Running the server is fairly straightforward - with one exception. Even though there are no particular configuration
files, it's necessary to set up TLS correctly, which might involve distributing the TLS certificate along with
the client, or having it signed by trusted authority.

In order to generate the self-signed key and certificate pair locally:

```shell
$ scripts/genkeys.sh
```

In order to repackage existing key and certificate to fit into PKCS12 format:

```shell
$ openssl pkcs12 -export -in fullchain.cer -inkey keyfile.key -out keystore.p12
```

Finally, to run the server using the generated keystore, using the password from the previous step (assuming 123456):

```shell
$ java -cp zatikon.jar -Dkeystore.password=123456 -Dkeystore.path=keystore.p12 leo.server.Server
```

If it's necessary for the client to trust the self-signed certificate, it can be done using client parameter:

```shell
$ java -cp zatikon.jar -Dtruststore.path=truststore.p12 leo.client.Client
```


# How to play?

The game leads you through a tutorial game the first time you play on a particular server instance. Tutorial covers the
basics of the controls - it's necessary for you to figure out the strategy!

Currently, you have to win the tutorial in order not to have to play it again. If you get stuck, check any of our channels for help!

# Building the game

In order to build the game, go to the main project directory and run:

```shell
$ mvn clean package
```

Resulting binaries can be found in the `target` directory.

# Contributing

Pull requests are welcome! Check out [PLANS](PLANS.md) and the issue tracker to find out more about the things that we could use some help with.

Don't hesitate to inform us about any bugs you find as well - the codebase is fairly large, so there might be some issues here and there.

# Support us!

The main way for you to support the project, other than contributing to its code, is spreading the word about Zatikon!

You are very welcome to do so! The venues that we know about and we would like to share with you are:

- Community wishlist item on GOG that needs your votes, if you'd like to see Zatikon there: https://www.gog.com/wishlist/games/zatikon

Thank you for everything that shows that this project is worth spending time on!
