[
    new MagicWhenBecomesMonstrousTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final ChangeStateAction action) {
            return action.permanent == permanent ? 
                new MagicEvent(
                    permanent,
                    this,
                    "SN deals damage to each opponent equal to the number of cards in that player's hand."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent = event.getPlayer().getOpponent();
            game.doAction(new DealDamageAction(
                event.getSource(),
                opponent,
                opponent.getHandSize()
            ));
        }
    }
]
