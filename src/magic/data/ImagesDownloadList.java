package magic.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
public class ImagesDownloadList extends ArrayList<DownloadableFile> {

    public ImagesDownloadList(final Stream<MagicCardDefinition> cards) {
        createList(cards);
        sortListByFilename();
    }

    private void createList(final Stream<MagicCardDefinition> cards) {
        for (final MagicCardDefinition card : cards.collect(Collectors.toList())) {
            try {
                add(new CardImageFile(card));
            } catch (final java.net.MalformedURLException ex) {
                System.err.println("!!! Malformed URL " + card.getImageURL());
            }
        }
    }

    private void sortListByFilename() {
        Collections.sort(this, new Comparator<DownloadableFile>() {
            @Override
            public int compare(DownloadableFile o1, DownloadableFile o2) {
                return o1.getFilename().compareTo(o2.getFilename());
            }
        });
    }

}
