package magic.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
public class ImagesDownloadList extends ArrayList<DownloadableFile> {

    public ImagesDownloadList(final Collection<MagicCardDefinition> cards) {
        loadDownloadImageFiles(cards);
    }

    private void loadDownloadImageFiles(final Collection<MagicCardDefinition> cards) {
               
        for (final MagicCardDefinition card : cards) {
            if (card.getImageURL() != null) {
                try {
                    add(new DownloadImageFile(card));
                } catch (final java.net.MalformedURLException ex) {
                    System.err.println("!!! Malformed URL " + card.getImageURL());
                }
            }
        }

        Collections.sort(this, new Comparator<DownloadableFile>() {
            @Override
            public int compare(DownloadableFile o1, DownloadableFile o2) {
                return o1.getFilename().compareTo(o2.getFilename());
            }
        });

    }

}
