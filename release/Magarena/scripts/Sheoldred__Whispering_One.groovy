[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    MagicGraveyardTargetPicker.PutOntoBattlefield,
                    this,
                    "Return target creature card\$ from your graveyard to the battlefield."
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
    },
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isOpponent(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    this,
                    "PN sacrifices a creature."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent=event.getPlayer();
            if (opponent.controlsPermanent(MagicType.Creature)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    opponent,
                    MagicTargetChoice.SACRIFICE_CREATURE
                ));
            }
        }
    }
]
