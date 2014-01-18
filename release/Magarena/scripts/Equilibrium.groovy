[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (spell.isFriend(permanent) &&
                    spell.hasType(MagicType.Creature)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}")),
                        MagicTargetChoice.TARGET_CREATURE
                    ),
                    MagicBounceTargetPicker.create(),
                    this,
                    "PN may\$ pay {1}\$. If PN does, return target creature\$ to its owner's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent creature ->
                    game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
                });
            }
        }
    }
]
