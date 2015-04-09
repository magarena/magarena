[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_CREATURE_CARD_FROM_GRAVEYARD),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ return target creature card from his or her graveyard\$ to the battlefield. "+
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
