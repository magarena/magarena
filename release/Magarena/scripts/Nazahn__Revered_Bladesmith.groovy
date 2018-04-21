def choice = new MagicTargetChoice("an Equipment card from your library");

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                choice,
                this,
                "PN searches his or her library for an Equipment card and reveal it. "+
                "If PN reveal a card named Hammer of Nazahn this way, put it onto the battlefield. "+
                "Otherwise, put that card into PN's hand. Then shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                    game.doAction(new RevealAction(it));
                if (it.getName().equals("Hammer of Nazahn")) {
                    game.doAction(new ReturnCardAction(MagicLocationType.OwnersLibrary,it,event.getPlayer()));
                    } else {
                    game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
