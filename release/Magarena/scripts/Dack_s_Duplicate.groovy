[
    new MagicETBEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(A_CREATURE),
                MagicCopyPermanentPicker.create(),
                this,
                "PN may\$ have SN enter the battlefield as a copy of any creature on the battlefield\$, except it gains haste and dethrone."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new EnterAsCopyAction(event.getCardOnStack(), event.getTarget(), {
                    final MagicPermanent perm ->
                    final MagicGame G = perm.getGame();
                    G.doAction(new GainAbilityAction(perm,MagicAbility.Haste, MagicStatic.Forever));
                    G.doAction(new GainAbilityAction(perm,MagicAbility.Dethrone, MagicStatic.Forever));
                }));
            } else {
                game.doAction(new PlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
