def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer player = event.getPlayer();
    final MagicPermanent target = event.getRefPermanent();
    if (event.isYes()) {
        game.doAction(new ChangeCountersAction(player, MagicCounterType.Energy, -target.getConvertedCost()));
        game.doAction(new GainControlAction(player, target);
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
