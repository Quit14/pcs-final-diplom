import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    Map<String, List<PageEntry>> wordsOnPage = new HashMap<>();

    public Map<String, List<PageEntry>> getWordsOnPage() {
        return wordsOnPage;
    }

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        File[] pdfs = pdfsDir.listFiles();
        for (var pdf : pdfs) {
            var doc = new PdfDocument(new PdfReader(pdf));
            var pages = doc.getNumberOfPages();
            for (int i = 1; i < pages; i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                int currentPage = i;
                Iterator<Map.Entry<String, Integer>> itr = freqs.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry<String, Integer> entry = itr.next();
                    var word = entry.getKey();
                    var count = entry.getValue();
                    PageEntry pageEntry = new PageEntry(pdf.getName(), currentPage, count);
                    if (wordsOnPage.containsKey(word)) {
                        wordsOnPage.get(word).add(pageEntry);
                    }
                    else {wordsOnPage.computeIfAbsent(word, l -> new ArrayList<>()).add(pageEntry);}

                }
            }
        }
    }


    @Override
    public List<PageEntry> search(String word) {
        if (wordsOnPage.containsKey(word)) {
            return wordsOnPage.entrySet()
                    .stream()
                    .filter(set -> set.getKey().equals(word))
                    .map(Map.Entry::getValue)
                    .flatMap(List::stream)
                    .sorted()
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
