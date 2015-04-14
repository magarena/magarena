[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PERMANENT,
                MagicExileTargetPicker.create(),
                this,
                "Untap target permanent\$ and gain control of it until end of turn." +
                " It gains haste until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicUntapAction(it));
                game.doAction(new MagicGainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
                game.doAction(new GainAbilityAction(it,MagicAbility.Haste));
            });
        }
    }
]
