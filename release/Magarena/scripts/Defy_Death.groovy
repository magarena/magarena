[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Return target creature card\$ from your graveyard to the " +
                "battlefield. If it's an Angel, put two +1/+1 counters on it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new MagicRemoveCardAction(it,MagicLocationType.Graveyard));
                game.doAction(new MagicPlayCardAction(it,event.getPlayer(), {
                    final MagicPermanent perm ->
                    final MagicGame G = perm.getGame();
                    if (perm.hasSubType(MagicSubType.Angel)) {
                        G.doAction(new MagicChangeCountersAction(perm,MagicCounterType.PlusOne,2));
                    }
                }));
            });
        }
    }
]
