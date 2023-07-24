# History of the game

If you were wondering where the project structure is coming from, and why `leo`; or how did we get here at all -
you are due a trip down memory lane!

## Leopold

Zatikon started out as Leopold - a very similar game made by Gabe Jones, available to the public in early 2000s.
It was at its core already a lot of the same game that it is now - check for yourself:

![Leopold Screenshot](https://raw.githubusercontent.com/zatikon/zatikon-static/b5bd4abeeec4394592d61ae276f606cd000521a9/leo1.gif)

You can actually check out the old webpage through archive.org to see how the things were going back in the day: https://web.archive.org/web/20040212223620/http://catluck.com/leopold/

## Leopold 2, a.k.a. Zatikon

Later on, Leopold's main page was showing that a new version, Leopold 2, is in the works.

This is what became known as Zatikon - a new version of the game made in cooperation with Chronic Logic. In this version,
a fair number of features has been added, including new modes, and quite some extra units, some available as an
expansion of the base game.

This is when Zatikon also got its current look:

![Zatikon Screenshot](https://raw.githubusercontent.com/zatikon/zatikon-static/b5bd4abeeec4394592d61ae276f606cd000521a9/screenshot1.png)

## Time flies

Early 2010s is when I joined the party for the first time; I spent quite some time playing the game and gaining new ranks... in
games against AI, which as I learned later were not even a primary feature of the game :)

At the time, Zatikon saw its player peak, and was also available as a Java applet, Android app, and even through
Facebook. The game shared the infrastructure, so you could play in any of these, and be part of the game leaderboard.

Somewhere during that period, I also published an install script for Zatikon on Arch User Repository. While doing so,
I found out that the game doesn't work well with OpenJDK, or with Java >= 8. I dug around without anything conclusive,
and left the JRE dependency at Java 7 - but the findings of that time turned out important a few years later.

## Back in style - going open source!

A while later, I checked how Zatikon was doing on a random whim. The server didn't seem to be online; I decided
that it would be a shame not to try to do something, and wrote an message through Chronic Logic contact form, asking about
a possibility to open source the game.

I didn't expect it to be so easy! I received a reply from Josiah, who was very enthusiastic about the prospect
of getting Zatikon back on track and running. As I found out later, it was in particular because he really likes
playing the game himself :)

After receiving the source code, the first priority was to make sure the game runs correctly in newer Java. This,
thankfully, proved easier than expected - a small change regarding optimization of drawing the elements in Java was
all that was necessary; a change that I saw elsewhere in the code later on, possibly related to applet functionality.

Given this, I've gotten new energy to make things fully right and working, so that we could release the game.
In no particular order: I added Maven's pom.xml, moved Java all the way to 17, to have new features while running
on an LTS version; replaced some libraries, moved things around, replaced MySQL with SQLite, added standalone mode
and TLS support. There was probably more, but these seemed like the things that would be most useful to have ready
before the game code goes public. [CHANGELOG](CHANGELOG.md) for version 1.1.0 can tell you more!

Once things started getting into shape, I asked Josiah about the license, proposing AGPL 3.0 - to which he agree also
got a green light from Gabe.

The version number that was in the project code was at 1.003. I decided that then the next version number fitting
a FOSS release - with quite some work done but not a total makeover - would be a 1.1.0.

Even later on, I explored the possibility of publishing the game on Flathub, so it's easily accessible and portable
between various Linux distributions. And here we are!

## Future?

A new, big chapter begins just now; and to make it the best possible one - I invite you to join the journey and work on that future together!
