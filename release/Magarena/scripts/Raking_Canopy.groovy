[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature.getController()!=permanent.getController()&&creature.hasAbility(MagicAbility.Flying)) ?
                new MagicEvent(
                    permanent,
                    creature,
                    this,
                    "SN deals 4 damage to RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDealDamageAction(event.getSource(),event.getRefPermanent(),4));
        }
    }
]
