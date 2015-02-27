[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "When SN enters the battlefield, it deals damage to target creature or player\$ "+
                "equal to the number of creatures you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicDamage damage = new MagicDamage(
                event.getSource(),
                it,
                event.getPermanent().getController().getNrOfPermanents(MagicType.Creature)
                );
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
