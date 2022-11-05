public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "PageEntry{" +
                "pdfName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                '}';
    }

    @Override
    public int compareTo(PageEntry o) {
    return Integer.compare(o.count, this.count);
    }
}
/*
[PageEntry{pdf=Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf, page=12, count=6},
PageEntry{pdf=Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf, page=4, count=3},
PageEntry{pdf=Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf, page=5, count=3},
PageEntry{pdf=1. DevOps_MLops.pdf, page=5, count=2},
PageEntry{pdf=Что такое блокчейн.pdf, page=1, count=2},
PageEntry{pdf=Что такое блокчейн.pdf, page=3, count=2},
PageEntry{pdf=Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf, page=2, count=1},
PageEntry{pdf=Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf, page=11, count=1},
PageEntry{pdf=1. DevOps_MLops.pdf, page=3, count=1},
PageEntry{pdf=1. DevOps_MLops.pdf, page=4, count=1},
PageEntry{pdf=Что такое блокчейн.pdf, page=2, count=1},
PageEntry{pdf=Что такое блокчейн.pdf, page=4, count=1},
PageEntry{pdf=Что такое блокчейн.pdf, page=5, count=1},
PageEntry{pdf=Что такое блокчейн.pdf, page=7, count=1},
PageEntry{pdf=Что такое блокчейн.pdf, page=9, count=1},
PageEntry{pdf=Продвижение игр.pdf, page=7, count=1},
PageEntry{pdf=Как управлять рисками IT-проекта.pdf, page=2, count=1}]
 */