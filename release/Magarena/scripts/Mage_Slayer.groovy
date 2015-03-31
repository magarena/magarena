[
    new MagicWhenAttacksTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent equippedCreature=permanent.getEquippedCreature();
            return (equippedCreature.isValid() && equippedCreature==creature) ?
                new MagicEvent(
                    equippedCreature,
                    game.getDefendingPlayer(),
                    this,
                    "SN deals damage equal to its power to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            game.doAction(new MagicDealDamageAction(permanent,event.getPlayer(),permanent.getPower()));
        }
    }
]
