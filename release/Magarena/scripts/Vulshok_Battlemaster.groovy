[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                    permanent,
                    this,
                    "Attach all Equipment on the battlefield to SN."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(EQUIPMENT);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicAttachAction(target,event.getPermanent()));
            }
        }
    }
]
