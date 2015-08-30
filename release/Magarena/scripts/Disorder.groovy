[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 2 damage to each white creature and each player who controls a white creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer caster = event.getPlayer();
            for (final MagicPlayer player : game.getAPNAP()) {
                WHITE_CREATURE_YOU_CONTROL.filter(player) each {
                    game.doAction(new DealDamageAction(event.getSource(), it, 2));
                }
                if (player.controlsPermanent(WHITE_CREATURE_YOU_CONTROL)) {
                    game.doAction(new DealDamageAction(event.getSource(), player, 2));
                    game.logAppendMessage(caster, "${event.getSource().getName()} deals 2 damage to ${player.getName()}.")
                }
            }
        }
    }
]
