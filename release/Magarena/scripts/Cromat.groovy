def CREATURE_BLOCKING_BLOCKED_BY_SN = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.getBlockedCreature() == source || target.getBlockingCreatures().contains(source);
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Destroy"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{W}{B}")];
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
                "Destroy target creature blocking or blocked by SN.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
            });
        }
    }
]
