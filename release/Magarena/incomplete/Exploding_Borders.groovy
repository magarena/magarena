[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = cardOnStack.getController().getDomain();
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "PN searches his or her library for a basic land card, puts that card onto the battlefield tapped, then shuffles his or her library. "+
                "SN deals X damage to target player\$, where X is the number of basic land types among lands PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer targetPlayer ->
                final MagicPlayer castingPlayer = event.getPlayer()
                game.addEvent(new MagicSearchOntoBattlefieldEvent(event.getSource,castingPlayer,MagicTargetChoice.BASIC_LAND_FROM_LIBRARY);
                final int amount = castingPlayer.getDomain();
                game.logAppendMessage(castingPlayer," (X="+amount+")");
                final MagicDamage damage = new MagicDamage(event.getSource,targetPlayer,amount); 
                game.doAction(new MagicDealDamageAction(damage);
            });
        }
    }
]
//Searching onto battlefield should occur before domain calculated and damage dealt.
