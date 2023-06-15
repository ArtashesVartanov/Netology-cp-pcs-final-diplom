import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    @Override
    public int compareTo(PageEntry o) {
        return Integer.compare(o.count, this.count);  // сравнение записей по количеству вхождений в страницу
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStringResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            return jsonStringResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        return "PageEntry{" +
//                "pdfName='" + pdfName + '\'' +
//                ", page=" + page +
//                ", count=" + count +
//                '}';
    }
}
