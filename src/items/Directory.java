package items;

import java.util.*;

public class Directory extends FileSystemItem {
    private final Map<String, Directory> directories;
    private final Map<String, File> files;

    public Directory(String name) {
        super(name);
        directories = new HashMap<>();
        files = new HashMap<>();
    }

    public void addFile(File file) {
        files.put(file.name, file);
    }

    public void addDirectory(Directory directory) {
        directories.put(directory.name, directory);
    }

    /**
     * returns an Optional with the item with the name if found, else empty
     * Time complexity and Space complexity of O(1) - using hashmap to find the item with that name
     * @param name - the name of the item we look for
     * @return an Optional with the item with the name if found, else empty
     */
    public Optional<FileSystemItem> getItemByName(String name) {
        Directory directory = directories.get(name);
        if (directory != null)
            return Optional.of(directory);
        return Optional.ofNullable(files.get(name));
    }

    public Collection<Directory> getDirectories() {
        return directories.values();
    }

    public Collection<File> getFiles() { return files.values(); }

    /**
     *  the methods return the file with the maximum size if there are files
     *  Time complexity: O(f) - iterating over all files
     *  Space complexity: O(1)
     * @return the file with the biggest size, if there are files
     */
    public Optional<File> getBiggestFile() {
        return files.values().stream().max(Comparator.comparingLong(File::getSize));
    }

    /**
     * the method deletes the item by its name, can be a file or a directory
     * Time complexity and Space complexity of O(1) - using {@code getItemByName} and delete HashMap operation O(1)
     * @param name - the name of the item we want to delete
     */
    public void deleteByName(String name) {
        Optional<FileSystemItem> optionalFileSystemItem = getItemByName(name);
        FileSystemItem item = optionalFileSystemItem.orElseThrow(() -> new RuntimeException("Object with name " + name + "not found"));
        if (item.getClass() == File.class)
            files.remove(name);
        else if (item.getClass() == Directory.class)
            directories.remove(name);

    }



    @Override
    public String toString() {
        return "Directory: {" +
                "name='" + name + '\'' +
                ", creationDate=" + creationDate.format(FORMATTER) +
                '}';
    }
}
