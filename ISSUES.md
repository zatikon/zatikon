# Known issues

There might be cases where the contents of the window are too low/high, or the game window appears too big for the content.
In that case, you can move the things around a bit using these properties (they're Java properties, so in arguments
you'll need to prepend them with `-D`):

```
patch.insetLeft         # adds inset pixels to the left edge, moving window contents to the right
patch.insetTop          # adds inset pixels to the top edge, moving window contents down
patch.extraWidth        # adds extra pixels window width
patch.extraHeight       # adds extra pixels window height
```

You can add these as parameters to the run command - you don't have to provide them all. You can use negative values as well:

```
$ flatpak run com.chroniclogic.Zatikon -Dpatch.insetLeft=100 -Dpatch.extraHeight=10
```

In case you run from the .jar, these have to go before `-jar` like so:

```
$ java -Dpatch.insetLeft=100 -Dpatch.extraHeight=10 -jar zatikon.jar
```

In order to help resolving the issue for good (or at least providing workaround for common cases until proper fix),
please file an issue on GitHub with parameters that worked for you, your desktop environment name and version, and
information on custom theming if any.