def CREATURE_BLOCKING_BLOCKED_BY_SN = {
    final MagicPermanent permanent ->
    return new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.getBlockedCreature() == permanent || permanent.getBlockedCreature() == target;
        }
    }
}

def TARGET_CREATURE_BLOCKING_BLOCKED_BY_SN = {
    final MagicPermanent permanent ->
    return new MagicTargetChoice(
        CREATURE_BLOCKING_BLOCKED_BY_SN(permanent),
        MagicTargetHint.Negative,
        "target creature blocking or blocked by ${permanent.getName()}"
    );
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Destroy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{W}{B}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_BLOCKING_BLOCKED_BY_SN(source),
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
