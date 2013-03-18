[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            final MagicPlayer opponent = player.getOpponent();
            return new MagicEvent(
                    permanent,
                    opponent,
                    this,
                    opponent + " discards a card.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPlayer player=event.getPlayer();
            game.addEvent(new MagicDiscardEvent(permanent,player,1,false));
        }        
    }
]
