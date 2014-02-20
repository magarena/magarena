[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_ATTACKING_CREATURE,
                    new MagicDamageTargetPicker(1),
                    this,
                    "SN deals 1 damage to target attacking creature\$."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicTarget target ->
                final MagicDamage damage=new MagicDamage(event.getSource(),target,1);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
