[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_CREATURE),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ untap target creature\$ and gain control of it until end of turn. " + 
                "That creature gains haste until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicUntapAction(it));
                    game.doAction(new GainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
                    game.doAction(new GainAbilityAction(it,MagicAbility.Haste));
                });
            }
        }
    }
]
