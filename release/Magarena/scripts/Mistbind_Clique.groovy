[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_FAERIE_YOU_CONTROL,
                    permanent
                ),
                MagicTargetHint.None,
                "another Faerie to exile"
            );
            return new MagicEvent(
                permanent,
                new MagicMayChoice(targetChoice),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ exile another Faerie you control\$. " +
                "If you don't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicExileUntilThisLeavesPlayAction(permanent,creature));
                        final Collection<MagicPermanent> targets = game.filterPermanents(
                                event.getPlayer().getOpponent(),
                                MagicTargetFilter.TARGET_LAND_YOU_CONTROL);
                        for (final MagicPermanent land : targets) {
                            game.doAction(new MagicTapAction(land,true));
                        }
                    }
                });
            } else {
                game.doAction(new MagicSacrificeAction(permanent));
            }
        }
    },
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            if (act.isPermanent(permanent) &&
                !permanent.getExiledCards().isEmpty()) {
                final MagicCard exiledCard = permanent.getExiledCards().get(0);
                return new MagicEvent(
                    permanent,
                    exiledCard,
                    this,
                    "Return RN to the battlefield"
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard exiledCard = event.getRefCard();
            game.doAction(new MagicRemoveCardAction(exiledCard,MagicLocationType.Exile));
            game.doAction(new MagicPlayCardAction(exiledCard,exiledCard.getOwner()));
        }
    }
]
