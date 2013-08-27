[
    new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            final MagicPlayer targetPlayer = target.getController();
            return (target.containsInChoiceResults(permanent) &&
                    targetPlayer != permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    targetPlayer,
                    this,
                    "PN loses 4 life."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-4));
        }
    }
]
