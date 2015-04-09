[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "When SN enters the battlefield, it deals damage to target creature or player\$ "+
                "equal to the number of creatures you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new MagicDealDamageAction(
                    event.getSource(),
                    it,
                    event.getPlayer().getNrOfPermanents(MagicType.Creature)
                ));
            });
        }
    }
]
