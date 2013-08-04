[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_PERMANENT,
                MagicTapTargetPicker.TapOrUntap, 
                payedCost,
                this,
                "PN may tap or untap target permanent\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        new MagicMayChoice((perm.isTapped() ? "Untap " : "Tap ") + perm + "?"),
                        perm,
                        {
                            final MagicGame G, final MagicEvent E ->
                            final MagicPermanent P = E.getRefPermanent();
                            if (E.isYes()) {
                                if (P.isTapped()) {
                                    G.doAction(new MagicUntapAction(P));
                                } else {
                                    G.doAction(new MagicTapAction(P, true))
                                }
                            }
                        } as MagicEventAction,
                        "PN may\$ " + (perm.isTapped() ? "untap" : "tap") + " RN."
                    ));
                }
            });
        }
    }
]
