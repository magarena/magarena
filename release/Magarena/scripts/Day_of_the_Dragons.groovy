[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN exiles all creatures he or she controls. " +
                "Then creates that many 5/5 red Dragon creature tokens with flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = CREATURE_YOU_CONTROL.filter(event);
            for (final MagicPermanent target : targets) {
                game.doAction(new ExileLinkAction(
                    event.getPermanent(),
                    target
                ));
            }
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("5/5 red Dragon creature token with flying"),
                targets.size()
            ));
        }
    },
    new ThisLeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "PN sacrifices all Dragons he or she controls. Then returns the exiled cards to the battlefield under his or her control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            DRAGON_YOU_CONTROL.filter(event) each {
                game.doAction(new SacrificeAction(it));
            }
            game.doAction(new ReturnLinkedExileAction(
                event.getPermanent(),
                MagicLocationType.Battlefield,
                event.getPlayer()
            ));
        }
    }
]
