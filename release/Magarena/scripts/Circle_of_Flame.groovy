[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent.isEnemy(creature) &&
                    !creature.hasAbility(MagicAbility.Flying)) ?
                new MagicEvent(
                    permanent,
                    creature,
                    this,
                    "SN deals 1 damage to attacking creature without flying."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(event.getSource(),event.getRefPermanent(),1);
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
