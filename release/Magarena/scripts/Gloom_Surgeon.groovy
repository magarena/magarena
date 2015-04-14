[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            if (damage.isCombat() && damage.getTarget() == permanent) {
                final int amt = damage.getAmount();
                // Prevention effect
                damage.prevent();
                return new MagicEvent(
                    permanent,
                    amt,
                    this,
                    "Exile RN cards from the top of your library."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            final MagicPlayer player = event.getPlayer();
            final MagicCardList topN = player.getLibrary().getCardsFromTop(amount);
            for (final MagicCard card : topN) {
                game.doAction(new MagicRemoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new MoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.Exile
                ));
            }
        }
    }
]
