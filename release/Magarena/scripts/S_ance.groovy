def Spirit = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Spirit);
    }
};
[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    TARGET_CREATURE_CARD_FROM_GRAVEYARD
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ exile target creature card\$ from his or her graveyard. " +
                "If he or she does, put a token onto the battlefield that's a copy " +
                "of that card except it's a Spirit in addition to its other types. " +
                "Exile it at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,{
                    final MagicPlayer player=event.getPlayer();
                    game.doAction(new MagicRemoveCardAction(
                        it,
                        MagicLocationType.Graveyard
                    ));
                    game.doAction(new MoveCardAction(
                        it,
                        MagicLocationType.Graveyard,
                        MagicLocationType.Exile
                    ));
                    game.doAction(new PlayTokenAction(
                        player,
                        it,
                        {
                            final MagicPermanent perm ->
                            game.doAction(new AddStaticAction(perm, Spirit));
                            game.doAction(new AddTriggerAction(perm, MagicAtEndOfTurnTrigger.ExileAtEnd));
                        }
                    ));
                });
            }
        }
    }
]
