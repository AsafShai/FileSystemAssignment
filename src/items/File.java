package items;

import java.util.Objects;

public class File extends FileSystemItem {
    private long size;
    public File(String name, long size) {
        super(name);
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "File: {" +
                "name=" + name +
                ", size='" + size + '\'' +
                ", creationDate=" + creationDate.format(FORMATTER) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        File file = (File) o;
        return size == file.size && name.equals(file.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), size, name);
    }
}
