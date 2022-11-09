import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> wordsOnPage = new HashMap<>();
    private final Set<String> STOP_LIST = new HashSet<>();
    private final File STOP_WORDS = new File("stop-ru.txt");


    private void readStopList() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(STOP_WORDS))) {
            String line = br.readLine();
            while (line != null) {
                STOP_LIST.add(line);
                line = br.readLine();
            }
        }
    }


    public BooleanSearchEngine(File pdfsDir) throws IOException {
        readStopList();
        File[] pdfs = pdfsDir.listFiles();
        for (var pdf : pdfs) {
            var doc = new PdfDocument(new PdfReader(pdf));
            var pages = doc.getNumberOfPages();
            for (int i = 1; i < pages; i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if ((word.isEmpty()) | (STOP_LIST.contains(word.toLowerCase()))) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    var word = entry.getKey();
                    var count = entry.getValue();
                    PageEntry pageEntry = new PageEntry(pdf.getName(), i, count);
                    if (wordsOnPage.containsKey(word)) {
                        wordsOnPage.get(word).add(pageEntry);
                    } else {
                        wordsOnPage.computeIfAbsent(word, l -> new ArrayList<>()).add(pageEntry);
                    }
                }
            }
        }
    }


    @Override
    public List<PageEntry> search(String words) {
        String[] input = words.split(" ");
        Set<String> respond = new HashSet<>(Arrays.asList(input));
        List<PageEntry> filteredList = new ArrayList<>();
        List<PageEntry> resultList = new ArrayList<>();
        for (var element : respond) {
            List<PageEntry> temp = wordsOnPage.entrySet()
                    .stream()
                    .filter(set -> set.getKey().equals(element))
                    .map(Map.Entry::getValue)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            filteredList.addAll(temp);
        }
        for (var p1 : filteredList) {
            var count = filteredList.stream()
                    .filter(pageEntry -> pageEntry.samePageEntry(p1))
                    .mapToInt(PageEntry::getCount)
                    .sum();
            if (resultList.stream().noneMatch(pageEntry -> pageEntry.samePageEntry(p1))) {
                resultList.add(new PageEntry(p1.getPdfName(), p1.getPage(), count));
            }
        }
        Collections.sort(resultList);
        return resultList;
    }
}

