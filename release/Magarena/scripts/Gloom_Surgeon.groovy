[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            if (damage.isCombat() && damage.getTarget() == permanent) {
                final int amt = damage.getAmount();
                // Prevention effect
                damage.prevent();
                return new MagicEvent(
                    permanent,
                    amt,
                    this,
                    "Exile RN cards from the top of your library."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            final MagicPlayer player = event.getPlayer();
            final MagicCardList library = player.getLibrary();
            final int size = library.size();
            final int count = size >= amount ? amount : size;
            if (count > 0) {
                //setScore(player,ArtificialScoringSystem.getMillScore(count));
                for (int c=count;c>0;c--) {
                    final MagicCard card = library.getCardAtTop();
                    game.doAction(new MagicRemoveCardAction(
                        card,
                        MagicLocationType.OwnersLibrary
                    ));
                    game.doAction(new MagicMoveCardAction(
                        card,
                        MagicLocationType.OwnersLibrary,
                        MagicLocationType.Exile
                    ));
                }
            }
        }
    }
]
