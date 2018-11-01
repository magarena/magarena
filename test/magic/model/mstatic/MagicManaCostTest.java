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
        assertEquals("{X}{R}", rx.reducedBy(MagicManaCost.create("{2}")).toString());
        assertEquals("{X}{X}{U}", uxx.reducedBy(MagicManaCost.create("{2}")).toString());

        assertEquals("{X}{R}", rx.increasedBy(MagicManaCost.create("{2}")).reducedBy(MagicManaCost.create("{2}")).toString());
    }
}
