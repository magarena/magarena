def AN_EQUIPMENT_YOU_CONTROL = new MagicTargetChoice("an Equipment you control");

[
    new MagicPlaneswalkerActivation(2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN creates a 1/1 white Kor Soldier creature token. PN may attach an Equipment he/she controls to it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent sn = event.getPermanent();
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 white Kor Soldier creature token"),
                {
                    final MagicPermanent token ->
                    final MagicGame G1 = token.getGame();
                    final sn1 = sn.map(game);
                    G1.addEvent(new MagicEvent(
                        sn1,
                        new MagicMayChoice("Attach an equipment you control to ${token}?", AN_EQUIPMENT_YOU_CONTROL),
                        token,
                        {
                            final MagicGame G2, final MagicEvent E2 ->
                            if (E2.isYes()) {
                                E2.processTargetPermanent(G2, {
                                    G2.doAction(new AttachAction(
                                      it,
                                      E2.getRefPermanent()
                                    ));
                                })
                            }
                        },
                        "PN may\$ attach an Equipment he/she controls\$ to RN."
                    ));

                }
            ));
        }
    },
    new MagicPlaneswalkerActivation(-2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicMayChoice(
                    "Put an Equipment card from your hand or graveyard onto the battlefield?",
                    new MagicTargetChoice(
                        card(MagicSubType.Equipment).from(MagicTargetType.Hand).from(MagicTargetType.Graveyard),
                        "an Equipment card from your hand or graveyard"
                    )
                ),
                this,
                "PN may\$ put an Equipment card from his/her hand or graveyard\$ onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    final MagicLocationType from = it.isInHand() ? MagicLocationType.OwnersHand : MagicLocationType.Graveyard; 
                    game.doAction(new ReturnCardAction(from,it,event.getPlayer()));
                });
            }
        }
    }
]
