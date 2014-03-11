[
   new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {
    
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{3}{U}"),
                new MagicTapEvent(source),
                new MagicDiscardEvent(source)
            ];
        }
    
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Untap target creature\$ and gain control of it until end of turn." +
                "That creature gains haste until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicUntapAction(creature));
                game.doAction(new MagicGainControlAction(event.getPlayer(),creature,MagicStatic.UntilEOT));
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Haste));
            });
        }
    }
]
