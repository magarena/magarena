[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Until end of turn, creatures you control get +1/+1 and creatures your opponent controls get -1/-1."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            final Collection<MagicPermanent> targets=
                game.filterPermanents(player,MagicTargetFilter.TARGET_CREATURE);
            for (final MagicPermanent creature : targets) {
                if (creature.isController(player)) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                } else {
                    game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
                }
            }
        }
    }
]
