def CREATURE_BLOCKED_BY_SN = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isCreature() && target.getBlockingCreatures().contains(source);
    }
}

def TARGET_CREATURE_BLOCKED_BY_SOURCE = {
    final MagicSource source ->
    return new MagicTargetChoice(
        CREATURE_BLOCKED_BY_SN,
        MagicTargetHint.Negative,
        "a creature blocked by ${source.getName()}"
    )
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Damage"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_BLOCKED_BY_SOURCE(source),
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature it's blocking.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getPermanent(), it, 1));
            });
        }
    }
]
