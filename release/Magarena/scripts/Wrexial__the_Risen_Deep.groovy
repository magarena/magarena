[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD
                ),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ cast target instant or sorcery card\$ from your opponent's graveyard without paying its mana cost. "+
                "If that card would be put into a graveyard this turn, exile it instead."
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
                    for (final MagicEvent cevent : it.getAdditionalCostEvent()) {
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
