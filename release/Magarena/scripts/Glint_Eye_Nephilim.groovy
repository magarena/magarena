[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.isCombat() && damage.isTargetPlayer()) ?
                new MagicEvent(
                    permanent,
                    damage.getAmount(),
                    this,                            
                    "PN draws "+damage.getAmount()+" cards."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(),event.getRefInt()));      
        }
    }
]
