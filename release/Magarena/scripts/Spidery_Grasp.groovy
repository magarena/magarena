[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Untap target creature\$. It gets +2/+4 " +
                "and gains reach until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicUntapAction(it));
                game.doAction(new MagicChangeTurnPTAction(it,2,4));
                game.doAction(new MagicGainAbilityAction(it,MagicAbility.Reach));
            });
        }
    }
]
