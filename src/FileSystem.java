import customExceptions.DuplicateNameException;
import customExceptions.FileSystemTypeException;
import customExceptions.NotFoundException;
import items.Directory;
import items.File;
import items.FileSystemItem;

import java.util.*;

public class FileSystem {
    private final Directory rootDirectory;


    public FileSystem() {
        rootDirectory = new Directory("");
    }

    /**
     * the method adds a new file to parentDirName if the parentDirName exists and the name is valid
     * Time complexity: O(d) where d is the number of directories in the FileSystem
     *  O(d) for getItemByName to check duplicates, O(d) for getParentDirectory and O(1) for files check using Hashmap - total O(d)
     * Space complexity: O(d) - the function uses {@code getItemByName} that uses a Queue that stores the next directories to search in
     * @param parentDirName - the name of the directory we want to add the file to
     * @param fileName - the name of the file that will be added to parentDirName
     * @param fileSize - the name of the file that will be added to parentDirName
     */
    public void addFile(String parentDirName, String fileName, long fileSize) {
        if (getItemByName(fileName).isPresent()) {
            throw new DuplicateNameException("name already exists");
        }
        Optional<Directory> optionalDirectory = getParentDirectory(parentDirName);
        Directory directory = optionalDirectory.orElseThrow(() -> new NotFoundException("Parent directory not found"));
        directory.addFile(new File(fileName, fileSize));
    }

    /**
     * the method adds a new directory to parentDirName if the parentDirName exists and the name is valid
     * Time complexity: O(d) where d is the number of directories in the FileSystem
     *  O(d) for getItemByName to check duplicates, O(d) for getParentDirectory and O(1) for files check using Hashmap - total O(d)
     * Space complexity: O(d) - the function uses {@code getItemByName} that uses a Queue that stores the next directories to search in
     * @param parentDirName - the name of the directory we want to add the file to
     * @param dirName - the name of the directory that will be added to parentDirName
     */
    public void addDirectory(String parentDirName, String dirName) {
        if (getItemByName(dirName).isPresent()) {
            throw new DuplicateNameException("name already exists");
        }
        Optional<Directory> optionalDirectory = getParentDirectory(parentDirName);
        Directory directory = optionalDirectory.orElseThrow(() -> new NotFoundException("Parent directory not found"));
        directory.addDirectory(new Directory(dirName));
    }

    /**
     * the method gets a name of an item and returns an optional with the parent directory of the item, if found
     * Time complexity: O(d) where d is the number of directories in the FileSystem - in worst case we check all the directories (O(d)),
     *  and the access to the files is O(1) using hashMap
     * Space complexity is O(d): the function uses a Queue that stores the next directories to search in
     * @param name - the name of the item we look for its parent
     * @return an Optional of the parent Directory if exists, empty if there's no
     */
    private Optional<Directory> getParentByItemName(String name) {
        Queue<Directory> nextDirectories = new LinkedList<>();
        nextDirectories.add(rootDirectory);
        while (!nextDirectories.isEmpty()) {
            Directory directory = nextDirectories.poll();
            Optional<FileSystemItem> fileSystemItemOptional = directory.getItemByName(name);
            if (fileSystemItemOptional.isPresent()) return Optional.of(directory);
            nextDirectories.addAll(directory.getDirectories());
        }
        return Optional.empty();
    }

    /**
     * the method gets a name of an item and returns an optional with the item if found
     * Time complexity: O(d) where d is the number of directories in the FileSystem - in worst case we check all the directories (O(d)),
     * and the access to the files is O(1) using hashMap
     * Space complexity: O(d) - the function uses a Queue that stores the next directories to search in
     * @param name - the name of the item we look for its parent
     * @return an Optional of the parent Directory if exists, empty if there's no
     */
    private Optional<FileSystemItem> getItemByName(String name) {
        Queue<Directory> nextDirectories = new LinkedList<>();
        nextDirectories.add(rootDirectory);
        while (!nextDirectories.isEmpty()) {
            Directory directory = nextDirectories.poll();
            Optional<FileSystemItem> fileSystemItemOptional = directory.getItemByName(name);
            if (fileSystemItemOptional.isPresent()) return fileSystemItemOptional;
            nextDirectories.addAll(directory.getDirectories());
        }
        return Optional.empty();
    }

