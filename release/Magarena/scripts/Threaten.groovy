[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Untap target creature\$ and gain control of it until end of turn." +
                "That creature gains haste until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicUntapAction(creature));
                    game.doAction(new MagicGainControlAction(event.getPlayer(),creature,MagicStatic.UntilEOT));
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Haste));
                }
            });
        }
    }
]
