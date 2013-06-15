[
    MagicAtDrawTrigger.EachPlayerDraw,
    new MagicWhenOtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            final MagicPlayer player = card.getOwner();
            return new MagicEvent(
                permanent,
                player,
                this,
                "SN deals 1 damage to PN.");
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),1);
            game.doAction(new MagicDealDamageAction(damage));
        }        
    }
]
