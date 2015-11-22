[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        NEG_TARGET_ARTIFACT
                    ),
                    MagicDestroyTargetPicker.Destroy,
                    this,
                    "PN may\$ destroy target artifact\$. " +
                    "If that artifact is put into a graveyard this way, SN deals damage to that artifact's controller equal to the number of Allies you control."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new DestroyAction(it));
                    final MagicCard card = it.getCard();
                    // only deal damage when the target is destroyed
                    if (card.isInGraveyard() 
                        ||
                        (card.isToken() && !card.getOwner().getPermanents().contains(it))) {
                        final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Ally);
                        game.doAction(new DealDamageAction(event.getPermanent(),card.getOwner(),amount));
                    }
                });
            }
        }
    }
]
