[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Creatures your opponent controls get -4/-0 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent = event.getPlayer().getOpponent();
            final Collection<MagicPermanent> targets =
                game.filterPermanents(opponent,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target,-4,0));
            }
        }
    }
]
