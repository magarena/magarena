[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Put SN onto the battlefield. You may\$ have SN enter the battlefield as a copy of any creature\$ card in a graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(new MagicEnterAsCopyAction(event.getCardOnStack(), it))
                });
            } else {
                game.doAction(new MagicPlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
