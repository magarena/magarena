[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Each player discards a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPlayer player=permanent.getController();
            final MagicPlayer opponent=player.getOpponent();
            game.addEvent(new MagicDiscardEvent(permanent,player));
            game.addEvent(new MagicDiscardEvent(permanent,opponent));
        }
    }
]
