[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Return target creature card from your graveyard\$ to the battlefield. "+
                "That creature gains haste. Exile it at the beginning of the next end step."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(new MagicReanimateAction(
                        it,
                        event.getPlayer(),
                        [MagicPlayMod.HASTE, MagicPlayMod.EXILE_AT_END_OF_TURN]
                    ));
                });
            }
        }
    }
]
