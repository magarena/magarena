[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ATTACKING_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target attacking creature\$ if its power is less than or equal to the number of Soldiers on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer()
                final int amount = game.getNrOfPermanents(MagicSubType.Soldier)
                game.logAppendValue(player,amount);
                if (it.getPower() <= amount) {
                    game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
                }
            });
        }
    }
]
