[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent.getEnchantedPermanent() &&
                    damage.getTarget().hasType(MagicType.Creature)) ?
                new MagicEvent(
                    permanent,
                    damage.getTargetPermanent(),
                    this,
                    "PN gains control of RN for as long as SN is on the battlefield"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    MagicStatic.ControlAsLongAsSourceIsOnBattlefield(
                        event.getPlayer(),
                        event.getRefPermanent()
                    )
                ));
        }
    }
]
