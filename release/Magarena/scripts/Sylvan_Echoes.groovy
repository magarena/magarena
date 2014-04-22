[
    new MagicWhenClashTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Boolean winClash) {
            return (winClash)? new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ draw a card. "
            ) : MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicDrawAction(event.getPlayer()));
            }
        }
    }
]
