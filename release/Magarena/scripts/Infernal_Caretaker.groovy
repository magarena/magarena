[
    new ThisTurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent faceUp) {
            return new MagicEvent(
                permanent,
                this,
                "Return all Zombie cards from all graveyards to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            ZOMBIE_CARD_FROM_ALL_GRAVEYARDS.filter(event) each {
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            }
        }
    }
]