    /**
     * the method returns the Directory with the name parentDirName if found
     * Time complexity: O(d) where d is the number of directories in the FileSystem - in worst case we check all the directories (O(d)),
     *  and the access to the files is O(1) using hashMap
     * Space complexity: O(d) - the function uses {@code getItemByName} that uses a Queue that stores the next directories to search in
     * @param parentDirName - the name of the directory we want to get
     * @return an Optional with the Directory with the name parentDirName, if found
     */
    private Optional<Directory> getParentDirectory(String parentDirName) {
        if (rootDirectory.getName().equals(parentDirName)) return Optional.of(rootDirectory);
        Optional<FileSystemItem> fileSystemItemOptional = getItemByName(parentDirName);
        if (fileSystemItemOptional.isEmpty()) return Optional.empty();
        FileSystemItem fileSystemItem = fileSystemItemOptional.get();
        if (fileSystemItem.getClass() != Directory.class) return Optional.empty();
        Directory directory = (Directory) fileSystemItem;
        return Optional.of(directory);
    }


    /**
     * the method returns the name of the biggest file by size
     * Time complexity: O(d + f) where d is the number of directories in the FileSystem - in worst case we check all the directories
     *  and check all the files (in directory.getBiggestFile) to find the maximum - O(d + f)
     * Space complexity: O(d) - the function uses a Queue that stores the next directories to search in
     * @return - the name of the biggest file by size
     */
    public String getBiggestFile() {
        Queue<Directory> nextDirectories = new LinkedList<>();
        nextDirectories.add(rootDirectory);
        File biggestFile = null;
        while (!nextDirectories.isEmpty()) {
            Directory directory = nextDirectories.poll();
            Optional<File> biggestFileInDirectoryOptional = directory.getBiggestFile();
            if (biggestFileInDirectoryOptional.isPresent()) {
                File biggestFileInDirectory = biggestFileInDirectoryOptional.get();
                if (biggestFile == null || biggestFileInDirectory.getSize() > biggestFile.getSize()) {
                    biggestFile = biggestFileInDirectory;
                }
            }
            nextDirectories.addAll(directory.getDirectories());
        }
        if (biggestFile == null) throw new NotFoundException("There are no files");
        return biggestFile.getName();
    }


    /**
     * the method returns the Directory with the name parentDirName if found
     * Time complexity: O(d) where d is the number of directories in the FileSystem - in worst case we check all the directories (O(d)),
     *  and the access to the files is O(1) using hashMap
     * Space complexity: O(d) - the function uses {@code getItemByName} that uses a Queue that stores the next directories to search in
     * @param fileName - the name of the file we want to get its size
     * @return the size of the file with fileName as long
     */
    public long getFileSize(String fileName) {
        Optional<FileSystemItem> fileSystemItemOptional = getItemByName(fileName);
        FileSystemItem fileSystemItem = fileSystemItemOptional.orElseThrow(() -> new NotFoundException("File " + fileName + " not found"));
        if (fileSystemItem.getClass() == File.class) {
            File file = (File) fileSystemItem;
            return file.getSize();
        }
        throw new FileSystemTypeException("Object with name " + fileName + "is not a File");
    }

    /**
     * the method deletes the item from the file system by its name
     * Time complexity: O(d) where d is the number of directories in the FileSystem - we use {@code getParentByItemName} that is O(d)
     *  and deleteByName of the directory that gets the item by name in O(1) using hashmap lookup
     * Space complexity: O(d) - the function uses {@code getParentByItemName} that uses a Queue that stores the next directories to search in
     * @param name - the name of the item we want to delete
     */
    public void delete(String name) {
        Optional<Directory> optionalDirectory = getParentByItemName(name);
        Directory parentDirectory = optionalDirectory.orElseThrow(() -> new NotFoundException("Object with name " + name + " not found"));
        parentDirectory.deleteByName(name);
    }

    /**
     * the method prints the full FileSystem
     * Time Complexity: O(d + f) - where d is the number of directories and f is the number of files. we iterate over all of them once
     *  and print exactly once because names are unique
     * Space Complexity: O(d) - we store the next directories to search in a stack, maximum d directories
     */
    public void showFileSystem() {
        Stack<Map.Entry<Directory, Integer>> stack = new Stack<>();
        stack.push(Map.entry(rootDirectory, 0));

        while (!stack.isEmpty()) {
            Map.Entry<Directory, Integer> current = stack.pop();
            Directory directory = current.getKey();
            int depth = current.getValue();

            String indentation = "  ".repeat(depth);
            System.out.println(indentation + directory);

            indentation = "  ".repeat(depth + 1);
            for (File file : directory.getFiles()) {
                System.out.println(indentation + file);
            }

            for (Directory subdirectory : directory.getDirectories())
                stack.push(Map.entry(subdirectory, depth + 1));
        }
    }
}
