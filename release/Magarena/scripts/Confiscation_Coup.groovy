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
                final int amount = it.getConvertedCost();
                if (event.getPlayer().getEnergy() >= amount) {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        event.getSource().getController(),
                        new MagicMayChoice("Pay {E}?"),
                        {
                            final MagicGame g, final MagicEvent e ->
                            if (e.isYes()) {
                                g.doAction(new ChangeCountersAction(e.getPlayer(), MagicCounterType.Energy, -amount));
                                g.doAction(new GainControlAction(e.getPlayer(), it));
                            }
                        },
                        "PN may\$ pay an amount of {E} equal to that permanent's converted mana cost. " +
                        "If PN do, gain control of it."
                    ));
                }
            });
        }
    }
]
