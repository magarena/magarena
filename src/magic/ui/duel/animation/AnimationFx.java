package magic.ui.duel.animation;

import magic.data.GeneralConfig;

public class AnimationFx {

    private AnimationFx() {}

    public static final long DRAW_CARD         = 1;
    public static final long PLAY_CARD         = 1 << 1;
    public static final long CARD_SHADOW       = 1 << 2;
    public static final long FROM_ARROW        = 1 << 3;
    public static final long FLIP_CARD         = 1 << 4;
    public static final long FLASH_ZONE        = 1 << 5;
    public static final long AVATAR_STROBE     = 1 << 6;
    public static final long INFLATE_ZONE      = 1 << 7;
    public static final long NEW_TURN_MSG      = 1 << 8;
    public static final long CARD_FADEIN       = 1 << 9;

    private static long flags =
        DRAW_CARD | PLAY_CARD | FROM_ARROW | CARD_FADEIN |
        FLIP_CARD | FLASH_ZONE | AVATAR_STROBE | INFLATE_ZONE |
        NEW_TURN_MSG;

    public static boolean isOn(long flag) {
        return (flags & flag) == flag && GeneralConfig.getInstance().showGameplayAnimations();
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
