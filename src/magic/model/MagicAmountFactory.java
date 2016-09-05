package magic.model;

import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetHint;

public class MagicAmountFactory {

    public static MagicAmount FromFilter(final MagicTargetFilter<MagicTarget> filter) {
        return new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                return filter.filter(source, player, MagicTargetHint.None).size();
            }
        };
    }

    public static MagicAmount CounterOnSource(final MagicCounterType type) {
        return new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                final MagicPermanent perm = (MagicPermanent)source;
                return perm.getCounters(type);
            }
        };
    }

    public static MagicAmount Devotion(final MagicColor color) {
        return new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                return source.getController().getDevotion(color);
            }
        };
    }

    public static MagicAmount AllCountersOnSource =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                final MagicPermanent perm = (MagicPermanent)source;
                int amount = 0;
                for (final MagicCounterType counterType : MagicCounterType.values()) {
                    amount+=perm.getCounters(counterType);
                }
                return amount;
            }
        };

    public static MagicAmount One =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                return 1;
            }
            @Override
            public boolean isConstant() {
                return true;
            }
        };

    public static MagicAmount Constant(final int n) {
        return new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                return n;
            }
            @Override
            public boolean isConstant() {
                return true;
            }
        };
    }

    public static MagicAmount Equipment =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                final MagicPermanent perm = (MagicPermanent)source;
                return perm.getEquipmentPermanents().size();
            }
        };

    public static MagicAmount Aura =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                final MagicPermanent perm = (MagicPermanent)source;
                return perm.getAuraPermanents().size();
            }
        };

    public static MagicAmount Domain =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                return player.getDomain();
            }
        };

    public static MagicAmount SN_Power =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                final MagicPermanent perm = (MagicPermanent)source;
                return perm.getPower();
            }
        };

    public static MagicAmount LifeTotal =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                return player.getLife();
            }
        };

    public static MagicAmount ColorsOnPerms =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                int amount = 0;
                for (final MagicColor color : MagicColor.values()) {
                    if (player.controlsPermanent(color)) {
                        amount++;
                    }
                }
                return amount;
            }
        };

    public static MagicAmount XCost =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicEvent event) {
                return event.getX();
            }
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                throw new RuntimeException("getAmount(source, player) called on XCost");
            }
        };

    public static MagicAmount NegXCost =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicEvent event) {
                return -event.getX();
            }
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                throw new RuntimeException("getAmount(source, player) called on NegXCost");
            }
        };

    public static MagicAmount GreatestPower =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                return MagicTargetFilterFactory.CREATURE_YOU_CONTROL
                    .filter(source, player, MagicTargetHint.None)
                    .stream()
                    .mapToInt(it -> it.getPower())
                    .max()
                    .orElse(0);
            }
        };

    public static MagicAmount HighestCMCPermanentsYouControl =
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source, final MagicPlayer player) {
                return MagicTargetFilterFactory.PERMANENT_YOU_CONTROL
                    .filter(source, player, MagicTargetHint.None)
                    .stream()
                    .mapToInt(it -> it.getConvertedCost())
                    .max()
                    .orElse(0);
            }
        };
}
