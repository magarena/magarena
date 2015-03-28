package magic.model.action;

import java.util.Set;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class MagicBecomesAction extends MagicAction {

    private final Set<MagicStatic> staticCollect = null;
    private final MagicPermanent permanent;
    private final String[] pt;
    private final MagicColor[] color;
    private final MagicSubType[] subType;
    private final MagicType[] type;
    private final Boolean duration;
    private final Boolean additionTo;

    public MagicBecomesAction(final MagicPermanent aPermanent, final MagicColor[] aColor, final Boolean aDuration, final Boolean aAdditionTo) {
        this(aPermanent, null, aColor, null, null, aDuration, aAdditionTo);
    }

    public MagicBecomesAction(final MagicPermanent aPermanent, final MagicType[] aType, final Boolean aDuration) {
        this(aPermanent, null, null, null, aType, aDuration, false);
    }

    public MagicBecomesAction(final MagicPermanent aPermanent, final String[] aPt, final MagicColor[] aColor, final MagicSubType[] aSubType, final MagicType[] aType, final Boolean aDuration, final Boolean aAdditionTo) {
        permanent = aPermanent;
        pt=aPt;
        color=aColor;
        subType=aSubType;
        type=aType;
        duration=aDuration;
        additionTo=aAdditionTo;
    }

    public MagicBecomesAction(final MagicPermanent aPermanent, final String[] aPt, final MagicSubType[] aSubType, final MagicType[] aType) {
        this(aPermanent, aPt, null, aSubType, aType, false, false);
    }

    public MagicBecomesAction(final MagicPermanent aPermanent, final String[] aPt, final MagicSubType[] aSubType, final MagicType[] aType, final Boolean aDuration) {
        this(aPermanent,aPt,null,aSubType,aType,aDuration,false);
    }

    @Override
    public void doAction(final MagicGame game) {
        if (pt != null) {
            final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, duration) {
                @Override
                public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness bPt) {
                    bPt.set(Integer.parseInt(pt[0]), Integer.parseInt(pt[1]));
                }
            };
            staticCollect.add(PT);
        }
        if (color !=null) {
            final MagicStatic C = new MagicStatic(MagicLayer.Color, duration) {
                @Override
                public int getColorFlags(final MagicPermanent permanent,final int flags) {
                    int mask = 0;
                    for (final MagicColor element : color) {
                        mask += element.getMask();
                    }
                    if (additionTo) {
                        return flags|mask;
                    } else {
                        return mask;
                    }
                }
            };
            staticCollect.add(C);
        }
        if (type!=null|subType!=null) {
            final MagicStatic ST = new MagicStatic(MagicLayer.Type, duration) {
                @Override
                public int getTypeFlags(final MagicPermanent permanent,final int flags) {
                    boolean creature = false;
                    boolean artifact = false;
                    if (type !=null) {
                        int mask = 0;
                        for (final MagicType element : type) {
                            mask += element.getMask();
                            if (element==MagicType.Creature) {
                                creature=true;
                            }
                            if (element==MagicType.Artifact) {
                                artifact=true;
                            }
                        }
                        if (additionTo|(creature && artifact)) { // Turning into an artifact creature retains previous types
                            return flags|mask;
                        } else {
                            return mask;
                        }
                    } else {
                        return flags; // Return original types if type not changed
                    }
                }
                @Override
                public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
                    if (subType !=null) {
                        if (!additionTo) {
                            flags.clear();
                        }
                        for (final MagicSubType element : subType) {
                            flags.add(element);
                        }
                    }
                }
            };
            staticCollect.add(ST);
        }

        // final collected statics returned
        for (final MagicStatic mstatic : staticCollect) {
            game.doAction(new MagicAddStaticAction(permanent, mstatic));
        }
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {}
}
