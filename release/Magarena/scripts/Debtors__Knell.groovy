[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                    MagicGraveyardTargetPicker.PutOntoBattlefield,
                    this,
                    "Put target creature card\$ in a graveyard onto the battlefield under your control."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicReanimateAction(
                        card,
                        event.getPlayer()
                    ));
                }
            });
        }
    }
]
