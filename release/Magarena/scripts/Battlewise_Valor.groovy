[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                this,
                "Target creature\$ gets +2/+2 until end of turn. " +
                "Scry 1."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent target ->
                game.doAction(new MagicChangeTurnPTAction(target,2,2));
                game.addEvent(new MagicScryEvent(event));
            });
        }
    }
]
