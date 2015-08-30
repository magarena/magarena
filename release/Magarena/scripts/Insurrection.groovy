[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN untaps all creatures and gains control of them until end of turn. They gain haste until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPermanent creature : CREATURE.filter(event)) {
                game.doAction(new UntapAction(creature));
                game.doAction(new GainControlAction(event.getPlayer(), creature, MagicStatic.UntilEOT));
                game.doAction(new GainAbilityAction(creature, MagicAbility.Haste));
            }
        }
    }
]
