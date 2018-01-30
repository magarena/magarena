[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return a Pirate card from your graveyard to your hand, " +
                "then do the same for Vampire, Dinosaur, and Merfolk."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            [
                MagicSubType.Pirate,
                MagicSubType.Vampire,
                MagicSubType.Dinosaur,
                MagicSubType.Merfolk
            ].each({
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicTargetChoice(
                        card(it).from(MagicTargetType.Graveyard),
                        "a ${it.name()} card from your graveyard"
                    ),
                    action,
                    "PN returns a ${it.name()} card from PN's graveyard\$ to PN's hand"
                ));
            });
        }
    }
]

