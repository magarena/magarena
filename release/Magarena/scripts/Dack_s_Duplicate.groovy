[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(A_CREATURE),
                MagicCopyPermanentPicker.create(),
                this,
                "Put SN onto the battlefield. You may\$ have SN enter the battlefield as a copy of any creature\$ on the battlefield, except it gains haste and dethrone."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new EnterAsCopyAction(event.getCardOnStack(), it, {
                        final MagicPermanent perm ->
                        final MagicGame G = perm.getGame();
                        G.doAction(new MagicGainAbilityAction(perm,MagicAbility.Haste, MagicStatic.Forever));
                        G.doAction(new MagicGainAbilityAction(perm,MagicAbility.Dethrone, MagicStatic.Forever));
                    }));
                });
            } else {
                game.doAction(new MagicPlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
