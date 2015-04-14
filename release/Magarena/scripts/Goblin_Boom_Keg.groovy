[
    new MagicWhenDiesTrigger() {     
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {    
            new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals 3 damage to target creature or player\$."
            );
    }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,3));
            });
        }
    }
]
