[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final int amount = (damage.isCombat() && damage.getTarget() == permanent) ? damage.prevent() : 0;
            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN exiles RN cards from the top of his or her library."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            final MagicPlayer player = event.getPlayer();
            final MagicCardList topN = player.getLibrary().getCardsFromTop(amount);
            for (final MagicCard card : topN) {
                game.doAction(new ShiftCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.Exile
                ));
            }
        }
    }
]
