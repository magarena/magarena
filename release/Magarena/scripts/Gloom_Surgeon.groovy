[
    new MagicIfDamageWouldBeDealtTrigger(4) {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final int amount = damage.getAmount();
            if (!damage.isUnpreventable() &&
                amount > 0 &&
                damage.isCombat() &&
                damage.getTarget() == permanent) {
                // Prevention effect.
                damage.setAmount(0);
                return new MagicEvent(
                    permanent,
                    amount,
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
