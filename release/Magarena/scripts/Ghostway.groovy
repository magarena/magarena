[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Exile each creature you control. " +
                "Return those cards to the battlefield under their owner's control at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicExileUntilEndOfTurnAction(target));
            }
        }
    }
]
