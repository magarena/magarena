[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_CONTROL,
                MagicPowerTargetPicker.create(),
                this,
                "Target creature you control\$ deals damage equal to " +
                "its power to each other creature and each opponent."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final Collection<MagicPermanent> creatures = CREATURE.except(it).filter(it);
                final int amount = it.getPower();
                final MagicPlayer player = event.getPlayer();
                game.logAppendValue(player, amount);
                for (final MagicPermanent creature : creatures) {
                    game.doAction(new DealDamageAction(it,creature,amount));
                }
                game.doAction(new DealDamageAction(it,player.getOpponent(),amount));
            });
        }
    }
]
