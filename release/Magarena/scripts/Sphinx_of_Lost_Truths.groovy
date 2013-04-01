[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                this,
                permanent.isKicked() ? 
                    "PN draws three cards." :
                    "PN draws three cards. Then discards three cards."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicDrawAction(player,3));
            if (!event.getPermanent().isKicked()) {
                game.addEvent(new MagicDiscardEvent(event.getPermanent(),player,3,false));
            }
        }        
    }
]
