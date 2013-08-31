package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicPumpActivation extends MagicPermanentActivation {

    private static final MagicActivationHints ACTIVATION_HINTS = new MagicActivationHints(MagicTiming.Pump);
    private final MagicManaCost cost;
    private final int power;
    private final int toughness;

    public MagicPumpActivation(final MagicManaCost cost,final int power,final int toughness) {
        super(ACTIVATION_HINTS,"Pump");
        this.cost=cost;
        this.power=power;
        this.toughness=toughness;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return Arrays.asList(new MagicPayManaCostEvent(source,cost));
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "SN gets " + getPTChange() + " until end of turn."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),power,toughness));
    }

    private String getPTChange() {
        String pSign = getSign(power);
        String tSign = getSign(toughness);

        if (tSign.isEmpty() && pSign.isEmpty()) {
            tSign = "+";
            pSign = "+";
        } else if (tSign.isEmpty()) {
            tSign = pSign;
        } else if (pSign.isEmpty()) {
            pSign = tSign;
        }

        return signStr(power, pSign) + power + "/" + signStr(toughness, tSign) + toughness;
    }

    private String signStr(int v, String sign) {
        return v >= 0 ? sign : "";
    }

    private String getSign(int v) {
        if (v > 0) {
            return "+";
        } else if (v < 0) {
            return "-";
        } else {
            return "";
        }
    }
}
