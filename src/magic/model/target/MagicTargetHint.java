package magic.model.target;

import magic.model.MagicPlayer;

public enum MagicTargetHint {
    Positive,
    Negative,
    None;

    public boolean accept(final MagicPlayer player,final MagicTarget target) {
        switch (this) {
            case Positive:
                return target.getController()==player;
            case Negative:
                return target.getController()!=player;
            default:
                return true;
        }
    }

    public static MagicTargetHint getHint(final String target) {
        if (target.startsWith("pos")) {
            return Positive;
        } else if (target.startsWith("neg")) {
            return Negative;
        } else {
            return None;
        }
    }

    public static String removeHint(final String target) {
        return target.replaceAll("^pos |^neg ", "");
    }
}
