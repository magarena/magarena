[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ can't be the target of spells or abilities your opponent controls this turn. " +
                "If SN was kicked, that creature gets +4/+4 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainAbilityAction(
                        creature,
                        MagicAbility.CannotBeTheTarget(event.getPlayer().getOpponent())
                    ));
                    if (event.isKicked()) {
                        game.doAction(new MagicChangeTurnPTAction(creature,4,4));
                    }
                }
            });
        }
    }
]
