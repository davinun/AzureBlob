package com.harman.azure.blob10;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class FileByteBufferIterable implements Iterable<ByteBuffer> {

    private File file;
    private int bufferSize;

    public FileByteBufferIterable(File file, int bufferSize) {
        this.file = file;
        this.bufferSize = bufferSize;
    }

    @Override
    public Iterator<ByteBuffer> iterator() {
        try {
            return new FileByteBufferIterator(file, bufferSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void forEach(Consumer<? super ByteBuffer> action) {
        throw new RuntimeException("FileByteBufferIterable.forEach not implemented");
    }

    @Override
    public Spliterator<ByteBuffer> spliterator() {
        throw new RuntimeException("FileByteBufferIterable.spliterator not implemented");
    }
}
