[
    new MagicWhenAttacksTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent equippedCreature=permanent.getEquippedCreature();
            final MagicPlayer player=permanent.getController();
            return (equippedCreature.isValid() && equippedCreature==creature) ?
                new MagicEvent(
                    equippedCreature,
                    player.getOpponent(),
                    this,
                    "SN deals damage equal to its power to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicDamage damage=new MagicDamage(permanent,event.getPlayer(),permanent.getPower());
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
