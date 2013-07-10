[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Exile all lands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_LAND);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicExileUntilThisLeavesPlayAction(
                    event.getPermanent(),
                    target
                ));
            }
        }
    },
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            if (act.isPermanent(permanent) &&
                !permanent.getExiledCards().isEmpty()) {
                final MagicCardList clist = new MagicCardList(permanent.getExiledCards());
                return new MagicEvent(
                    permanent,
                    this,
                    clist.size() == 1 ?
                        "Return " + clist.get(0) + " to the battlefield tapped." :
                        "Return exiled cards to the battlefield tapped."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new MagicReturnExiledUntilThisLeavesPlayAction(
                event.getPermanent(),
                MagicLocationType.Play,
                MagicPlayMod.TAPPED
            ));
        }
    }
]
