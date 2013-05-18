[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Tap all creatures your opponent controls and untap all creatures you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            final Collection<MagicPermanent> targets=
                game.filterPermanents(player,MagicTargetFilter.TARGET_CREATURE);
            for (final MagicPermanent creature : targets) {
                if (creature.isController(player)) {
                    game.doAction(new MagicUntapAction(creature));
                } else {
                    game.doAction(new MagicTapAction(creature,true));
                }
            }
        }
    }
]
