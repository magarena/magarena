[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Return target creature card\$ from your graveyard to the battlefield. Put a +1/+1 counter on it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new MagicRemoveCardAction(it,MagicLocationType.Graveyard));
                final MagicPlayCardAction action = new MagicPlayCardAction(it,event.getPlayer());
                game.doAction(action);
                final MagicPermanent permanent = action.getPermanent();
                game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,1));
            });
        }
    }
]
