[
    new MagicWhenBecomesMonstrousTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicChangeStateAction action) {
            return action.permanent == permanent ? 
                new MagicEvent(
                    permanent,
                    this,
                    "SN deals damage to each opponent equal to the number of cards in that players hand."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent=event.getPermanent().getOpponent();
            final MagicDamage damage=new MagicDamage(event.getPermanent(),opponent,opponent.getHandSize());
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
