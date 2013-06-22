[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                new MagicNoCombatTargetPicker(true,true,false),
                this,
                "Detain target creature\$ an opponent controls. Draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDetainAction(event.getPlayer(), creature));
                    game.doAction(new MagicDrawAction(event.getPlayer()));
                }
            });
        }
    }
]
