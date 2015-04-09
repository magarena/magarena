[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_CONTROL,
                MagicPowerTargetPicker.create(),
                this,
                "Draw cards equal to the power of target creature\$ you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDrawAction(event.getPlayer(),it.getPower()));
            });
        }
    }
]
