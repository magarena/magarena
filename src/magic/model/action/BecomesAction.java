package magic.model.action;

import java.util.Collections;
import java.util.Set;
import magic.model.MagicAbilityList;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class BecomesAction extends MagicAction {

    private final MagicPermanent permanent;
    private final int[] pt;
    private final Set<MagicColor> color;
    private final Set<MagicSubType> subType;
    private final Set<MagicType> type;
    private final MagicAbilityList ability;
    private final boolean duration;
    private final boolean additionTo;

    public BecomesAction(
        final MagicPermanent aPermanent,
        final int[] aPt,
        final Set<MagicColor> aColor,
        final Set<MagicSubType> aSubType,
        final Set<MagicType> aType,
        final MagicAbilityList aAbility,
        final boolean aDuration,
        final boolean aAdditionTo
    ) {
        permanent = aPermanent;
        pt=aPt;
        color=aColor;
        subType=aSubType;
        type=aType;
        ability=aAbility;
        duration=aDuration;
        additionTo=aAdditionTo;
    }

    public BecomesAction(final MagicPermanent aPermanent, final Set<MagicColor> aColor, final boolean aDuration, final boolean aAdditionTo) {
        this(aPermanent, null, aColor, Collections.<MagicSubType>emptySet(), Collections.<MagicType>emptySet(), null, aDuration, aAdditionTo);
    }

    public BecomesAction(final MagicPermanent aPermanent, final Set<MagicType> aType, final boolean aDuration) {
        this(aPermanent, null, Collections.<MagicColor>emptySet(), Collections.<MagicSubType>emptySet(), aType, null, aDuration, false);
    }

    public BecomesAction(final MagicPermanent aPermanent, final int[] aPt, final Set<MagicSubType> aSubType, final Set<MagicType> aType) {
        this(aPermanent, aPt, Collections.<MagicColor>emptySet() , aSubType, aType, null, MagicStatic.UntilEOT, false);
    }

    public BecomesAction(final MagicPermanent aPermanent, final int[] aPt, final Set<MagicSubType> aSubType, final Set<MagicType> aType, final boolean aDuration) {
        this(aPermanent, aPt, Collections.<MagicColor>emptySet(), aSubType, aType, null, aDuration, false);
    }

    @Override
    public void doAction(final MagicGame game) {
        if (pt != null) {
            final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, duration) {
                @Override
                public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness bPt) {
                    bPt.set(pt[0], pt[1]);
                }
            };
            game.doAction(new AddStaticAction(permanent, PT));
        }
        if (color.isEmpty() == false) {
            int _mask = 0;
            for (final MagicColor element : color) {
                _mask |= element.getMask();
            }
            final int mask = _mask;
            final MagicStatic C = new MagicStatic(MagicLayer.Color, duration) {
                @Override
                public int getColorFlags(final MagicPermanent permanent,final int flags) {
                    // if color change is in addition to original colors, return all
                    if (additionTo) {
                        return flags | mask;
                    // if color change replaces original color, return changes
                    } else {
                        return mask;
                    }
                }
            };
            game.doAction(new AddStaticAction(permanent, C));
        }
        if (type.isEmpty() == false) {
            int _mask = 0;
            for (final MagicType element : type) {
                _mask |= element.getMask();
            }
            final int mask = _mask;
            final MagicStatic T = new MagicStatic(MagicLayer.Type, duration) {
                @Override
                public int getTypeFlags(final MagicPermanent permanent,final int flags) {
                    // turning into an artifact creature retains previous types
                    if (additionTo || (type.contains(MagicType.Creature) && type.contains(MagicType.Artifact))) {
                        return flags | mask;
                    } else {
                        return mask;
                    }
                }
            };
            game.doAction(new AddStaticAction(permanent, T));
        }
        if (subType.isEmpty() == false) {
            final MagicStatic ST = new MagicStatic(MagicLayer.Type, duration) {
                @Override
                public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
                    // turning into an artifact creature retains previous subtypes
                    if (additionTo || (type.contains(MagicType.Creature) && type.contains(MagicType.Artifact))) {
                        flags.addAll(subType);
                    } else {
                        flags.clear();
                        flags.addAll(subType);
                    }
                }
            };
            game.doAction(new AddStaticAction(permanent, ST));
        }
        if (ability != null) {
            game.doAction(new GainAbilityAction(permanent, ability, duration));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {}
}
