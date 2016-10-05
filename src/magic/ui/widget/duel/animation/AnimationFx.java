package magic.ui.widget.duel.animation;

public class AnimationFx {

    private AnimationFx() {}

    public static final long DRAW_CARD         = 1;
    public static final long PLAY_CARD         = 1 << 1;
    public static final long CARD_SHADOW       = 1 << 2;
    public static final long STATIC_ARROW      = 1 << 3;
    public static final long FLIP_CARD         = 1 << 4;
    public static final long ZBUTTON_PULSE     = 1 << 5;
    public static final long AVATAR_PULSE      = 1 << 6;
    public static final long INFLATE_ZONE      = 1 << 7;
    public static final long NEW_TURN_MSG      = 1 << 8;
    public static final long CARD_FADEIN       = 1 << 9;
    public static final long ELASTIC_ARROW     = 1 << 10;

    // all these effects and animations are on by default.
    private static long flags =
        DRAW_CARD | PLAY_CARD | STATIC_ARROW | CARD_FADEIN |
        FLIP_CARD | ZBUTTON_PULSE | AVATAR_PULSE | INFLATE_ZONE |
        NEW_TURN_MSG;

    public static boolean isOn(long flag) {
        return (flags & flag) == flag;
    }

    private static void setOn(long flag) {
        flags |= flag;
    }

    private static void setOff(long flag) {
        flags &= ~flag;
    }

    public static long getFlags() {
        return flags;
    }

    public static void setFlags(long flags) {
        AnimationFx.flags = flags;
    }

    public static void setFlag(long flag, boolean b) {
        if (b) {
            setOn(flag);
        } else {
            setOff(flag);
        }
    }

}
