[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent creature) {
            if (permanent == creature &&
                permanent.getController().getNrOfPermanents(MagicType.Creature) > 1) {
                final MagicTargetChoice targetChoice = new MagicTargetChoice(
                    new MagicOtherPermanentTargetFilter(
                        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
                        permanent
                    ),
                    MagicTargetHint.None,"another creature to return"
                );
                return new MagicEvent(
                    permanent,
                    targetChoice,
                    MagicBounceTargetPicker.create(),
                    this,
                    "Return another creature PN controls\$ to its owner's hand."
                );
            }
            return MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRemoveFromPlayAction(
                        creature,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
        }
    }
]
