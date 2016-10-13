[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all creatures. They can't be regenerated. " +
                "If seven or more cards are in PN's graveyard, instead destroy all creatures, " +
                "then PN creates two 1/1 white Spirit creature tokens with flying. " +
                "Creatures destroyed this way can't be regenerated."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = CREATURE.filter(event);
            for (final MagicPermanent target : targets) {
                game.doAction(ChangeStateAction.Set(target,MagicPermanentState.CannotBeRegenerated));
            }
            game.doAction(new DestroyAction(targets));
            if (MagicCondition.THRESHOLD_CONDITION.accept(event.getSource())) {
                game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 white Spirit creature token with flying"),
                    2
                ));
            }
        }
    }
]
