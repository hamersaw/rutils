package com.bushpath.rutils.reader;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class NoaaReader extends Reader<NoaaRecord> {
    protected Worker[] workers;
    protected BlockingQueue<byte[]> byteQueue;
    protected BlockingQueue<NoaaRecord> recordQueue;

    public NoaaReader(String filename, int workerCount) throws Exception {
        DataInputStream in = new DataInputStream(new FileInputStream(filename));

        this.byteQueue = new ArrayBlockingQueue(4096);
        this.recordQueue = new ArrayBlockingQueue(4096);

        // start dispatcher
        Dispatcher dispatcher = new Dispatcher(in);
        dispatcher.start();

        // start workers
        this.workers = new Worker[workerCount];
        for (int i=0; i<workers.length; i++) {
            this.workers[i] = new Worker();
            this.workers[i].start();
        }
    }

    @Override
    public String[] getHeader() {
        return null; // TODO
    }

    @Override
    public NoaaRecord next() throws Exception {
        while (true) {
            NoaaRecord record = recordQueue.poll(50, TimeUnit.MILLISECONDS);
            if (record != null) {
                return record;
            }

            // if record == null check if everything is complete
            boolean cont = false;
            for (Worker worker : this.workers) {
                if (!worker.isShutdown()) {
                    cont = true;
                    break;
                }
            }

            if (!cont && this.recordQueue.isEmpty()) {
                return null;
            }
        }
    }

    protected NoaaRecord parseBytes(byte[] bytes) throws Exception {
        NoaaRecord noaaRecord = new NoaaRecord();

        // open input stream
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));

        // read name
        String name = in.readUTF();

        // read temporal properties
        boolean hasTemporal = in.readBoolean();
        if (hasTemporal) {
            noaaRecord.setStartTime(in.readLong());
            noaaRecord.setEndTime(in.readLong());
        }

        // read spatial properties
        boolean hasSpatial = in.readBoolean();
        if (hasSpatial) {
            // read spatial properties
            boolean hasRange = in.readBoolean();
            if (hasRange) {
                noaaRecord.setLowerLatitude(in.readFloat());
                noaaRecord.setUpperLatitude(in.readFloat());
                noaaRecord.setLowerLongitude(in.readFloat());
                noaaRecord.setLowerLongitude(in.readFloat());
            }

            // read coordinates
            boolean hasCoordinates = in.readBoolean();
            if (hasCoordinates) {
                noaaRecord.setLatitude(in.readFloat());
                noaaRecord.setLongitude(in.readFloat());
            }
        }

        // read attributes
        int attributesLength = in.readInt();
        for (int i=0; i<attributesLength; i++) {
            String featureName = in.readUTF();
            int featureType = in.readInt();
            Object data = null;

            switch (featureType) {
            case 0: // null
                data = null;
                break;
            case 1: // integer
                data = in.readInt();
                break;
            case 2: // long
                data = in.readLong();
                break;
            case 3: // float
                data = in.readFloat();
                break;
            case 4: // double
                data = in.readDouble();
                break;
            case 5: // string
                data = in.readUTF();
                break;
            case 6: // byte[]
                int length = in.readInt();
                byte[] dataBytes = new byte[length];
                in.readFully(dataBytes);
                break;
            }

            noaaRecord.putAttribute(featureName, data);
        }

        // read features
        int featuresLength = in.readInt();
        for (int i=0; i<featuresLength; i++) {
            boolean hasName = in.readBoolean();
            if (hasName) {
                String featureName = in.readUTF();
            }

            boolean hasType = in.readBoolean();
            if (hasType) {
                int type = in.readInt();
            }

            int rank = in.readInt();
            Object[][] dataArray = new Object[rank][]; 
            for (int j=0; j<rank; j++) {
                int featureCount = in.readInt();
                dataArray[j] = new Object[featureCount];
            }

            for (int j=0; j<dataArray.length; j++) {
                for (int k=0; k<dataArray[j].length; k++) {
                    String featureValueName = in.readUTF();
                    int featureType = in.readInt();
                    Object data = null;

                    switch (featureType) {
                    case 0: // null
                        data = null;
                        break;
                    case 1: // integer
                        data = in.readInt();
                        break;
                    case 2: // long
                        data = in.readLong();
                        break;
                    case 3: // float
                        data = in.readFloat();
                        break;
                    case 4: // double
                        data = in.readDouble();
                        break;
                    case 5: // string
                        data = in.readUTF();
                        break;
                    case 6: // byte[]
                        int length = in.readInt();
                        byte[] dataBytes = new byte[length];
                        in.readFully(dataBytes);
                        break;
                    }
                    
                    dataArray[j][k] = data;
                }
            }

            // TODO - add feature
        }

        // read runtimeMetadata
        int storageNodeIdLength = in.readInt();
        byte[] storageNodeIdBytes = new byte[storageNodeIdLength];
        in.readFully(storageNodeIdBytes);
        noaaRecord.setStorageNodeId(new String(storageNodeIdBytes));

        int physicalGraphPathLength = in.readInt();
        byte[] physicalGraphPathBytes = new byte[physicalGraphPathLength];
        in.readFully(physicalGraphPathBytes);
        noaaRecord.setPhysicalGraphPath(new String(physicalGraphPathBytes));

        return noaaRecord;
    }

    @Override
    public void close() throws Exception {
    }

    protected class Dispatcher extends Thread {
        protected DataInputStream in;

        public Dispatcher(DataInputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                int recordCount = this.in.readInt();
                for (int i=0; i<recordCount; i++) {
                    // read latitude and longitude
                    float latitude = this.in.readFloat();
                    float longitude = this.in.readFloat();

                    // read record bytes
                    int length = this.in.readInt();
                    byte[] bytes = new byte[length];
                    this.in.readFully(bytes);

                    while (!byteQueue.offer(bytes)) {}
                }
                
                // close data input stream
                this.in.close();
            } catch (IOException e) {
                e.printStackTrace(); // TODO - log
            }

            // shutdown workers
            for (Worker worker : workers) {
                worker.shutdown();
            }
        }
    }

    protected class Worker extends Thread {
        protected boolean shutdown;

        public Worker() {
            this.shutdown = false;
        }

        @Override
        public void run() {
            while (!byteQueue.isEmpty() || !this.shutdown) {
                try {
                    // read next
                    byte[] bytes = byteQueue.poll(50, TimeUnit.MILLISECONDS);
                    if (bytes == null) {
                        continue;
                    }

                    NoaaRecord noaaRecord = parseBytes(bytes);

                    while (!recordQueue.offer(noaaRecord)) {}
                } catch (InterruptedException e) {
                } catch (Exception e) {
                    e.printStackTrace(); // TODO - log
                }
            }
        }

        public boolean isShutdown() {
            return this.shutdown;
        }

        public void shutdown() {
            this.shutdown = true;
        }
    }
}
