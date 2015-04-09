[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target creature\$ until end of turn. Put a +1/+1 counter on it and untap it. " +
                "That creature gains haste until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
                game.doAction(new MagicChangeCountersAction(it,MagicCounterType.PlusOne,1));
                game.doAction(new MagicUntapAction(it));
                game.doAction(new MagicGainAbilityAction(it,MagicAbility.Haste));
            });
        }
    }
]
