[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            game.doAction(new ChangeCountersAction(
                permanent,
                MagicCounterType.PlusOne,
                1
            ));
            final int amount = permanent.getCounters(MagicCounterType.PlusOne);
            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(new MagicPayManaCostChoice(MagicManaCost.create(amount))),
                    this,
                    "PN may\$ pay {"+amount+"}. If he or she doesn't, sacrifice SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    },
    new ThisLeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final RemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ puts an X/X blue Orb creature token with flying onto the battlefield, "+
                "where X is the number of +1/+1 counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int X = event.getPermanent().getCounters(MagicCounterType.PlusOne);
                game.logAppendX(event.getPlayer(),X);
                game.doAction(new PlayTokenAction(
                    it,
                    MagicCardDefinition.create(
                        CardDefinitions.getToken("blue Orb creature token with flying"),
                        {
                            it.setPowerToughness(X, X);
                            it.setValue(X);
                        }
                    )
                ));
            });
        }
    }
]
