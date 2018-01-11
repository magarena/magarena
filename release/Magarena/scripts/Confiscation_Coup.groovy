def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        final MagicPermanent target = event.getRefPermanent();
        game.addEvent(new MagicPayEnergyEvent(event.getSource(), target.getConvertedCost()));
        game.doAction(new GainControlAction(event.getPlayer(), target));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ARTIFACT_OR_CREATURE,
                this,
                "Choose target artifact or creature\$. " +
                "PN gets {E}{E}{E}{E}."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPlayer(), MagicCounterType.Energy, 4));

            event.processTargetPermanent(game, {
                if (event.getPlayer().getEnergy() >= it.getConvertedCost()) {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        event.getPlayer(),
                        new MagicMayChoice("Pay {E} to gain control of ${it}?"),
                        it,
                        action,
                        "PN may\$ pay an amount of {E} equal to RN's converted mana cost. " +
                        "If PN does, PN gains control of RN."
                    ));
                }
            });
        }
    }
]
