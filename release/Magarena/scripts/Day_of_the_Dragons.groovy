[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Exile all creatures you control. Then put that many 5/5 " +
                "red Dragon creature tokens with flying onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(event.getPlayer(),CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new ExileLinkAction(
                    event.getPermanent(),
                    target
                ));
            }
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("5/5 red Dragon creature token with flying"),
                targets.size()
            ));
        }
    },
    new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice all Dragons. Return exiled cards to the battlefield under your control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = event.getPlayer().filterPermanents(DRAGON_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicSacrificeAction(target));
            }
            game.doAction(new MagicReturnLinkedExileAction(
                event.getPermanent(),
                MagicLocationType.Play,
                event.getPlayer()
            ));
        }
    }
]
