[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isTargetPlayer()) {
                final int amount = damage.replace();
                damage.getTargetPlayer().getLibrary().getCardsFromTop(amount) each {
                    game.doAction(new RemoveCardAction(it,MagicLocationType.OwnersLibrary));
                    game.doAction(new MoveCardAction(it,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
                }
            }
            return MagicEvent.NONE;
        }
    }
]
