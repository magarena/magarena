[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    SACRIFICE_MERFOLK
                ),
                MagicSacrificeTargetPicker.create(),
                this,
                "PN may\$ sacrifice a Merfolk\$. " +
                "If PN does, he or she takes an extra turn after this one"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new SacrificeAction(it));
                    game.doAction(new ChangeExtraTurnsAction(event.getPlayer(),1));
                });
            }
        }
    }
]
