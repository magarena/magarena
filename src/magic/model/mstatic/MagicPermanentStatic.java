package magic.model.mstatic;

import magic.model.MagicCopyMap;
import magic.model.MagicCounterType;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicType;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.MagicSubType;
import magic.model.MagicAbility;
import magic.model.event.*;

import java.util.Set;

public class MagicPermanentStatic implements Comparable<MagicPermanentStatic> {
    public static final MagicPermanentStatic CountersEffect = new MagicPermanentStatic(
        0,
        MagicPermanent.NONE,
        new MagicStatic(MagicLayer.CountersPT, MagicTargetFilterFactory.CREATURE) {
            @Override
            public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
                final int amtP = permanent.getCounters(MagicCounterType.PlusOnePlusZero)
                                + permanent.getCounters(MagicCounterType.PlusOne)
                                + permanent.getCounters(MagicCounterType.PlusOnePlusTwo)
                                + (2 * permanent.getCounters(MagicCounterType.PlusTwoPlusZero))
                                + (2 * permanent.getCounters(MagicCounterType.PlusTwo))
                                - permanent.getCounters(MagicCounterType.MinusOneMinusZero)
                                - permanent.getCounters(MagicCounterType.MinusOne)
                                - (2 * permanent.getCounters(MagicCounterType.MinusTwoMinusOne))
                                - (2 * permanent.getCounters(MagicCounterType.MinusTwo));
                final int amtT = permanent.getCounters(MagicCounterType.PlusZeroPlusOne)
                                + (2 * permanent.getCounters(MagicCounterType.PlusZeroPlusTwo))
                                + permanent.getCounters(MagicCounterType.PlusOne)
                                + (2 * permanent.getCounters(MagicCounterType.PlusOnePlusTwo))
                                + (2 * permanent.getCounters(MagicCounterType.PlusTwo))
                                - permanent.getCounters(MagicCounterType.MinusZeroMinusOne)
                                - (2 * permanent.getCounters(MagicCounterType.MinusZeroMinusTwo))
                                - permanent.getCounters(MagicCounterType.MinusOne)
                                - permanent.getCounters(MagicCounterType.MinusTwoMinusOne)
                                - (2 * permanent.getCounters(MagicCounterType.MinusTwo));
                pt.add(amtP,amtT);
            }
        }
    );

    public static final MagicPermanentStatic ManaEffect = new MagicPermanentStatic(
        0,
        MagicPermanent.NONE,
        new MagicStatic(MagicLayer.Game, MagicTargetFilterFactory.PERMANENT) {
            @Override
            public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                if (permanent.hasSubType(MagicSubType.Plains)) {
                    permanent.addAbility(MagicTapManaActivation.White);
                }
                if (permanent.hasSubType(MagicSubType.Island)) {
                    permanent.addAbility(MagicTapManaActivation.Blue);
                }
                if (permanent.hasSubType(MagicSubType.Swamp)) {
                    permanent.addAbility(MagicTapManaActivation.Black);
                }
                if (permanent.hasSubType(MagicSubType.Mountain)) {
                    permanent.addAbility(MagicTapManaActivation.Red);
                }
                if (permanent.hasSubType(MagicSubType.Forest)) {
                    permanent.addAbility(MagicTapManaActivation.Green);
                }
                if (permanent.hasType(MagicType.Snow) && permanent.producesMana()) {
                    permanent.addAbility(MagicTapManaActivation.Snow);
                }
            }
        }
    );

    public static final MagicPermanentStatic SnowManaEffect = new MagicPermanentStatic(
        0,
        MagicPermanent.NONE,
        new MagicStatic(MagicLayer.Ability, MagicTargetFilterFactory.PERMANENT) {
            @Override
            public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                if (permanent.hasType(MagicType.Snow) && permanent.producesMana()) {
                    permanent.addAbility(MagicTapManaActivation.Snow);
                }
            }
        }
    );

    private final long id;
    private final MagicPermanent permanent;
    private final MagicStatic mstatic;

    public MagicPermanentStatic(final long id,final MagicPermanent permanent,final MagicStatic mstatic) {
        this.id=id;
        this.permanent=permanent;
        this.mstatic=mstatic;
    }

    public MagicPermanentStatic(final MagicCopyMap copyMap,final MagicPermanentStatic source) {
        this(source.id, copyMap.copy(source.permanent), source.mstatic);
    }

    public long getId() {
        return id;
    }

    public MagicPermanent getPermanent() {
        return permanent;
    }

    public MagicStatic getStatic() {
        return mstatic;
    }

    public MagicLayer getLayer() {
        return mstatic.getLayer();
    }

    @Override
    public int compareTo(final MagicPermanentStatic permanentStatic) {
        //sort by layer, break ties by id
        final int layerDiff = mstatic.getLayer().compareTo(permanentStatic.mstatic.getLayer());
        return (layerDiff != 0) ?
            layerDiff :
            Long.signum(id-permanentStatic.id);
    }
}
