def choice = new MagicTargetChoice("an enchantment card from your library");

[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ exile SN. If PN does, PN search his or her library for an enchantment card, "+
                "puts that card onto the battlefield, then shuffles his or her library."
            );
        }

       @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                final MagicCard card = event.getPermanent().getCard();
                if (card.isInGraveyard()) {
                    game.doAction(new RemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.Exile));
                    game.addEvent(new MagicSearchOntoBattlefieldEvent(
                        event.getSource(),
                        event.getPlayer(),
                        choice
                    ));
                }
            }
        }
    }
]
