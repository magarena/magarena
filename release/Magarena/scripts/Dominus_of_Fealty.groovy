[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(NEG_TARGET_PERMANENT),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ gain control of target permanent\$ until end of turn. " + 
                "Untap it. It gains haste until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicGainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
                    game.doAction(new MagicUntapAction(it));
                    game.doAction(new GainAbilityAction(it,MagicAbility.Haste));
                });
            }
        }
    }
]
