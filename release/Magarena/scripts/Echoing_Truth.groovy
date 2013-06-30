[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_NONLAND_PERMANENT,
                MagicBounceTargetPicker.create(),
                this,
                "Return target nonland permanent\$ and all other " +
                "permanents with the same name as that permanent " +
                "to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent targetPermanent) {
                    final MagicTargetFilter<MagicPermanent> targetFilter =
                        new MagicTargetFilter.NameTargetFilter(targetPermanent.getName());
                    final Collection<MagicPermanent> targets =
                        game.filterPermanents(event.getPlayer(),targetFilter);
                    for (final MagicPermanent target : targets) {
                        game.doAction(new MagicRemoveFromPlayAction(
                            target,
                            MagicLocationType.OwnersHand
                        ));
                    }
                }
            });
        }
    }
]
