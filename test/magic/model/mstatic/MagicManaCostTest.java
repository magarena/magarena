package magic.model.mstatic;

import magic.model.MagicManaCost;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MagicManaCostTest {
    @Test
    public void testCostModification() {
        MagicManaCost rg = MagicManaCost.create("{R}{G}");
        MagicManaCost wwbb1 = MagicManaCost.create("{1}{W}{W}{B}{B}");
        MagicManaCost rx = MagicManaCost.create("{X}{R}");
        MagicManaCost uxx = MagicManaCost.create("{X}{X}{U}");
        assertEquals("{2}{R}{G}", rg.increasedBy(MagicManaCost.create("{2}")).toString());
        assertEquals("{R}{R}{G}{G}", rg.increasedBy(MagicManaCost.create("{R}{G}")).toString());
        assertEquals("{R}{G}", rg.reducedBy(MagicManaCost.create("{2}")).toString());
        assertEquals("{0}", rg.reducedBy(MagicManaCost.create("{R}{G}")).toString());
        assertEquals("{W}{W}{B}{B}", wwbb1.reducedBy(MagicManaCost.create("{2}")).toString());
        assertEquals("{1}{W}{W}{B}{B}", wwbb1.reducedBy(MagicManaCost.create("{R}{G}")).toString());
        assertEquals("{1}{W}{B}{B}", wwbb1.reducedBy(MagicManaCost.create("{W}{U}")).toString());
        assertEquals("{W}{B}{B}", wwbb1.reducedBy(MagicManaCost.create("{W}{U}{3}")).toString());

        assertEquals("{X}{2}{R}", rx.increasedBy(MagicManaCost.create("{2}")).toString());
        assertEquals("{X}{X}{2}{U}", uxx.increasedBy(MagicManaCost.create("{2}")).toString());

        // Following two are not 100% correct, but as close to the wanted result as possible
        // (as the X should be replaced by something like "X-2", or rather the discount should be fully resolved at cast time)
        // Note that -2 is stored internally in the amount, but not used for discount when casting.
        assertEquals("{X}{R}", rx.reducedBy(MagicManaCost.create("{2}")).toString());
        assertEquals("{X}{X}{U}", uxx.reducedBy(MagicManaCost.create("{2}")).toString());

        assertEquals("{X}{R}", rx.increasedBy(MagicManaCost.create("{2}")).reducedBy(
                MagicManaCost.create("{2}")).toString());
    }

    @Test
    public void testSplitCostModification() {
        MagicManaCost rw3 = MagicManaCost.create("{R/W}{R/W}{R/W}");
        assertEquals("{R/W}{R/W}", rw3.reducedBy(MagicManaCost.create("{R}{G}")).toString());
        assertEquals("{R/W}", rw3.reducedBy(MagicManaCost.create("{R}{G}{R/W}")).toString());
        MagicManaCost rw2gw = MagicManaCost.create("{R/W}{R/W}{G/W}");

        assertEquals("{0}", rw2gw.reducedBy(MagicManaCost.create("{R}{G}{R/W}")).toString());
        assertEquals("{0}", rw2gw.reducedBy(MagicManaCost.create("{R}{G}{W}")).toString());

        MagicManaCost hybrid = MagicManaCost.create("{2/R}{2/G}{2/W}");
        assertEquals("{2/W}", hybrid.reducedBy(MagicManaCost.create("{R}{G}{U}")).toString());

        MagicManaCost phyR = MagicManaCost.create("{R}{R/P}{G/P}{W/P}");
        assertEquals("{W/P}", phyR.reducedBy(MagicManaCost.create("{R}{R}{G}{U}")).toString());

        MagicManaCost phy = MagicManaCost.create("{R/P}{G/P}{W/P}");
        assertEquals("{W/P}", phy.reducedBy(MagicManaCost.create("{R}{G}{U}")).toString());


    }
}
