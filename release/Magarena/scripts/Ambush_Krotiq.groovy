[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                CREATURE_YOU_CONTROL.except(permanent),
                MagicTargetHint.None,
                "another creature to return"
            );
            return new MagicEvent(
                permanent,
                targetChoice,
                MagicBounceTargetPicker.create(),
                this,
                "PN returns another creature PN controls\$ to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(
                    it,
                    MagicLocationType.OwnersHand
                ));
            });
        }
    }
]
