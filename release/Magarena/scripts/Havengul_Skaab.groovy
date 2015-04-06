[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent creature) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL.except(permanent),
                MagicTargetHint.None,
                "another creature to return"
            );
            return new MagicEvent(
                permanent,
                targetChoice,
                MagicBounceTargetPicker.create(),
                this,
                "Return another creature PN controls\$ to its owner's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(
                    it,
                    MagicLocationType.OwnersHand
                ));
            });
        }
    }
]
