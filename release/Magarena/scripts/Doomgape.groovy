[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                SACRIFICE_CREATURE,
                MagicSacrificeTargetPicker.create(),
                this,
                "Sacrifice a creature. " +
                "PN gains life equal to that creature's toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int toughness=it.getToughness();
                game.doAction(new MagicSacrificeAction(it));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),toughness));
            });
        }
    }
]
