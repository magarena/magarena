[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(MagicTargetChoice.CREATURE),
                MagicCopyPermanentPicker.create(),
                this,
                "Put SN onto the battlefield. You may\$ have SN enter the battlefield as a copy of any creature\$ on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent chosen ->
                    game.doAction(MagicPlayCardFromStackAction.EnterAsCopy(
                        event.getCardOnStack(),
                        chosen
                    ));
                });
            } else {
                game.doAction(new MagicPlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
