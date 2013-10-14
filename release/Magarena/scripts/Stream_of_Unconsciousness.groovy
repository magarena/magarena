[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE
                new MagicWeakenTargetPicker(4, 0),
                this,
                "Target creature\$ gets -4/-0 until end of turn. " +
                "If you control a Wizard, draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature, -4, 0));
                    final MagicPlayer you = event.getPlayer();
                    if (you.controlsPermanent(MagicSubType.Wizard)){
                        game.doAction(new MagicDrawAction(you));
                    }
                }
            });
        }
    }
]
