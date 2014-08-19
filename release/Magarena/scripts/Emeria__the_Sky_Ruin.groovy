[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getNrOfPermanents(MagicSubType.Plains) >= 7 ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD
                    ),
                    MagicGraveyardTargetPicker.PutOntoBattlefield,
                    this,
                    "If PN controls seven or more Plains, " +
                    "PN may\$ return target creature card from his or her graveyard\$ to the battlefield."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPlayer().getNrOfPermanents(MagicSubType.Plains) >= 7) {
                event.processTargetCard(game, {
                    game.doAction(new MagicReanimateAction(it,event.getPlayer()));
                });
            }
        }
    }
]
