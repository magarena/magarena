[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.NEG_TARGET_ARTIFACT
                    ),
                    new MagicDestroyTargetPicker(false),
                    this,
                    "PN may\$ destroy target artifact\$. " +
                    "If that artifact is put into a graveyard this way, SN deals damage to that artifact's controller equal to the number of Allies you control."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent target) {
                        game.doAction(new MagicDestroyAction(target));
                        final MagicCard card = target.getCard();
                        final MagicPlayer player = event.getPlayer();
                        // only deal damage when the target is destroyed
                        if (card.isInGraveyard() 
                            ||
                            (card.isToken() && !card.getOwner().getPermanents().contains(target))) {
                            final int amount =
                                    player.getNrOfPermanents(MagicSubType.Ally);
                            if (amount > 0) {
                                final MagicDamage damage = new MagicDamage(
                                    event.getPermanent(),
                                    card.getOwner(),
                                    amount
                                );
                                game.doAction(new MagicDealDamageAction(damage));
                            }
                        }
                    }
                });
            }
        }
    }
]
