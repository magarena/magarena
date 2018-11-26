[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ reveals cards from the top of his or her library until he or she "+
                "reveals a creature card. That player puts all noncreature cards revealed this way "+
                "into his or her graveyard, then PN puts the creature card onto the battlefield "+
                "under his or her control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCardList library = it.getLibrary();
                int creatureCards = 0;
                while (creatureCards < 1 && library.size() > 0) {
                    game.doAction(new RevealAction(library.getCardAtTop()));
                    if (!library.getCardAtTop().hasType(MagicType.Creature)) {
                        game.doAction(new MillLibraryAction(it,1));
                    } else {
                        creatureCards++;
                        game.doAction(new PutOntoBattlefieldAction(
                            MagicLocationType.OwnersLibrary,
                            library.getCardAtTop(),
                            event.getPlayer()
                        ));
                    }
                }
            });
        }
    }
]
