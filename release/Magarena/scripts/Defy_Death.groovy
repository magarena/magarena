[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Return target creature card\$ from your graveyard to the " +
                "battlefield. If it's an Angel, put two +1/+1 counters on it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                    final MagicPlayCardAction action = new MagicPlayCardAction(targetCard,event.getPlayer());
                    game.doAction(action);
                    final MagicPermanent permanent = action.getPermanent();
                    if (permanent.hasSubType(MagicSubType.Angel)) {
                        game.doAction(new MagicChangeCountersAction(
                            permanent,
                            MagicCounterType.PlusOne,
                            2,
                            true
                        ));
                    }
                }
            });
        }
    }
]
