[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new MagicMayChoice(),
                    damage.getTarget(),
                    this,
                    "PN may\$ have RN discard a card. If PN chooses not to, RN returns a creature he or she controls to owner's hand."
                ) :
                MagicEvent.NONE;
        }
        public MagicEvent bounceChoice(MagicPermanent permanent, MagicPlayer player){
            return new MagicEvent(
                permanent,
                player,
                MagicTargetChoice.CREATURE_YOU_CONTROL,
                MagicBounceTargetPicker.create(),
                MagicPlayer.NONE,
                this,
                "PN has to return a creature\$ he or she controls to its owner's hand."
            );
        }                  
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if(event.getRefPlayer().isValid() && event.isYes()) {
                game.addEvent(new MagicDiscardEvent(event.getSource(),event.getRefPlayer()));
            }
            if(event.getRefPlayer().isValid() && event.isNo()) {
                game.addEvent(bounceChoice(event.getPermanent(),event.getRefPlayer()));
            }
            if(!event.getRefPlayer().isValid()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent creature ->
                        game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
                });
            }
        }
    }
]
