[
    new MagicSpellCardEvent() {
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                new MagicTargetChoice(
                    new MagicCMCPermanentFilter(permanent(MagicType.Creature, Control.Any), Operator.EQUAL, x),
                    MagicTargetHint.Negative,
                    "target creature with converted mana cost ${x}"
                ),
                this,
                "PN gains control of target creature with converted mana cost ${x}\$."
            );
        }
    }
]

