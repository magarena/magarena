def choice = new MagicTargetChoice("target noncreature permanent an opponent controls");


def effect = MagicRuleEventAction.create("Search your library for a Forest card and put that card onto the battlefield tapped. Then shuffle your library.")

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                choice
                 ),
                MagicDestroyTargetPicker.Destroy,
                this,
                "PN may\$ destroy target noncreature permanent an opponent controls\$."
                );
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new DestroyAction(it));
                    final MagicCard card = it.getCard();
                    if (card.isInGraveyard()
                        ||
                        (card.isToken() && !card.getOwner().getPermanents().contains(it))) {
                game.addEvent(effect.getEvent(event));
                    }
                });
            }
        }
    }
]
