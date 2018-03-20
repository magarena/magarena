[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return damage.getSource() == permanent.getEquippedCreature() &&
                    damage.getTarget().isCreaturePermanent() ?
                new MagicEvent(
                    permanent,
                    damage.getTargetPermanent(),
                    this,
                    "Tap RN. "+
                    "That creature doesn't untap during its controller's untap step for as long as SN remains on the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new TapAction(event.getRefPermanent()));
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    MagicStatic.AsLongAsCond(
                        event.getRefPermanent(),
                        MagicAbility.DoesNotUntap,
                        MagicConditionFactory.PlayerControlsSource(event.getPlayer()))));
        }
    }
]
