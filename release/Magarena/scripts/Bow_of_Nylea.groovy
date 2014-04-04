[
    new MagicStatic(MagicLayer.Ability,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if(permanent.isAttacking()) {
               permanent.addAbility(MagicAbility.Deathtouch, flags);
            }
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Card"
    ){
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), 
                new MagicPayManaCostEvent(source,"{1}{G}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.ExileOpp,
                this,
                "Put target card\$ from your graveyard on the bottom of your library"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard targetCard ->
                game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(
                    targetCard,
                    MagicLocationType.Graveyard,
                    MagicLocationType.BottomOfOwnersLibrary
                ));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
