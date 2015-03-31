[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.isSource(permanent.getEquippedCreature()) && damage.isTargetPlayer() && damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "SN deals damage to RN equal to the number of cards in his or her hand and " +
                    "PN gains 1 life for each card in his or her hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer targetPlayer=event.getRefPlayer();
            game.doAction(new MagicDealDamageAction(event.getSource(),targetPlayer,targetPlayer.getHandSize()));
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicChangeLifeAction(player,player.getHandSize()));
        }
    }
]
