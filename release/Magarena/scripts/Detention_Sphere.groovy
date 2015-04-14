def filter = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isLand() == false && target.getName().equals("Detention Sphere") == false;
    }
};

def choice = new MagicTargetChoice(filter, "target nonland permanent not named Detention Sphere");

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                choice,
                MagicExileTargetPicker.create(),
                this,
                "Exile target nonland permanent\$ not named Detention Sphere and all other permanents with the same name as that permanent."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    new MagicNameTargetFilter(it.getName())
                );
                for (final MagicPermanent target : targets) {
                    game.doAction(new ExileLinkAction(
                        event.getPermanent(),
                        target
                    ));
                }
            });
        }
    }
]
