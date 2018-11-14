[
    new AtBeginOfCombatTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer turnPlayer) {
            return permanent.isController(turnPlayer);
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer turnPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Choose a target creature you control?", TARGET_CREATURE_YOU_CONTROL),
                this,
                "PN chooses up to one target creature PN controls.\$\$ " +
                "Until end of turn, that creature gets +2/+0, gains trample if it's red, and gains vigilance if it's white."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new ChangeTurnPTAction(it, 2, 0));
                    if (it.hasColor(MagicColor.Red)) {
                        game.doAction(new GainAbilityAction(it, MagicAbility.Trample));
                    }
                    if (it.hasColor(MagicColor.White)) {
                        game.doAction(new GainAbilityAction(it, MagicAbility.Vigilance));
                    }
                });
            }
        }
    }
]
