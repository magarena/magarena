[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(MagicTargetChoice.CREATURE),
                MagicCopyPermanentPicker.create(),
                this,
                "Put SN onto the battlefield. You may\$ have SN enter the battlefield as a copy of any creature\$ on the battlefield, except it gains haste and dethrone."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent chosen ->
                    game.doAction(MagicPlayCardFromStackAction.EnterAsCopy(
                        event.getCardOnStack(),
                        chosen,
                        {
                            final MagicPermanent permanent ->
                            game.doAction(new MagicGainAbilityAction(permanent,MagicAbility.Haste, MagicStatic.Forever));
                            game.doAction(new MagicGainAbilityAction(permanent,MagicAbility.Dethrone, MagicStatic.Forever));
                        }
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
