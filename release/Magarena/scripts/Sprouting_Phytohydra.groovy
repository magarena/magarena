[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getTarget() == permanent) ?
                new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "Put a token that's a copy of SN onto the battlefield?"
            ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCard card = MagicCard.createTokenCard(event.getPermanent(),player);        
            if (event.isYes()){
            game.doAction(new MagicPlayCardAction(card,player));
            }
        }
    }
]
