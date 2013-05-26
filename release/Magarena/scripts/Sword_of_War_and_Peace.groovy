[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget targetPlayer=damage.getTarget();
            return (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    targetPlayer,
                    this,
                    "SN deals damage to RN equal to the number of cards in his or her hand and " +
                    "PN gains 1 life for each card in his or her hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer targetPlayer=event.getRefPlayer();
            final int amount1=targetPlayer.getHandSize();
            if (amount1>0) {
                final MagicDamage damage=new MagicDamage(event.getSource(),targetPlayer,amount1);
                game.doAction(new MagicDealDamageAction(damage));
            }
            final MagicPlayer player=event.getPlayer();
            final int amount2=player.getHandSize();
            if (amount2>0) {
                game.doAction(new MagicChangeLifeAction(player,amount2));
            }
        }
    }
]
