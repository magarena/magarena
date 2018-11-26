[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Control"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{X}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                payedCost.getX(),
                this,
                "Target opponent\$ puts cards from the top of his or her library into his or her graveyard "+
                "until a creature card or X cards are put into that graveyard this way, whichever comes first. "+
                "If a creature card is put into that graveyard this way, PN sacrifices SN and puts that card onto "+
                "the battlefield under his or her control. X can't be 0."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCardList library = it.getLibrary();
                int amount = event.getRefInt();
                int creatureCards = 0;
                while (creatureCards < 1 && amount > 0 && library.size() > 0) {
                    final MagicCard card = library.getCardAtTop();
                    if (!card.hasType(MagicType.Creature)) {
                        game.doAction(new MillLibraryAction(it,1));
                        if (it.getGraveyard().contains(card)) {
                            amount--
                        }
                    } else {
                        game.doAction(new MillLibraryAction(it,1));
                        if (it.getGraveyard().contains(card)) {
                            creatureCards++;
                            game.doAction(new SacrificeAction(event.getPermanent()));
                            game.doAction(new PutOntoBattlefieldAction(
                                MagicLocationType.Graveyard,
                                card,
                                event.getPlayer()
                            ));
                        }
                    }
                }
            });
        }
    }
]
