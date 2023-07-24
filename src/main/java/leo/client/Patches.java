package leo.client;

import java.util.Optional;

public class Patches {
    public static final int INSET_LEFT = Optional.ofNullable(System.getProperty("patch.insetLeft")).map(Integer::parseInt).orElse(0);
    public static final int INSET_TOP = Optional.ofNullable(System.getProperty("patch.insetTop")).map(Integer::parseInt).orElse(0);
    public static final int EXTRA_WIDTH = Optional.ofNullable(System.getProperty("patch.extraWidth")).map(Integer::parseInt).orElse(0);
    public static final int EXTRA_HEIGHT = Optional.ofNullable(System.getProperty("patch.extraHeight")).map(Integer::parseInt).orElse(0);

}
