[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Return target creature card\$ from your graveyard to the battlefield. Put a +1/+1 counter on it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new RemoveCardAction(it,MagicLocationType.Graveyard));
                game.doAction(new PlayCardAction(it,event.getPlayer(),{
                    final MagicPermanent perm ->
                    final MagicGame G = perm.getGame();
                    G.doAction(new ChangeCountersAction(perm,MagicCounterType.PlusOne,1));
                }));
            });
        }
    }
]
