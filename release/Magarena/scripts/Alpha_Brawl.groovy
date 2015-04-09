[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                MagicPowerTargetPicker.create(),
                this,
                "Target creature\$ an opponent controls deals damage equal to " +
                "its power to each other creature that player controls, then " +
                "each of those creatures deals damage equal to its power to that creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicTargetFilter<MagicPermanent> filter = CREATURE_YOU_CONTROL.except(it);
                final Collection<MagicPermanent> creatures = game.filterPermanents(it.getController(),filter);
                for (final MagicPermanent creature : creatures) {
                    game.doAction(new MagicDealDamageAction(it,creature,it.getPower()));
                }
                for (final MagicPermanent creature : creatures) {
                    game.doAction(new MagicDealDamageAction(creature,it,creature.getPower()));
                }
            });
        }
    }
]
