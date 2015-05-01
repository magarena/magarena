[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget().isPlayer()){
                // Replacement effect. Generates no event or action.
                final int amount = damage.getAmount();
                damage.prevent();
                final MagicCardList exile = new MagicCardList(damage.getTargetPlayer().getLibrary().getCardsFromTop(amount));
                for (final MagicCard card : exile) {
                    game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersLibrary));
                    game.doAction(new MoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
                }
            }
            return MagicEvent.NONE;
        }
    }
]
