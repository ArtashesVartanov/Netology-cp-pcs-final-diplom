import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    Map<String, List<PageEntry>> words = new HashMap<>();  // мапа всех слов из файлов и результатов поиска этих слов

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
        File[] files = pdfsDir.listFiles();  // получаем список всех файлов
        for (File file : files) {
            if (file.isFile()) {
                PdfDocument doc = new PdfDocument(new PdfReader(file));  // открываем файл
                for (int i = 1; i <= doc.getNumberOfPages(); i++) {  // проходимся по его страницам
                    PdfPage page = doc.getPage(i);  // получаем страницу
                    String text = PdfTextExtractor.getTextFromPage(page);  // получаем текст со страницы
                    String[] pageWords = text.split("\\P{IsAlphabetic}+");  // получаем список слов без знаков препинаний
                    Map<String, Integer> freqs = new HashMap<>();  // создаем мапу для хранения слова и сколько раз оно встречается на этой странице
                    for (String word: pageWords) {  // проходимся по списку слов
                        if (!word.isEmpty()) {
                            freqs.put(word.toLowerCase(), freqs.getOrDefault(word, 0) + 1);  // добавляем слово в мапу
                        }
                    }
                    for (Map.Entry<String, Integer> entry: freqs.entrySet()) {  // проходимся по записям в мапе
                        PageEntry pageEntry = new PageEntry(file.getName(), i, entry.getValue());  // создаем запись результатов поиска
                        if (!words.containsKey(entry.getKey())) {  // если такого слова нет в мапе слов
                            words.put(entry.getKey(), new ArrayList<>());  // добавляем новое слово
                        }
                        words.get(entry.getKey()).add(pageEntry);  // добавляем запись о результатах поиска для слова
                    }

                }
            }
        }
        for (Map.Entry<String, List<PageEntry>> entry: words.entrySet()) {
            entry.getValue().sort(PageEntry::compareTo);  // сортировка записей
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return this.words.getOrDefault(word, Collections.emptyList());
    }
}
