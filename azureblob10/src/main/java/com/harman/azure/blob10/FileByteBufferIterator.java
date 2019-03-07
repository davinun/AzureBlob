package com.harman.azure.blob10;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.util.Iterator;

public class FileByteBufferIterator implements Iterator<ByteBuffer> {

    private int bufferSize;

    private FileChannel fci;
    private ByteBuffer lastBuffer;
    private boolean lastCallWasHasNext = false;
    private boolean lastHasNextResult = true;

    public FileByteBufferIterator(File sourceFile, int bufferSize) throws FileNotFoundException {
        this.bufferSize = bufferSize;

        FileInputStream fis = new FileInputStream(sourceFile);
        fci = fis.getChannel();

    }

    @Override
    public boolean hasNext() {
        if (lastCallWasHasNext) {
            return lastHasNextResult;
        }
        lastCallWasHasNext = true;
        try {
            lastBuffer = ByteBuffer.allocate(bufferSize);
            int res = read(lastBuffer);
            return (res > 0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public ByteBuffer next() {

        if (lastCallWasHasNext) {
            lastCallWasHasNext = false;
            if (lastHasNextResult == false) {
                //Called next() after getting false from hasNext();
                return ByteBuffer.allocate(0);
            }
            return lastBuffer;
        }
        try {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            if (read(buffer) > 0) {
                return buffer;
            } else {
                throw new EOFException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private int read(ByteBuffer buffer) throws Exception {
        buffer.clear();
        int i = fci.read(buffer);
        if (i != -1) {
            buffer.flip();
        }
        System.out.println(i);
        return i;
    }
}
