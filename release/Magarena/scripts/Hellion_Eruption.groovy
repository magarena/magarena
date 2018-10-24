[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN sacrifice all creatures he or she controls. " +
                "Then creates that many 4/4 red Hellion creature tokens."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = CREATURE_YOU_CONTROL.filter(event);
            for (final MagicPermanent target : targets) {
                game.doAction(new SacrificeAction(target))
            }
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("4/4 red Hellion creature token"),
                targets.size()
            ));
        }
    }
]
