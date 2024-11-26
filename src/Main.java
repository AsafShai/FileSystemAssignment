
public class Main {
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();

        fs.addDirectory("", "documents");
        fs.addDirectory("", "pictures");
        fs.addFile("", "readme.txt", 1024);
        fs.addFile("documents", "doc1.pdf", 2048);
        fs.addFile("documents", "doc2.txt", 512);
        fs.addDirectory("documents", "work");
        fs.addFile("work", "report.doc", 4096);
        fs.addFile("pictures", "vacation.jpg", 8192);
        fs.addFile("pictures", "something.png", 6144);

        fs.showFileSystem();

        System.out.println("\nBiggest file: " + fs.getBiggestFile());
        System.out.println("Size of doc1.pdf: " + fs.getFileSize("doc1.pdf"));

    }
}