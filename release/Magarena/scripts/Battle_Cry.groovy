def DelayedTrigger = {
    final MagicSource staleSource, final MagicPlayer stalePlayer ->
    return new BlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent blocker) {
            return new MagicEvent(
                game.createDelayedSource(staleSource, stalePlayer),
                blocker,
                this,
                "RN gets +0/+1 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getRefPermanent(), 0, 1));
        }
    };
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN untaps all white creatures he or she controls. Whenever a creature blocks this turn, it gets +0/+1 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            WHITE_CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new UntapAction(it));
            }
            game.doAction(new AddTurnTriggerAction(DelayedTrigger(event.getSource(), event.getPlayer())));
        }
    }
]
