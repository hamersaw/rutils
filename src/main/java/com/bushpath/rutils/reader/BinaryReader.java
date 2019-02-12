package com.bushpath.rutils.reader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class BinaryReader extends Reader<float[]> {
    protected FileInputStream fileIn;
    protected BufferedInputStream bufIn;
    protected DataInputStream in;
    protected String[] header;

    public BinaryReader(String filename) throws IOException {
        // open input streams
        this.fileIn = new FileInputStream(filename);
        this.bufIn = new BufferedInputStream(fileIn);
        this.in = new DataInputStream(bufIn);

        // read header
        int length = in.readInt();
        this.header = new String[length];
        for (int i=0; i<header.length; i++) {
            header[i] = in.readUTF();
        }
    }

    @Override
    public String[] getHeader() {
        return this.header;
    }

    @Override
    public float[] next() throws Exception {
        float[] record = new float[this.header.length];
        try {
            for (int i=0; i<record.length; i++) {
                record[i] = in.readFloat();
            }

            return record;
        } catch (EOFException e) {
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        this.in.close();
        this.bufIn.close();
        this.fileIn.close();
    }
}
