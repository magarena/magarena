[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (spell.hasColor(MagicColor.Black)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{3}")),
                        MagicTargetChoice.POS_TARGET_PERMANENT
                    ),
                    MagicTapTargetPicker.Untap,
                    this,
                    "PN may\$ pay {3}\$. If PN does, untap target permanent\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent target ->
                    game.doAction(new MagicUntapAction(target));
                });
            }
        }
    }
]
