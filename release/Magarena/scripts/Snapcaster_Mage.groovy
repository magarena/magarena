def A_PAYABLE_INSTANT_OR_SORCERY_CARD_FROM_YOUR_GRAVEYARD = new MagicTargetChoice(
    PAYABLE_INSTANT_OR_SORCERY_FROM_GRAVEYARD,
    "a instant or sorcery card from your graveyard"
);

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    A_PAYABLE_INSTANT_OR_SORCERY_CARD_FROM_YOUR_GRAVEYARD
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ flashback target instant or sorcery card\$ from his or her graveyard. " + 
                "The flashback cost is equal to its mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    final MagicPlayer player = event.getPlayer();
                    for (final MagicEvent cevent : it.getAdditionalCostEvent()) {
                        if (cevent.isSatisfied() == false) {
                            game.logAppendMessage(player, "Casting failed as " + player + " is unable to pay additional casting costs.");
                            return;
                        }
                    }
                    for (final MagicEvent cevent : it.getCostEvent()) {
                        game.addEvent(cevent);
                    }
                    game.doAction(new RemoveCardAction(it, MagicLocationType.Graveyard));
                    game.addEvent(new MagicPutCardOnStackEvent(
                        it, 
                        player, 
                        MagicLocationType.Graveyard, 
                        MagicLocationType.Exile
                    ));
                });
            }
        }
    }
]
