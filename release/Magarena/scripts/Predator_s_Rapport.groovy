[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "PN gain life equal to that creature\$'s power plus its toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeLifeAction(player,it.getPower()+it.getToughness()));
            });
        }
    }
]
