def CREATURE_BLOCKING_BLOCKED_BY_SN = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.getBlockedCreature() == source || target.getBlockingCreatures().contains(source);
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Pump"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{1}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicTargetChoice(
                    CREATURE_BLOCKING_BLOCKED_BY_SN,
                    MagicTargetHint.Negative,
                    "target creature blocking or blocked by ${source}"
                ),
                this,
                "SN's power becomes the toughness of target creature blocking or being blocked by SN\$ "+
                "minus 1 until end of turn, and SN's toughness becomes 1 plus the power of that creature "+
                "until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int power = it.getPower();
                final int toughness = it.getToughness();
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
                        @Override
                        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                            pt.set(toughness-1, power+1);
                        }
                    }
                ));
            });
        }
    }
]
